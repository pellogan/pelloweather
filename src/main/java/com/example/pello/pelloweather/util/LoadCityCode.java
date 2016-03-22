package com.example.pello.pelloweather.util;

import android.content.res.Resources;
import android.content.res.XmlResourceParser;

import com.example.pello.pelloweather.R;
import com.example.pello.pelloweather.db.PelloWeatherDB;
import com.example.pello.pelloweather.model.City;
import com.example.pello.pelloweather.model.County;
import com.example.pello.pelloweather.model.Province;

import org.xmlpull.v1.XmlPullParser;

/**
 * Created by Pello on 2016/2/24.
 */
public class LoadCityCode {


    public static void initCityCode(final Resources resources,final  PelloWeatherDB db) throws Exception {
//        LogUtil.d("XMLid", "--"+R.xml.cityid + "--");

        if (!db.isInited() && resources != null && db != null) {

            new Thread(new Runnable() {
                @Override
                public void run() {
//                LogUtil.d("resources", resources.toString());
                    XmlResourceParser parser = resources.getXml(R.xml.cityid);
                    try {
                        LogUtil.d("InitCityId", "start init thread");
                        int eventType = parser.getEventType();
                        String provinceCode = "";
                        String cityCode = "";
                        while (eventType != XmlResourceParser.END_DOCUMENT) {
                            switch (eventType) {
                                case XmlResourceParser.START_TAG:
                                    String nodeName = parser.getName();
                                    if ("province".equals(nodeName)) {
                                        String code = parser.getIdAttribute();
                                        String name = parser.getAttributeValue(null, "name");
                                        provinceCode = code;
                                        Province province = new Province();
                                        province.setProvinceCode(code);
                                        province.setProvinceName(name);
                                        db.saveProvince(province);
//                                LogUtil.d("ReadXml", "province :" + code + "--" + name);
                                    } else if ("city".equals(nodeName)) {
                                        String code = parser.getIdAttribute();
                                        String name = parser.getAttributeValue(null, "name");
                                        cityCode = code;
                                        City city = new City();
                                        city.setCityCode(code);
                                        city.setCityName(name);
                                        city.setProvinceCode(provinceCode);
                                        db.saveCity(city);
//                                LogUtil.d("CityId", "--" + code + "--" + name + "--" + provinceCode);
                                    } else if ("county".equals(nodeName)) {
                                        String code = parser.getIdAttribute();
                                        String name = parser.getAttributeValue(null, "name");
                                        String weatherCode = parser.getAttributeValue(null, "weatherCode");
                                        County county = new County();
                                        county.setCountyCode(code);
                                        county.setCountyName(name);
                                        county.setCountyWeatherCode(weatherCode);
                                        county.setCityCode(cityCode);
                                        db.saveCounty(county);
//                                LogUtil.d("countyId", "--" + code + "--" + name + "--" + weatherCode + "--" + cityCode);
                                    }
                                    break;
                                case XmlResourceParser.END_TAG:
                                    break;
                                case XmlPullParser.ENTITY_REF:
                                    break;
                                default:
                                    break;
                            }
                            eventType = parser.next();
                        }
                        LogUtil.d("InitCityId", "init succeed");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }





}
