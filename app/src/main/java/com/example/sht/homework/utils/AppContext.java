package com.example.sht.homework.utils;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;

import com.example.sht.homework.App;
import com.example.sht.homework.sqlite.SQDao;


/**单例模式
 *
 * 向整个应用提供 App (application) 单例
 */
public class AppContext {

    private static AppContext instance;
    // 全局唯一 context 对象，由 application 获取
    private Context applicationContext;
    // 全剧唯一 SQDao 对象，由于 SQLite 数据库操作
    private SQDao sqDao;

    public AppContext(Context application){
        this.applicationContext = application;
        this.sqDao = new SQDao(applicationContext);
    }

    public Context getApplicationContext() {
        return applicationContext;
    }

    public SQDao getSqDao() {
        return sqDao;
    }

    public static AppContext getInstance() {
        if (instance == null){
            throw new RuntimeException();
        }
        return instance;
    }

    public static void init(Context context){
        if (instance != null){
            throw new RuntimeException();
        }
        instance = new AppContext(context);
    }

    public static boolean isInitialized(){
        return (instance != null);
    }
}
