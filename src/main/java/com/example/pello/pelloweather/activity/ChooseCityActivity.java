package com.example.pello.pelloweather.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pello.pelloweather.R;
import com.example.pello.pelloweather.db.PelloWeatherDB;
import com.example.pello.pelloweather.model.City;
import com.example.pello.pelloweather.model.County;
import com.example.pello.pelloweather.model.Province;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pello on 2016/2/24.
 */
public class ChooseCityActivity extends Activity  {


    public static final int LEVEL_PROVINCE = 1;
    public static final int LEVEL_CITY = 2;
    public static final int LEVEL_COUNTY = 3;

    private List<String> dataList = new ArrayList<String>();
    private List<Province> provinceList;
    private List<City> cityList;
    private List<County> countyList;

    private Province selectedProvince;
    private City selectedCity;
    private County selectedCounty;

    private ListView listView;
    private TextView titleText;
    private ArrayAdapter<String> adapter;

    private PelloWeatherDB db = null;
    private int currentLevel = 1;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intentIn = getIntent();
        Boolean reChooseCity = intentIn.getBooleanExtra("rechoosecity", false);


        setContentView(R.layout.main_layout);
            db = PelloWeatherDB.getInstance(this);
            listView = (ListView) findViewById(R.id.list_view);
            titleText = (TextView) findViewById(R.id.title_text);
            db = PelloWeatherDB.getInstance(this);
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataList);
            listView.setAdapter(adapter);
            queryProvinces();
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (currentLevel == LEVEL_PROVINCE) {
                        selectedProvince = provinceList.get(position);
                        queryCities();
                    } else if (currentLevel == LEVEL_CITY) {
                        selectedCity = cityList.get(position);
                        queryCounties();
                    } else if (currentLevel == LEVEL_COUNTY) {
                        selectedCounty = countyList.get(position);
                        Intent intent = new Intent(ChooseCityActivity.this, WeatherActivity.class);
                        intent.putExtra("county_code", selectedCounty.getCountyWeatherCode());
                        startActivity(intent);
                        finish();
                    }
                }
            });

    }

    private void queryProvinces() {
        provinceList = db.loadProvinces();
        if (provinceList.size() > 0) {
            dataList.clear();
            for (Province province : provinceList) {
                dataList.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            titleText.setText("中国");
            currentLevel = LEVEL_PROVINCE;
        }else {
            Toast.makeText(this, "no province data in db", Toast.LENGTH_SHORT).show();
        }
    }

    private void queryCities() {
        cityList = db.loadCities(selectedProvince.getProvinceCode());
        if (cityList.size() > 0) {
            dataList.clear();
            for (City city : cityList) {
                dataList.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_CITY;
            titleText.setText(selectedProvince.getProvinceName());

        }else {
            Toast.makeText(this, "no city data in db", Toast.LENGTH_SHORT).show();
        }

    }

    private void queryCounties() {
        countyList = db.loadCounties(selectedCity.getCityCode());
        if (countyList.size() > 0) {
            dataList.clear();
            for (County county : countyList) {
                dataList.add(county.getCountyName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_COUNTY;
            titleText.setText(selectedCity.getCityName());
        }else {
            Toast.makeText(this, "no county data in db ", Toast.LENGTH_SHORT).show();
        }

    }

    public void onBackPressed() {
        switch (currentLevel) {
            case LEVEL_CITY:
                queryProvinces();
                break;
            case LEVEL_COUNTY:
                queryCities();
                break;
            default:
                finish();
                break;
        }
    }


}
