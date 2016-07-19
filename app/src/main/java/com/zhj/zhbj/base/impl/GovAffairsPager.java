package com.zhj.zhbj.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

import com.zhj.zhbj.base.BasePager;

/**
 * Created by HongJay on 2016/7/16.
 */
public class GovAffairsPager extends BasePager {
    public GovAffairsPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        tv_title.setText("政务");
        setSlidingMenuEnable(true);
        TextView tv_content = new TextView(mActivity);
        tv_content.setText("政务");
        tv_content.setTextColor(Color.RED);
        tv_content.setTextSize(25);
        tv_content.setGravity(Gravity.CENTER);

        fl_content.addView(tv_content);
    }
}
