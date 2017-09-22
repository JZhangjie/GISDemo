package com.demo.jzhangjie.gisdemo.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

/**
 * Created by admin on 2017/9/20.
 */

public class PermissionHelper {
    public static void getPermission(Activity activity){
        if (Build.VERSION.SDK_INT >= 23) {
            int checkReadExternalStoragePermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
            int checkWriteExternalStoragePermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int checkCameraPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);
            int checkInternetPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.INTERNET);
            int checkLocationPermission1= ActivityCompat.checkSelfPermission(activity,Manifest.permission.ACCESS_COARSE_LOCATION);
            int checkLocationPermission2= ActivityCompat.checkSelfPermission(activity,Manifest.permission.ACCESS_FINE_LOCATION);
            if (checkReadExternalStoragePermission != PackageManager.PERMISSION_GRANTED || checkWriteExternalStoragePermission != PackageManager.PERMISSION_GRANTED
                    || checkCameraPermission != PackageManager.PERMISSION_GRANTED || checkInternetPermission != PackageManager.PERMISSION_GRANTED || checkLocationPermission1 !=PackageManager.PERMISSION_GRANTED
                    || checkLocationPermission1 !=PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA, Manifest.permission.INTERNET,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            }
        }
    }
}
