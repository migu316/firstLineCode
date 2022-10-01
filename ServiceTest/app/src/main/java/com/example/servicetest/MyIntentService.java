package com.example.servicetest;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

/**
 * 使用intentService，集开启线程和自动停止于一身
 * @author admin
 */
public class MyIntentService extends IntentService {
    /**
     * @deprecated
     */
    public MyIntentService() {
        // 调用父类的有参构造函数
        super("MyIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        // 打印当前线程的ID
        Log.d("MyIntentService", "Thread id is " + Thread.currentThread().getId());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("MyIntentService", "onDestroy executed");
    }
}
