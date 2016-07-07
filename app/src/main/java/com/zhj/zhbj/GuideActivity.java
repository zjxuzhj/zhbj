package com.zhj.zhbj;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import java.util.ArrayList;

public class GuideActivity extends Activity {
private ArrayList<ImageView> mImageList=new ArrayList<ImageView>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_guide);
        ViewPager vp = (ViewPager) findViewById(R.id.vp);
        initViews();
        vp.setAdapter(new myAdapter());
    }

    private void initViews() {

        int [] imageResIds={R.drawable.guide_1,R.drawable.guide_2,R.drawable.guide_3};
        for (int i=0;i<imageResIds.length;i++){
            ImageView iv = new ImageView(this);
            iv.setBackgroundResource(imageResIds[i]);
            mImageList.add(iv);
        }
    }
    class myAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return mImageList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mImageList.get(position));
            return mImageList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
        }
    }
}
