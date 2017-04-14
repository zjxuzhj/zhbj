package com.zhj.zhbj.base.impl;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.viewpagerindicator.CirclePageIndicator;
import com.zhj.zhbj.R;
import com.zhj.zhbj.activity.NewsDetailActivity;
import com.zhj.zhbj.activity.ProductDetailActivity;
import com.zhj.zhbj.base.BaseMenuDetailPager;
import com.zhj.zhbj.domain.ad;
import com.zhj.zhbj.domain.news;
import com.zhj.zhbj.domain.product;
import com.zhj.zhbj.utils.PrefUtils;
import com.zhj.zhbj.view.RefreshListView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


/**
 * Created by HongJay on 2016/7/17.
 */
public class TabDetailPager extends BaseMenuDetailPager implements ViewPager.OnPageChangeListener {

    private RefreshListView mLvList;
    private TextView tvTitle;
    private ViewPager vpTabDetail;
    private String ids;
    private CirclePageIndicator indicator;
    private String mMoreUrl;   //更多页面的地址
    private MyListAdapter myListAdapter;
    private TabAdapter tabAdapter;
    private List<news> newsdataList = new ArrayList<>();
    private List<news> topnewsList = new ArrayList<>();
    private List<ad> mAdList = new ArrayList<>();
    private Integer position;

    public TabDetailPager(Activity activity, int i) {
        super(activity);
        position = i;
    }

