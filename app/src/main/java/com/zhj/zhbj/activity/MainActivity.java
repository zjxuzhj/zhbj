package com.zhj.zhbj.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import com.zhj.zhbj.R;
import com.zhj.zhbj.fragment.MainFragment;

/**
 * 主页面，用来放fragment
 */
public class MainActivity extends FragmentActivity {
    private static final String FRAGMENT_CONTENT= "fragment_content";

    @Override
   public  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
//        Person p2 = new Person();
//        p2.setName("lucky");
//        p2.setAddress("北京海淀");
//        p2.save(new SaveListener<String>() {
//            @Override
//            public void done(String objectId,BmobException e) {
//                if(e==null){
//                    Toast.makeText(MainActivity.this,"添加数据成功，返回objectId为：" + objectId,Toast.LENGTH_SHORT).show();
//                }else{
//                    Toast.makeText(MainActivity.this,"创建数据失败：" + e.getMessage(),Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

        initFragment();
    }

    //在MainActivity中调用fragment
    private void initFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        ft.replace(R.id.fl_content,new MainFragment(),FRAGMENT_CONTENT);
        ft.commit();

    }

    //获取内容对象
    public MainFragment getContentFragment(){
        FragmentManager fm = getSupportFragmentManager();
        MainFragment fragment = (MainFragment) fm.findFragmentByTag(FRAGMENT_CONTENT);
        return fragment;
    }
}
