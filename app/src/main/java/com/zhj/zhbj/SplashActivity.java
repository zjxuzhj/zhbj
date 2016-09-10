package com.zhj.zhbj;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import com.zhj.zhbj.utils.PrefUtils;


import cn.jpush.android.api.JPushInterface;

public class SplashActivity extends AppCompatActivity {

    Handler mHandler=new Handler() ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

//        RotateAnimation ra = new RotateAnimation(0,360,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
//        ra.setDuration(1000);
//        ra.setFillAfter(true);
//
//        ScaleAnimation scale = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF,0.5f, Animation.RELATIVE_TO_SELF,0.5f);
//        scale.setDuration(1000);
//        scale.setFillAfter(true);
//
        AlphaAnimation alpha=new AlphaAnimation(0,1);
        alpha.setDuration(2000);

//        AnimationSet set=new AnimationSet(false);
//        set.addAnimation(ra);
//        set.addAnimation(scale);
//        set.addAnimation(alpha);
        rl.startAnimation(alpha);
//
//        set.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                SharedPreferences sp=getSharedPreferences("config",MODE_PRIVATE);
//                boolean first = PrefUtils.getBoolean(getApplication(), "first", false);
//
//
//                if(first){
//                   Intent intent=new Intent(SplashActivity.this,MainActivity.class);
//                    startActivity(intent);
//                }else{
//                    Intent intent=new Intent(SplashActivity.this,GuideActivity.class);
//                    startActivity(intent);
//                }
//
//
//                finish();
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
    }
}
