package com.example.activitytest;

import org.jetbrains.annotations.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

//public class SecondActivity extends AppCompatActivity {
// 获取当前活动对象
public class SecondActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Log.d("SecendActivity", this.toString());
        // 活动模式 singleInstance
        Log.d("SecondActivity", "Task id is " + getTaskId());
        setContentView(R.layout.second_layout);
        // 接收上一个活动发送过来的数据
//        Intent intent = getIntent();
//        String data = intent.getStringExtra("extra_data");
//        Log.d("SecondActivity", data);
//        System.out.println("end");

        // 返回数据给上一个活动
//        Button button2 = (Button) findViewById(R.id.button_2);
//        button2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.putExtra("data_return", "Hello FirstActivity");
//                setResult(RESULT_FIRST_USER, intent);
//                finish();
//            }
//        });

        // 活动模式 singleTop singleInstance
        Button button2 = (Button) findViewById(R.id.button_2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // singleTop
//                Intent intent = new Intent(SecondActivity.this, FirstActivity.class);
                // singleInstance
                Intent intent = new Intent(SecondActivity.this, ThirdActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("data_return", "Hello FirstActivity");
        setResult(RESULT_FIRST_USER, intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("SecondActivity", "onDestroy");
    }

    /**
     * 向secondActivity中传递数据
     * @param context 上下文对象
     * @param data1 参数1
     * @param data2 参数2
     */
    public static void actionStart(Context context, String data1, String data2) {
        Intent intent = new Intent(context, SecondActivity.class);
        intent.putExtra("param1", data1);
        intent.putExtra("param2", data2);
        context.startActivity(intent);
    }
}