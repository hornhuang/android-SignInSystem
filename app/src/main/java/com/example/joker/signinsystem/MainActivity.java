package com.example.joker.signinsystem;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.joker.signinsystem.MainFragment.Personal;
import com.example.joker.signinsystem.MainFragment.Ranking;
import com.example.joker.signinsystem.MainFragment.Summary;
import com.example.joker.signinsystem.baseclasses.User;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    public static String APPID = "bd4814e57ed9c8f00aa0d119c5676cf9";

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    replaceFragment(new Personal());
                    return true;
                case R.id.navigation_dashboard:
                    replaceFragment(new Ranking());
                    return true;
                case R.id.navigation_ranking:
                    replaceFragment(new Summary());
                    return true;
            }
            return false;
        }
    };

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment,fragment);
        transaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //第二：默认初始化
        Bmob.initialize(this, APPID);

        //先用首页碎片替换碎片
        replaceFragment(new Personal());

        //底部选择碎片切换
        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //Toolbar代替ActionBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String userId = getSavedCode();
        BmobQuery<User> bmobQuery = new BmobQuery<User>();
        bmobQuery.getObject(userId, new QueryListener<User>() {
            @Override
            public void done(User object, BmobException e) {
                if (e == null) {
                    CircleImageView imageView = findViewById(R.id.user_image);
                    TextView userName = findViewById(R.id.user_name);
                    TextView userMotto = findViewById(R.id.user_motto);

                    imageView.setImageBitmap(object.getHeadIcon());
                    userName.setText(object.getUsername() + "");//
                    userMotto.setText(object.getMotto());
                } else {
                    Toast.makeText(MainActivity.this, "查询失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    /*
    读取密码操作
     */
    private String getSavedCode(){
        FileInputStream in = null;
        BufferedReader reader = null;
        StringBuffer context = new StringBuffer();
        try {
            in = openFileInput("data");
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
        return context.toString();
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onStop(){
        super.onStop();
    }

    @Override
    public void onStart(){
        super.onStart();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

}
