package com.zhj.zhbj.entry;

/**
 * Created by HongJay on 2017/4/14.
 */

public class Location {
    public static Location getInstance() {
        if (instance == null)
            instance = new Location();
        return instance;
    }

    private static Location instance = null;
    private Location() {
    }
    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    private String cityName;
}
