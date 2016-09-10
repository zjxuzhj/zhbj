package com.zhj.zhbj.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.zhj.zhbj.base.BasePager;

/**
 * Created by HongJay on 2016/7/16.
 */
public class SettingPager extends BasePager {
    public SettingPager(Activity activity) {

        super(activity);
    }

    @Override
    public void initData() {
        System.out.println("初始化setting");
        setSlidingMenuEnable(false);
        tv_title.setText("设置");
//        btn_menu.setVisibility(View.GONE);
        TextView tv_content = new TextView(mActivity);
        tv_content.setText("设置");
        tv_content.setTextColor(Color.RED);
        tv_content.setTextSize(25);
        tv_content.setGravity(Gravity.CENTER);

        fl_content.addView(tv_content);
    }
}
