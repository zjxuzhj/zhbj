package com.zhj.zhbj.base.impl;

import android.app.Activity;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zhj.zhbj.R;
import com.zhj.zhbj.adapter.CommenAdapter;
import com.zhj.zhbj.adapter.ViewHolder;
import com.zhj.zhbj.base.BasePager;
import com.zhj.zhbj.domain.User;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;

/**
 * Created by HongJay on 2016/7/16.
 */
public class ShoppingPager extends BasePager {
    @BindView(R.id.listView)
    ListView mListView;
    private Activity mActivity;
    private MyAdapter mMyAdapter;
    private ArrayList<String> a = new ArrayList<>();

    public ShoppingPager(Activity activity) {
        super(activity);
        this.mActivity = activity;
        a.add("a");
        a.add("a");
        a.add("a");
    }


    @Override
    public void initData() {
        LayoutInflater lInflater = (LayoutInflater) mActivity.getSystemService(
                Activity.LAYOUT_INFLATER_SERVICE);

        View view = lInflater.inflate(R.layout.fragment_shopping, null);
        ButterKnife.bind(this, view);
        fl_content.addView(view);

        initView();
        mMyAdapter = new MyAdapter(a);

        mListView.setAdapter(mMyAdapter);
    }

    private void initView() {
        User userInfo = BmobUser.getCurrentUser(User.class);

    }

    class MyAdapter extends CommenAdapter {

        public MyAdapter(List data) {
            super(data);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {


            ViewHolder holder = ViewHolder.get(mActivity, R.layout.goods_list_item, convertView, viewGroup);


            TextView tv = holder.getView(R.id.title);
            ImageView iv_photo = holder.getView(R.id.photo);
            tv.setText("aaaa");
            return holder.getConvertView();
        }
    }

}
