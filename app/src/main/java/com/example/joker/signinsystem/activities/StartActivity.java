package com.example.joker.signinsystem.activities;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;


import com.example.joker.signinsystem.R;
import com.example.joker.signinsystem.baseclasses.User;
import com.example.joker.signinsystem.utils.MyToast;
import com.example.joker.signinsystem.utils.SDKFileManager;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;

/**开始界面 负责选择功能
 * 登录、注册、短信验证码登录
 *
 * @author fishinwater
 */
public class StartActivity extends AppCompatActivity implements View.OnClickListener{

    /*
    数据成员
     */
    private TextView tv_regist;
    private EditText et_login_user, et_login_password;
    private Button bt_login;
    private Button bt_mobile_login;
    private RadioButton radioButton;
    private Boolean isClick = false; // 登录按钮是否已经点击
    User user;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_start);

        iniViews();
//
//        if (!SDKFileManager.getSavedCode(this).toString().equals("")){
//            Toast.makeText(this, "欢迎回来 ～",Toast.LENGTH_SHORT).show();
//            startActivity(new Intent(this, MainActivity.class));
//            finish();
//        }
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
                if (!isClick){// 避免重复点击
                    isClick = true;
                    logIn();
                }
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
        String user_name = et_login_user.getText().toString();
        String user_password = et_login_password.getText().toString().trim();
        // 非空验证
        if (user_name.isEmpty() || user_password.isEmpty()) {
            Toast.makeText(StartActivity.this, "账号或密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        loginByAccount(user_name, user_password); // 使用BmobSDK提供的登录功能
    }

    /**
     * 账号密码登录
     */
    public void loginByAccount(String account, String password) {
        //此处替换为你的用户名密码
        BmobUser.loginByAccount(account, password, new LogInListener<User>() {

            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    MyToast.makeToast(StartActivity.this, "登录成功");
                    try {
                        user = BmobUser.getCurrentUser(User.class);
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
                } else {
                    MyToast.makeToast(StartActivity.this, "登陆失败");
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && requestCode == 200) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) { // 用户点的拒绝，仍未拥有权限
                    Toast.makeText(this, "请在设置中打开摄像头或存储权限", Toast.LENGTH_SHORT).show();
                    // 可以选择添加如下代码在系统设置中打开该应用的设置页面
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                    return;
                }
            }
        }
    }
}
