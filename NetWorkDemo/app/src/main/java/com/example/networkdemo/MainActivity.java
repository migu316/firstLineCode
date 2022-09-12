package com.example.networkdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * @author admin
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    String address = "http://www.baidu.com";

    TextView responseText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button send = (Button) findViewById(R.id.send);
        responseText = (TextView) findViewById(R.id.response_text);
        send.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.send) {
            // 使用HttpConnection
//            HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
//                @Override
//                public void onFinish(String response) {
//                    showResponse(response);
//                }
//
//                @Override
//                public void onError(Exception e) {
//                    showResponse(e.toString());
//                }
//            });
            // 使用OkHttp
            HttpUtil.sendOkHttpRequest(address, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    showResponse(e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    // 得到服务器返回的具体内容
                    String responseData = response.body().string();
                    showResponse(responseData);
                }
            });
        }
    }

    private void showResponse(String response) {
        /*
            在Android中，是不允许在子线程中进行UI操作的，我们需要通过这个方法将线程切换到主线程，然后再更新UI元素
         */
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // 在这里进行UI操作，将结果显示到界面上
//                response.setText(response);
                responseText.setText(response);
            }
        });
    }

}