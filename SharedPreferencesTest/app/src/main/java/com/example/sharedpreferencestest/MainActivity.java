package com.example.sharedpreferencestest;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button save_data = (Button) findViewById(R.id.save_data);
        save_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 调用getSharedPreferences对象的edit方法来获取SharedPreferences.Editor对象，用于向文件中添加数据
                SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
                editor.putString("name", "tom");
                editor.putInt("age", 18);
                editor.putBoolean("married", false);
                // 将添加的数据提交
                editor.apply();
            }
        });

        Button restoreData = (Button) findViewById(R.id.restore_data);
        restoreData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 调用getSharedPreferences方法得到一个SharedPreferences对象
                SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
                // 调用SharedPreferences的各种类型的获取方法来获取对应键的值
                String name = pref.getString("name", "");
                int age = pref.getInt("age", 0);
                Boolean married = pref.getBoolean("married", false);
                Log.d("MainActivity", "name is " + name);
                Log.d("MainActivity", "age is " + age);
                Log.d("MainActivity", "married is " + married);
            }
        });
    }
}