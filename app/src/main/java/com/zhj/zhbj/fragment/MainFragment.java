package com.zhj.zhbj.fragment;


import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.lidroid.xutils.ViewUtils;
import com.zhj.zhbj.R;
import com.zhj.zhbj.base.BasePager;
import com.zhj.zhbj.base.impl.NewsCenterPager;
import com.zhj.zhbj.base.impl.PicturesPager;
import com.zhj.zhbj.base.impl.SettingPager;

import java.util.ArrayList;

/**
 * Created by HongJay on 2016/7/8.
 */
public class MainFragment extends BaseFragment {
    private RadioGroup rgGroup;

    private ViewPager mViewPager;
    private ArrayList<BasePager> mPagerList;
    private ContentAdapter mContentAdapter;

    @Override
    public View initViews() {
        mContentAdapter = new ContentAdapter();
        View view = View.inflate(getActivity(), R.layout.fragment_content, null);
        rgGroup = (RadioGroup) view.findViewById(R.id.rg_group);
        mViewPager = (ViewPager) view.findViewById(R.id.vp_content);
        ViewUtils.inject(this, view); //注入view和事件
        return view;
    }

    @Override
    public void initData() {

        rgGroup.check(R.id.rb_home);

        rgGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.rb_home) {
                    mViewPager.setCurrentItem(0, false);//去掉切换页面的动画
                } else if (i == R.id.rb_news) {
                    mViewPager.setCurrentItem(1, false);
                } else if (i == R.id.rb_picture) {
                    mViewPager.setCurrentItem(2, false);
                } else if (i == R.id.rb_setting) {
                    mViewPager.setCurrentItem(3, false);
                }

            }
        });
        mPagerList = new ArrayList<>();

        mPagerList.add(new NewsCenterPager(mActivity));
        mPagerList.add(new PicturesPager(mActivity));
        mPagerList.add(new PicturesPager(mActivity));
        mPagerList.add(new SettingPager(mActivity));

        mPagerList.get(1).initData();
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mPagerList.get(position).initData();
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mViewPager.setAdapter(mContentAdapter);
    }

    class ContentAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mPagerList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BasePager pager = mPagerList.get(position);
            container.addView(pager.mRootView);

            return pager.mRootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    //获取新闻中心页面
    public NewsCenterPager getNewsCenterPager() {
        return (NewsCenterPager) mPagerList.get(1);
    }
}
