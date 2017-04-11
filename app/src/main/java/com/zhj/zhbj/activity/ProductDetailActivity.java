package com.zhj.zhbj.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.zhj.zhbj.R;
import com.zhj.zhbj.adapter.ObservableScrollView;
import com.zhj.zhbj.domain.Order;
import com.zhj.zhbj.domain.User;
import com.zhj.zhbj.domain.product;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by HongJay on 2017/4/5.
 */

public class ProductDetailActivity extends AppCompatActivity {
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_decs)
    TextView mTvDecs;
    @BindView(R.id.tv_title2)
    TextView mTvTitle2;
    @BindView(R.id.scrollView)
    ObservableScrollView mScrollView;
    @BindView(R.id.tv_titlebar)
    TextView mTvTitlebar;
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.iv_share)
    ImageView mIvShare;
    @BindView(R.id.layout_title)
    RelativeLayout mLayoutTitle;
    @BindView(R.id.tv_price)
    TextView mTvPrice;
    @BindView(R.id.btn_buy)
    Button mBtnBuy;
    @BindView(R.id.layout_buy)
    RelativeLayout mLayoutBuy;
    @BindView(R.id.iv_product)
    ImageView mIvProduct;
    private product mProduct;
    private BitmapUtils bitmapUtils;
    private User mCurrentUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        ButterKnife.bind(this);
        mProduct = (product) getIntent().getSerializableExtra("productDetail");
        initView();
        initData();
    }

    private void initData() {

    }

    private void initView() {
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mTvPrice.setText(mProduct.getScore()+"");
        mTvTitle.setText(mProduct.getTitle());
        mTvDecs.setText(mProduct.getName());
        mTvTitle2.setText(mProduct.getDetials());
        bitmapUtils = new BitmapUtils(ProductDetailActivity.this);

        bitmapUtils.configDefaultLoadingImage(R.mipmap.default_list_pic);
        bitmapUtils.display(mIvProduct, mProduct.getImg().getUrl());
        mBtnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ProductDetailActivity.this)
                        .setMessage("确定要购买吗？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        // 点击按钮事件
                        Integer score = mProduct.getScore();
                        mCurrentUser = BmobUser.getCurrentUser(User.class);
                        if(mCurrentUser ==null){
                            Toast.makeText(ProductDetailActivity.this, "当前未登录，请您先登录账户在进行购买。", Toast.LENGTH_SHORT).show();
                            return;
                        }else{
                            Integer currentUserScore = mCurrentUser.getScore();
                            //更新用户积分
                            if(currentUserScore>=score){
                                User newUser = new User();

                                User bmobUser = User.getCurrentUser(User.class);
                                newUser.setScore(bmobUser.getScore()-score);
                                newUser.update(bmobUser.getObjectId(), new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if (e == null) {
                                            Toast.makeText(ProductDetailActivity.this, "您的订单已经下达，急速配送中！", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Log.i("bmob", "更新用户信息失败:" + e.getMessage());
                                        }
                                    }
                                });
                            }
                           //添加订单数据
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
                            Order order = new Order();
                            order.setUid(mCurrentUser.getObjectId());
                            order.setState("配送中");
                            order.setSoldDate(df.format(new Date()));
                            order.setProductId(mProduct);
                            order.save(new SaveListener<String>() {

                                @Override
                                public void done(String objectId, BmobException e) {
                                    if(e==null){
                                        Log.i("bmob","创建数据成功：" + objectId);
                                    }else{
                                        Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                                    }
                                }
                            });

                        }

                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        // 点击按钮事件
                    }
                });
                builder.create();
                builder.show();
            }
        });

    }

}
