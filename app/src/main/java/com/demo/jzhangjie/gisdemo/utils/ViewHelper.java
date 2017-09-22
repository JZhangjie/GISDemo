package com.demo.jzhangjie.gisdemo.utils;

import android.app.Activity;
import android.util.DisplayMetrics;

/**
 * Created by admin on 2017/9/5.
 */

public class ViewHelper {
    public static int getPixelsFromDp(int size, Activity context) {
        DisplayMetrics metrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return (size * metrics.densityDpi) / DisplayMetrics.DENSITY_DEFAULT;
    }
}
