package com.zhj.zhbj;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;

/**
 * Created by HongJay on 2016/7/25.
 */
public class NewsDetailActivity extends Activity implements View.OnClickListener {
    private WebView mWebView;
    private ImageButton btnBack;
    private ImageButton btnSize;
    private ImageButton btnShare;

    private ProgressBar pbProgress;
    private WebSettings settings;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_news_detail);


        mWebView = (WebView) findViewById(R.id.wv_web);
        btnBack = (ImageButton) findViewById(R.id.btn_back);
        pbProgress = (ProgressBar) findViewById(R.id.pb_header);
        btnSize = (ImageButton) findViewById(R.id.btn_size);
        btnShare = (ImageButton) findViewById(R.id.btn_share);
        btnBack.setOnClickListener(this);
        btnSize.setOnClickListener(this);
        btnShare.setOnClickListener(this);

        String url = getIntent().getStringExtra("url");
        System.out.println("url" + url);
        settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);// 表示支持js
        settings.setBuiltInZoomControls(true);// 显示放大缩小按钮
        settings.setUseWideViewPort(true);// 支持双击缩放

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {

                super.onPageStarted(view, url, favicon);
                pbProgress.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {

                super.onPageFinished(view, url);
                pbProgress.setVisibility(View.INVISIBLE);
            }
        });
        mWebView.loadUrl(url);// 加载网页
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_size:
                showTextSizeDialog();
                break;
        }

    }

    private int mCurrentSizeIndex= 2 ; //点击确定前选择的值
    private  int mCurrentItem=2 ;  //点击确定后选择的值

    private void showTextSizeDialog() {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String items[] = new String[]{"超大号字体", "大号字体", "正常字体", "小号字体", "超小号字体"};
        builder.setSingleChoiceItems(items, mCurrentSizeIndex, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mCurrentItem = i;
            }
        });
        builder.setTitle("字体设置");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // 点击按钮事件

                switch (mCurrentItem) {
                    case 0:
                        settings.setTextSize(WebSettings.TextSize.LARGEST);
                        break;
                    case 1:
                        settings.setTextSize(WebSettings.TextSize.LARGER);
                        break;
                    case 2:
                        settings.setTextSize(WebSettings.TextSize.NORMAL);
                        break;
                    case 3:
                        settings.setTextSize(WebSettings.TextSize.SMALLER);
                        break;
                    case 4:
                        settings.setTextSize(WebSettings.TextSize.SMALLEST);
                        break;
                }
                mCurrentSizeIndex = mCurrentItem;
            }
        });
        builder.setNegativeButton("取消", null);
        builder.create();
        builder.show();

    }
}
