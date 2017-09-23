package com.demo.jzhangjie.gisdemo.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.demo.jzhangjie.gisdemo.constant.ApplicationConstant;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by alex on 2017/9/1.
 */

public class FileHelper {
    public static String getRootPath() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+ ApplicationConstant.ROOTPATH;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    // 使用/结尾的表示文件夹，没有的表示文件
    public static File getPath(String relativePath) {
        String path = getRootPath();
        String filename = null;
        if (relativePath.startsWith(File.separator)) {
            relativePath = relativePath.substring(1);
        }
        List<String> plist = new ArrayList<String>(Arrays.asList(relativePath.split(File.separator)));
        int num = plist.size();
        if (!relativePath.endsWith(File.separator)) {
            filename = plist.get(plist.size() - 1);
            plist.remove(filename);
        }
        for (int i = 0; i < plist.size(); i++) {
            String p = plist.get(i);
            if (p == null || p.trim() == "") {
                continue;
            }
            path += "/" + p;
            File f = new File(path);
            if (!f.exists()) {
                f.mkdirs();
            }
        }
        if (filename != null)
            path = path + "/" + filename;
        return new File(path);
    }

    //最长边压缩为1024px
    public static void compressImage(File file) {
        compressImage(file.getAbsolutePath());
    }

    public static void compressImage(String filepath) {
        compressImage(filepath, filepath);
    }

    public static void compressImage(String filepath, String despath) {
        File file = new File(filepath);
        if (file.exists()) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            Bitmap bitmap_bound = BitmapFactory.decodeFile(filepath, options);
            options.inJustDecodeBounds = false;
            float w = options.outWidth;
            float h = options.outHeight;
            float ww = 1024f;
            float hh = 1024f;
            float scale = 1.0f;
            if (w > h && w > ww) {
                scale = w / ww;
            } else if (h > w && h > hh) {
                scale = h / hh;
            }
            if (scale < 0) {
                scale = 1.0f;
            }
            options.inSampleSize = (int) scale;
            Bitmap bitmap = BitmapFactory.decodeFile(filepath, options);
            int desw = (int) (w / scale);
            int desh = (int) (h / scale);
            Bitmap newbitmap = Bitmap.createScaledBitmap(bitmap, desw, desh, true);
            File temp = new File(despath);
            if (temp.exists()) {
                temp.delete();
            }
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(despath);
                if (newbitmap != null) {
                    newbitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (fos != null)
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        }
    }

    //文件大小压缩小于等于filesize KB
    public static void compressImage(File file, float filesize) {
        compressImage(file.getAbsolutePath(), filesize);
    }

    public static void compressImage(String filepath, float filesize) {
        compressImage(filepath, filepath, filesize);
    }

    public static void compressImage(String filepath, String despath, float filesize) {
        File file = new File(filepath);
        if (file.exists()) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(filepath, options);
            File temp = new File(despath);
            if (temp.exists()) {
                temp.delete();
            }
            ByteArrayOutputStream fos = null;
            fos = new ByteArrayOutputStream();
            if (bitmap != null) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                int scale = 90;
                while (fos.toByteArray().length / 1024 > filesize && scale>1) {
                    fos.reset();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, scale, fos);
                    if(scale>10)
                        scale -= 10;
                    else
                        scale-=1;
                }
            }
            FileOutputStream os = null;
            try {
                os = new FileOutputStream(temp);
                fos.writeTo(os);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    fos.close();
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static StringBuilder readFile(String filePath) {
        return readFile(filePath, "UTF-8");
    }

    public static StringBuilder readFile(String filePath, String charsetName) {
        File file = new File(filePath);
        StringBuilder fileContent = new StringBuilder("");
        if (file == null || !file.isFile()) {
            return null;
        }

        BufferedReader reader = null;
        try {
            InputStreamReader is = new InputStreamReader(new FileInputStream(file), charsetName);
            reader = new BufferedReader(is);
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (!fileContent.toString().equals("")) {
                    fileContent.append("\r\n");
                }
                fileContent.append(line);
            }
            reader.close();
            return fileContent;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }

    public static boolean CopyFiles(String oldPath, String newPath) throws IOException {
        File oldFile = new File(oldPath);
        File newFile = new File(newPath);
        return CopyFiles(oldFile,newFile);
    }

    public static boolean CopyFiles(File oldFile, File newFile) throws IOException {
        boolean isCopy = true;
        if(!oldFile.exists()){
            return false;
        }
        if (oldFile.isDirectory()) {//如果是目录,如果是具体文件则长度为0
            newFile.mkdirs();//如果文件夹不存在，则递归
            for (File file : oldFile.listFiles()) {
                File nfile = new File(newFile.getAbsolutePath()+"/"+file.getName());
                CopyFiles(file,nfile);
            }
        }else {//如果是文件
            InputStream is = new FileInputStream(oldFile);
            if(newFile.exists())
                newFile.delete();
            FileOutputStream fos = new FileOutputStream(newFile);
            byte[] buffer = new byte[1024];
            int byteCount=0;
            while((byteCount=is.read(buffer))!=-1) {//循环从输入流读取 buffer字节
                fos.write(buffer, 0, byteCount);//将读取的输入流写入到输出流
            }
            fos.flush();//刷新缓冲区
            is.close();
            fos.close();
        }
        return isCopy;
    }
}
