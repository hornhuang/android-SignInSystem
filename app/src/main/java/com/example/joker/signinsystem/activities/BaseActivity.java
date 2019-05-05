package com.example.joker.signinsystem.activities;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import cn.bmob.v3.Bmob;

public class BaseActivity  extends AppCompatActivity implements View.OnClickListener {

    public static String APPID = "bd4814e57ed9c8f00aa0d119c5676cf9";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    //提供以下两种方式进行初始化操作：
    //第一：设置BmobConfig，允许设置请求超时时间、文件分片上传时每片的大小、文件的过期时间(单位为秒)
		/*BmobConfig config =new BmobConfig.Builder(this)
		//设置appkey
		.setApplicationId(APPID)
		//请求超时时间（单位为秒）：默认15s
		.setConnectTimeout(30)
		//文件分片上传时每片的大小（单位字节），默认512*1024
		.setUploadBlockSize(1024*1024)
		//文件的过期时间(单位为秒)：默认1800s
		.setFileExpiration(5500)
		.build();
		Bmob.initialize(config);*/
    //第二：默认初始化
        Bmob.initialize(this,APPID);
//		Bmob.resetDomain("http://open-vip.bmob.cn/8/");
//		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
//		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());

    }

    @Override
    public void onClick(View v) {

    }

}
