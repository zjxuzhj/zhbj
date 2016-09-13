package com.zhj.zhbj.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.zhj.zhbj.R;
import com.zhj.zhbj.base.BasePager;

import java.util.zip.Inflater;

/**
 * Created by HongJay on 2016/7/16.
 */
public class HomePager extends BasePager {
    private Activity mActivity;
    public HomePager(Activity activity) {
        super(activity);
        this.mActivity=activity;
    }

    @Override
    public void initData() {
        setSlidingMenuEnable(false);
        /*tv_title.setText("组图");
//        btn_menu.setVisibility(View.GONE);
        TextView tv_content = new TextView(mActivity);
        tv_content.setText("组图");
        tv_content.setTextColor(Color.RED);
        tv_content.setTextSize(25);
        tv_content.setGravity(Gravity.CENTER);*/
        LayoutInflater lInflater = (LayoutInflater)mActivity.getSystemService(
                Activity.LAYOUT_INFLATER_SERVICE);

        View  view = lInflater.inflate(R.layout.activity_setting, null);

        fl_content.addView(view);


    }
}
