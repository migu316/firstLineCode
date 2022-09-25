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
 * 服务的生命周期：
 * 1.调用startService()
 *      我们用到的onCreate(),onStartCommand(),onBind(),onDestroy()等方法都是在服务的生存周期内可能回调的方法
 *      在Context的任何位置调用startService()方法，服务便会启动。同时回调onStartCommand()
 *      如果没有创建，那么就会先调用onCreate()
 *      直到stopService()或stopSelf()方法被调用
 *      startService()→onCreate()→onStartCommand()→stopService()/stopSelf()→onDestroy()
 *      **注意**
 *      虽然每调用一次startService()方法，onStartCommand()就会执行一次，但是实际上每个服务就只会存在一次，
 *      所以无论你调用了多少次，只需要调用一次stopService()/stopSelf()就可以把服务停止了
 * 2.调用bindService()
 *      我们可以使用Context中的bindService()来获取一个服务的持久连接，这时就会回调服务中的onBind()方法。
 *      同时，如果服务没有创建，那么onCreate()方法就会再onBind()方法之前调用
 *      之后，调用方即可获取到onBind()方法中返回的IBinder对象的实例，此时就可以和服务进行通信了
 *      bindService()→onCreate()→(调用方)onServiceConnect()→unbindService()→onDestroy()
 * 3.关于销毁活动
 *      startService()→stopService()→onDestroy()
 *      bindService()→unbindService()→onDestroy()
 *      如果既调用startService()，又调用了bindService()
 *      startService()→bindService():running
 *      bindService()→startService():running
 *      这种情况下如何销毁呢？
 *      bindService()→startService()→unbindService()→stopService()
 *      根据Android的机制，一个服务只要被启动或者被绑定之后，就会一直处于运行状态，必须要让以上两种条件同时不满足，服务
 *      才会被销毁。所以需要同时调用stopService()和unbindService(),无先后顺序
 * @author admin
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    MyService.DownloadBinder downloadBinder;
    /**
     * 将会在服务和活动绑定的时候进行调用
     */
    private final ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            downloadBinder = (MyService.DownloadBinder) iBinder;
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