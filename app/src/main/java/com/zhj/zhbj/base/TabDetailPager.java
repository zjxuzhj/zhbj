package com.zhj.zhbj.base;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.zhj.zhbj.domain.NewsData;

/**
 * Created by HongJay on 2016/7/17.
 */
public class TabDetailPager extends  BaseMenuDetailPager {
    NewsData.NewsTabData mTabData;
    private TextView tv_content;

    public TabDetailPager(Activity activity, NewsData.NewsTabData newsTabData) {
        super(activity);
        mTabData=newsTabData;
    }

    @Override
    public View initViews() {
        tv_content = new TextView(mActivity);
        tv_content.setText("页签详情页");
        tv_content.setTextColor(Color.RED);
        tv_content.setTextSize(25);
        tv_content.setGravity(Gravity.CENTER);

        return tv_content;
    }

    @Override
    public void initData() {
        tv_content.setText(mTabData.title);
    }
}
