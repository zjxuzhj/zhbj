package com.zhj.zhbj.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by HongJay on 2016/7/20.
 */
public class TabViewPager extends ViewPager {

    private float startY;
    private float startX;
    private float endX;
    private float endY;

    public TabViewPager(Context context) {
        super(context);
    }

    public TabViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);
                startX = ev.getRawX();
                startY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                endX = ev.getRawX();
                endY = ev.getRawY();

                if (Math.abs(endX - startX) > Math.abs(endY - startY)) {  //左右滑动
                    if (endX > startX) {
                        if (getCurrentItem() == 0) {
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }

                    } else {
                        if (getCurrentItem() == getAdapter().getCount() - 1) {//最后一个界面
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }
                    }
                }else{
                    getParent().requestDisallowInterceptTouchEvent(false);
                }

                break;
        }

        return super.dispatchTouchEvent(ev);
    }
}
