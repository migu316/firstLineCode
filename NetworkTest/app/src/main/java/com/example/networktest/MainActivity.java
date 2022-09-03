package com.example.networktest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    /*
        使用HttpUrlConnection
        首先new一个URL对象，并传入网络地址    URL url = new URL("https://www.baidu.com")
                        ↓
        然后调用openConnection方法即可      HttpURLConnection connection = (HttpURLConnection) url.openConnection
                        ↓
        在获取到HttpUrlConnection的实例之后，我们可以设置一下HTTP请求所使用的方法(GET / POST) connection.setRequestMethod("GET")
                        ↓
        调用getInputStream()方法就可以获取到服务器返回的输入流了 InputStream in = connection.getInputStream();
                        ↓
        调用disconnect()方法将这个HTTP连接关闭掉 connection.disconnect()

        设置连接超时：connection.setConnectTimeout(8000)
        设置读取超时：connection.setReadTimeout(8000)

        如果我们想要提交数据给服务器，只需要将HTTP请求的方法改成POST
        connection.setRequestMethod("POST");
        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
        out.writeBytes("username=admin&password=123456");
     */
    TextView responseText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button sendRequest = (Button) findViewById(R.id.send_request);
        responseText = (TextView) findViewById(R.id.response_text);
        sendRequest.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.send_request) {
//            sendRequestWithHttpUrlConnection();
            sendRequestWithOkHttp();
        }
    }

    /*  使用OkHttp
        在使用OkHttp之前，需要在项目中添加OkHttp库的依赖
            implementation('com.squareup.okhttp3:okhttp:3.4.1')
        首先创建一个OkHttpClient的实例
        OkHttpClient client = new okHttpClient();
                            ↓
        接下来如果想要发起一条http请求，就需要创建一个request对象
        Request request = new Request.Builder().build();
        这只是创建了一个空的request对象，可以在build方法前连缀很多其他方法来丰富这个request对象，比如：
        Request request = new Request.Builder()
                    .url("https://www.baidu.com")
                    .build();
                                    ↓
        之后调用OkHttpClient的newCall方法来创建一个Call对象，并调用他的execute()方法来发送请求并获取服务器返回的数据
        Response response = client.newCall(request).execute();
                                    ↓
        其中response对象就是服务器返回的数据了，我们可以使用如下写法来获取返回的具体内容
        String responseData = response.body().string();

        如果是发起一条post请求就需要先构建出一个requestBody对象来存放待提交的参数
        RequestBody requestBody = new FormBody.Builder()
                    .add("username", "admin")
                    .add("password", "123456")
                    .build();
        然后在Request.Builder中调用一下post方法，并将requestBody对象传入
        Request request = new Request.Builder()
                .url("https://www.baidu.com")
                .post(requestBody)
                .build();
        接下来的操作就和get请求一样了，调用execute方法来发送请求并获取服务器返回的数据即可

     */


    private void sendRequestWithOkHttp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // FormBody继承自RequestBody
//                    RequestBody requestBody = new FormBody.Builder()
//                            .add("username", "admin")
//                            .add("password", "123456")
//                            .build();

                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("https://www.baidu.com")
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    showResponse(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void sendRequestWithHttpUrlConnection() {
        // 开启子线程来发起网络请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                try {
                    URL url = new URL("https://www.baidu.com");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    // 获取服务器返回的输入流
                    InputStream in = connection.getInputStream();

                    // 下面对获取到的输入流进行读取
                    reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    showResponse(response.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    private void showResponse(String response) {
        /*
            在Android中，是不允许在子线程中进行UI操作的，我们需要通过这个方法将线程切换到主线程，然后再更新UI元素
         */
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // 在这里进行UI操作，将结果显示到界面上
                responseText.setText(response);
            }
        });
    }


}





















