package com.zhj.zhbj.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zhj.zhbj.R;

import java.text.SimpleDateFormat;

/**
 * Created by HongJay on 2016/7/21.
 */
public class RefreshListView extends ListView implements AbsListView.OnScrollListener, AdapterView.OnItemClickListener {
    public static final int STATE_PULL_REFRESH = 0;
    public static final int STATE_RELEASH_REFRESH = 1;
    public static final int STATE_REFRESHING = 2;
    private View mHeaderView;
    private int startY;
    private int endY;
    private int dY;
    private int mHeaderViewHeight;
    private int paddingTop;
    private TextView tv_title;
    private TextView tv_date;
    private ImageView iv_Arr;
    private ProgressBar pb_2;
    private RotateAnimation rotateUpAnim;
    private RotateAnimation rotateDownAnim;
    private OnRefreshListener mListener;
    private View mFooterView;
    private int mFooterViewHeight;
    private int scrollState;

    public RefreshListView(Context context) {
        super(context);
        initHeadView();
        initAnimation();
        initFooterView();
    }


    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initHeadView();
        initAnimation();
        initFooterView();
    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHeadView();
        initAnimation();
        initFooterView();
    }


    private void initHeadView() {
        mHeaderView = View.inflate(getContext(), R.layout.refresh_header, null);
        addHeaderView(mHeaderView);
        // 提前手动测量宽高

        mHeaderView.measure(0, 0);// 按照设置的规则测量
        mHeaderViewHeight = mHeaderView.getMeasuredHeight();
        // 设置内边距, 可以隐藏当前控件 , -自身高度
        mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);


        tv_title = (TextView) mHeaderView.findViewById(R.id.textView3);
        tv_date = (TextView) mHeaderView.findViewById(R.id.textView4);
        iv_Arr = (ImageView) mHeaderView.findViewById(R.id.iv_arr);
        pb_2 = (ProgressBar) mHeaderView.findViewById(R.id.progressBar2);
    }

    /**
     * 初始化脚布局
     */
    private void initFooterView() {
        mFooterView = View.inflate(getContext(), R.layout.refresh_listview_footer, null);
        mFooterView.measure(0, 0);
        mFooterViewHeight = mFooterView.getMeasuredHeight();
        // 隐藏脚布局

        mFooterView.setPadding(0, -mFooterViewHeight, 0, 0);
        addFooterView(mFooterView);
        this.setOnScrollListener(this);
    }

    private int mCurrentState = 0;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = (int) ev.getRawY();
                if (mCurrentState == STATE_PULL_REFRESH) {
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (startY == -1) {
                    startY = (int) ev.getRawY();
                }
                endY = (int) ev.getRawY();
                if (startY == 0) {
                    startY = endY;
                }
                dY = endY - startY;

                if (dY > 0 && getFirstVisiblePosition() == 0) {
                    paddingTop = dY - mHeaderViewHeight;
                    mHeaderView.setPadding(0, paddingTop, 0, 0);
                    if (paddingTop >= 0 && mCurrentState != STATE_RELEASH_REFRESH) {
                        mCurrentState = STATE_RELEASH_REFRESH;
                        refreshState();
                    } else if (paddingTop < 0 && mCurrentState != STATE_PULL_REFRESH) {
                        mCurrentState = STATE_PULL_REFRESH;
                        refreshState();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                startY = -1;
                if (mCurrentState == STATE_PULL_REFRESH) {
                    mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);
                } else if (mCurrentState == STATE_RELEASH_REFRESH) {
                    mHeaderView.setPadding(0, 0, 0, 0);
                    mCurrentState = STATE_REFRESHING;
                    refreshState();
                }
                break;

        }
        return super.onTouchEvent(ev);
    }

    private void refreshState() {
        switch (mCurrentState) {
            case STATE_PULL_REFRESH:
                iv_Arr.setVisibility(VISIBLE);
                pb_2.setVisibility(INVISIBLE);
                iv_Arr.startAnimation(rotateDownAnim);
                tv_title.setText("下拉刷新");
                break;
            case STATE_RELEASH_REFRESH:
                iv_Arr.setVisibility(VISIBLE);
                pb_2.setVisibility(INVISIBLE);
                iv_Arr.startAnimation(rotateUpAnim);
                tv_title.setText("释放刷新");
                break;
            case STATE_REFRESHING:
                iv_Arr.clearAnimation();
                iv_Arr.setVisibility(INVISIBLE);
                pb_2.setVisibility(VISIBLE);
                tv_title.setText("正在刷新...");
                if (mListener != null) {
                    mListener.onRefresh();
                }

                break;
            default:
                break;
        }
    }

    private void initAnimation() {
        // 向上转, 围绕着自己的中心, 逆时针旋转0 -> -180.
        rotateUpAnim = new RotateAnimation(0f, -180f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotateUpAnim.setDuration(200);
        rotateUpAnim.setFillAfter(true); // 动画停留在结束位置
        // 向下转, 围绕着自己的中心, 逆时针旋转 -180 -> -360
        rotateDownAnim = new RotateAnimation(-180f, -360,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotateDownAnim.setDuration(200);
        rotateDownAnim.setFillAfter(true); // 动画停留在结束位置
    }

    public void setOnRefreshListener(OnRefreshListener listener) {
        this.mListener = listener;
    }

    private boolean isLoadMore = false;

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {
        if (scrollState == SCROLL_STATE_IDLE || scrollState == SCROLL_STATE_FLING) {
            if (getLastVisiblePosition() == getCount() - 1) { //滑到最后
                if (isLoadMore == false) {
                    mFooterView.setPadding(0, 0, 0, 0);
                    setSelection(getCount() - 1);
                }
                isLoadMore = true;
                if (mListener != null) {
                    mListener.onLoadMore();
                }

            }
        }
    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {

    }

    OnItemClickListener mItemClickListener;

    @Override
    public void setOnItemClickListener(
            android.widget.AdapterView.OnItemClickListener listener) {
        super.setOnItemClickListener(this);

        mItemClickListener = listener;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        if (mItemClickListener != null) {
            mItemClickListener.onItemClick(parent, view, position
                    - getHeaderViewsCount(), id);
        }
    }

    public interface OnRefreshListener {
        void onRefresh();

        void onLoadMore(); //加载下一页数据
    }

    //收起下拉刷新的控件
    public void onRefreshComplete(boolean success) {
        if (isLoadMore) {
            mFooterView.setPadding(0, -mFooterViewHeight, 0, 0);
            isLoadMore = false;
        } else {


            mCurrentState = STATE_PULL_REFRESH;
            iv_Arr.setVisibility(VISIBLE);
            pb_2.setVisibility(INVISIBLE);
            tv_title.setText("下拉刷新");
            if (success) {
                tv_date.setText("最后刷新时间：" + getTime());
            }
            mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);
        }
    }

    private String getTime() {
        long currentTimeMillis = System.currentTimeMillis();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        return format.format(currentTimeMillis);
    }
}
