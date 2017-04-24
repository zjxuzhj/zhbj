package com.zhj.zhbj.base.impl;

import android.app.Activity;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.zhj.zhbj.base.BaseMenuDetailPager;
import com.zhj.zhbj.base.BasePager;
import com.zhj.zhbj.domain.NewsData;
import com.zhj.zhbj.global.GlobalConstant;

import java.util.ArrayList;

/**
 * Created by HongJay on 2017/3/27.
 */

public class NewsCenterPager extends BasePager {
    private ArrayList<BaseMenuDetailPager> basePagersList;

    public NewsCenterPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        basePagersList = new ArrayList<>();
        basePagersList.add(new NewsMenuDetailPager(mActivity));
        setCurrentMenuDetailPager(0);

    }

    //设置当前菜单详情页
    public void setCurrentMenuDetailPager(int position) {
        BaseMenuDetailPager pager = basePagersList.get(0);
        fl_content.removeAllViews();
        fl_content.addView(pager.mRootView);

        tv_title.setText("杭州知道");
        pager.initData();//初始化当前页面的数据

    }
}
