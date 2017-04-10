package com.zhj.zhbj.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by HongJay on 2016/7/8.
 */
public class PrefUtils {


    private static SharedPreferences mPref;

    public static void putBoolean(Context content, String key, boolean value) {
        if (mPref == null) {
            mPref = content.getSharedPreferences("config", content.MODE_PRIVATE);
        }
        mPref.edit().putBoolean(key, value).commit();
    }

    public static boolean getBoolean(Context content, String key, boolean defValue) {
        if (mPref == null) {
            mPref = content.getSharedPreferences("config", content.MODE_PRIVATE);
        }

        return mPref.getBoolean(key, defValue);
    }
    public static void putString(Context content, String key, String value) {
        if (mPref == null) {
            mPref = content.getSharedPreferences("config", content.MODE_PRIVATE);
        }
        mPref.edit().putString(key, value).commit();
    }

    public static String getString(Context content, String key, String defValue) {
        if (mPref == null) {
            mPref = content.getSharedPreferences("config", content.MODE_PRIVATE);
        }return mPref.getString(key, defValue);
    }
    public static void putInt(Context content, String key, int value) {
        if (mPref == null) {
            mPref = content.getSharedPreferences("config", content.MODE_PRIVATE);
        }
        mPref.edit().putInt(key, value).commit();
    }

    public static int getInt(Context content, String key, int defValue) {
        if (mPref == null) {
            mPref = content.getSharedPreferences("config", content.MODE_PRIVATE);
        }return mPref.getInt(key, defValue);
    }

    public static void remove(Context content,String key){
        if (mPref == null) {
            mPref = content.getSharedPreferences("config", content.MODE_PRIVATE);
        }
        mPref.edit().remove(key);
    }
}
