package com.example.pello.pelloweather.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.pello.pelloweather.model.City;
import com.example.pello.pelloweather.model.County;
import com.example.pello.pelloweather.model.Province;
import com.example.pello.pelloweather.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pello on 2016/2/22.
 */
public class PelloWeatherDB {

    //database name
    public static final String DB_NAME = "pello_weather";

    //database version
    public static final int VERSION = 1;

    private static PelloWeatherDB pelloWeatherDB;
    private SQLiteDatabase db;

    public static boolean isInited = false;

    //构造方法私有化
    private PelloWeatherDB(Context context) {
        PelloWeatherOpenhelper dbHelper = new PelloWeatherOpenhelper(context, DB_NAME, null, VERSION);
        db = dbHelper.getWritableDatabase();
    }

    //获取PelloWeatherDB实例
    public synchronized static PelloWeatherDB getInstance(Context context) {
        if (pelloWeatherDB == null) {
            pelloWeatherDB = new PelloWeatherDB(context);
        }
        return pelloWeatherDB;
    }

    //是否已经初始化过了
    public boolean isInited() {
        Cursor cursor = db.rawQuery("select * from Province", null);
        int num = cursor.getCount();
        if (num > 0) {
            isInited = true;
        }
        LogUtil.d("WeatherDb", "isInited  : ##" + isInited + "###"+num+"##'");
        return isInited;
    }


    //将province实例存储到数据库
    public void saveProvince(Province province) {
        if (province != null) {
            String insertProvince = "insert into Province  values ( '" + province.getProvinceName() + "', '"
                    + province.getProvinceCode() + "')";
            db.execSQL(insertProvince);

        }
    }

    //获取province列表
    public List<Province> loadProvinces() {
        List<Province> list = new ArrayList<Province>();
        Cursor cursor = db.query("Province", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Province province = new Province();
                province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
                province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
                list.add(province);
            } while (cursor.moveToNext());
        }
        return list;
    }

    //将City实例存储到数据库
    public void saveCity(City city) {
        if (city != null) {
            String insertCity = "insert into City  values ( '" + city.getCityName() + "', '"
                    + city.getCityCode() + "', '" + city.getProvinceCode() + "')";
            db.execSQL(insertCity);
        }

    }

    //获取省份下的City实例
    public List<City> loadCities(String provinceCode) {
        List<City> list = new ArrayList<City>();
        Cursor cursor = db.query("City", null, "province_code = ?", new String[]{provinceCode}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                City city = new City();
                city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
                city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
                city.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
                list.add(city);
            } while (cursor.moveToNext());
        }
        return list;
    }

    //将County存储到数据库
    public void saveCounty(County county) {
        if (county != null) {
            String insertCounty = "insert into County values ( '" + county.getCountyName() + "', '"
                    + county.getCountyCode() + "', '" + county.getCountyWeatherCode() + "','"
                    + county.getCityCode() + "')";
            db.execSQL(insertCounty);

        }
    }

    //获取City下的County实例
    public List<County> loadCounties(String cityCode) {
        List<County> list = new ArrayList<County>();
        Cursor cursor = db.query("County", null, "city_code = ?", new String[]{String.valueOf(cityCode)}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                County county = new County();
                county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
                county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
                county.setCountyWeatherCode(cursor.getString(cursor.getColumnIndex("county_weather_code")));
                county.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
                list.add(county);
            } while (cursor.moveToNext());
        }
        return list;
    }
}
