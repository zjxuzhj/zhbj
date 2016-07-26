package com.zhj.zhbj.base;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.lidroid.xutils.view.annotation.ViewInject;
import com.viewpagerindicator.CirclePageIndicator;
import com.zhj.zhbj.NewsDetailActivity;
import com.zhj.zhbj.R;
import com.zhj.zhbj.domain.NewsData;
import com.zhj.zhbj.domain.TabData;
import com.zhj.zhbj.global.GlobalConstant;
import com.zhj.zhbj.utils.CacheUtils;
import com.zhj.zhbj.utils.PrefUtils;
import com.zhj.zhbj.view.RefreshListView;

import java.util.ArrayList;


/**
 * Created by HongJay on 2016/7/17.
 */
public class TabDetailPager extends BaseMenuDetailPager implements ViewPager.OnPageChangeListener {

    private RefreshListView mLvList;
    @ViewInject(R.id.tv_title)
    private TextView tvTitle;
    @ViewInject(R.id.vp_tab_detail)
    private ViewPager vpTabDetail;
    NewsData.NewsTabData mTabData;
    private TextView tv_content;
    private String mUrl;
    private TabData mTabData1;
    private String ids;
    private ArrayList<TabData.TabDetailData.TopNewsData> topnews;
    private CirclePageIndicator indicator;
    private ArrayList<TabData.TabDetailData.TabNewsData> mNewsList; //新闻数据集合
    private String mMoreUrl;   //更多页面的地址
    private MyListAdapter myListAdapter;
    private TabAdapter tabAdapter;

    public TabDetailPager(Activity activity, NewsData.NewsTabData newsTabData) {
        super(activity);
        mTabData = newsTabData;
        mUrl = GlobalConstant.SERVER_URL + mTabData.url;
    }

    @Override
    public View initViews() {
        View view = View.inflate(mActivity, R.layout.tab_detail_pager, null);

        mLvList = (RefreshListView) view.findViewById(R.id.lv_tab_detail);
        //加载头布局
        View headerView = View.inflate(mActivity, R.layout.list_header_topnews, null);
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
                    getMoreDataFromServer();
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
                String readId = mNewsList.get(i).id;
                if (!ids.contains(readId)) {
                    ids = ids + readId + ",";
                    PrefUtils.setString(mActivity, "read_ids", ids);
                }
                changeReadColor(view);

                // 跳转新闻详情页
                Intent intent = new Intent();
                intent.setClass(mActivity, NewsDetailActivity.class);
                intent.putExtra("url", mNewsList.get(i).url);
                System.out.println(mNewsList.get(i).url);
                mActivity.startActivity(intent);
            }
        });
        indicator = (CirclePageIndicator) headerView.findViewById(R.id.indicator);

        return view;
    }

    @Override
    public void initData() {
        String cache = CacheUtils.getCache(mUrl, mActivity);//读取缓存
        if (!TextUtils.isEmpty(cache)) {
            parseData(cache, false);
        }
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, mUrl, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                parseData(result, false);
                mLvList.onRefreshComplete(true);
                CacheUtils.setCache(mUrl, result, mActivity);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
                error.printStackTrace();
                mLvList.onRefreshComplete(false);
            }
        });

    }

    //加载下一页数据
    public void getMoreDataFromServer() {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, mMoreUrl, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                parseData(result, true);
                mLvList.onRefreshComplete(true);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
                error.printStackTrace();
                mLvList.onRefreshComplete(false);
            }
        });

    }

    private Handler handler;

    //解析拿到的网络数据
    private void parseData(String result, boolean isMore) {
        Gson gson = new Gson();
        mTabData1 = gson.fromJson(result, TabData.class);
        topnews = mTabData1.data.topnews;
        mNewsList = mTabData1.data.news;
        String more = mTabData1.data.more;
        if (!TextUtils.isEmpty(more)) {
            mMoreUrl = GlobalConstant.SERVER_URL + more;
        } else {
            mMoreUrl = null;
        }
        if (!isMore) {
            if (topnews != null) {
                tabAdapter = new TabAdapter();
                indicator.setOnPageChangeListener(this);
                vpTabDetail.setAdapter(tabAdapter);
                indicator.onPageSelected(0);
                tvTitle.setText(topnews.get(0).title);
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
                        if (currentItem < topnews.size() - 1) {
                            currentItem++;
                        } else {
                            currentItem = 0;
                        }

                        vpTabDetail.setCurrentItem(currentItem); //切换到下一个页面
                        handler.sendEmptyMessageDelayed(0, 3000); //循环发送消息。
                    };
                };

            handler.sendEmptyMessageDelayed(0, 3000);
            }
        } else {
            ArrayList<TabData.TabDetailData.TabNewsData> news = mTabData1.data.news;
            mNewsList.addAll(news);
            myListAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        tvTitle.setText(topnews.get(position).title);
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
            return topnews.size();
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
            utils.display(image, topnews.get(position).topimage); //传递image对象和图片地址

            container.addView(image);
            return image;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
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
            return mNewsList.size();
        }

        @Override
        public TabData.TabDetailData.TabNewsData getItem(int i) {
            return mNewsList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder;
            TabData.TabDetailData.TabNewsData item = getItem(i);
            if (view == null) {
                holder = new ViewHolder();
                View view1 = View.inflate(mActivity, R.layout.list_news_item, null);
                holder.title = (TextView) view1.findViewById(R.id.textView);
                holder.time = (TextView) view1.findViewById(R.id.textView2);
                holder.image = (ImageView) view1.findViewById(R.id.imageView);
                holder.title.setText(item.title);
                holder.time.setText(item.pubdate);
                utils.display(holder.image, item.listimage);
                view1.setTag(holder);
                return view1;
            } else {
                holder = (ViewHolder) view.getTag();
            }


            holder.title.setText(item.title);
            holder.time.setText(item.pubdate);
            utils.display(holder.image, item.listimage);

            String ids = PrefUtils.getString(mActivity, "read_ids", "");
            if (ids.contains(getItem(i).id)) {
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
