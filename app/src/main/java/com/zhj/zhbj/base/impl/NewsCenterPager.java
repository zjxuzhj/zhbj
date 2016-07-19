package com.zhj.zhbj.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.zhj.zhbj.MainActivity;
import com.zhj.zhbj.base.BaseMenuDetailPager;
import com.zhj.zhbj.base.BasePager;
import com.zhj.zhbj.base.menudetail.InteractMenuDetailPager;
import com.zhj.zhbj.base.menudetail.MenuDetailPager;
import com.zhj.zhbj.base.menudetail.PhotosMenuDetailPager;
import com.zhj.zhbj.base.menudetail.TopicMenuDetailPager;
import com.zhj.zhbj.domain.NewsData;
import com.zhj.zhbj.fragment.LeftMenuFragment;
import com.zhj.zhbj.global.GlobalConstant;

import java.util.ArrayList;

/**
 * Created by HongJay on 2016/7/16.
 */
public class NewsCenterPager extends BasePager {
    private ArrayList<BaseMenuDetailPager> basePagersList;
    private NewsData newsData;

    public NewsCenterPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        tv_title.setText("新闻中心");
        setSlidingMenuEnable(true);
        getDataFromServer();

    }

    //从服务器获得数据
    private void getDataFromServer() {
        HttpUtils utils = new HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET, GlobalConstant.CATEGORIES_URL, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                parseData(result);
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
        MainActivity mainUi = (MainActivity) mActivity;
        LeftMenuFragment leftFragment = mainUi.getLeftFragment();
        leftFragment.setMenuData(newsData);

        //准备四个菜单详情页
        basePagersList = new ArrayList<BaseMenuDetailPager>();
        basePagersList.add(new MenuDetailPager(mActivity, newsData.data.get(0).children));
        basePagersList.add(new TopicMenuDetailPager(mActivity));
        basePagersList.add(new PhotosMenuDetailPager(mActivity));
        basePagersList.add(new InteractMenuDetailPager(mActivity));
        setCurrentMenuDetailPager(0);
    }

    //设置当前菜单详情页
    public void setCurrentMenuDetailPager(int position) {
        BaseMenuDetailPager pager = basePagersList.get(position);
        fl_content.removeAllViews();
        fl_content.addView(pager.mRootView);

        NewsData.NewsMenuData newsMenuData = newsData.data.get(position);
        tv_title.setText(newsMenuData.title);
        pager.initData();//初始化当前页面的数据
    }
}
