package com.java.陈敬哲;


import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 登录界面Demo
 *
 * @author ZHY
 *
 */
public class LoginActivity extends AppCompatActivity implements OnClickListener {

    private EditText username, password;
    private Button bt_username_clear;
    private Button bt_pwd_clear;
    private Button bt_pwd_eye;
    private Button login;
    private Button register;
    private Button up_collect;
    private Button down_collect;
    private Button up_history;
    private Button down_history;
    private Button logout;
    private TextView tx;
    private boolean isOpen = false;
    private RelativeLayout layout1;//out  //已经登陆的界面
    private RelativeLayout layout0;//in  登陆界面
    private RelativeLayout layout;//总的
    private EditText editText_ip,editText_code,editText_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        if(Data.theme==false){
            layout.setBackgroundColor(Color.BLACK);
            editText_code.setTextColor(Color.WHITE);
            editText_user.setTextColor(Color.WHITE);
            editText_ip.setTextColor(Color.WHITE);
            editText_ip.setHintTextColor(Color.WHITE);
            editText_code.setHintTextColor(Color.WHITE);
            editText_user.setHintTextColor(Color.WHITE);
            tx.setTextColor(Color.WHITE);
        }
        if(Data.login==false){
            layout1.setVisibility(View.GONE);
        }
        else{
            layout0.setVisibility(View.GONE);
            tx.setText(Data.user);
        }
    }

    private void initView() {
        username = (EditText) findViewById(R.id.username);
        // 监听文本框内容变化
        password = (EditText) findViewById(R.id.password);
        // 监听文本框内容变化

        bt_pwd_eye = (Button) findViewById(R.id.bt_pwd_eye);
        bt_pwd_eye.setOnClickListener(this);

        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(this);
        register = (Button) findViewById(R.id.register);
        register.setOnClickListener(this);
        editText_code=findViewById(R.id.password);
        editText_ip=findViewById(R.id.ip);
        editText_user=findViewById(R.id.username);

        layout=findViewById(R.id.layout1);
        layout0=findViewById(R.id.login_layout);
        layout1=findViewById(R.id.logout_layout);
        up_history=findViewById(R.id.up_history);
        down_history=findViewById(R.id.down_history);
        up_collect=findViewById(R.id.up_collect);
        down_collect=findViewById(R.id.down_collect);
        logout=findViewById(R.id.logout);
        tx=findViewById(R.id.user);
        up_history.setOnClickListener(this);
        down_history.setOnClickListener(this);
        up_collect.setOnClickListener(this);
        down_collect.setOnClickListener(this);
        logout.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_pwd_eye:
                // 密码可见与不可见的切换
                if (isOpen) {
                    isOpen = false;
                } else {
                    isOpen = true;
                }

                // 默认isOpen是false,密码不可见
                changePwdOpenOrClose(isOpen);

                break;
            case R.id.login:
                // TODO 登录按钮
                //Client.connect(editText_ip.getText().toString());
                new Thread(){
                    public void run(){
                        if(Data.connected==false){
                            Client.connect("183.172.146.138");
                            Data.connected=true;
                        }
                        Data.user=editText_user.getText().toString();
                        Client.login(Data.user, editText_code.getText().toString());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                long startTime = System.currentTimeMillis();
                                while(Client.isLogedin==false&&System.currentTimeMillis()-startTime<3000)
                                    Toast.makeText(LoginActivity.this,"登陆中。。。",Toast.LENGTH_SHORT).show();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(Client.isLogedin) {
                                            layout0.setVisibility(View.GONE);
                                            layout1.setVisibility(View.VISIBLE);
                                            tx.setText("当前用户："+Data.user);
                                            Data.login=true;
                                        }
                                        else
                                            Toast.makeText(LoginActivity.this, "登陆失败", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }
                }.start();

                break;
            case R.id.register:
                // 注册按钮
                new Thread(){
                    public void run(){
                        if(Data.connected==false){
                            Client.connect("183.172.146.138");
                            Data.connected=true;
                        }
                        Toast.makeText(LoginActivity.this,"注册。",Toast.LENGTH_SHORT).show();
                        Client.register(editText_user.getText().toString(), editText_code.getText().toString());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                long startTime = System.currentTimeMillis();
                                while(Client.isRegistered==false&&System.currentTimeMillis()-startTime<3000)
                                    Toast.makeText(LoginActivity.this,"注册中。。。",Toast.LENGTH_SHORT).show();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(Client.isRegistered)
                                            Toast.makeText(LoginActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                                        else
                                            Toast.makeText(LoginActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        });
                    }
                }.start();

                break;
            case R.id.logout:
                new Thread(){
                    public void run(){
                        Client.logut();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                layout0.setVisibility(View.VISIBLE);
                                layout1.setVisibility(View.GONE);
                                Data.login=false;
                                Toast.makeText(LoginActivity.this, "退出登陆", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }.start();
            case R.id.up_history:
                new Thread(){
                    public void run(){
                        Client.uploadHistory();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }.start();
            case R.id.down_history:
                new Thread(){
                    public void run(){
                        Client.DownloadHistory();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //Toast.makeText(LoginActivity.this, "下载2成功", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }.start();
            case R.id.up_collect:
                new Thread(){
                    public void run(){
                        Client.uploadCollectedSet();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }.start();
            case R.id.down_collect:
                new Thread(){
                    public void run(){
                        Client.DownloadCollectedSet();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //Toast.makeText(LoginActivity.this, "下载1成功", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }.start();
            default:
                break;
        }
    }

    /**
     * 密码可见与不可见的切换
     *
     * @param flag
     */
    private void changePwdOpenOrClose(boolean flag) {
        // 第一次过来是false，密码不可见
        if (flag) {
            // 密码可见
            // 设置EditText的密码可见
            password.setTransformationMethod(HideReturnsTransformationMethod
                    .getInstance());
        } else {
            // 设置EditText的密码隐藏
            password.setTransformationMethod(PasswordTransformationMethod
                    .getInstance());
        }
    }
    private void toIn(){

    }

}
