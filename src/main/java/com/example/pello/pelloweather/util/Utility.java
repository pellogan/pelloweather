package com.example.pello.pelloweather.util;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.preference.PreferenceManager;

import com.example.pello.pelloweather.activity.MyApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.ContextWrapper;


/**
 * Created by Pello on 2016/3/1.
 */
public class Utility {


    //解析服务器返回的天气json数据,保存到本地数据文件里
    public static void handleWeatherResponse(Context context, String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject aqiJson = jsonObject.getJSONObject("aqi");
            JSONObject realtime = jsonObject.getJSONObject("realtime");
            JSONObject todayWeather = jsonObject.getJSONObject("today");
            JSONObject yesterdayWeather = jsonObject.getJSONObject("yestoday");

            String cityName = aqiJson.getString("city");
            String cityCode = aqiJson.getString("city_id");
            String pm25 = aqiJson.getString("pm25");
            String aqi = aqiJson.getString("aqi");
            String temp1 = todayWeather.getString("tempMin");
            String temp2 = todayWeather.getString("tempMax");


            String realTimeWeather = getRealTimeWeather(realtime);
            String weatherToday = getWeatherDescription(todayWeather);
            String weatherYesterday = getWeatherDescription(yesterdayWeather);

            saveWeahterInfo(context, cityName, cityCode, realTimeWeather, weatherToday, weatherYesterday,
                    pm25, aqi, temp1, temp2);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void saveWeahterInfo(Context context, String cityName, String cityCode,
                                        String realTimeWeather, String weatherToday, String weatherYesterday,
                                        String pm25, String aqi, String temp1, String temp2) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean("city_selected", true)
                .putString("city_name", cityName)
                .putString("city_code", cityCode)
                .putString("realtime_weather", realTimeWeather)
                .putString("weather_today", weatherToday)
                .putString("weather_yesterday", weatherYesterday)
                .putString("current_date", sdf.format(new Date()))
                .putString("pm25", pm25)
                .putString("aqi", aqi)
                .putString("temp1", temp1)
                .putString("temp2", temp2)
                .commit();


    }

    private static String getRealTimeWeather(JSONObject realtime) {
        StringBuffer weather = new StringBuffer();
        try {

            weather.append(realtime.getString("temp") + "℃  ");
            weather.append(realtime.getString("weather"));
            weather.append("  湿度:" + realtime.getString("SD") + " ");
            weather.append("refreshTime:" + realtime.getString("time"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return weather.toString();
    }

    public static String getWeatherDescription(JSONObject jsonWeather) {
        StringBuffer weather = new StringBuffer();

        try {
            String tempMin = jsonWeather.getString("tempMin");
            String tempMax = jsonWeather.getString("tempMax");
            weather.append(tempMin + "℃");
            if (tempMin.equals(tempMax)) {
                weather.append(" ");
            } else {
                weather.append("~" + tempMax + "℃  ");
            }

            String weatherStart = jsonWeather.getString("weatherStart");
            String weatherEnd = jsonWeather.getString("weatherEnd");
            weather.append(weatherStart);
            if (weatherStart.equals(weatherEnd)) {
                weather.append(" , ");
            } else {
                weather.append("转" + weatherEnd + " , ");
            }

            String windStart = jsonWeather.getString("windDirectionStart");
            String windEnd = jsonWeather.getString("windDirectionEnd");
            weather.append(windStart);
            if (windStart.equals(windEnd)) {
                weather.append(".");
            } else {
                weather.append("转" + windEnd + ".");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return weather.toString();
    }



}
