package com.example.androidthreadtest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


/**
 * 异步消息处理
 * 1.主线程中创建一个Handler对象，并重写handlerMessage()
 * 2.当子线程中需要进行UI操作的时候，就创建一个Message对象，并通过Handler将这条消息发送出去
 * 3.这条消息将会被添加到MessageQueue的队列中等待被处理
 * 4.looper将一直尝试从MessageQueue中取出待处理消息
 * 5.最后分发回handler的handleMessage方法中
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView text;

    private static final int UPDATE_TEXT = 1;

    private Handler handler = new Handler() {

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

/**
 * 泛型参数
 * 1.Params:在执行AsyncTask时需要传入的参数，可用于在后台任务中使用
 * 2.Progress:在后台任务执行的时候，如果需要在界面上显示当前的进度，则使用这里指定的泛型作为进度单位
 * 3.Result:在任务执行完毕后，如果需要对结果进行返回，则使用这里指定的泛型作为返回值类型
 */
//class DownloadTask extends AsyncTask<Void, Integer, Boolean> {
//    /**
//     * 这个方法会在后台任务开始执行之前调用，用于进行一些界面上的初始化操作，比如显示一个进度条对话框等
//     */
//    @Override
//    protected void onPreExecute() {
//        super.onPreExecute();
//        progressDialog.show(); // 显示进度对话框
//    }
//
//    /**
//     * 这个方法中的所有代码都会在子线程中运行，应该在这里去处理所有的耗时操作
//     * 一旦完成，就可以通过return语句将任务的执行结果进行返回，返回的类型取
//     * 决于AsyncTask第三个参数指定的类型
//     * 注意：这个方法中是不可以进行UI操作的，可以调用publishProgress(Progress...)方法来完成
//     */
//    @Override
//    protected Boolean doInBackground(Void... voids) {
//        return null;
//        try {
//            int downloadPercent = doDownload(); // 这是一个虚构的方法
//            publishProgress(downloadPercent);
//            if (downloadPercent >= 100) {
//                break;
//            }
//        } catch (Exception e) {
//            return false;
//        }
//        return true;
//    }
//
//    /**
//     * 当在后台任务中调用了 publishProgress(Progress...)方法后，onProgressUpdate(Progress...)方法就
//     * 会很快被调用，该方法中携带的参数就是在后台任务中被传递过来的
//     * 在这个方法中可以对UI进行操作，利用参数中的数值就可以对界面元素进行相应的更新
//     */
//    @Override
//    protected void onProgressUpdate(Integer... values) {
//        super.onProgressUpdate(values);
//        // 在这里更新下载进度
//        progressDialog.setMessage("Downloaded " + values[0] + "%");
//    }
//
//    /**
//     * 当后台任务执行完毕并通过return语句进行返回后，这个方法就很快会被调用，返回的数据会作为参数传递到此方法中
//     * 可以利用返回的数据来进行一些UI操作，比如说提醒任务执行的结果，以及关闭掉进度条对话框等
//     */
//    @Override
//    protected void onPostExecute(Boolean result) {
//        super.onPostExecute(result);
//        progressDialog.dismiss();   // 关闭进度对话框
//        // 在这里提示下载结果
//        if (result) {
//            Toast.makeText(context, "Download succeeded", Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(context, "Download failed", Toast.LENGTH_SHORT).show();
//        }
//    }
//}



























