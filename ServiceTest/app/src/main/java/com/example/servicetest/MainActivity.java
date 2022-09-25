package com.example.servicetest;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;

/**
 * @author admin
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * 将会在服务和活动绑定的时候进行调用
     */
    private final ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MyService.DownloadBinder downloadBinder = (MyService.DownloadBinder) iBinder;
            downloadBinder.startDownload();
            downloadBinder.getProgress();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button startService = (Button) findViewById(R.id.start_service);
        Button stopService = (Button) findViewById(R.id.stop_service);
        startService.setOnClickListener(this);
        stopService.setOnClickListener(this);
        Button bindService = (Button) findViewById(R.id.bind_service);
        Button unbindService = (Button) findViewById(R.id.unBind_service);
        bindService.setOnClickListener(this);
        unbindService.setOnClickListener(this);
    }

    /**
     * 通过点击事件来判断对服务应该进行什么操作
     * 通过Intent实现对服务的操作
     * startService() stopService()方法都是定义在context中的
     * 使用stopSelf()方法可以使服务自己停止下来
     */
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_service:
                Intent startIntent = new Intent(this, MyService.class);
                // 启动服务
                startService(startIntent);
                break;
            case R.id.stop_service:
                Intent stopIntent = new Intent(this, MyService.class);
                // 停止服务
                stopService(stopIntent);
                break;
            case R.id.bind_service:
                Intent bindIntent = new Intent(this, MyService.class);
                // 绑定服务
                /*
                 * 当点击该按钮后，调用bindService方法，自动创建服务，并绑定活动，同时将会通过服务中的IBinder方法将
                 * DownloadBinder实例返回到活动中的ServiceConnection实例的onServiceConnected方法中（该方法
                 * 将会自动调用）
                 * 参数一：Intent对象
                 * 参数二：ServiceConnection的实例
                 * 参数三：标志位  BIND_AUTO_CREATE：表示活动和服务进行绑定后自动创建服务
                 */
                bindService(bindIntent, connection, BIND_AUTO_CREATE);
                break;
            case R.id.unBind_service:
                // 解绑服务
                unbindService(connection);
                break;
            default:
                break;
        }
    }


}