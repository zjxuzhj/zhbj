package com.zhj.zhbj.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.zhj.zhbj.R;
import com.zhj.zhbj.adapter.CommenAdapter;
import com.zhj.zhbj.base.impl.ShoppingPager;
import com.zhj.zhbj.domain.Order;
import com.zhj.zhbj.domain.comment;
import com.zhj.zhbj.domain.product;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class CommentActivity extends AppCompatActivity {
    private List<comment> mOrderList;
    @BindView(R.id.listView)
    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {

        String nid = getIntent().getStringExtra("nid");

        BmobQuery<comment> query = new BmobQuery<>();
        //返回50条数据，如果不加上这条语句，默认返回10条数据
        //查询playerName叫“比目”的数据
        query.include("nid,uid");
        query.addWhereEqualTo("nid", nid);
//        query.setLimit(50);
        //执行查询方法
        query.findObjects(new FindListener<comment>() {
            @Override
            public void done(final List<comment> object, BmobException e) {
                if (e == null) {
                    if (object != null) {
                        mOrderList = object;
                        mListView.setAdapter(new MyAdapter(mOrderList));
                    }
                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

    class MyAdapter extends CommenAdapter {

        public MyAdapter(List<comment> object) {
            super(object);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            comment commentItem= (comment) mData.get(position);
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(CommentActivity.this, R.layout.item_comment, null);
                holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                holder.tv_location_time = (TextView) convertView.findViewById(R.id.tv_location_time);
//                holder.iv_photo = (ImageView) convertView.findViewById(R.id.photo);
                holder.tv_comment = (TextView) convertView.findViewById(R.id.tv_comment);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
//            bitmapUtils.display(holder.iv_photo, mproduct.getImg().getUrl());
            holder.tv_name.setText(commentItem.getUid().getUsername());
            holder.tv_location_time.setText(commentItem.getTime());
            holder.tv_comment.setText(commentItem.getMessage());
            return convertView;
        }

    }

    public static class ViewHolder {
        public TextView tv_comment;
        public TextView tv_name;
        public TextView tv_location_time;
        public ImageView iv_photo;
    }

}
