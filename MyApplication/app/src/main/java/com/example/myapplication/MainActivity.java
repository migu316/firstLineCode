package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private IntentFilter intentFilter;

    private NetworkChangeReceiver networkChangeReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intentFilter = new IntentFilter(); // 创建一个意图过滤器
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE"); // 为意图过滤器添加需要过滤的广播
        networkChangeReceiver = new NetworkChangeReceiver(); // 新建一个广播接收器对象
        registerReceiver(networkChangeReceiver, intentFilter); // 注册广播接收器，传入意图过滤器
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkChangeReceiver); // 注销广播接收器
    }

    class NetworkChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE); // 创建一个连接管理器 调用getSystemService(Context.CONNECTIVITY_SERVICE)获取 网络连接的服务 对象传递给连接管理器
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo(); // 上面获取了 网络连接的服务 对象，调用getActiveNetworkInfo 得到networkInfo对象（网络信息对象）

            // 判断当前是否有网络连接
            if (networkInfo != null && networkInfo.isAvailable()) {
                Toast.makeText(context, "network is available", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "network is unavailable", Toast.LENGTH_SHORT).show();
            }
        }
    }

}