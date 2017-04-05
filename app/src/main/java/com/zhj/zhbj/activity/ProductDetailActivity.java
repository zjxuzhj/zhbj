package com.zhj.zhbj.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.zhj.zhbj.R;

/**
 * Created by HongJay on 2017/4/5.
 */

public class ProductDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        initView();
        initData();
    }

    private void initData() {

    }

    private void initView() {
    }
}
