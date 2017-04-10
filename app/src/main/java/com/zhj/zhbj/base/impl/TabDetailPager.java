package com.zhj.zhbj.base.impl;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.viewpagerindicator.CirclePageIndicator;
import com.zhj.zhbj.R;
import com.zhj.zhbj.activity.NewsDetailActivity;
import com.zhj.zhbj.base.BaseMenuDetailPager;
import com.zhj.zhbj.domain.TabData;
import com.zhj.zhbj.domain.news;
import com.zhj.zhbj.global.GlobalConstant;
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

    public TabDetailPager(Activity activity) {
        super(activity);
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
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //把点击过的新闻的id加入
                ids = PrefUtils.getString(mActivity, "read_ids", "");
                String readId = newsdataList.get(i).getId().toString();
                if (!ids.contains(readId)) {
                    ids = ids + readId + ",";
                    PrefUtils.putString(mActivity, "read_ids", ids);
                }
                changeReadColor(view);

                // 跳转新闻详情页
                Intent intent = new Intent();
                intent.setClass(mActivity, NewsDetailActivity.class);
                intent.putExtra("news", newsdataList.get(i));
                System.out.println(newsdataList.get(i).getHtml().getUrl());
                mActivity.startActivity(intent);
            }
        });
        indicator = (CirclePageIndicator) headerView.findViewById(R.id.indicator);

        return view;
    }

    @Override
    public void initData() {
//        String cache = CacheUtils.getCache(mUrl, mActivity);//读取缓存
//        if (!TextUtils.isEmpty(cache)) {
//            parseData(cache, false);
//        }
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

                    parseData(object);
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
                newsdataList.add(newsBean);
            } else if (newsBean.getType() == 2) {
                topnewsList.add(newsBean);
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

//                        handler.sendEmptyMessageDelayed(0, 3000); //循环发送消息。
                    }
                };
//            handler.sendEmptyMessageDelayed(0, 3000);
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
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView image = new ImageView(mActivity);
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
//                    handler.sendEmptyMessageDelayed(0, 3000);
                    break;
                case MotionEvent.ACTION_UP:
//                    handler.sendEmptyMessageDelayed(0, 3000);
                    break;
            }

            return true;
        }
    }

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
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder;
            news item = getItem(i);
            if (view == null) {
                holder = new ViewHolder();
                View view1 = View.inflate(mActivity, R.layout.list_news_item, null);
                holder.title = (TextView) view1.findViewById(R.id.textView);
                holder.time = (TextView) view1.findViewById(R.id.textView2);
                holder.image = (ImageView) view1.findViewById(R.id.imageView);
                holder.title.setText(item.getTitle());
                holder.time.setText(item.getPubdate());
                utils.display(holder.image, item.getImg().getUrl());
                view1.setTag(holder);
                return view1;
            } else {
                holder = (ViewHolder) view.getTag();
            }


            holder.title.setText(item.getTitle());
            holder.time.setText(item.getPubdate());
            utils.display(holder.image, item.getImg().getUrl());

            String ids = PrefUtils.getString(mActivity, "read_ids", "");
            if (ids.contains(getItem(i).getId().toString())) {
                holder.title.setTextColor(Color.GRAY);
            } else {
                holder.title.setTextColor(Color.BLACK);
            }

            return view;
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
