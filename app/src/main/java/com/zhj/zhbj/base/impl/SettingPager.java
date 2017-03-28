package com.zhj.zhbj.base.impl;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.zhj.zhbj.R;
import com.zhj.zhbj.activity.LoginActivity;
import com.zhj.zhbj.activity.MainActivity;
import com.zhj.zhbj.base.BasePager;

import butterknife.BindView;
import butterknife.ButterKnife;

import static cn.bmob.v3.Bmob.getApplicationContext;

/**
 * Created by HongJay on 2016/7/16.
 */
public class SettingPager extends BasePager {

    @BindView(R.id.tv_login)
    TextView mTvLogin;
    private Activity mActivity;


    public SettingPager(Activity activity) {
        super(activity);
        this.mActivity = activity;

    }

    @Override
    public void initData() {
        LayoutInflater lInflater = (LayoutInflater) mActivity.getSystemService(
                Activity.LAYOUT_INFLATER_SERVICE);

        View view = lInflater.inflate(R.layout.activity_setting, null);
        ButterKnife.bind(this, view);
        fl_content.addView(view);
        mTvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                mActivity.startActivityForResult(intent,1);
            }
        });
    }
    public void setLogIn() {
        mTvLogin.setVisibility(View.GONE);
    }
}
