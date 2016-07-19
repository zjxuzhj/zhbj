package com.zhj.zhbj.base.menudetail;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhj.zhbj.R;
import com.zhj.zhbj.base.BaseMenuDetailPager;
import com.zhj.zhbj.base.TabDetailPager;
import com.zhj.zhbj.domain.NewsData;

import java.util.ArrayList;

/**
 * Created by HongJay on 2016/7/17.
 */
public class MenuDetailPager extends BaseMenuDetailPager{
    private ViewPager mViewPager;
    private MenuDetailAdapter menuDetailAdapter;
    private ArrayList<NewsData.NewsTabData> mTabMenuList;
    private ArrayList <TabDetailPager>mPagers;

    public MenuDetailPager(Activity activity, ArrayList<NewsData.NewsTabData> children) {
        super(activity);
        mTabMenuList=children;
    }

    @Override
    public View initViews() {
        View view = View.inflate(mActivity, R.layout.news_menu_detail, null);
        mViewPager = (ViewPager) view.findViewById(R.id.vp_menu_pager);
        return view;
    }

    @Override
    public void initData() {
        mPagers = new ArrayList<TabDetailPager>();
        for (int i=0;i<mTabMenuList.size();i++){
            mPagers.add(new TabDetailPager((mActivity),mTabMenuList.get(i)));
        }
        menuDetailAdapter=new MenuDetailAdapter();
        mViewPager.setAdapter(menuDetailAdapter);
    }
    class MenuDetailAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return mTabMenuList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TabDetailPager pager = mPagers.get(position);
            container.addView(pager.mRootView);
            pager.initData();
            return pager.mRootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
           container.removeView((View)object);
        }
    }
}
