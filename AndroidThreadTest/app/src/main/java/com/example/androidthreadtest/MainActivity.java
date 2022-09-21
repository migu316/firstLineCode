package com.example.androidthreadtest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


/**
 * 异步消息处理
 * 1.主线程中创建一个Handler对象，并重写handlerMessage()
 * 2.当子线程中需要进行UI操作的时候，就创建一个Message对象，并通过Handler将这条消息发送出去
 * 3.这条消息将会被添加到MessageQueue的队列中等待被处理
 * 4.looper将一直尝试从MessageQueue中取出待处理消息
 * 5.最后分发回handler的handleMessage方法中
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView text;

    private static final int UPDATE_TEXT = 1;

    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case UPDATE_TEXT:
                    // 在这里进行UI操作
                    text.setText("Nice to Meet you");
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = (TextView) findViewById(R.id.text);
        Button changeText = (Button) findViewById(R.id.change_text);
        changeText.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.change_text:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message message = new Message();
                        message.what = UPDATE_TEXT;
                        handler.sendMessage(message);   // 将Message对象发送出去（发送到MessageQueue队列中）
                    }
                }).start();
                break;
            default:
                break;
        }
    }
}