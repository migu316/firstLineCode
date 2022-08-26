package com.example.broadcasttest;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {

    private IntentFilter intentFilter;

    private NetworkChangReceiver networkChangReceiver;

    private LocalReceiver localReceiver;

    private LocalBroadcastManager localBroadcastManager;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        // 新建一个过滤器
//        intentFilter = new IntentFilter();
//        // 添加需要捕捉的广播
//        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
//        // 创建一个广播接收器
//        networkChangReceiver = new NetworkChangReceiver();
//        // 将广播接收器和过滤器注册，这样接收器就可以接收到值为android.net.conn.CONNECTIVITY_CHANGE广播
//        registerReceiver(networkChangReceiver, intentFilter);
//    }


//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        Button button = (Button) findViewById(R.id.button);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent("com.example.broadcasttest.MY_BROADCAST");
////                sendBroadcast(intent);
//                // 发送有序广播
//                sendOrderedBroadcast(intent, null);
//            }
//        });
//    }

    // 本地广播
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        localBroadcastManager = LocalBroadcastManager.getInstance(this); // 获取实例
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
                Intent intent = new Intent("com.example.broadcasttest.LOCAL_BROADCAST");
                localBroadcastManager.sendBroadcast(intent); // 发送本地广播
            }
        });
        intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.broadcasttest.LOCAL_BROADCAST");
        localReceiver = new LocalReceiver();
        localBroadcastManager.registerReceiver(localReceiver, intentFilter); // 注册本地广播监听器
    }

    /**
     * 在活动销毁的时候，取消注册广播
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
//        Log.d("onDestroy", "onDestroy");
//        unregisterReceiver(networkChangReceiver);
        localBroadcastManager.unregisterReceiver(localReceiver);
    }

    class NetworkChangReceiver extends BroadcastReceiver {


//        @Override
//        public void onReceive(Context context, Intent intent) {
//            Toast.makeText(context, "network changes", Toast.LENGTH_SHORT).show();
//        }

        @Override
        public void onReceive(Context context, Intent intent) {
            // 这是一个系统服务类，调用getSystemService获取connectivityManager实例，用于管理网络连接
            ConnectivityManager connectivityManager = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            /**
             *  因为获取网络状态属于敏感操作，需要在AndroidManifest中添加一行，需要在配置文件中声明权限，
             *  否则程序会直接崩溃
             *  <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
             */
            // 调用connectivityManager实例的getActiveNetworkInfo方法获取networkInfo实例
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            // 调用networkInfo的isAvailable方法获取当前是否有网络
            if (networkInfo != null && networkInfo.isAvailable()) {
                Toast.makeText(context, "network is available", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "network is unavailable", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class LocalReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "recevied local broadcast", Toast.LENGTH_SHORT).show();
        }
    }

    public void save() {
        String data = "Data to save";
        // 创建文件输出流对象
        FileOutputStream out = null;
        // 创建缓冲写入对象
        BufferedWriter writer = null;
        try {
            /*
                先通过openfileoutput对象得到一个FileOutputStream（文件输出流对象）
                然后通过文件输出流对象创建一个输出流写入对象，再通过输出流写入对象创建一个缓冲写入对象
                文件输出流对象 -> 文件输出流写入对象 -> 缓冲写入对象
             */
            out = openFileOutput("data", MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(data);
            Toast.makeText(this, "save", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}