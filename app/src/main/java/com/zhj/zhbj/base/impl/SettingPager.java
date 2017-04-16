package com.zhj.zhbj.base.impl;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhj.zhbj.R;
import com.zhj.zhbj.activity.AddressActivity;
import com.zhj.zhbj.activity.LoginActivity;
import com.zhj.zhbj.activity.MainActivity;
import com.zhj.zhbj.activity.MyOrderActivity;
import com.zhj.zhbj.activity.ProductDetailActivity;
import com.zhj.zhbj.base.BasePager;
import com.zhj.zhbj.domain.Order;
import com.zhj.zhbj.domain.User;
import com.zhj.zhbj.utils.PrefUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.jpush.android.api.JPushInterface;

import static cn.bmob.v3.Bmob.getApplicationContext;

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
    @BindView(R.id.tv_notice)
    TextView mTvNotice;
    @BindView(R.id.checkBox)
    CheckBox mCbNotice;
    @BindView(R.id.ll_isRight)
    RelativeLayout mRlNotice;
    @BindView(R.id.tv_address)
    TextView mTvAddress;
    @BindView(R.id.tv_order)
    TextView mTvOrder;
    @BindView(R.id.tv_feedback)
    TextView mTvFeedback;
    @BindView(R.id.tv_update)
    TextView mTvUpdate;
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
            UserIsLogIn();
        } else {
            UserIsLogOut();
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
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity)
                        .setMessage("确定要退出账号吗？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        // 点击按钮事件
                        setLogOut();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        // 点击按钮事件
                    }
                });
                builder.create();
                builder.show();

            }
        });
        if (userInfo != null) {
            mTvMyScore.setText("我的积分                " + userInfo.getScore());
        }
        mRlNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (PrefUtils.getBoolean(mActivity, "isNotice", false)) {
                    mCbNotice.setChecked(false);
                    JPushInterface.stopPush(getApplicationContext());
                    PrefUtils.putBoolean(mActivity, "isNotice", false);
                } else {
                    mCbNotice.setChecked(true);
                    JPushInterface.resumePush(getApplicationContext());
                    PrefUtils.putBoolean(mActivity, "isNotice", true);
                }
            }
        });
        mCbNotice.setChecked(PrefUtils.getBoolean(mActivity, "isNotice", true));
        mTvFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                feedback();
            }
        });
        mTvAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(mActivity, AddressActivity.class);
                mActivity.startActivity(intent);
            }
        });
        mTvUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                giveFavor();
            }
        });
        mTvOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.startActivity(new Intent(mActivity, MyOrderActivity.class));
            }
        });
    }

    private void setLogOut() {
        User userInfo = BmobUser.getCurrentUser(User.class);
        userInfo.logOut();
        Toast.makeText(mActivity, "账户成功登出！", Toast.LENGTH_SHORT).show();
        UserIsLogOut();
    }

    private void UserIsLogOut() {
        mTvMyScore.setVisibility(View.GONE);
        mTvLogin.setVisibility(View.VISIBLE);
        mTvLogOut.setVisibility(View.GONE);
        mTvAddress.setVisibility(View.GONE);
        mTvOrder.setVisibility(View.GONE);
    }

    public void setLogIn() {
        User userInfo = BmobUser.getCurrentUser(User.class);
        String username = userInfo.getUsername();
        Toast.makeText(mActivity, "欢迎" + username + "的登录！", Toast.LENGTH_SHORT).show();
        mTvMyScore.setText("我的积分                " + userInfo.getScore());
        UserIsLogIn();
    }

    private void UserIsLogIn() {
        mTvOrder.setVisibility(View.VISIBLE);
        mTvMyScore.setVisibility(View.VISIBLE);
        mTvLogin.setVisibility(View.GONE);
        mTvLogOut.setVisibility(View.VISIBLE);
        mTvAddress.setVisibility(View.VISIBLE);
    }

    //通过邮件反馈建议和意见
    private void feedback() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:zjxuzhj@gmail.com"));
        intent.putExtra(Intent.EXTRA_SUBJECT, "问题反馈");
        intent.putExtra(Intent.EXTRA_TEXT, "");
        if (intent.resolveActivity(mActivity.getPackageManager()) != null) {
            mActivity.startActivity(intent);
        } else {
            Toast.makeText(mActivity, "您的手机无法发送邮件反馈！", Toast.LENGTH_LONG).show();
        }

    }

    //去应用市场给应用评分
    private void giveFavor() {
        try {
            Uri uri = Uri.parse("market://details?id=" + mActivity.getPackageName());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mActivity.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }
}
