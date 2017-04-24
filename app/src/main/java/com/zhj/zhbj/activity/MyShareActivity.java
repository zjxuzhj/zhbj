package com.zhj.zhbj.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.zhj.zhbj.R;
import com.zhj.zhbj.domain.User;
import com.zhj.zhbj.domain.news;
import com.zhj.zhbj.domain.share;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class MyShareActivity extends AppCompatActivity {


    @BindView(R.id.listView)
    ListView mListView;
    private String mObjectId;
    private List<share> mOrderList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_share);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {


        User currentUser = BmobUser.getCurrentUser(User.class);
        if (currentUser != null) {
            mObjectId = currentUser.getObjectId();
        }

        BmobQuery<share> query = new BmobQuery<>();
        //返回50条数据，如果不加上这条语句，默认返回10条数据
        //查询playerName叫“比目”的数据
        query.include("nid,uid");
        query.addWhereEqualTo("uid", mObjectId);
//        query.setLimit(50);
        //执行查询方法
        query.findObjects(new FindListener<share>() {
            @Override
            public void done(final List<share> object, BmobException e) {
                if (e == null) {
                    mOrderList.clear();
                    if (object != null) {
                        mOrderList = object;
                        mListView.setAdapter(new MyListAdapter());
                    }
                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });

    }

    class MyListAdapter extends BaseAdapter {

        private BitmapUtils utils;


        public MyListAdapter() {
            utils = new BitmapUtils(MyShareActivity.this);
            utils.configDefaultLoadingImage(R.drawable.pic_item_list_default);
        }

        @Override
        public int getCount() {
            return mOrderList.size();
        }

        @Override
        public share getItem(int i) {
            return mOrderList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            MyListAdapter.NewsViewHolder newsViewHolder;
            share item = getItem(position);
            if (convertView == null) {
                newsViewHolder = new MyListAdapter.NewsViewHolder();
                View view1 = View.inflate(MyShareActivity.this, R.layout.list_share_item, null);
                newsViewHolder.title = (TextView) view1.findViewById(R.id.textView);
                newsViewHolder.time = (TextView) view1.findViewById(R.id.textView2);
                newsViewHolder.score = (TextView) view1.findViewById(R.id.textView3);
                newsViewHolder.image = (ImageView) view1.findViewById(R.id.photo);
                newsViewHolder.appoitment_img = (ImageView) view1.findViewById(R.id.appoitment_img);
                newsViewHolder.title.setText(item.getNid().getTitle());
                newsViewHolder.time.setText("分享时间: "+item.getTime());
                newsViewHolder.score.setText("获得积分: "+item.getNid().getScore());
                utils.display(newsViewHolder.image, item.getNid().getImg().getUrl());
                view1.setTag(newsViewHolder);
                return view1;
            } else {
                newsViewHolder = (MyListAdapter.NewsViewHolder) convertView.getTag();
            }
            newsViewHolder.score.setText("获得积分: "+item.getNid().getScore());
            newsViewHolder.title.setText(item.getNid().getTitle());
            newsViewHolder.time.setText("分享时间: "+item.getTime());
            utils.display(newsViewHolder.image, item.getNid().getImg().getUrl());

            return convertView;
        }

        class NewsViewHolder {
            TextView title, time,score;
            ImageView image, appoitment_img;
        }

    }
}
