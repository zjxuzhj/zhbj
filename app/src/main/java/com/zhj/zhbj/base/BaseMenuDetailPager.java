package com.zhj.zhbj.base;

import android.app.Activity;
import android.view.View;

/**
 * 菜单详情页基类
 * Created by HongJay on 2016/7/17.
 */
public abstract class BaseMenuDetailPager {
    public  Activity mActivity;
    public View  mRootView;
    public BaseMenuDetailPager(Activity activity){
        mActivity=activity;
        mRootView=initViews();
    }
    public abstract View initViews();
    public  void initData(){

    }
}
