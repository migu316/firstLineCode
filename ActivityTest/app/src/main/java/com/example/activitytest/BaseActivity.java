package com.example.activitytest;

import android.os.Bundle;
import android.os.Process;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("BaseActivity", getClass().getSimpleName());
        ActivityCollector.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("这个活动" + this.toString() + "调用onDestroy");
        ActivityCollector.removeActivity(this);
        System.out.println("杀死：" + Process.myPid());
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
