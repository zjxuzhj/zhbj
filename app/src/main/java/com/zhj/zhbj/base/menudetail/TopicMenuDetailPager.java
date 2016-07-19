package com.zhj.zhbj.base.menudetail;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.zhj.zhbj.base.BaseMenuDetailPager;

/**
 * Created by HongJay on 2016/7/17.
 */
public class TopicMenuDetailPager extends BaseMenuDetailPager{
    public TopicMenuDetailPager(Activity activity) {
        super(activity);
    }

    @Override
    public View initViews() {
        TextView tv_content = new TextView(mActivity);
        tv_content.setText("菜单详情页-专题");
        tv_content.setTextColor(Color.RED);
        tv_content.setTextSize(25);
        tv_content.setGravity(Gravity.CENTER);

        return tv_content;
    }
}
