package com.example.activitytest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

//public class FirstActivity extends AppCompatActivity {
// 获取当前活动对象
public class FirstActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Log.d("FirstActivity", this.toString());
        // 活动模式 singleInstance
        Log.d("FirstActivity", "Task id is " + getTaskId());
        setContentView(R.layout.first_layout);
        Button button1 = (Button) findViewById(R.id.button_1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(FirstActivity.this, "you clicked button 1",
//                        Toast.LENGTH_SHORT).show();


                // 销毁活动（和back一样）
//                finish();


                // 使用显示intent启动另外一个活动
//                Intent intent = new Intent(FirstActivity.this, SecondActivity.class);
//                startActivity(intent);

                // 1.使用隐式intent启动另外一个活动
//                Intent intent = new Intent("com.example.activitytest.ACTION_START");
//                startActivity(intent);

                // 2.使用隐式intent启动另外一个活动
//                Intent intent = new Intent("com.example.activitytest.ACTION_START");
//                intent.addCategory("com.example.activitytest.MY_CATEGORY");
//                startActivity(intent);

                // 使用Intent启动其他程序的活动
//                Intent intent = new Intent(Intent.ACTION_DIAL);
//                intent.setData(Uri.parse("tel:10086"));
//                startActivity(intent);

                // 向下一个活动传递数据
//                String data = "Hello SecondActivity";
//                Intent intent = new Intent(FirstActivity.this, SecondActivity.class);
//                intent.putExtra("extra_data",data);
//                startActivity(intent);

                // 返回数据给上一个活动
//                Intent intent = new Intent(FirstActivity.this, SecondActivity.class);
//                startActivityForResult(intent, 1);

                // 活动模式 standard
                // 活动模式 standard singleTop
//                Intent intent = new Intent(FirstActivity.this, FirstActivity.class);
//                startActivity(intent);

                // 活动模式 singleTop singleTask singleInstance
//                Intent intent = new Intent(FirstActivity.this, SecondActivity.class);
//                startActivity(intent);

                // 向secondActivity中传递数据
                SecondActivity.actionStart(FirstActivity.this, "data1", "data2");
            }
        });
    }

    //    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        System.out.println("asdasdjaksdhjqwiedhqjwdnjqwnduqw");
        switch (resultCode) {
            case 1:
                if (resultCode == RESULT_FIRST_USER) {
                    String returnedData = data.getStringExtra("data_return");
                    Log.d("FirstActivity", returnedData);
                }
                break;
            default:
        }

        System.out.println(resultCode);
        System.out.println(requestCode);
        System.out.println(RESULT_OK);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_item:
                Toast.makeText(this, "you clicked Add", Toast.LENGTH_SHORT).show();
                break;
            case R.id.remove_item:
                Toast.makeText(this, "you clicked Remove", Toast.LENGTH_SHORT).show();
                break;
            default:
        }
        return true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("FirstActivity", "onRestart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("FirstActivity", "onPause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("FirstActivity", "onDestroy");
    }
}