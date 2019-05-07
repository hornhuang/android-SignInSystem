package com.example.joker.signinsystem.activities;

import android.net.http.EventHandler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.joker.signinsystem.R;
import com.mob.MobSDK;

import java.util.Objects;


public class MobileLoad extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "WholeWorld";

    private Toolbar toolbar;

    private EditText mEditTextPhoneNumber;
    private EditText mEditTextCode;
    private Button mButtonGetCode;
    private Button mButtonLogin;

    private EventHandler eventHandler;
    private String strPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_load);

        iniViews();
        iniToolbar();
//        MobSDK.init(this, "2839cb4b272a6", "cbfe853cb76be819d9349911b9d9a720");
        MobSDK.init(this);

//        eventHandler = new EventHandler() {
//
//            /**
//             * 在操作之后被触发
//             *
//             * @param event  参数1
//             * @param result 参数2 SMSSDK.RESULT_COMPLETE表示操作成功，为SMSSDK.RESULT_ERROR表示操作失败
//             * @param data   事件操作的结果
//             */
//
//            @Override
//            public void afterEvent(int event, int result, Object data) {
//                Message message = myHandler.obtainMessage(0x00);
//                message.arg1 = event;
//                message.arg2 = result;
//                message.obj = data;
//                myHandler.sendMessage(message);
//            }
//        };
//
//        MobSDK.registerEventHandler(eventHandler);
    }

    private void iniViews(){
        mEditTextPhoneNumber = (EditText) findViewById(R.id.phone_number);
        mEditTextCode = (EditText) findViewById(R.id.verification_code);
        mButtonGetCode = (Button) findViewById(R.id.button_send_verification_code);
        mButtonLogin = (Button) findViewById(R.id.button_login);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        mButtonGetCode.setOnClickListener(this);
        mButtonLogin.setOnClickListener(this);
    }

    private void iniToolbar(){
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();//返回
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

        }
    }

//
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        SMSSDK.unregisterEventHandler(eventHandler);
//    }
//    @Override
//    public void onClick(View view) {
//
//        if (view.getId() == R.id.button_login) {
//            String strCode = mEditTextCode.getText().toString();
//            if (null != strCode && strCode.length() == 4) {
//                Log.d(TAG, mEditTextCode.getText().toString());
//                SMSSDK.submitVerificationCode("86", strPhoneNumber, mEditTextCode.getText().toString());
//            } else {
//                Toast.makeText(this, "密码长度不正确", Toast.LENGTH_SHORT).show();
//            }
//        } else if (view.getId() == R.id.button_send_verification_code) {
//            strPhoneNumber = mEditTextPhoneNumber.getText().toString();
//            if (null == strPhoneNumber || "".equals(strPhoneNumber) || strPhoneNumber.length() != 11) {
//                Toast.makeText(this, "电话号码输入有误", Toast.LENGTH_SHORT).show();
//                return;
//            }
//            SMSSDK.getVerificationCode("86", strPhoneNumber);
//            mButtonGetCode.setClickable(false);
//            //开启线程去更新button的text
//            new Thread() {
//                @Override
//                public void run() {
//                    int totalTime = 60;
//                    for (int i = 0; i < totalTime; i++) {
//                        Message message = myHandler.obtainMessage(0x01);
//                        message.arg1 = totalTime - i;
//                        myHandler.sendMessage(message);
//                        try {
//                            sleep(1000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    myHandler.sendEmptyMessage(0x02);
//                }
//            }.start();
//        }
//    }
//
//    Handler myHandler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case 0x00:
//                    int event = msg.arg1;
//                    int result = msg.arg2;
//                    Object data = msg.obj;
//                    Log.e(TAG, "result : " + result + ", event: " + event + ", data : " + data);
//                    if (result == SMSSDK.RESULT_COMPLETE) { //回调  当返回的结果是complete
//                        if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) { //获取验证码
//                            Toast.makeText(MobileLoad.this, "发送验证码成功", Toast.LENGTH_SHORT).show();
//                            Log.d(TAG, "get verification code successful.");
//                        } else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) { //提交验证码
//                            Log.d(TAG, "submit code successful");
//                            Toast.makeText(MobileLoad.this, "提交验证码成功", Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(MobileLoad.this, MainActivity.class);
//                            startActivity(intent);
//                        } else {
//                            Log.d(TAG, data.toString());
//                        }
//                    } else { //进行操作出错，通过下面的信息区分析错误原因
//                        try {
//                            Throwable throwable = (Throwable) data;
//                            throwable.printStackTrace();
//                            JSONObject object = new JSONObject(throwable.getMessage());
//                            String des = object.optString("detail");//错误描述
//                            int status = object.optInt("status");//错误代码
//                            //错误代码：  http://wiki.mob.com/android-api-%E9%94%99%E8%AF%AF%E7%A0%81%E5%8F%82%E8%80%83/
//                            Log.e(TAG, "status: " + status + ", detail: " + des);
//                            if (status > 0 && !TextUtils.isEmpty(des)) {
//                                Toast.makeText(MobileLoad.this, des, Toast.LENGTH_SHORT).show();
//                                return;
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    break;
//                case 0x01:
//                    mButtonGetCode.setText("重新发送" + msg.arg1 + ")");
//                    break;
//                case 0x02:
//                    mButtonGetCode.setText("获取验证码");
//                    mButtonGetCode.setClickable(true);
//                    break;
//            }
//        }
//    };

}
