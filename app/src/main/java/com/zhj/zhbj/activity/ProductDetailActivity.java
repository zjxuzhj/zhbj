package com.zhj.zhbj.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.zhj.zhbj.R;
import com.zhj.zhbj.adapter.ObservableScrollView;
import com.zhj.zhbj.domain.product;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    }


}
