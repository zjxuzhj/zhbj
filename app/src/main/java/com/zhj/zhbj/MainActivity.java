package com.zhj.zhbj;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.zhj.zhbj.fragment.ContentFragment;
import com.zhj.zhbj.fragment.LeftMenuFragment;

public class MainActivity extends SlidingFragmentActivity {
    private static final String FRAGMENT_LEFT_MENU = "fragment_left_menu";
    private static final String FRAGMENT_CONTENT= "fragment_content";

    @Override
   public  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setBehindContentView(R.layout.menu_left);
        SlidingMenu slidingMenu = getSlidingMenu();
        slidingMenu.setMode(SlidingMenu.LEFT);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        slidingMenu.setBehindOffset(700);

        initFragment();
    }

    //在MainActivity中调用fragment
    private void initFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        ft.replace(R.id.fl_content,new ContentFragment(),FRAGMENT_CONTENT);
        // 用fragment替换framelayout
        ft.replace(R.id.fl_left_menu, new LeftMenuFragment(),FRAGMENT_LEFT_MENU);
        ft.commit();

    }
//获取侧边栏对象
    public LeftMenuFragment getLeftFragment(){
        FragmentManager fm = getSupportFragmentManager();
        LeftMenuFragment fragment = (LeftMenuFragment) fm.findFragmentByTag(FRAGMENT_LEFT_MENU);
        return fragment;

    }
    //获取内容对象
    public ContentFragment getContentFragment(){
        FragmentManager fm = getSupportFragmentManager();
        ContentFragment fragment = (ContentFragment) fm.findFragmentByTag(FRAGMENT_CONTENT);
        return fragment;
    }

}
