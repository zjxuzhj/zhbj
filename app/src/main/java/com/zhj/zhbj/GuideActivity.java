package com.zhj.zhbj;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.zhj.zhbj.utils.PrefUtils;

import java.util.ArrayList;

public class GuideActivity extends Activity {
    private LinearLayout ll_point;
    private View view_red_point;
    private int width;
    private   int[] imageResIds;
    private ArrayList<ImageView> mImageList = new ArrayList<ImageView>();
    private  Button btn_open;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_guide);

        btn_open = (Button) findViewById(R.id.btn_open);
        btn_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PrefUtils.setBoolean(getApplication(),"first",true);
//                SharedPreferences sharedPreferences=getSharedPreferences("config",MODE_PRIVATE);
//                sharedPreferences.edit().putBoolean("first",true).commit();
                Intent intent = new Intent(GuideActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        view_red_point = findViewById(R.id.view_red_point);
        view_red_point.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                view_red_point.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                width = ll_point.getChildAt(1).getLeft() - ll_point.getChildAt(0).getLeft();
            }
        });
        ll_point = (LinearLayout) findViewById(R.id.ll_point);
        ViewPager vp = (ViewPager) findViewById(R.id.vp);
        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


                int leftMargin = ((int) ((positionOffset + position) * width));
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view_red_point.getLayoutParams();

                params.leftMargin = leftMargin;
                view_red_point.setLayoutParams(params);

            }

            @Override
            public void onPageSelected(int position) {

                if(position==imageResIds.length-1){
                    btn_open.setVisibility(View.VISIBLE);
                }else{
                    btn_open.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        initViews();
        vp.setAdapter(new myAdapter());
    }

    private void initViews() {


        imageResIds = new int[]{R.drawable.guide_1, R.drawable.guide_2, R.drawable.guide_3};
        for (int i = 0; i < imageResIds.length; i++) {
            ImageView iv = new ImageView(this);
            iv.setBackgroundResource(imageResIds[i]);
            View point = new View(this);
            point.setBackgroundResource(R.drawable.shape_point_gray);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(10, 10);

            mImageList.add(iv);
            if (i != 0) {
                params.leftMargin = 10;
            }
            point.setLayoutParams(params);
            ll_point.addView(point);
        }
    }

    class myAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mImageList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mImageList.get(position));
            return mImageList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}