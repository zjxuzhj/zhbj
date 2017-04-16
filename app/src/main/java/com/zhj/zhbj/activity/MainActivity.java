package com.zhj.zhbj.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.zhj.zhbj.R;
import com.zhj.zhbj.domain.User;
import com.zhj.zhbj.entry.Location;
import com.zhj.zhbj.fragment.MainFragment;

import java.util.List;

import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;

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
            final StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            sb.append(location.getRadius());
            if (location.getLocType() == BDLocation.TypeGpsLocation) { // GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed()); // 单位：公里每小时
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
                sb.append("\nheight : ");
                sb.append(location.getAltitude()); // 单位：米
                sb.append("\ndirection : ");
                sb.append(location.getDirection()); // 单位度
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\ndescribe : ");
                sb.append("gps定位成功");

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) { // 网络定位结果
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                //运营商信息
                sb.append("\noperationers : ");
                sb.append(location.getOperators());
                sb.append("\ndescribe : ");
                sb.append("网络定位成功");
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) { // 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
            sb.append("\nlocationdescribe : ");
            sb.append(location.getLocationDescribe()); // 位置语义化信息
            List<Poi> list = location.getPoiList(); // POI数据
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }
            Log.i("BaiduLocationApiDem", sb.toString());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, "" + location.getAddrStr(), Toast.LENGTH_SHORT).show();
                }
            });

            Location.getInstance().setCityName(sb.toString());
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
