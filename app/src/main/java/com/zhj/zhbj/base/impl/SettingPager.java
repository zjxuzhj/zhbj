package com.zhj.zhbj.base.impl;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zhj.zhbj.R;
import com.zhj.zhbj.activity.LoginActivity;
import com.zhj.zhbj.activity.MainActivity;
import com.zhj.zhbj.base.BasePager;
import com.zhj.zhbj.domain.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;

import static cn.bmob.v3.Bmob.getApplicationContext;
import static cn.jpush.android.api.d.g;

/**
 * Created by HongJay on 2016/7/16.
 */
public class SettingPager extends BasePager {

    @BindView(R.id.tv_login)
    TextView mTvLogin;
    @BindView(R.id.tv_logOut)
    TextView mTvLogOut;
    @BindView(R.id.tv_my_score)
    TextView mTvMyScore;
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

        initView();
    }

    private void initView() {
        User userInfo = BmobUser.getCurrentUser(User.class);
        if (userInfo != null) {
            mTvLogin.setVisibility(View.GONE);
            mTvLogOut.setVisibility(View.VISIBLE);
        } else {
            mTvLogin.setVisibility(View.VISIBLE);
            mTvLogOut.setVisibility(View.GONE);
        }
        mTvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                ((MainActivity) mActivity).getContentFragment().startActivityForResult(intent, 1);
            }
        });
        mTvLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLogOut();
            }
        });
        if(userInfo!=null) {
            mTvMyScore.setText("我的积分                " + userInfo.getScore());
        }
    }

    private void setLogOut() {
        User userInfo = BmobUser.getCurrentUser(User.class);
        userInfo.logOut();
        Toast.makeText(mActivity, "账户成功登出！", Toast.LENGTH_SHORT).show();
        mTvMyScore.setVisibility(View.GONE);
        mTvLogin.setVisibility(View.VISIBLE);
        mTvLogOut.setVisibility(View.GONE);
    }

    public void setLogIn() {
        User userInfo = BmobUser.getCurrentUser(User.class);
        String username = userInfo.getUsername();
        Toast.makeText(mActivity, "欢迎" + username + "的登录！", Toast.LENGTH_SHORT).show();
        mTvMyScore.setText("我的积分                "+ userInfo.getScore());
        mTvMyScore.setVisibility(View.VISIBLE);
        mTvLogin.setVisibility(View.GONE);
        mTvLogOut.setVisibility(View.VISIBLE);
    }
}
