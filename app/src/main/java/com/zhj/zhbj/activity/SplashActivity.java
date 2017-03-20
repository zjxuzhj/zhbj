package com.zhj.zhbj.activity;


import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;

import com.zhj.zhbj.R;


import cn.bmob.v3.Bmob;
import cn.jpush.android.api.JPushInterface;

public class SplashActivity extends AppCompatActivity {

    Handler mHandler=new Handler() ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //第一：默认初始化
        Bmob.initialize(this, "639c210afd35e933d5a9e60e9d074a95");
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        initViews();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent();
                intent.setClass(SplashActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        },2000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }

    private void initViews() {
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.rl_root);
        AlphaAnimation alpha=new AlphaAnimation(0,1);
        alpha.setDuration(2000);
        rl.startAnimation(alpha);
    }
}