    @Override
    public View initViews() {
        View view = View.inflate(mActivity, R.layout.tab_detail_pager, null);

        mLvList = (RefreshListView) view.findViewById(R.id.lv_tab_detail);
        //加载头布局
        View headerView = View.inflate(mActivity, R.layout.list_header_topnews, null);
        vpTabDetail = (ViewPager) headerView.findViewById(R.id.vp_tab_detail);
        tvTitle = (TextView) headerView.findViewById(R.id.tv_title);
        ViewUtils.inject(this, view);//注入view和事件
        ViewUtils.inject(this, headerView);//注入view和事件
        mLvList.addHeaderView(headerView);
        mLvList.setDividerHeight(0);
        //设置下拉刷新接口
        mLvList.setOnRefreshListener(new RefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }

            @Override
            public void onLoadMore() {
                if (mMoreUrl != null) {
//                    getMoreDataFromServer();
                } else {
                    Toast.makeText(mActivity, "最后一页了", Toast.LENGTH_SHORT).show();
                    mLvList.onRefreshComplete(false);//收起脚布局
                }

            }
        });
        mLvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                //把点击过的新闻的id加入
                ids = PrefUtils.getString(mActivity, "read_ids", "");
                String readId = newsdataList.get(position).getId().toString();
                if (!ids.contains(readId)) {
                    ids = ids + readId + ",";
                    PrefUtils.putString(mActivity, "read_ids", ids);
                }
                changeReadColor(view);
                if(position==5){
                    Intent intent = new Intent();
                    intent.setClass(mActivity, ProductDetailActivity.class);
                    intent.putExtra("productDetail", mAdList.get(0).getPid());
                    mActivity.startActivity(intent);
                }else{
                // 跳转新闻详情页
                Intent intent = new Intent();
                intent.setClass(mActivity, NewsDetailActivity.class);
                intent.putExtra("news", newsdataList.get(position));
                System.out.println(newsdataList.get(position).getHtml().getUrl());
                mActivity.startActivity(intent);
                }
            }
        });
        indicator = (CirclePageIndicator) headerView.findViewById(R.id.indicator);

        return view;
    }

    @Override
    public void initData() {

        BmobQuery<ad> queryAd = new BmobQuery<>();
        //返回50条数据，如果不加上这条语句，默认返回10条数据
        //查询playerName叫“比目”的数据
        queryAd.include("pid");
        //执行查询方法
        queryAd.findObjects(new FindListener<ad>() {
            @Override
            public void done(final List<ad> object, BmobException e) {
                if (e == null) {
                    mAdList.clear();
                    mAdList.addAll(object);
                    BmobQuery<news> query = new BmobQuery<>();
                    //返回50条数据，如果不加上这条语句，默认返回10条数据
                    query.setLimit(50);
                    //执行查询方法
                    query.findObjects(new FindListener<news>() {
                        @Override
                        public void done(List<news> object, BmobException e) {
                            if (e == null) {
                                newsdataList.clear();
                                topnewsList.clear();
                                mLvList.onRefreshComplete(true);
                                parseData(object);
                            } else {
                                mLvList.onRefreshComplete(false);
                                Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                            }
                        }
                    });
                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

    private Handler handler;

    //解析拿到的网络数据
    private void parseData(List<news> object) {
        for (news newsBean : object) {
            if (newsBean.getType() == 1) {
                if (newsBean.getTabType() == position) {
                    newsdataList.add(newsBean);

                }
            } else if (newsBean.getType() == 2) {
                topnewsList.add(newsBean);
            }
        }
        if(newsdataList.size()>5){
            if(mAdList.size()>0) {
                product product = mAdList.get(0).getPid();
                news news = new news(product.getTitle(),product.getImg(),product.getScore(),product.getCreatedAt());
                newsdataList.add(5,news);
            }

        }
        myListAdapter = new MyListAdapter();
        mLvList.setAdapter(myListAdapter);

        if (!false) {
            if (topnewsList != null) {
                tabAdapter = new TabAdapter();
                indicator.setOnPageChangeListener(TabDetailPager.this);
                vpTabDetail.setAdapter(tabAdapter);
                indicator.onPageSelected(0);
                tvTitle.setText(topnewsList.get(0).getTitle());
                indicator.setViewPager(vpTabDetail);
                indicator.setSnap(true);
            }
            if (mLvList != null) {
                //填充新闻列表数据
                myListAdapter = new MyListAdapter();
                mLvList.setAdapter(myListAdapter);
            }
            if (handler == null) {
                handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        int currentItem = vpTabDetail.getCurrentItem();
                        if (currentItem < topnewsList.size() - 1) {
                            currentItem++;
                        } else {
                            currentItem = 0;
                        }
                        vpTabDetail.setCurrentItem(currentItem); //切换到下一个页面
                        handler.sendEmptyMessageDelayed(0, 3000); //循环发送消息。
                    }
                };
                handler.sendEmptyMessageDelayed(0, 3000);
            }
        } else {
            myListAdapter.notifyDataSetChanged();
            tabAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        tvTitle.setText(topnewsList.get(position).getTitle());
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    class TabAdapter extends PagerAdapter {

        private BitmapUtils utils;

        public TabAdapter() {
            utils = new BitmapUtils(mActivity);
            utils.configDefaultLoadingImage(R.drawable.topnews_item_default);
        }

        @Override
        public int getCount() {
            return topnewsList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            final ImageView image = new ImageView(mActivity);
            image.setImageResource(R.drawable.topnews_item_default);
            image.setScaleType(ImageView.ScaleType.FIT_XY);//基于控件大小填充图片
            utils.display(image, topnewsList.get(position).getImg().getUrl()); //传递image对象和图片地址

            container.addView(image);

            image.setOnTouchListener(new TopNewsTouchListener());
            return image;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    //头条新闻的触摸监听
    class TopNewsTouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    handler.removeMessages(0);

                    break;
                case MotionEvent.ACTION_CANCEL:
                    handler.sendEmptyMessageDelayed(0, 3000);
                    break;
                case MotionEvent.ACTION_UP:
                    handler.sendEmptyMessageDelayed(0, 3000);
                    // 跳转新闻详情页
                    Intent intent = new Intent();
                    intent.setClass(mActivity, NewsDetailActivity.class);
                    intent.putExtra("news", newsdataList.get(position));
                    System.out.println(newsdataList.get(position).getHtml().getUrl());
                    mActivity.startActivity(intent);
                    break;
            }

            return true;
        }
    }

    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_AD = 1;

    class MyListAdapter extends BaseAdapter {

        private BitmapUtils utils;


        public MyListAdapter() {
            utils = new BitmapUtils(mActivity);
            utils.configDefaultLoadingImage(R.drawable.pic_item_list_default);
        }

        @Override
        public int getCount() {
            return newsdataList.size();
        }

        @Override
        public news getItem(int i) {
            return newsdataList.get(i);
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 5) {
                return TYPE_AD;
            } else {
                return TYPE_NORMAL;
            }
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            NewsViewHolder newsViewHolder;
            AdViewHolder adViewHolder;
            switch (getItemViewType(position)) {
                case TYPE_NORMAL:
                    news item = getItem(position);
                    if (convertView == null) {
                        newsViewHolder = new NewsViewHolder();
                        View view1 = View.inflate(mActivity, R.layout.list_news_item, null);
                        newsViewHolder.title = (TextView) view1.findViewById(R.id.textView);
                        newsViewHolder.time = (TextView) view1.findViewById(R.id.textView2);
                        newsViewHolder.image = (ImageView) view1.findViewById(R.id.photo);
                        newsViewHolder.appoitment_img = (ImageView) view1.findViewById(R.id.appoitment_img);
                        newsViewHolder.title.setText(item.getTitle());
                        newsViewHolder.time.setText(item.getPubdate());
                        utils.display(newsViewHolder.image, item.getImg().getUrl());
                        view1.setTag(newsViewHolder);
                        return view1;
                    } else {
                        newsViewHolder = (NewsViewHolder) convertView.getTag();
                    }
                    newsViewHolder.title.setText(item.getTitle());
                    newsViewHolder.time.setText(item.getPubdate());
                    utils.display(newsViewHolder.image, item.getImg().getUrl());

                    String ids = PrefUtils.getString(mActivity, "read_ids", "");
                    if (ids.contains(getItem(position).getId().toString())) {
                        newsViewHolder.title.setTextColor(Color.GRAY);
                    } else {
                        newsViewHolder.title.setTextColor(Color.BLACK);
                    }
                    break;
                case TYPE_AD:
                    news itemAd = getItem(position);
                    if (convertView == null) {
                        adViewHolder = new AdViewHolder();
                        convertView = View.inflate(mActivity, R.layout.ad_list_item, null);
                        adViewHolder.iv_photo = (ImageView) convertView.findViewById(R.id.photo);
                        adViewHolder.title = (TextView) convertView.findViewById(R.id.textView);
                        convertView.setTag(adViewHolder);
                    } else {
                        adViewHolder = (AdViewHolder) convertView.getTag();
                    }
                    utils.display(adViewHolder.iv_photo, itemAd.getImg().getUrl());
                    adViewHolder.title.setText(itemAd.getTitle());

                    break;
            }
            return convertView;
        }

        class NewsViewHolder {
            TextView title, time;
            ImageView image,appoitment_img;
        }

        class AdViewHolder {
            public TextView title;
            public TextView price;
            public TextView tv_content;
            public ImageView iv_photo;
        }
    }

    private void changeReadColor(View view) {
        TextView textView = (TextView) view.findViewById(R.id.textView);
        textView.setTextColor(Color.GRAY);
    }

    public static class ViewHolder {
        public TextView title;
        public TextView time;
        public ImageView image;
    }

}
