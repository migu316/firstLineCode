package com.example.broadcastbestpractice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    private ForceOfflineReceiver receiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    // 当顶层活动处于生命周期的当前状态时，进行广播接收器以及意图过滤器的注册
    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("con.example.broadcastbestpractice.FORCE_OFFLINE");
        receiver = new ForceOfflineReceiver();
        registerReceiver(receiver, intentFilter);
    }

    // 当当前活动暂停的时候，注销当前活动，并使receiver=null
    @Override
    protected void onPause() {
        super.onPause();
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
    }

    // 广播接收器类
    class ForceOfflineReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 当收到广播的时候，创建一个对话框在当前活动
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            // 设置对话框的标题
            builder.setTitle("Warning");
            // 设置对话框的消息框内容
            builder.setMessage("you are forced to be offline. please try to login again.");
            // 设置对话框为不可取消（即不会因为用户点击back而关闭对话框）
            builder.setCancelable(false);
            // 注册对话框的按键
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                // 设置按键的点击事件
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ActivityCollector.finishAll(); // 销毁所有的活动
                    Intent intent1 = new Intent(context, LoginActivity.class);
                    context.startActivity(intent1); // 重新启动LoginActivity
                }
            });
            builder.show();
        }
    }
}


























