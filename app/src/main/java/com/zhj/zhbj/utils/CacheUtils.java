package com.zhj.zhbj.utils;

import android.content.Context;

/**
 * Created by HongJay on 2016/7/26.
 */
public class CacheUtils {
    public static void setCache(String key, String value, Context ctx) {
        PrefUtils.setString(ctx, key, value);
    }

    public static String getCache(String key ,Context ctx) {
        return PrefUtils.getString(ctx,key,"");
    }

}
