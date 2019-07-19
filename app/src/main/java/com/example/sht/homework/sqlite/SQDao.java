package com.example.sht.homework.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;


import com.example.sht.homework.baseclasses.Artical;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.SocketHandler;

public class SQDao {

    private final String TAG = "SQDao";
    private String[] ARTICLE_COLNUMS = new String[]{"Title","Content","CommentNum"};

    private Context context;
    private SQLiteHelper sqLiteHelper;

    public SQDao(Context context){
        this.context = context;
        sqLiteHelper = new SQLiteHelper(context);
    }

    // 判断表中是否有数据
    public boolean isDataExists(){
        int count = 0;
        SQLiteDatabase database = null;
        Cursor cursor = null;

        try {
            database = sqLiteHelper.getWritableDatabase();
            cursor = database.query(SQLiteHelper.TABLE_NAME, new String[]{"COUNT(Id)"}, null, null, null, null, null);

            if (cursor.moveToFirst()){
                count = cursor.getInt(0);
            }
            if (count != 0){
                cursor.close();
                database.close();
                return true;
            }
        }catch (Exception e){

        }finally {
            if (cursor != null){
                cursor.close();
            }
            if (database != null){
                database.close();
            }
        }
        return false;
    }

    public boolean iniTable(){
        SQLiteDatabase database = null;

        try {
            database = sqLiteHelper.getWritableDatabase();
            database.beginTransaction();

            for (int i = 0 ; i < 5 ; i ++){
                database.execSQL("insert into " + sqLiteHelper.TABLE_NAME + " (Title, Content, CommentNum) values ('Arc', 'China', 100)");
            }

            database.setTransactionSuccessful();
        }catch (Exception e){
            Log.e(TAG, "---", e);
        }finally {
            if (database != null){
                database.endTransaction();
                database.close();
            }
        }
        return true;
    }

    public boolean insertItem(Artical artical){
        SQLiteDatabase database = null;
        boolean flag = false;
        try {
            database = sqLiteHelper.getWritableDatabase();
            database.beginTransaction();

            ContentValues values = new ContentValues();
            values.put("Title", "title");
            values.put("Content", "content");
            values.put("CommentNum", 5);
            database.insertOrThrow(SQLiteHelper.TABLE_NAME, null, values);

            database.setTransactionSuccessful();
            flag = true;
        }catch (SQLiteConstraintException e){
            Toast.makeText(context, "主键重复", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Log.e(TAG, "---", e);
        }finally {
            if (database != null){
                database.endTransaction();
                database.close();
            }
        }
        return flag;
    }

    // 执行自定义SQL语句
    public void execSQL(String sql) {
        SQLiteDatabase db = null;

        try {
            if (sql.contains("select")){
//                Toast.makeText(context, R.string.strUnableSql, Toast.LENGTH_SHORT).show();
            }else if (sql.contains("insert") || sql.contains("update") || sql.contains("delete")){
                db = sqLiteHelper.getWritableDatabase();
                db.beginTransaction();
                db.execSQL(sql);
                db.setTransactionSuccessful();
//                Toast.makeText(context, R.string.strSuccessSql, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
//            Toast.makeText(context, R.string.strErrorSql, Toast.LENGTH_SHORT).show();
            Log.e(TAG, "", e);
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
    }

    public List<Artical> getAllArticle(){
        SQLiteDatabase database = null;
        Cursor cursor = null;

        try {
            database = sqLiteHelper.getReadableDatabase();
            cursor = database.query(SQLiteHelper.TABLE_NAME, ARTICLE_COLNUMS,
                    null, null, null, null, null);

            if (cursor.getCount() > 0){
                List<Artical> list = new ArrayList<>(cursor.getCount());
                while (cursor.moveToNext()){
                    list.add(parseArticle(cursor));
                }
                database.close();
                cursor.close();
                return list;
            }
        }catch (Exception e){
            Log.d(TAG, e.getMessage());
        }finally {
            if (database != null){
                database.close();
            }
            if (cursor != null){
                cursor.close();
            }
        }
        return null;
    }

    private Artical parseArticle(Cursor cursor){
        Artical artical = new Artical();
        artical.setArticalTitleText(cursor.getString(cursor.getColumnIndex("Title")));
        artical.setArticalContextText(cursor.getString(cursor.getColumnIndex("Content")));
        artical.setCommentNum(cursor.getInt(cursor.getColumnIndex("CommentNum")));
        return artical;
    }

}
