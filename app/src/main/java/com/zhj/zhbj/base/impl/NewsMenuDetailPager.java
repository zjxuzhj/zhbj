package com.zhj.zhbj.base.impl;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.lidroid.xutils.ViewUtils;
import com.viewpagerindicator.TabPageIndicator;
import com.zhj.zhbj.R;
import com.zhj.zhbj.base.BaseMenuDetailPager;

import java.util.ArrayList;

/**
 * Created by HongJay on 2016/7/17.
 */
public class NewsMenuDetailPager extends BaseMenuDetailPager implements ViewPager.OnPageChangeListener{
    private ViewPager mViewPager;
    private MenuDetailAdapter menuDetailAdapter;
    private ArrayList <TabDetailPager>mPagers;
    private TabPageIndicator indicator;
    private ImageButton ib;

    public NewsMenuDetailPager(Activity activity) {
        super(activity);
    }
    @Override
    public View initViews() {
        View view = View.inflate(mActivity, R.layout.news_menu_detail, null);
        ib = (ImageButton) view.findViewById(R.id.ib_newt);
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentItem = mViewPager.getCurrentItem();
                mViewPager.setCurrentItem(++currentItem);
            }
        });
        mViewPager = (ViewPager) view.findViewById(R.id.vp_menu_pager);
        indicator = (TabPageIndicator)view.findViewById(R.id.indicator);
        indicator.setOnPageChangeListener(this);
        ViewUtils.inject(this,view);
        return view;
    }

    @Override
    public void initData() {
        mPagers = new ArrayList<>();
        for (int i=0;i<10;i++){
            mPagers.add(new TabDetailPager((mActivity)));
        }
        menuDetailAdapter=new MenuDetailAdapter();
        mViewPager.setAdapter(menuDetailAdapter);
        indicator.setViewPager(mViewPager);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    class MenuDetailAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return 15;
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

        //得到当前页顶部的tab的标题
        @Override
        public CharSequence getPageTitle(int position) {
            CharSequence[] strings = {"杭州","中国","国际","体育","科技","生活","旅游","军事"};

            if(position>=strings.length){
                position=position%strings.length;
            }
            return strings[position];
//            return mTabMenuList.get(position).title;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
           container.removeView((View)object);
        }
    }
}
