package com.example.playaudiotest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;

/* android中播放音频文件一般都是使用MediaPlayer类来实现的
    方法名                     功能描述
    setDataSource()           设置要播放的音频文件的位置
    prepare()                 在开始播放之前调用这个方法完成准备工作
    start()                   开始或继续播放音频
    pause()                   暂停播放音频
    reset()                   将MediaPlayer对象重置到刚刚创建的状态
    seekTo()                  从指定的位置开始播放音频
    stop()                    停止播放音频，调用这个方法后的MediaPlayer对象无法再播放音频
    release()                 释放掉与MediaPlayer对象相关的资源
    isPlaying()               判断当前MediaPlayer是否正在播放音频
    getDuration()             获取载入的音频文件的时长

    工作流程
    首先创建出一个MediaPlayer对象
                ↓
    然后调用setDataSource()方法来设置音频文件的路径
                ↓
    再调用prepare()方法使MediaPlayer进入到准备状态
                ↓
    接下来调用start()方法就可以开始播放音频
                ↓
    调用pause()方法就会暂停
                ↓
    调用reset()方法就会停止播放
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private MediaPlayer mediaPlayer = new MediaPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button play = (Button) findViewById(R.id.play);
        Button pause = (Button) findViewById(R.id.pause);
        Button stop = (Button) findViewById(R.id.stop);
        play.setOnClickListener(this);
        pause.setOnClickListener(this);
        stop.setOnClickListener(this);
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[] {
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 1);
        } else {
            initMediaPlayer();      // 初始化MediaPlayer
        }
    }

    private void initMediaPlayer() {
        try {
            // 创建一个file对象，指定了系统根目录下的music.mp3文件
            File file = new File(Environment.getExternalStorageDirectory(), "music.mp3");
            mediaPlayer.setDataSource(file.getPath());  // 指定音频文件的路径
            mediaPlayer.prepare();  // 让MediaPlayer进入到准备状态
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initMediaPlayer();
                } else {
                    Toast.makeText(this, "拒绝权限将无法使用程序", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
                break;
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.play:
                // 判断是否在播放音频，返回false则开始播放
                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start();    // 开始播放
                }
                break;
            case R.id.pause:
                // 判断是否在播放音频，true则暂停播放
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();    // 暂停播放
                }
                break;
            case R.id.stop:
                if (mediaPlayer.isPlaying()) {
                    // 判断是否在播放音频，true则重置播放
                    mediaPlayer.reset();     // 停止播放
                    initMediaPlayer();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 判断mediaPlayer是否为null，如果不为null，则停止播放，同时释放资源
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }
}

































