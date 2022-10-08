package com.example.lbstest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.common.BaiduMapSDKException;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public LocationClient mLocationClient;

    private TextView positionText;

    private MapView mapView;

    private BaiduMap baiduMap;

    private boolean isFirstLocate = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 同意百度的隐私设置
//        LocationClient.setAgreePrivacy(true);
//        SDKInitializer.setAgreePrivacy(getApplicationContext(), true);
        setAgreePrivacy(getApplicationContext(), true);
        try {
            //位置客户端对象：需要一个context参数 通过getApplicationContext来获取上下文对象
            mLocationClient = new LocationClient(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 通过位置客户端对象来注册一个定位监听器：当获取到位置信息的时候，就会回调这个定位监听器
        mLocationClient.registerLocationListener(new MyLocationListener());
        // 进行初始化操作，需要在setContentView()方法之前调用
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        mapView = findViewById(R.id.bmapView);
        // 获取BaiduMap实例
        baiduMap = mapView.getMap();
        // 启用显示当前位置光标显示的功能
        baiduMap.setMyLocationEnabled(true);
        positionText = findViewById(R.id.position_text_view);

        // 通过一个List集合将3个权限一次性申请，首先判断是否以及拥有该权限，若没有，将会添加到List集合中
        // ACCESS_COARSE_LOCATION和ACCESS_FINE_LOCATION都属于同一个权限组，因此两者只需要申请其一即可
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (!permissionList.isEmpty()) {
            // 将集合转为数组，然后一次性申请
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this, permissions, 1);
        } else {
            requestLocation();
        }
    }

    private void navigateTo(BDLocation location) {
        if (isFirstLocate) {
            // 存放经纬度
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            // 返回一个包含了经纬度的MapStatusUpdate对象
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
            // 以动画方式更新地图状态，默认动画耗时 300 ms
            baiduMap.animateMapStatus(update);
            // 设置地图缩放级别，值越大，地图显示的信息就越精细
            update = MapStatusUpdateFactory.zoomTo(16f);
            baiduMap.animateMapStatus(update);
            // 防止多次调用animateMapStatus()方法
            isFirstLocate = false;
        }
        // 写在if语句的外面，因为让地图移动到当前所在位置只需要在第一次定位的时候使用，而当前位置的光标是需要一直修改的
        // 创建一个封装设备当前所在位置的类
        MyLocationData.Builder locationBuilder = new MyLocationData.Builder();
        // 对经纬度进行封装
        locationBuilder.latitude(location.getLatitude());
        locationBuilder.longitude(location.getLongitude());
        // 生成MyLocationData对象
        MyLocationData locationData = locationBuilder.build();
        // 调用setMyLocationData()方法，传入locationData数据即可在地图上显示当前所在位置的光标
        baiduMap.setMyLocationData(locationData);
    }

    /**
     * 设置隐私模式，默认false
     * 如果设置true,一定要保证在调用 SDKInitializer.initialize(this); 之前设置
     *
     * @param context  必须是Application Context
     * @param isEnable ture-同意隐私政策； false-不同意隐私政策；
     */
    public static void setAgreePrivacy(Context context, boolean isEnable) {
        // 是否同意隐私政策，默认为false
        LocationClient.setAgreePrivacy(true);
        SDKInitializer.setAgreePrivacy(context, isEnable);
    }


    private void requestLocation() {
        // 启用自动更新位置
        initLocation();
        // 调用位置客户端的start方法即可开始定位,结果会返回到前面注册的监听器当中
        mLocationClient.start();
    }

    /**
     * 设置一个更新周期
     */
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setScanSpan(1000);
        // 获取当前位置详细的地址信息
        option.setIsNeedAddress(true);
        // 获取最新的地址信息
        option.setNeedNewVersionRgc(true);
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        mLocationClient.setLocOption(option);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 停用资源，否则会在后台一直调用定位
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
        mapView.onDestroy();
        baiduMap.setMyLocationEnabled(false);
    }

    /**
     * 对权限申请结果的逻辑处理，这次对每个权限都进行了判断，如果有一个被拒绝都会直接调用finish方法关闭程序，只有当
     * 所有的权限都被用户同意了后，才会调用registerLocation()方法开始地理位置定位
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "必须同意所有权限才能使用本程序", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    requestLocation();
                } else {
                    Toast.makeText(this, "发生未知错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 前面获取到的位置信息会返回到这里,然后获取经纬度以及定位方式，显示在屏幕上
     */
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    StringBuilder currentPosition = new StringBuilder();
                    currentPosition.append("纬度：").append(location.getLatitude()).append("\n");
                    currentPosition.append("经线：").append(location.getLongitude()).append("\n");
                    currentPosition.append("国家：").append(location.getCountry()).append("\n");
                    currentPosition.append("省：").append(location.getProvince()).append("\n");
                    currentPosition.append("市：").append(location.getCity()).append("\n");
                    currentPosition.append("区：").append(location.getDistrict()).append("\n");
                    currentPosition.append("街道：").append(location.getStreet()).append("\n");
                    currentPosition.append("定位方式：");
                    if (location.getLocType() == BDLocation.TypeGpsLocation) {
                        currentPosition.append("GPS");
                    } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                        currentPosition.append("网络");
                    }
                    positionText.setText(currentPosition);
                }
            });
            // 判断定位方式
            if (location.getLocType() == BDLocation.TypeGpsLocation ||
                    location.getLocType() == BDLocation.TypeNetWorkLocation) {
                navigateTo(location);
            }
        }
    }

    public void onConnectHotSpotMessage(String s, int i) {

    }
}