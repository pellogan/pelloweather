package com.example.pello.pelloweather.util;

import android.util.Log;

/**
 * Created by Pello on 2016/2/22.
 */
public class LogUtil {

    public static final int VERBOSE = 1;
    public static final int DEBUG = 2;
    public static final int INFO = 3;
    public static final int WARNING = 4;
    public static final int ERROR = 5;
    public static final int NOTHING = 6;

    public static final int LOG_LEVEL = VERBOSE;


    public static void v(String tag, String msg) {
        if (LOG_LEVEL <= VERBOSE) {
            Log.v(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (LOG_LEVEL <= DEBUG) {
            Log.d(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (LOG_LEVEL <= INFO) {
            Log.i(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (LOG_LEVEL <= WARNING) {
            Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (LOG_LEVEL <= ERROR) {
            Log.v(tag, msg);
        }
    }
}
