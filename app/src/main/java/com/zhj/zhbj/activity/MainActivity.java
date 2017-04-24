package com.zhj.zhbj.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.zhj.zhbj.R;
import com.zhj.zhbj.domain.User;
import com.zhj.zhbj.fragment.MainFragment;

import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 主页面，用来放fragment
 */
public class MainActivity extends AppCompatActivity {
    private static final String FRAGMENT_CONTENT = "fragment_content";
    private LocationClient mLocationClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        initFragment();
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new MyLocationListener());//MyLocationListener后面会定义
        initLocation();
        mLocationClient.start();


    }

    private void initLocation() {
        LocationClientOption locationClientOption = new LocationClientOption();
        locationClientOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        locationClientOption.setCoorType("bd09ll");
        locationClientOption.setIsNeedAddress(true);
        locationClientOption.setScanSpan(0);
        locationClientOption.setOpenGps(true);
        locationClientOption.setIsNeedLocationDescribe(true); //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        locationClientOption.setIgnoreKillProcess(false); //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        locationClientOption.SetIgnoreCacheException(false);
        locationClientOption.setEnableSimulateGps(true);
        mLocationClient.setLocOption(locationClientOption);

    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(final BDLocation location) {
            //Receive Location
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, "" + location.getCity(), Toast.LENGTH_SHORT).show();
                    if (location.getCity() != null && !location.getCity().equals("")) {
                        User newUser = new User();
                        User bmobUser = User.getCurrentUser(User.class);
                        if(bmobUser!=null) {
                            newUser.setCity(location.getCity());
                            newUser.update(bmobUser.getObjectId(), new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                }
                            });
                        }
                    }
                }
            });

        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }

    //在MainActivity中调用fragment
    private void initFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        ft.replace(R.id.fl_content, new MainFragment(), FRAGMENT_CONTENT);
        ft.commit();

    }

    //获取内容对象
    public MainFragment getContentFragment() {
        FragmentManager fm = getSupportFragmentManager();
        MainFragment fragment = (MainFragment) fm.findFragmentByTag(FRAGMENT_CONTENT);
        return fragment;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        User userInfo = BmobUser.getCurrentUser(User.class);
        userInfo.logOut();
        mLocationClient.stop();
    }
}
