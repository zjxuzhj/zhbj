package com.zhj.zhbj.base.menudetail;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.viewpagerindicator.TabPageIndicator;
import com.zhj.zhbj.MainActivity;
import com.zhj.zhbj.R;
import com.zhj.zhbj.base.BaseMenuDetailPager;
import com.zhj.zhbj.base.TabDetailPager;
import com.zhj.zhbj.domain.NewsData;

import java.util.ArrayList;

/**
 * Created by HongJay on 2016/7/17.
 */
public class NewsMenuDetailPager extends BaseMenuDetailPager implements ViewPager.OnPageChangeListener{
    private ViewPager mViewPager;
    private MenuDetailAdapter menuDetailAdapter;
    private ArrayList<NewsData.NewsTabData> mTabMenuList;
    private ArrayList <TabDetailPager>mPagers;
    private TabPageIndicator indicator;

    public NewsMenuDetailPager(Activity activity, ArrayList<NewsData.NewsTabData> children) {
        super(activity);
        mTabMenuList=children;
    }
    @Override
    public View initViews() {
        View view = View.inflate(mActivity, R.layout.news_menu_detail, null);
        mViewPager = (ViewPager) view.findViewById(R.id.vp_menu_pager);
        indicator = (TabPageIndicator)view.findViewById(R.id.indicator);
        indicator.setOnPageChangeListener(this);
        ViewUtils.inject(this,view);
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
        indicator.setViewPager(mViewPager);
    }
    //跳转下一个页面
    @OnClick(R.id.ib_newt)
    public void nextPage(View view){
        int currentItem = mViewPager.getCurrentItem();
        mViewPager.setCurrentItem(++currentItem);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        MainActivity mainUi= (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainUi.getSlidingMenu();
        if(position==0){
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        }else{
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

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
        public CharSequence getPageTitle(int position) {
            return mTabMenuList.get(position).title;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
           container.removeView((View)object);
        }
    }
}