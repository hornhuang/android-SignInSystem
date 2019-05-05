package com.example.joker.signinsystem.activities;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;


import com.example.joker.signinsystem.MainActivity;
import com.example.joker.signinsystem.R;
import com.example.joker.signinsystem.baseclasses.User;
import com.example.joker.signinsystem.utils.SDKFileManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class StartActivity extends AppCompatActivity implements View.OnClickListener{

    /*
    数据成员
     */
    private TextView tv_regist;
    private EditText et_login_user, et_login_password;
    private Button bt_login;
    private Button bt_mobile_login;
    private RadioButton radioButton;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_start);
        Bmob.initialize(this, "bd4814e57ed9c8f00aa0d119c5676cf9");

        iniViews();

        radioButton.isSelected();
        if (!SDKFileManager.getSavedCode(this).toString().equals("")){
            Toast.makeText(this, "欢迎回来 ～",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    /*
    绑定控件
     */
    private void iniViews(){
        tv_regist = (TextView) findViewById(R.id.tv_regist);
        bt_login = (Button) findViewById(R.id.login);
        bt_mobile_login = (Button) findViewById(R.id.monile_login);
        et_login_user = (EditText) findViewById(R.id.et_login_user);
        et_login_password = (EditText) findViewById(R.id.et_login_password);
        radioButton = (RadioButton) findViewById(R.id.radio_button);
        tv_regist.setOnClickListener(StartActivity.this);
        bt_login.setOnClickListener(StartActivity.this);
        bt_mobile_login.setOnClickListener(StartActivity.this);
        radioButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        switch (arg0.getId()) {
            case R.id.tv_regist:
                Intent intent_regist = new Intent(StartActivity.this, RegistActivity.class);
                startActivity(intent_regist);
                break;
            case R.id.monile_login:
                Intent intent_mobile = new Intent(StartActivity.this, MobileLoad.class);
                startActivity(intent_mobile);
                break;
            case R.id.login:
                logIn();
                break;
            case R.id.radio_button:
                if (radioButton.isSelected()){
                    radioButton.setSelected(false);
                    radioButton.setChecked(false);
                }else {
                    radioButton.setSelected(true);
                    radioButton.setChecked(true);
                }
                break;
        }
    }

    /*
    检查信息是否完整并登录
     */
    private void logIn(){
        final String user_num = et_login_user.getText().toString();
        String user_password = et_login_password.getText().toString().trim();
        // 非空验证
        if (user_num.isEmpty() || user_password.isEmpty()) {
            Toast.makeText(StartActivity.this, "账号或密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        User bu2 = new User();
        bu2.setUsername(user_num);
        bu2.setPassword(user_password);
        // 使用BmobSDK提供的登录功能

        bu2.login(new SaveListener<BmobUser>() {
            @Override
            public void done(BmobUser bmobUser, BmobException e) {
                if(e==null){
                    try {
                        Toast.makeText(StartActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                        User user = BmobUser.getCurrentUser(User.class);
                        SDKFileManager.saveObjectId(user.getObjectId(), StartActivity.this);
                        if (radioButton.isSelected()){
                            SDKFileManager.saveUserCode(user.getObjectId(), StartActivity.this);
                        }else {
                            SDKFileManager.saveUserCode("", StartActivity.this);
                        }
                        finish();
                        MainActivity.actionStart(StartActivity.this);
                    }catch (Exception e2){
                        Log.d("TAG", e2.getMessage());
                    }
                    //通过BmobUser user = BmobUser.getCurrentUser()获取登录成功后的本地用户信息
                    //如果是自定义用户对象MyUser，可通过MyUser user = BmobUser.getCurrentUser(MyUser.class)获取自定义用户信息
                }else{
                    Toast.makeText(StartActivity.this, "登陆失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
