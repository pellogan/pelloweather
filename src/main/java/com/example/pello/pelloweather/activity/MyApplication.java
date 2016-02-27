package com.example.pello.pelloweather.activity;

import android.app.Application;
import android.content.ContentProvider;
import android.content.Context;

import com.example.pello.pelloweather.db.PelloWeatherDB;
import com.example.pello.pelloweather.util.LoadCityCode;

/**
 * Created by Pello on 2016/2/25.
 */
public class MyApplication extends Application {

    public static Context context;

    public void onCreate() {
        context = getApplicationContext();
            try {
                LoadCityCode.initCityCode(context.getPackageManager().getResourcesForApplication(context.getPackageName()),
                        PelloWeatherDB.getInstance(context));
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    public static Context getContext() {
        return context;
    }
}
