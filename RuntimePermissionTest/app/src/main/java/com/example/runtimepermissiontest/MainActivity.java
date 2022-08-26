package com.example.runtimepermissiontest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button makeCall = (Button) findViewById(R.id.make_call);
        makeCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 该代码在高于6.0的系统上无法正常使用，因为需要声明运行时权限
//                try {
//                    Intent intent = new Intent(Intent.ACTION_CALL);
//                    intent.setData(Uri.parse("tel:10086"));
//                    startActivity(intent);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
                /**
                 * 使用ContextCompat.checkSelfPermission方法判断用户是否已经给过我们授权了
                 * 接收两个参数
                 * 1.Context
                 * 2.具体的权限名
                 * 然后使用方法的返回值和PackageManager.PERMISSION_GRANTED做比较，相等就代表用户已经授权，反之就没有授权
                 *
                 * 如果没有授权，那么调用ActivityCompat.requestPermissions方法请求获取权限
                 * 接收三个参数
                 * 1.授权的活动
                 * 2.权限数组
                 * 3.请求码，唯一值即可
                 */
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) !=
                        PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[] {
                            Manifest.permission.CALL_PHONE
                    }, 1);
                } else {
                    call();
                }
            }
        });
    }

    private void call() {
        try {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:10086"));
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 在调用了onRequestPermissionsResult方法后，系统会弹出一个权限申请的对话框，可以选择同意或者拒绝权限申请
     * 无论什么结果，最终都会回调到onRequestPermissionsResult方法中，而授权的结果则会封装在grantResults中
     * @param requestCode 请求码
     * @param permissions 权限
     * @param grantResults 授权的结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    call();
                } else {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }
}