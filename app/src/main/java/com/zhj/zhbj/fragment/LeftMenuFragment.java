package com.zhj.zhbj.fragment;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.zhj.zhbj.MainActivity;
import com.zhj.zhbj.R;
import com.zhj.zhbj.base.impl.NewsCenterPager;
import com.zhj.zhbj.domain.NewsData;

import java.util.ArrayList;

/**
 * Created by HongJay on 2016/7/8.
 */
public class LeftMenuFragment extends BaseFragment {
    private int mCurrentPos;
    private NewsData menuData;
    ListView lvLeft;
    private ArrayList<NewsData.NewsMenuData> data1;
    private MyAdapter myAdapter;

    @Override
    public View initViews() {
        View view = View.inflate(getActivity(), R.layout.fragment_left_menu, null);
        ViewUtils.inject(this, view); //注入view和事件
        lvLeft = (ListView) view.findViewById(R.id.lv_left_menu);
        return view;
    }

    @Override
    public void initData() {
        lvLeft.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mCurrentPos = i;
                myAdapter.notifyDataSetChanged();
                setCurrentMenuDetailPager(i);
//                toogleSlidingMenu();
            }
        });
    }

//    private void toogleSlidingMenu() {
//        MainActivity mainUi= (MainActivity) mActivity;
//        SlidingMenu slidingMenu = mainUi.getSlidingMenu();
//        slidingMenu.toggle();//切换状态
//    }

    protected void setCurrentMenuDetailPager(int position) {
        MainActivity mainUi= (MainActivity) mActivity;
        ContentFragment contentFragment = mainUi.getContentFragment();
        NewsCenterPager pager = contentFragment.getNewsCenterPager();
        pager.setCurrentMenuDetailPager(position);
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return data1.size();
        }

        @Override
        public NewsData.NewsMenuData getItem(int i) {
            return data1.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
           View view1=View.inflate(mActivity,R.layout.item_list_left_menu,null);
            TextView tv= (TextView) view1.findViewById(R.id.tv_menu_title);
            tv.setText(data1.get(i).title);
            if(mCurrentPos==i){
                tv.setTextColor(Color.RED);
            }else{
                tv.setTextColor(Color.WHITE);
            }
            return view1;
        }
    }

    public void setMenuData(NewsData data) {
        data1 = data.data;
        lvLeft.setDividerHeight(0);
        myAdapter = new MyAdapter();
        lvLeft.setAdapter(myAdapter);
    }
}
