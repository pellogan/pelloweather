package com.example.pello.pelloweather.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.example.pello.pelloweather.R;
import com.example.pello.pelloweather.util.HttpCallBackListener;
import com.example.pello.pelloweather.util.HttpUtil;
import com.example.pello.pelloweather.util.LogUtil;
import com.example.pello.pelloweather.util.Utility;

import com.baidu.mapapi.SDKInitializer;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by Pello on 2016/3/9.
 */
public class WeatherActivity extends Activity implements View.OnClickListener{
    public static final String MAP_KEY_WEB = "7jGdi3MBVo8ht1ukutIXdp20";

    private TextView cityName;
    private TextView weather;
    private TextView temp1;
    private TextView temp2;
    private TextView weatherDescript;
    private Button updateWeather;

    private TextView textTest1;
    private TextView textTest2;
    private TextView textTest3;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (!TextUtils.isEmpty(msg.obj.toString())) {
                showWeatherInfo();
            }
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.show_weather);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (!prefs.getBoolean("city_selected", false)) {
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            Location location = null;
            String provider = null;
            List<String> providerList = locationManager.getProviders(true);
            if (providerList.contains(locationManager.GPS_PROVIDER)) {
                provider = locationManager.GPS_PROVIDER;
            } else if (providerList.contains(locationManager.NETWORK_PROVIDER)) {
                provider = locationManager.NETWORK_PROVIDER;
            } else {
                Intent intent = new Intent(this, ChooseCityActivity.class);
                startActivity(intent);
                finish();
            }
            try {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for Activity#requestPermissions for more details.
                        return;
                    }
                }
                location = locationManager.getLastKnownLocation(provider);
                if (TextUtils.equals(provider, locationManager.GPS_PROVIDER) && location == null &&
                        providerList.contains(locationManager.NETWORK_PROVIDER)) {
                    location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);

                }
                if (location != null) {
                    StringBuilder builder = new StringBuilder();
                    builder.append("http://api.map.baidu.com/geocoder/v2/?ak=")
                            .append(MAP_KEY_WEB)
                            .append("&location=")
                            .append(location.getLatitude() + "," + location.getLongitude())
                            .append("&output=json&pois=1");
                    HttpUtil.sendHttpRequest(builder.toString(), new HttpCallBackListener() {
                        @Override
                        public void onFinish(String response) {
                            queryWeather(getCityWeatherCode(response));
                        }

                        @Override
                        public void onError(Exception e) {
                            e.printStackTrace();
                        }
                    });

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        cityName = (TextView) findViewById(R.id.city_name);
        weather = (TextView) findViewById(R.id.weather);
        temp1 = (TextView) findViewById(R.id.temp1);
        temp2 = (TextView) findViewById(R.id.temp2);
        weatherDescript = (TextView) findViewById(R.id.weather_description);
        updateWeather = (Button) findViewById(R.id.update_weather);

        textTest1 = (TextView) findViewById(R.id.text_test1);
        textTest2 = (TextView) findViewById(R.id.text_test2);
        textTest3 = (TextView) findViewById(R.id.text_test3);

        cityName.setOnClickListener(this);
        updateWeather.setOnClickListener(this);


        String countyCode = getIntent().getStringExtra("county_code");
        if (!TextUtils.isEmpty(countyCode)) {
            queryWeather(countyCode);
        }else {
            showWeatherInfo();
        }

    }

    private String getCityWeatherCode(String response) {
        String cityWeatherCode = "101190101";
        /*try {
            JSONObject jsonObject = new JSONObject(response);


        } catch (Exception e) {
            e.printStackTrace();
        }*/




        return cityWeatherCode;
    }



    private void queryWeather(String countyWeatherCode) {

        String address = "http://weatherapi.market.xiaomi.com/wtr-v2/weather?cityId=" + countyWeatherCode;
        LogUtil.d("getWeather", address);
        HttpUtil.sendHttpRequest(address, new HttpCallBackListener() {
            @Override
            public void onFinish(String response) {
                Utility.handleWeatherResponse(WeatherActivity.this, response);
//                showWeatherInfo();
                Message msg = new Message();
                msg.obj = "saveWeatherInfoSucceed";
                handler.sendMessage(msg);
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });


    }

    private void showWeatherInfo() {
        SharedPreferences perfs = PreferenceManager.getDefaultSharedPreferences(this);
        String realtimeWeather = perfs.getString("realtime_weather","") +" pm25:"+perfs.getString("pm25","")+
                " aqi:"+perfs.getString("aqi","");
        cityName.setText(perfs.getString("city_name", ""));
        weather.setText(realtimeWeather);
        temp1.setText(perfs.getString("temp1","")+"℃ ");
        temp2.setText(perfs.getString("temp2","")+"℃");
        textTest1.setText(perfs.getString("weather_today", ""));
        textTest2.setText(perfs.getString("weather_yesterday", ""));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.update_weather:
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                String cityCode = prefs.getString("city_code", "");
                if (!TextUtils.isEmpty(cityCode)) {
                    LogUtil.d("UPDATE_WEATHER", cityCode);
                    queryWeather(cityCode);
                }
                break;
            case R.id.city_name:
                Intent intent = new Intent(WeatherActivity.this, ChooseCityActivity.class);
                intent.putExtra("rechoosecity", true);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
    }
}
