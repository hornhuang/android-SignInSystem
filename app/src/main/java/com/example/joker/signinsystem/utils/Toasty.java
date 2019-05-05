package com.example.joker.signinsystem.utils;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class Toasty {

    public static void Toasty(AppCompatActivity activity, String content){
        Toast.makeText(activity, content, Toast.LENGTH_SHORT).show();
    }

}
