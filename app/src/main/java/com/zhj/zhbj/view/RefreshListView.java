package com.zhj.zhbj.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

import com.zhj.zhbj.R;

/**
 * Created by HongJay on 2016/7/21.
 */
public class RefreshListView extends ListView {
    private View mHeaderView;

    public RefreshListView(Context context) {
        super(context);
        initHeadView();
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initHeadView();
    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHeadView();
    }
    private void initHeadView(){
        View  mHeaderView =   View.inflate(getContext(), R.layout.refresh_header, null);
        addHeaderView(mHeaderView);
        // 提前手动测量宽高

        mHeaderView.measure(0, 0);// 按照设置的规则测量
        int measuredHeight = mHeaderView.getMeasuredHeight();
        // 设置内边距, 可以隐藏当前控件 , -自身高度
        mHeaderView.setPadding(0, -measuredHeight, 0, 0);
    }
}
