package com.zhj.zhbj.base.impl;

import android.app.Activity;
import android.text.TextUtils;
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
import com.zhj.zhbj.utils.CacheUtils;

import java.util.ArrayList;

/**
 * Created by HongJay on 2017/3/27.
 */

public class NewsCenterPager extends BasePager {
    private ArrayList<BaseMenuDetailPager> basePagersList;
    private NewsData newsData;

    public NewsCenterPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
//        setSlidingMenuEnable(true);
//        String cache = CacheUtils.getCache(GlobalConstant.CATEGORIES_URL, mActivity);
//        if (!TextUtils.isEmpty(cache)) {
//            parseData(cache);
//            System.out.println("读取缓存");
//        }
        getDataFromServer();  //不管有没有缓存，都获取最新数据，保证数据最新


    }

    //从服务器获得数据
    private void getDataFromServer() {
        HttpUtils utils = new HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET, GlobalConstant.CATEGORIES_URL, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                parseData(result);
                //设置缓存
//                CacheUtils.setCache(GlobalConstant.CATEGORIES_URL, result, mActivity);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });

    }

    //解析网络数据
    protected void parseData(String result) {
        Gson gson = new Gson();
        newsData = gson.fromJson(result, NewsData.class);

        //准备四个菜单详情页
        basePagersList = new ArrayList<>();
        basePagersList.add(new NewsMenuDetailPager(mActivity, newsData.data.get(0).children));
        setCurrentMenuDetailPager(0);
    }

    //设置当前菜单详情页
    public void setCurrentMenuDetailPager(int position) {
        BaseMenuDetailPager pager = basePagersList.get(0);
        fl_content.removeAllViews();
        fl_content.addView(pager.mRootView);

        NewsData.NewsMenuData newsMenuData = newsData.data.get(position);
        tv_title.setText(newsMenuData.title);
        pager.initData();//初始化当前页面的数据

    }
}
