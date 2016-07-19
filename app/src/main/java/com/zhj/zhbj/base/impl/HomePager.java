package com.zhj.zhbj.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.zhj.zhbj.R;
import com.zhj.zhbj.base.BasePager;

/**
 * Created by HongJay on 2016/7/16.
 */
public class HomePager extends BasePager {
    public HomePager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        setSlidingMenuEnable(false);
        tv_title.setText("杭州知道");
        btn_menu.setVisibility(View.GONE);
        TextView tv_content = new TextView(mActivity);
        tv_content.setText("首页");
        tv_content.setTextColor(Color.RED);
        tv_content.setTextSize(25);
        tv_content.setGravity(Gravity.CENTER);

        fl_content.addView(tv_content);


    }
}
