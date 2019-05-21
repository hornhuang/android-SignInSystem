package com.example.sht.homework.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sht.homework.R;
import com.example.sht.homework.utils.MyToast;
import com.mob.MobSDK;

import java.util.HashMap;
import java.util.Objects;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;


public class MobileLoad extends AppCompatActivity implements View.OnClickListener{

    private Toolbar mToolbar;
    private EditText mEditTextPhoneNumber;
    private EditText mEditTextCode;
    private Button mButtonGetCode;
    private Button mButtonLogin;

    private String phoneNumber;         // 电话号码
    private String verificationCode;    // 验证码

    private boolean flag;   // 操作是否成功

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_load);

        iniViews();
        iniToolbar();

        MobSDK.init(this,"12279ce0cf646","46d8f076d89a1ac9448d4325add1531e");
        EventHandler eventHandler = new EventHandler(){     // 操作回调
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                handler.sendMessage(msg);
            }
        };
        SMSSDK.registerEventHandler(eventHandler);     // 注册回调接口
    }

    private void iniViews(){
        mEditTextPhoneNumber= findViewById(R.id.phone_number);
        mEditTextCode       = findViewById(R.id.verification_code);
        mButtonGetCode      = findViewById(R.id.button_send_verification_code);
        mButtonLogin        = findViewById(R.id.button_login);
        mToolbar             = findViewById(R.id.toolbar);

        mButtonGetCode.setOnClickListener(this);
        mButtonLogin.setOnClickListener(this);
    }

    private void iniToolbar(){
        setSupportActionBar(mToolbar);
        mToolbar.setTitle("手机登录");
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();//返回
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_send_verification_code:
                if (!TextUtils.isEmpty(mEditTextPhoneNumber.getText())) {
                    if (mEditTextPhoneNumber.getText().toString().length() == 11) {
                        phoneNumber = mEditTextPhoneNumber.getText().toString();
                        SMSSDK.getVerificationCode("86", phoneNumber); // 发送验证码给号码的 phoneNumber 的手机
                        mEditTextCode.requestFocus();
                    } else {
                        Toast.makeText(this, "请输入完整的电话号码", Toast.LENGTH_SHORT).show();
                        mEditTextPhoneNumber.requestFocus();
                    }
                } else {
                    Toast.makeText(this, "请输入电话号码", Toast.LENGTH_SHORT).show();
                    mEditTextPhoneNumber.requestFocus();
                }
                break;

            case R.id.button_login:
                if (!TextUtils.isEmpty(mEditTextCode.getText())) {
                    if (mEditTextCode.getText().length() == 4) {
                        verificationCode = mEditTextCode.getText().toString();
                        SMSSDK.submitVerificationCode("86", phoneNumber, verificationCode);
                        Toast.makeText(this, "验证中。。。", Toast.LENGTH_SHORT).show();
                        flag = false;
                    } else {
                        Toast.makeText(this, "请输入完整的验证码", Toast.LENGTH_SHORT).show();
                        mEditTextCode.requestFocus();
                    }
                } else {
                    Toast.makeText(this, "请输入验证码", Toast.LENGTH_SHORT).show();
                    mEditTextCode.requestFocus();
                }
                break;

            default:
                break;
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int event = msg.arg1;
            int result = msg.arg2;
            Object data = msg.obj;

            if (result == SMSSDK.RESULT_COMPLETE) {
                // 如果操作成功
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    // 校验验证码，返回校验的手机和国家代码
                    Toast.makeText(MobileLoad.this, "验证成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MobileLoad.this, MainActivity.class);
                    startActivity(intent);
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    // 获取验证码成功，true为智能验证，false为普通下发短信
                    Toast.makeText(MobileLoad.this, "验证码已发送", Toast.LENGTH_SHORT).show();
                } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                    // 返回支持发送验证码的国家列表
                    Toast.makeText(MobileLoad.this, "国家列表", Toast.LENGTH_SHORT).show();
                }
            } else {
                // 如果操作失败
                if (flag) {
                    Toast.makeText(MobileLoad.this, "验证码获取失败，请重新获取", Toast.LENGTH_SHORT).show();
                    mEditTextPhoneNumber.requestFocus();
                } else {
                    ((Throwable) data).printStackTrace();
                    Toast.makeText(MobileLoad.this, "验证码错误", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterAllEventHandler();  // 注销回调接口
    }
}
