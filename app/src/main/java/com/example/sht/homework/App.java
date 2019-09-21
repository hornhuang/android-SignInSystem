package com.example.sht.homework;

import android.app.Application;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.example.sht.homework.baseclasses.User;
import com.example.sht.homework.utils.AppContext;
import com.example.sht.homework.utils.ImageLoader;
import com.example.sht.homework.utils.LruCacheHelper;

import java.util.BitSet;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;

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

    private final String APPID = "bd4814e57ed9c8f00aa0d119c5676cf9";
    // 生成图片缓存
    private static LruCache<String, Bitmap> mLruCache;

    @Override
    public void onCreate() {
        super.onCreate();
        Bmob.initialize(this,APPID);

        if (!AppContext.isInitialized()){
            AppContext.init(getApplicationContext());
        }
        int maxMemory = Math.round(Runtime.getRuntime().maxMemory() / 8);
        if (!LruCacheHelper.isCreated()) {
            LruCacheHelper.create(maxMemory);
        }
    }

    public static LruCache<String, Bitmap> getBitmapLruCache() {
        if (mLruCache == null) {
            int maxMemory = Math.round(Runtime.getRuntime().maxMemory() / 1024);
            mLruCache = LruCacheHelper.create(maxMemory);
        }
        return mLruCache;
    }

    public static String getCurrentUserId() {
        return BmobUser.getCurrentUser(User.class).getObjectId();
    }
}
