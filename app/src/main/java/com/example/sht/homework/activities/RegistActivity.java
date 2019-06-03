package com.example.sht.homework.activities;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.sht.homework.R;
import com.example.sht.homework.baseclasses.User;

import java.util.Objects;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class RegistActivity extends BaseActivity implements View.OnClickListener {

    private Toolbar toolbar;

    private EditText et_regist_user;
    private EditText et_regist_password;
    private EditText et_regist_fullname;
    private EditText et_regist_group;
    private EditText et_regist_telephone;
    private Button bt_regist_save,bt_regist_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_regist);

        iniviews();
        iniToolbar();
    }

    private void iniviews(){
        et_regist_user= (EditText)findViewById(R.id.et_regist_user);
        et_regist_password= (EditText) findViewById(R.id.et_regist_password);
        et_regist_fullname = (EditText) findViewById(R.id.et_regist_fullname);
        et_regist_group= (EditText) findViewById(R.id.et_regist_group);
        et_regist_telephone = (EditText) findViewById(R.id.et_regist_telephone);
        bt_regist_save= (Button) findViewById(R.id.bt_regist_save);// 组册按钮
        bt_regist_cancel= (Button) findViewById(R.id.bt_regist_cancel);
        toolbar = findViewById(R.id.tool_bar);

        bt_regist_save.setOnClickListener(this);
        bt_regist_cancel.setOnClickListener(this);
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
        switch(v.getId()){
            case R.id.bt_regist_save:
                String user_num=et_regist_user.getText().toString();
                String user_password=et_regist_password.getText().toString().trim();
                String user_fullname = et_regist_fullname.getText().toString().trim();
                String user_group = et_regist_group.getText().toString().trim();
                String user_telephone = et_regist_telephone.getText().toString().trim();
                // 非空验证
                if (user_num.isEmpty() || user_password.isEmpty() || user_fullname.isEmpty() ||  user_group.isEmpty()  ||  user_telephone.isEmpty()) {
                    Toast.makeText(RegistActivity.this, "账号或密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                // 使用BmobSDK提供的注册功能
                User myUser=new User();
                myUser.setUsername(user_num);
                myUser.setPassword(user_password);
                myUser.setFullname(user_fullname);
                myUser.setGroup(user_group);
                myUser.setTelephone(user_telephone);
                myUser.signUp(new SaveListener<User>() {
                    @Override
                    public void done(User s, BmobException e) {
                        if(e==null){
                            Toast.makeText(RegistActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                        }else{
                            //loge(e);
                            Toast.makeText(RegistActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                //注意：不能用save方法进行注册
                finish();

                break;
            case R.id.bt_regist_cancel:
                finish();

                break;
            default:
                break;
        }
    }
}
