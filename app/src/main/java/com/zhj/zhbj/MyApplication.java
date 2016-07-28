package com.zhj.zhbj;

import android.app.Application;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by HongJay on 2016/7/28.
 * 自定义Application
 */
public class MyApplication  extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }
}
