package com.zhj.zhbj.base.impl;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.zhj.zhbj.R;
import com.zhj.zhbj.activity.ProductDetailActivity;
import com.zhj.zhbj.adapter.CommenAdapter;
import com.zhj.zhbj.base.BasePager;
import com.zhj.zhbj.domain.User;
import com.zhj.zhbj.domain.product;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by HongJay on 2016/7/16.
 */
public class ShoppingPager extends BasePager {
    @BindView(R.id.listView)
    ListView mListView;
    private Activity mActivity;
    private MyAdapter mMyAdapter;
    private List<product> objectList;

    public ShoppingPager(Activity activity) {
        super(activity);
        this.mActivity = activity;
    }


    @Override
    public void initData() {
        LayoutInflater lInflater = (LayoutInflater) mActivity.getSystemService(
                Activity.LAYOUT_INFLATER_SERVICE);

        View view = lInflater.inflate(R.layout.fragment_shopping, null);
        ButterKnife.bind(this, view);
        fl_content.addView(view);


        BmobQuery<product> query = new BmobQuery<>();
        //返回50条数据，如果不加上这条语句，默认返回10条数据
        query.setLimit(50);
        //执行查询方法
        query.findObjects(new FindListener<product>() {
            @Override
            public void done(List<product> object, BmobException e) {
                if (e == null) {
                    if (object != null) {
                        objectList = object;
                        mMyAdapter = new MyAdapter(object);
                        mListView.setAdapter(mMyAdapter);
                        initView();
                    }
                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });


    }

    private void initView() {
        User userInfo = BmobUser.getCurrentUser(User.class);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent();
                    intent.setClass(mActivity, ProductDetailActivity.class);
                intent.putExtra("productDetail", objectList.get(i));
                    mActivity.startActivity(intent);
            }
        });
    }

    class MyAdapter extends CommenAdapter {
        private BitmapUtils bitmapUtils;
        private List<product> objectList;
        public MyAdapter(List<product> object) {
            super(object);
            objectList = object;
            bitmapUtils = new BitmapUtils(mActivity);

            bitmapUtils.configDefaultLoadingImage(R.mipmap.default_list_pic);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            product mproduct=objectList.get(position);
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(mActivity, R.layout.goods_list_item, null);
                holder.price = (TextView) convertView.findViewById(R.id.price);
                holder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
                holder.iv_photo = (ImageView) convertView.findViewById(R.id.photo);
                holder.title = (TextView) convertView.findViewById(R.id.textView);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            bitmapUtils.display(holder.iv_photo, mproduct.getImg().getUrl());
            holder.price.setText(mproduct.getScore()+" 积分");
            holder.tv_content.setText(mproduct.getName());
            holder.title.setText(mproduct.getTitle());
            return convertView;
        }

    }
    public static class ViewHolder {
        public TextView title;
        public TextView price;
        public TextView tv_content;
        public ImageView iv_photo;
    }

}
