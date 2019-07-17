package com.example.sht.homework.utils;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;

import com.example.sht.homework.App;


/**单例模式
 *
 * 向整个应用提供 App (application) 单例
 */
public class AppContext {

    private static AppContext instance;

    private Context applicationContext;

    public AppContext(Context application){
        this.applicationContext = application;
    }

    public Context getApplicationContext() {
        return applicationContext;
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
