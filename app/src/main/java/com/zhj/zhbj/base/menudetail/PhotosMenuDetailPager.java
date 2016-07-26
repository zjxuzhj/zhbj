package com.zhj.zhbj.base.menudetail;

import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.zhj.zhbj.R;
import com.zhj.zhbj.base.BaseMenuDetailPager;
import com.zhj.zhbj.domain.PhotosData;
import com.zhj.zhbj.global.GlobalConstant;
import com.zhj.zhbj.utils.CacheUtils;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by HongJay on 2016/7/17.
 */
public class PhotosMenuDetailPager extends BaseMenuDetailPager {

    private ArrayList<PhotosData.PhotoInfo> mPhotoList;
    private PhotosData data;
    private PhotoAdapter mAdapter;
    private ListView lvPhoto;
    private GridView gvPhoto;
    private ImageButton btnPhoto;
    public PhotosMenuDetailPager(Activity activity, ImageButton btnPhoto) {
        super(activity);
        this.btnPhoto=btnPhoto;
        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    changeDisplay();
            }
        });
    }
private boolean isListDiaplay=false;
    private void changeDisplay() {
        if(isListDiaplay){
            isListDiaplay = false;
            lvPhoto.setVisibility(View.VISIBLE);
            gvPhoto.setVisibility(View.INVISIBLE);
            btnPhoto.setImageResource(R.drawable.icon_pic_grid_type);
        }else{
            isListDiaplay = true;
            lvPhoto.setVisibility(View.INVISIBLE);
            gvPhoto.setVisibility(View.VISIBLE);
            btnPhoto.setImageResource(R.drawable.icon_pic_list_type);
        }
    }

    @Override
    public View initViews() {
        View view = View.inflate(mActivity, R.layout.menu_photo_pager, null);
        lvPhoto = (ListView) view.findViewById(R.id.lv_photo);
        gvPhoto = (GridView) view.findViewById(R.id.gv_photo);
        lvPhoto.setDividerHeight(0);

        return view;
    }

    @Override
    public void initData() {
        String cache = CacheUtils.getCache(GlobalConstant.PHOTOS_URL, mActivity);
        if (!TextUtils.isEmpty(cache)) {
            parseData(cache);
        }
        getDataFromServer();
    }

    private void getDataFromServer() {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, GlobalConstant.PHOTOS_URL, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                parseData(result);
                CacheUtils.setCache(GlobalConstant.PHOTOS_URL,result,mActivity);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
                error.printStackTrace();
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
        public PhotoAdapter(){
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
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder;
            if (view == null) {
                holder = new ViewHolder();
                View view1 = View.inflate(mActivity, R.layout.list_photo_item, null);
                holder.title = (TextView) view1.findViewById(R.id.tv_title);
                holder.image = (ImageView) view1.findViewById(R.id.iv_pic);
                bitmapUtils.display(holder.image, mPhotoList.get(i).listimage);
                holder.title.setText(mPhotoList.get(i).title);
                view1.setTag(holder);
                return view1;
            } else {
                holder = (ViewHolder) view.getTag();
            }

            return view;
        }

    }

    public static class ViewHolder {
        public TextView title;
        public ImageView image;
    }
}
