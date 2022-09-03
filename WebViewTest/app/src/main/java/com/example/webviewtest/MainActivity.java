package com.example.webviewtest;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WebView webView = (WebView) findViewById(R.id.web_view);
        // 使WebView支持js
        webView.getSettings().setJavaScriptEnabled(true);
        // 当需要从一个网页跳转到另外一个网页的时候，我们希望目标网页仍然在当前WebView中显示，而不是去打开系统浏览器
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://www.future-science.cn");
    }
}