package com.example.sht.homework.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteHelper extends SQLiteOpenHelper {

    private final String TAG = "SQLiteHelper";

    private static final int VERSION_CODE = 3;
    private static final String DB_NAME = "SignInSystemDB.db";
    public static final String TABLE_ARTICLE = "Article";
    public static final String TABLE_USER = "User";

    public SQLiteHelper(Context context) {
        super(context, DB_NAME, null, VERSION_CODE);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table if not exists " + TABLE_ARTICLE + " (Title text, Content text, CommentNum integer)";
        db.execSQL(sql);
        sql = "create table if not exists " + TABLE_USER + " (NickName text, Groups text, Motto text)";
        db.execSQL(sql);
        Log.d(TAG, "succeed");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + TABLE_ARTICLE;
        db.execSQL(sql);
        sql = "DROP TABLE IF EXISTS " + TABLE_USER;
        db.execSQL(sql);
        onCreate(db);
    }
}
