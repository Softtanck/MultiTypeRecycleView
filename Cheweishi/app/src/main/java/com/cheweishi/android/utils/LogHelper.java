package com.cheweishi.android.utils;

import android.util.Log;

import com.cheweishi.android.BuildConfig;

/**
 * Created by tangce on 3/23/2016.
 */
public class LogHelper {
    private static String TAG = "Tanck";

    public static void d(String content) {

        if (BuildConfig.LogFlag && null != content)
            Log.d(TAG, content);
    }

    public static void e(String content) {
        if (BuildConfig.LogFlag && null != content)
            Log.e(TAG, content);
    }
}
