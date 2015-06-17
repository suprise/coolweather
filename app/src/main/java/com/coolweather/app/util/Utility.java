package com.coolweather.app.util;

import android.text.TextUtils;
import android.util.Log;

import com.coolweather.app.db.CoolWeatherDB;
import com.coolweather.app.model.City;
import com.coolweather.app.model.Province;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;

/**
 * Created by Aeolia on 2015/6/17.
 */
public class Utility {
    public synchronized static boolean handleProvincesWithPull(CoolWeatherDB coolWeatherDB,String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser xmlPullParser = factory.newPullParser();
                xmlPullParser.setInput(new StringReader(response));

                int eventType = xmlPullParser.getEventType();
                Log.d("Utility","xmlParserInitialized");
                while(eventType != XmlPullParser.END_DOCUMENT) {

                    String nodeName = xmlPullParser.getName();
                    Log.d("Utility","xmlParser nodeName " + nodeName);
                    switch (eventType) {
                        case XmlPullParser.START_TAG: {
                            Log.d("Utility","xmlParseStart_Tag");
                            if ("city".equals(nodeName) && xmlPullParser.getAttributeValue(null,"quName") != null) {
                                Log.d("Utility","xmlParseDoProvince");
                                Province province = new Province();
                                province.setProvinceName(xmlPullParser.getAttributeValue(null,"quName"));
                                if (coolWeatherDB.loadProvinceByName(province.getProvinceName()) == null) {
                                    Log.d("Utility","xmlParseSaveProvince");
                                    coolWeatherDB.saveProvince(province);
                                }
                            }

                            break;
                        }
                        default:
                            break;
                    }
                    eventType = xmlPullParser.next();
                }
                Log.d("Utility","xmlParseComplete");
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public synchronized static boolean handleCityWithPull(CoolWeatherDB coolWeatherDB,String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser xmlPullParser = factory.newPullParser();
                xmlPullParser.setInput(new StringReader(response));
                int eventType = xmlPullParser.getEventType();
                Log.d("Utility","xmlParserInitialized");
                while(eventType != XmlPullParser.END_DOCUMENT) {
                    String nodeName = xmlPullParser.getName();
                    Log.d("Utility","xmlParser nodeName " + nodeName);
                    switch (eventType) {
                        case XmlPullParser.START_TAG: {
                            Log.d("Utility","xmlParseStart_Tag");
                            if ("d".equals(nodeName) && xmlPullParser.getAttributeValue(null,"d2") != null) {
                                City city = new City();
                                city.setCityName(xmlPullParser.getAttributeValue(null,"d2"));
                                city.setCityPinyin(xmlPullParser.getAttributeValue(null, "d3"));
                                Log.d("Utility","xmlParseCityName "+ xmlPullParser.getAttributeValue(null,"d2"));
                                Province belongProvince = coolWeatherDB.loadProvinceByName(xmlPullParser.getAttributeValue(null,"d4"));
                                //Log.d("Utility","xmlParseProvinceName "+ belongProvince.getProvinceName());
                                if (belongProvince != null &&
                                        coolWeatherDB.loadCityByName(city.getCityName()) == null) {
                                    city.setProvinceId(belongProvince.getId());
                                    coolWeatherDB.saveCity(city);
                                    Log.d("Utility","xmlParseSaveCity "+city.getCityName());
                                }
                            }

                            break;
                        }
                        default:
                            break;
                    }
                    eventType = xmlPullParser.next();
                }
                Log.d("Utility","xmlParseComplete");
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }
}
