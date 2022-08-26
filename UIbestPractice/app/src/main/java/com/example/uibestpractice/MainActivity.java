package com.example.uibestpractice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Msg> msgList = new ArrayList<>();

    private EditText inputText;

    private Button send;

    private RecyclerView msgRecyclerView;

    private MsgAdapter adapter;

    private static int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initMsgs(); // 初始化消息数据
        inputText = (EditText) findViewById(R.id.input_text);
        send = (Button) findViewById(R.id.send);
        msgRecyclerView = (RecyclerView) findViewById(R.id.msg_recycler_view);
        // 指定 RecyclerView的布局 此处LinearLayoutManager为线性布局
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        msgRecyclerView.setLayoutManager(layoutManager);
        // 创建一个msgAdapter类型的适配器，同时将数据传入到适配器的构造函数中
        adapter = new MsgAdapter(msgList);
        // 设置RecyclerView的适配器，完成RecyclerView与数据源之间的关联建立
        msgRecyclerView.setAdapter(adapter);
        // 设置点击事件
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 获取文本框内的字符串
                String content = inputText.getText().toString();
                // 判断获取到的是否为空字符串
                if (!"".equals(content)) {
                    // 将字符串封装为msg对象
                    if (count % 2 == 0) {
                        Msg msg = new Msg(content, Msg.TYPE_SENT);
                        // 存入消息到键值对集合中
                        msgList.add(msg);
                        // 当有新消息时，刷新RecyclerView中的显示
                        adapter.notifyItemInserted(msgList.size() - 1);
                        // 将RecyclerView 定位到最后一行
                        msgRecyclerView.scrollToPosition(msgList.size() - 1);
                        // 清空输入框中的内容
                        inputText.setText("");
                    } else {
                        Msg msg = new Msg(content, Msg.TYPE_RECEIVED);
                        // 存入消息到键值对集合中
                        msgList.add(msg);
                        // 当有新消息时，刷新RecyclerView中的显示
                        adapter.notifyItemInserted(msgList.size() - 1);
                        // 将RecyclerView 定位到最后一行
                        msgRecyclerView.scrollToPosition(msgList.size() - 1);
                        // 清空输入框中的内容
                        inputText.setText("");
                    }
                    count++;
                }
            }
        });
    }
    private void initMsgs() {
        Msg msg1 = new Msg("Hello guy, ", Msg.TYPE_RECEIVED);
        msgList.add(msg1);
        Msg msg2 = new Msg("Hello. Who is that?", Msg.TYPE_SENT);
        msgList.add(msg2);
        Msg msg3 = new Msg("This is tom, nice talking to you.", Msg.TYPE_RECEIVED);
        msgList.add(msg3);
    }
}