package com.coolweather.app.model;

/**
 * Created by Aeolia on 2015/6/16.
 */
public class City {
    private int id;
    private String cityName;
    private String cityPinyin;
    private int provinceId;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
    public String getCityPinyin() {
        return cityPinyin;
    }

    public void setCityPinyin(String cityPinyin) {
        this.cityPinyin = cityPinyin;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int id) {
        this.provinceId = id;
    }
}
