package com.example.notificationtest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button sendNotice = (Button) findViewById(R.id.send_notice);
        sendNotice.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.send_notice:
                Intent intent = new Intent(this, NotificationAcitivity.class);
                PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
                // 使用getSystemService(服务名称)来获取一个通知管理器
                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                // 从Android8.0开始，应用显示通知时，必须为通知指定一个ChannelId
                /*
                    Build.VERSION.SDK_INT:代表运行该应用的手机系统的sdk版本
                    Build.VERSION_CODES.O:android 8.0
                 */
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    // 只在android o 之上需要频道，这里的第一个参数要和下面的channelID一样
                    /*
                        public NotificationChannel(String id, CharSequence name, int importance){}
                        id:频道id
                        name:频道的名称
                        importance:频道优先级  如果这里用IMPORTANCE_NONE就需要在系统的设置里面开启频道，通知就可以正常弹出
                     */
                    NotificationChannel notificationChannel = new NotificationChannel("1", "name", NotificationManager.IMPORTANCE_HIGH);
                    // 使用通知管理器将通知频道注册到系统中
                    manager.createNotificationChannel(notificationChannel);
                }
                // 这里的第2个参数要和上面的第1个参数一样
                // 使用supportV4中的NotificationCompat类的构造器来创建一个Notification对象
                Notification notification = new NotificationCompat.Builder(this, "1")
                        .setContentTitle("This is content title")
//                        .setContentText("This is content text")
                        .setWhen(System.currentTimeMillis())    // 指定通知被创建的时间，以毫秒为单位，当下拉通知栏的时候会显示在相应的通知上
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                        .setDefaults(NotificationCompat.DEFAULT_ALL)    // 根据手机的环境来决定播放什么铃声，以及如何振动
//                        .setSound(Uri.fromFile(new File("/system/media/audio/ringtomes/Luna.ogg"))) // 设置来通知的时候播放的音频
//                        .setVibrate(new long[]{0, 1000, 1000, 1000})    // 设置震动效果 需要在AndroidManifest中进行权限声明
//                        .setLights(Color.GREEN, 1000, 1000) // 设置led灯显示
                        .setContentIntent(pi)
                        // NotificationCompat.BigTextStyle这个对象用于封装长文字信息的，调用它的bigtext方法，并将文字内容传入即可
//                        .setStyle(new NotificationCompat.BigTextStyle().bigText("Learn how to build" +
//                                " notifications, send and sync data, and use voice actions.Get the " +
//                                "official Android IDE and developer tools to build apps for android"))
                        // 显示一张大图片 创建了一个NotificationCompat.BigPictureStyle()对象，调用bigPicture()方法将图片传入，再通过BitmapFactory.decodeResource方法将图片解析成Bigmap对象，传入到bigPicture方法中即可
                        .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(BitmapFactory.decodeResource(getResources(), R.drawable.big_image)))
                        // 设置通知的重要程度：将会影响通知显示的顺序，大小，PRIORITY_MAX代表最高的重要程度，将会让用户立刻看到通知（悬浮通知）
                        .setPriority(NotificationCompat.PRIORITY_MAX)
//                        .setAutoCancel(true)    // 当点击通知后，通知将自动取消
                        .build();
                // 需要保证每个通知的id都是唯一的，再传入通知对象
                manager.notify(1, notification);
                break;
            default:
                break;
        }
    }
}