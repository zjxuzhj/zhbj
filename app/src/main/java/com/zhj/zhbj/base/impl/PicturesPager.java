package com.zhj.zhbj.base.impl;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.zhj.zhbj.R;
import com.zhj.zhbj.base.BasePager;
import com.zhj.zhbj.domain.PhotosData;
import com.zhj.zhbj.global.GlobalConstant;
import com.zhj.zhbj.utils.CacheUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by HongJay on 2016/7/16.
 */
public class PicturesPager extends BasePager {

    private ArrayList<PhotosData.PhotoInfo> mPhotoList;
    private PhotosData data;
    private PhotoAdapter mAdapter;

    private ListView lvPhoto;
    private GridView gvPhoto;

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

        String cache = CacheUtils.getCache(GlobalConstant.PHOTOS_URL, mActivity);
        if (!TextUtils.isEmpty(cache)) {
            parseData(cache);
        }
//        getDataFromServer();
        fl_content.addView(initView());
    }


    public View initView() {
        View view = View.inflate(mActivity, R.layout.menu_photo_pager, null);
        lvPhoto = (ListView) view.findViewById(R.id.lv_photo);
        gvPhoto = (GridView) view.findViewById(R.id.gv_photo);
        lvPhoto.setDividerHeight(0);

        return view;
    }


    private void  getDataFromServer() {
        OkHttpClient client = new OkHttpClient();
        Request request=new Request.Builder()
                .url(GlobalConstant.PHOTOS_URL)
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(mActivity, e.toString(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                mActivity.runOnUiThread(new TimerTask() {
                    @Override
                    public void run() {
                        parseData(result);
                        CacheUtils.setCache(GlobalConstant.PHOTOS_URL, result, mActivity);

                    }
                });

            }
        });

    }

    private void parseData(String result) {
        Gson gson = new Gson();
        data = gson.fromJson(result, PhotosData.class);
        //获取组图列表集合

        mPhotoList = data.data.news;
        if (mPhotoList != null) {
            mAdapter = new PhotoAdapter();
            lvPhoto.setAdapter(mAdapter);
            gvPhoto.setAdapter(mAdapter);
        }


    }

    class PhotoAdapter extends BaseAdapter {
        private BitmapUtils bitmapUtils;


        public PhotoAdapter() {
            bitmapUtils=new BitmapUtils(mActivity);

            bitmapUtils.configDefaultLoadingImage(R.drawable.news_pic_default);
        }

        @Override
        public int getCount() {
            return mPhotoList.size();
        }

        @Override
        public Object getItem(int i) {
            return mPhotoList.get(i);
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
                bitmapUtils.display(holder.image, mPhotoList.get(i).listimage);
                holder.title.setText(mPhotoList.get(i).title);

                convertView.setTag(holder);
                return convertView;
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            bitmapUtils.display(holder.image, mPhotoList.get(i).listimage);
            holder.title.setText(mPhotoList.get(i).title);
            return convertView;
        }

    }

    public static class ViewHolder {
        public TextView title;
        public ImageView image;
    }
}
