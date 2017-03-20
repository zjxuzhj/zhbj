package com.zhj.zhbj.base;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import com.zhj.zhbj.R;

/**
 * 主页下，五个主页面的基类。
 * Created by HongJay on 2016/7/15.
 */
public class BasePager {
    public Activity mActivity;
    public View mRootView;//布局对象
    public TextView tv_title;
    public FrameLayout fl_content;
    public ImageView btn_menu;
    public ImageButton btnPhoto;

    public BasePager(Activity activity) {
        mActivity = activity;
        initViews();
    }

    //初始化布局
    public void initViews() {
        mRootView = View.inflate(mActivity, R.layout.base_pager, null);
        tv_title = (TextView) mRootView.findViewById(R.id.tv_title);
        fl_content = (FrameLayout) mRootView.findViewById(R.id.fl_content);
        btnPhoto = (ImageButton) mRootView.findViewById(R.id.btn_photo);
    }
    //初始化数据
    public void initData() {

    }
}
