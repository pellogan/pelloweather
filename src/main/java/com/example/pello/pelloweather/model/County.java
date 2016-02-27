package com.example.pello.pelloweather.model;

/**
 * Created by Pello on 2016/2/22.
 */
public class County {

    private String countyName;
    private String countyCode;
    private String countyWeatherCode;
    private String cityCode;

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getCountyCode() {
        return countyCode;
    }

    public void setCountyCode(String countyCode) {
        this.countyCode = countyCode;
    }

    public String getCountyWeatherCode() {
        return countyWeatherCode;
    }

    public void setCountyWeatherCode(String weatherCode) {
        this.countyWeatherCode = weatherCode;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }
}
