package com.example.broadcastbestpractice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.util.prefs.PreferenceChangeEvent;

// 首先将活动继承自BaseActivity,这样的好处是由BaseActivity对活动进行创建，以及添加到活动管理器中
public class LoginActivity extends BaseActivity {

    private EditText accountEdit;

    private EditText passwordEdit;

    private Button login;

    private Button register;

    // 添加记住密码功能
    private SharedPreferences pref;

    private SharedPreferences.Editor editor;

    private CheckBox rememberPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /* getDefaultSharedPreferences这是一个静态方法，由PreferenceManager类调用，传入当前活动作为参数
            并自动使用活动名作为前缀来命名SharedPreferences文件
         */
        pref = PreferenceManager.getDefaultSharedPreferences(this);

        // 使几个类分别获取到对应的实例
        accountEdit = (EditText) findViewById(R.id.account);
        passwordEdit = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);

        // 添加注册功能
        register = (Button) findViewById(R.id.register);

        rememberPass = (CheckBox) findViewById(R.id.remember_pass);
        boolean isRemember = pref.getBoolean("remember_password", false);
        if (isRemember) {
            // 将账户和密码都设置到文本框中
            String account = pref.getString("account", "");
            String password = pref.getString("password", "");
            accountEdit.setText(account);
            passwordEdit.setText(password);
            rememberPass.setChecked(true);
        }


        // 接着在登录按钮的点击事件中添加对输入的账号密码的判断
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String account = accountEdit.getText().toString();
                String password = passwordEdit.getText().toString();
                // 如果账号是admin且密码是123456，就认为登录成功
//                if (account.equals("admin") && password.equals("123456")) {
                if (account.equals(pref.getString("account", "")) && password.equals(pref.getString("password", ""))) {
                    // 使用SharedPreferences实例获取一个SharedPreferences.Editor对象并传递给editor
                    editor = pref.edit();
                    if (rememberPass.isChecked()) { // 检查复选框是否被选中
                        editor.putBoolean("remember_password", true);
                        editor.putString("account", account);
                        editor.putString("password", password);
                    } else {
                        editor.clear();
                    }
                    editor.apply();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent); // 启动登录成功后的活动
                    finish(); // 结束当前登录活动
                } else {
                    // 如果失败将会弹出提示
                    Toast.makeText(LoginActivity.this, "account or password is invalid", Toast.LENGTH_SHORT).show();
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String account = accountEdit.getText().toString();
                String password = passwordEdit.getText().toString();
                editor = pref.edit();
                editor.putString("account", account);
                editor.putString("password", password);
                editor.apply();
            }
        });
    }
}
















