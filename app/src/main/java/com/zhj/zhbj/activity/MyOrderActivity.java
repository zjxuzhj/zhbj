package com.zhj.zhbj.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.zhj.zhbj.R;
import com.zhj.zhbj.domain.Order;
import com.zhj.zhbj.domain.User;
import com.zhj.zhbj.domain.product;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class MyOrderActivity extends AppCompatActivity {

    @BindView(R.id.recycleview)
    RecyclerView mRecycleview;
    private String mObjectId;
    private List<Order> mOrderList;
    private List<product> mProductList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initData() {

        User currentUser = BmobUser.getCurrentUser(User.class);
        if (currentUser != null) {
            mObjectId = currentUser.getObjectId();
        }
        BmobQuery<Order> query = new BmobQuery<>();
        //返回50条数据，如果不加上这条语句，默认返回10条数据
        //查询playerName叫“比目”的数据
        query.include("ProductId");
        query.addWhereEqualTo("uid", "7cLiMMMs");
//        query.setLimit(50);
        //执行查询方法
        query.findObjects(new FindListener<Order>() {
            @Override
            public void done(final List<Order> object, BmobException e) {
                if (e == null) {
                    if (object != null) {
                        mOrderList = object;
                        mRecycleview.setAdapter(new MyAdapter(mOrderList));
                    }
                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });

    }

    private void initView() {
        mRecycleview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        private List<Order> mDatas;
        private BitmapUtils bitmapUtils;

        //创建构造参数，用来接受数据集
        public MyAdapter(List<Order> datas) {
            this.mDatas = datas;
            bitmapUtils = new BitmapUtils(MyOrderActivity.this);
            bitmapUtils.configDefaultLoadingImage(R.mipmap.default_list_pic);
        }

        //创建ViewHolder
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //加载布局文件，记得传入parent而不是null
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_order_list, parent, false);
            MyViewHolder viewHolder = new MyViewHolder(view);
            return viewHolder;
        }

        //绑定ViewHolder
        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            //将数据填充到具体的view中
            holder.tv_order_time.setText("下单时间: " + mDatas.get(position).getSoldDate().toString());
            holder.tv_product_name.setText("产品名称: " + mDatas.get(position).getProductId().getTitle());
            holder.tv_order_state.setText("订单状态: " + mDatas.get(position).getState());
            bitmapUtils.display(holder.imageView2, mDatas.get(position).getProductId().getImg().getUrl());
        }


        @Override
        public int getItemCount() {
            return mDatas.size();
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_order_time;
        TextView tv_product_name;
        TextView tv_order_state;
        ImageView imageView2;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_order_time = (TextView) itemView.findViewById(R.id.textView6);
            tv_product_name = (TextView) itemView.findViewById(R.id.textView7);
            tv_order_state = (TextView) itemView.findViewById(R.id.textView8);
            imageView2 = (ImageView) itemView.findViewById(R.id.imageView2);

        }
    }
}

