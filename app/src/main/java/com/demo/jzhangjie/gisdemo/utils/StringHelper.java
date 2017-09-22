package com.demo.jzhangjie.gisdemo.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by admin on 2017/9/5.
 */

public class StringHelper {
    public static String getImageName() {
        DateFormat dataFormat = new SimpleDateFormat("yyyyMMddHHmmssSS");
        return dataFormat.format(new Date());
    }

    public static String getDate2String(Date date) {
        if (date == null)
            return null;
        DateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dataFormat.format(date);
    }

    public static Date getDateFromString(String date) {
        DateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (date == null)
            return null;
        try {
            Date result = dataFormat.parse(date);
            return result;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
