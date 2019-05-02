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

        radioButton.isSelected();
        loadUserCode();
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
                                saveCode(user.getObjectId());
                                if (radioButton.isSelected()){
                                    saveUserCode(user.getObjectId());
                                }else {
                                    saveUserCode("");
                                }
                                finish();
                                startActivity(new Intent(StartActivity.this, MainActivity.class));
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
    保存密码操作
    由于其他活动读取用户信息
     */
    private void saveCode(String objectId){
        FileOutputStream out = null;
        BufferedWriter writer = null;
        try {
            out = openFileOutput("data", Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(objectId);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null){
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*
    读取密码操作
    判断是否保存密码
     */
    private void loadUserCode(){
        FileInputStream in = null;
        BufferedReader reader = null;
        StringBuffer context = new StringBuffer();
        try {
            in = openFileInput("userdata");
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null){
                context.append(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (!context.toString().equals((""))){
            Toast.makeText(this, "欢迎回来 ～",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    /*
    保存密码操作
    判断是否保存密码
     */
    private void saveUserCode(String objectId){
        FileOutputStream out = null;
        BufferedWriter writer = null;
        try {
            out = openFileOutput("userdata", Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(objectId);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null){
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*
    用户登录信息
     */
    class UserLogInIfo implements Serializable {
        private static final long serialVersionUID = -6846034858002233878L;
        private String account;
        private String password;

        public UserLogInIfo(String account, String password) {
            this.account = account;
            this.password = password;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

}
