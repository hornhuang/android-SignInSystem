package com.example.sht.homework.utils;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * 该类用于 SDK 文件的存储
 * 1.登录时选择保存登录，存储用户名到 userdata 文件中
 * 2.登录时登录已有的用户，则从 userdata 中读取用户名
 * 3.主活动中，登录个人用户从 userdata 中读取用户名
 */
public class SDKFileManager {

    /*
    保存密码操作
    判断是否保存密码
     */
    public static void saveAccount(String objectId, AppCompatActivity activity){
        FileOutputStream out = null;
        BufferedWriter writer = null;
        try {
            out = activity.openFileOutput("account", Context.MODE_PRIVATE);
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
    读取用户名
     */
    public static String getAccount(AppCompatActivity activity){
        FileInputStream in = null;
        BufferedReader reader = null;
        StringBuffer context = new StringBuffer();
        try {
            in = activity.openFileInput("account");
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

    /*
    保存密码操作
    判断是否保存密码
     */
    public static void savePassword(String objectId, AppCompatActivity activity){
        FileOutputStream out = null;
        BufferedWriter writer = null;
        try {
            out = activity.openFileOutput("password", Context.MODE_PRIVATE);
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
    public static String getPassword(AppCompatActivity activity){
        FileInputStream in = null;
        BufferedReader reader = null;
        StringBuffer context = new StringBuffer();
        try {
            in = activity.openFileInput("password");
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
}
