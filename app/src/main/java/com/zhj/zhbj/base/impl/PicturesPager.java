package com.zhj.zhbj.base.impl;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.zhj.zhbj.R;
import com.zhj.zhbj.activity.NewsDetailActivity;
import com.zhj.zhbj.base.BasePager;
import com.zhj.zhbj.domain.news;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by HongJay on 2016/7/16.
 */
public class PicturesPager extends BasePager {

    private PhotoAdapter mAdapter;

    private ListView lvPhoto;
    private GridView gvPhoto;
    private List<news> picNewsList = new ArrayList<>();

    public PicturesPager(Activity activity) {
        super(activity);
        initView();
        btnPhoto.setVisibility(View.VISIBLE);
        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeDisplay();
            }
        });
    }

    private boolean isListDiaplay = false;

    private void changeDisplay() {
        if (isListDiaplay) {
            isListDiaplay = false;
            lvPhoto.setVisibility(View.VISIBLE);
            gvPhoto.setVisibility(View.INVISIBLE);
            btnPhoto.setImageResource(R.drawable.icon_pic_grid_type);
        } else {
            isListDiaplay = true;
            lvPhoto.setVisibility(View.INVISIBLE);
            gvPhoto.setVisibility(View.VISIBLE);
            btnPhoto.setImageResource(R.drawable.icon_pic_list_type);
        }
    }

    @Override
    public void initData() {

        tv_title.setText("组图");
        fl_content.removeAllViews();

//        String cache = CacheUtils.getCache(GlobalConstant.PHOTOS_URL, mActivity);
//        if (!TextUtils.isEmpty(cache)) {
//            parseData(cache);
//        }
        getDataFromServer();
        fl_content.addView(initView());
    }


    public View initView() {
        View view = View.inflate(mActivity, R.layout.menu_photo_pager, null);
        lvPhoto = (ListView) view.findViewById(R.id.lv_photo);
        gvPhoto = (GridView) view.findViewById(R.id.gv_photo);
        lvPhoto.setDividerHeight(0);
        lvPhoto.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                // 跳转新闻详情页
                Intent intent = new Intent();
                intent.setClass(mActivity, NewsDetailActivity.class);
                intent.putExtra("news", picNewsList.get(i));
                System.out.println(picNewsList.get(i).getHtml().getUrl());
                mActivity.startActivity(intent);
            }
        });
        gvPhoto.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                // 跳转新闻详情页
                Intent intent = new Intent();
                intent.setClass(mActivity, NewsDetailActivity.class);
                intent.putExtra("news", picNewsList.get(i));
                System.out.println(picNewsList.get(i).getHtml().getUrl());
                mActivity.startActivity(intent);
            }
        });
        return view;
    }


    private void getDataFromServer() {
        BmobQuery<news> query = new BmobQuery<>();
        //返回50条数据，如果不加上这条语句，默认返回10条数据
        query.setLimit(50);
        //执行查询方法
        query.findObjects(new FindListener<news>() {
            @Override
            public void done(List<news> object, BmobException e) {
                if (e == null) {
                    for (news newsBean : object) {
                        if (newsBean.getType() == 3) {
                            picNewsList.add(newsBean);
                        }
                    }

                    if (picNewsList != null) {
                        mAdapter = new PhotoAdapter();
                        lvPhoto.setAdapter(mAdapter);
                        gvPhoto.setAdapter(mAdapter);
                    }
                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });

    }

    class PhotoAdapter extends BaseAdapter {
        private BitmapUtils bitmapUtils;


        public PhotoAdapter() {
            bitmapUtils = new BitmapUtils(mActivity);

            bitmapUtils.configDefaultLoadingImage(R.drawable.news_pic_default);
        }

        @Override
        public int getCount() {
            return picNewsList.size();
        }

        @Override
        public Object getItem(int i) {
            return picNewsList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(mActivity, R.layout.list_photo_item, null);
                holder.title = (TextView) convertView.findViewById(R.id.tv_title);
                holder.image = (ImageView) convertView.findViewById(R.id.iv_pic);
                bitmapUtils.display(holder.image, picNewsList.get(i).getImg().getUrl());
                holder.title.setText(picNewsList.get(i).getTitle());

                convertView.setTag(holder);
                return convertView;
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            bitmapUtils.display(holder.image, picNewsList.get(i).getImg().getUrl());
            holder.title.setText(picNewsList.get(i).getTitle());
            return convertView;
        }

    }

    public static class ViewHolder {
        public TextView title;
        public ImageView image;
    }
}
