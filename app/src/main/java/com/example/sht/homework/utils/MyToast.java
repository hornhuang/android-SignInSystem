package com.example.sht.homework.utils;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public final class MyToast {

    public static void makeToast(Activity activity, String content){
        Toast.makeText(activity, content, Toast.LENGTH_SHORT).show();
    }

}

