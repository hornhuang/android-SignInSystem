package com.example.joker.signinsystem;

import android.app.Application;

/**
 * Application 类
 * 用途 信息传递的桥梁
 * 其他活动中听过调用 (App)getApplicationContext() 获得
 * 对其数据成员进行读取、赋值
 * 以此实现了 信息的通讯
 *
 * 实例化方法：在 MainFest 中
 * application 标签下加 android:name=".App"
 *
 * author：finshinwater
 * */
public class App extends Application {

    private String userId;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
