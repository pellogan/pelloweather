package com.example.pello.pelloweather.db;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Pello on 2016/2/22.
 */
public class PelloWeatherOpenhelper  extends SQLiteOpenHelper{

    //province 的建表语句
    public static final String CREATE_PROVINCE = "create table Province (" +
            "province_name text," +
            "province_code text)";

    //city的建表语句
    public static final String CREATE_CITY = "create table City(" +
            "city_name text," +
            "city_code text," +
            "province_code text)";

    //county的建表语句
    public static final String CREATE_COUNTY = "create table County(" +
            "county_name text," +
            "county_code text," +
            "county_weather_code text,"+
            "city_code text )";

    public PelloWeatherOpenhelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_PROVINCE);
        db.execSQL(CREATE_CITY);
//        if (isExistTable("County")) {
//            db.execSQL("drop County");
//        }
        db.execSQL(CREATE_COUNTY);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public  boolean isExistTable(String tableName) {
        if (tableName == null || "".equals(tableName)) {
            return false;
        }
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("select count(*) from " + tableName, null);
            if (cursor.moveToFirst()) {
                int count = cursor.getInt(0);
                if (count > 0) {
                    return true;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }
}
