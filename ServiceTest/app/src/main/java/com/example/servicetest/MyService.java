package com.example.servicetest;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

/**
 *   IBinder
 *      ↓
 *    Binder
 *      ↓
 * DownloadBinder
 *
 * DownloadBinder extends Binder
 * Binder extends iBinder
 * @author admin
 */
public class MyService extends Service {

    private final DownloadBinder mBinder = new DownloadBinder();

    static class DownloadBinder extends Binder {

        public void startDownload() {
            Log.d("MyService", "startDownload executed");
        }

        public void getProgress() {
            Log.d("MyService", "getProgress executed");
        }
    }

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("MyService", "onCreate executed");
        // 创建前台服务
        // 创建一个意图
        Intent intent = new Intent(this, MainActivity.class);
        // PendingIntent可以看作是对Intent的一个封装，但它不是立刻执行某个行为，而是满足某些条件或触发某些事件后才执行指定的行为。
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("This is content title")
                .setContentText("this is content text")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                // 设置点击后的处理：跳转到activity
                .setContentIntent(pi)
                .build();
        // 设置服务为前台服务：
        // 参数1：通知的id
        // 参数2：构建出的notification对象
        startForeground(1, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        Log.d("MyService", "onStartCommand executed");
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 处理具体的逻辑
                stopSelf();
            }
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("MyService", "onDestroy executed");
    }
}