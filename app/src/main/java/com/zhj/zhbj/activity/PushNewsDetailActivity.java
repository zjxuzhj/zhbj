package com.zhj.zhbj.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.zhj.zhbj.R;
import com.zhj.zhbj.domain.User;
import com.zhj.zhbj.domain.comment;
import com.zhj.zhbj.domain.news;
import com.zhj.zhbj.domain.share;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by HongJay on 2016/7/25.
 */
public class PushNewsDetailActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.et_input_comment)
    EditText mEtInputComment;
    @BindView(R.id.ib_show_comment)
    ImageButton mIbShowComment;
    @BindView(R.id.tv_send)
    TextView mTvSend;
    private WebView mWebView;
    private ImageButton btnBack;
    private ImageButton btnSize;
    private ImageButton btnShare;

    private ProgressBar pbProgress;
    private WebSettings settings;
    private news mCurrentNews;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        ShareSDK.initSDK(PushNewsDetailActivity.this, "154352dbcb695");
        setContentView(R.layout.activity_news_detail);
        ButterKnife.bind(this);
        User bmobUser = User.getCurrentUser(User.class);
        if (bmobUser == null) {
            Toast.makeText(PushNewsDetailActivity.this, "当前未登录，无法通过分享获得积分！", Toast.LENGTH_SHORT).show();
        }
        //通过点击得到当前热点新闻标题
        Bundle bundle = getIntent().getExtras();
        String title = bundle.getString(JPushInterface.EXTRA_ALERT);

        mWebView = (WebView) findViewById(R.id.wv_web);
        btnBack = (ImageButton) findViewById(R.id.btn_back);
        pbProgress = (ProgressBar) findViewById(R.id.pb_header);
        btnSize = (ImageButton) findViewById(R.id.btn_size);
        btnShare = (ImageButton) findViewById(R.id.btn_share);
        btnBack.setOnClickListener(this);
        btnSize.setOnClickListener(this);
        btnShare.setOnClickListener(this);
        BmobQuery<news> query = new BmobQuery<>();
        query.addWhereEqualTo("title", title);
        //执行查询方法
        query.findObjects(new FindListener<news>() {
            @Override
            public void done(List<news> object, BmobException e) {
                if (e == null) {
                    if(object!=null&&object.size()>0){
                        mCurrentNews = object.get(0);

                        settings = mWebView.getSettings();
                        settings.setJavaScriptEnabled(true);// 表示支持js
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
                        mWebView.loadUrl(mCurrentNews.getHtml().getUrl());// 加载网页

                        mIbShowComment.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                intent.setClass(PushNewsDetailActivity.this, CommentActivity.class);
                                intent.putExtra("nid", mCurrentNews.getObjectId());
                                startActivity(intent);
                            }
                        });
                        mTvSend.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                User user = BmobUser.getCurrentUser(User.class);
                                if(user!=null) {
                                    if (!mEtInputComment.getText().toString().equals("")) {
                                        comment comment = new comment();
                                        comment.setUid(user);
                                        comment.setNid(mCurrentNews);
                                        comment.setMessage(mEtInputComment.getText().toString());
                                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
                                        comment.setTime(df.format(new Date()));
                                        comment.save(new SaveListener<String>() {
                                            @Override
                                            public void done(String s, BmobException e) {
                                                if (e == null) {
                                                    Toast.makeText(PushNewsDetailActivity.this, "评论成功！", Toast.LENGTH_SHORT).show();
                                                    mEtInputComment.setText("");
                                                } else {
                                                    Toast.makeText(PushNewsDetailActivity.this, "评论发表失败，请检查网络！", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                }else{
                                    Toast.makeText(PushNewsDetailActivity.this, "登录账户后才可以进行评论", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                } else {
//                    mLvList.onRefreshComplete(false);
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
        mCurrentNews = (news) getIntent().getExtras().get("news");

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_back) {
            finish();
        } else if (view.getId() == R.id.btn_size) {
            showTextSizeDialog();
        } else if (view.getId() == R.id.btn_share) {
            showShare();
        }
    }

    private int mCurrentSizeIndex = 2; //点击确定前选择的值
    private int mCurrentItem = 2;  //点击确定后选择的值

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

    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
        oks.setTitle("分享新闻");
        // titleUrl是标题的网络链接，QQ和QQ空间等使用
        oks.setTitleUrl(mCurrentNews.getHtml().getUrl());
        // text是分享文本，所有平台都需要这个字段
        oks.setText(mCurrentNews.getTitle());
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(mCurrentNews.getHtml().getUrl());
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(mCurrentNews.getHtml().getUrl());
        oks.setCallback(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                User bmobUser = User.getCurrentUser(User.class);
                if (bmobUser == null) {
                    Toast.makeText(PushNewsDetailActivity.this, "当前未登录，无法获得分享积分！", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Toast.makeText(PushNewsDetailActivity.this, "恭喜您分享成功，您得到 " + mCurrentNews.getScore() + " 点积分，继续努力！", Toast.LENGTH_LONG).show();
                    final User newUser = new User();
                    newUser.setScore(bmobUser.getScore() + mCurrentNews.getScore());
                    newUser.update(bmobUser.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Log.i("bmob", "更新用户信息成功");
                                //保存分享数据
                                        share share = new share();
                                        share.setUid(newUser);
                                        share.setNid(mCurrentNews);
                                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
                                        share.setTime(df.format(new Date()));
                                        share.save(new SaveListener<String>() {
                                            @Override
                                            public void done(String s, BmobException e) {
                                                if (e == null) {
                                                    Log.i("bmob", "分享保存成功");
                                                } else {
                                                    Log.i("bmob", "保存失败：" + e.getMessage());
                                                }
                                            }
                                        });
                                    }
                        }
                    });
                }
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {

            }

            @Override
            public void onCancel(Platform platform, int i) {
                Toast.makeText(PushNewsDetailActivity.this, "分享失败", Toast.LENGTH_SHORT).show();
            }
        });
        // 启动分享GUI
        oks.show(this);
    }
}
