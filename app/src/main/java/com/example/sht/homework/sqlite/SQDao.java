package com.example.sht.homework.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.StringDef;
import android.util.Log;
import android.widget.Toast;


import com.example.sht.homework.baseclasses.Artical;
import com.example.sht.homework.baseclasses.User;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

/**
 * 集合所有本地 SQLite 操作
 */
public class SQDao {

    private final String TAG = "SQDao";

    /** 仿造 Toast 的 Toast.LENGTH_LONG & Toast.LENGTH_SHORT 固定参数传值
     * https://www.jianshu.com/p/3ba6c409b275
     */
    public static final String ARTICLE = "Article";
    public static final String USER = "User";

    @Retention(RetentionPolicy.RUNTIME)
    @StringDef({ARTICLE, USER})
    public @interface Table{

    }

    private String[] ARTICLE_COLNUMS = new String[]{"Title","Content","CommentNum"};
    private String[] USER_COLNUMS    = new String[]{"NickName","Groups","Motto"};

    private Context context;
    private SQLiteHelper sqLiteHelper;

    public SQDao(Context context){
        this.context = context;
        sqLiteHelper = new SQLiteHelper(context);
    }

    /**
     * 判断表中是否有数据
     *
     * Determine if there is data in the table
     * @param table
     * @return
     */
    public boolean isDataExists(@Table String table){
        int count = 0;
        SQLiteDatabase database = null;
        Cursor cursor = null;

        try {
            database = sqLiteHelper.getWritableDatabase();
            switch (table){
                case USER:
                    cursor = database.query(SQLiteHelper.TABLE_USER, new String[]{"COUNT(NickName)"},
                            null, null, null, null, null);
                    break;

                case ARTICLE:
                    cursor = database.query(SQLiteHelper.TABLE_ARTICLE, new String[]{"COUNT(Title)"},
                            null, null, null, null, null);
                    break;
            }

            assert cursor != null;
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

    /**
     * 用于初始化数据库
     * 作为测试使用 可以向数据库中添加五条数据用于测试
     *
     * Used to initialize the database as a test.
     * You can add five pieces of data to the database for testing.
     * @param table
     * @return
     */
    public boolean iniTable(@Table String table){
        SQLiteDatabase database = null;
        boolean flag = false;

        try {
            database = sqLiteHelper.getWritableDatabase();
            database.beginTransaction();

            for (int i = 0 ; i < 5 ; i ++){
                switch (table){
                    case SQDao.ARTICLE:
                        database.execSQL("insert into " + sqLiteHelper.TABLE_ARTICLE + " (Title, Content, CommentNum) values ('Arc', 'China', 100)");
                        break;
                    case SQDao.USER:
                        database.execSQL("insert into " + sqLiteHelper.TABLE_USER + " (NickName, Groups, Motto) values ('Name', 'China', 'Motto')");
                        break;
                }
            }

            database.setTransactionSuccessful();
            flag = true;
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

    /**
     * 用户表 User
     *
     * 一次性插入多条数据
     *
     * User table User
     * Insert multiple pieces of data at once
     * @param lists
     * @return
     */
    public boolean insertAllUsers(List<User> lists){
        boolean flag = true;

        for (User user : lists){
            flag = flag && insertUser(user);
        }

        return flag;
    }

    /**
     * 帖子（文章）表 Article
     *
     * 一次性插入多条数据
     *
     * Article table Article
     * Insert multiple pieces of data at once
     * @param lists
     * @return
     */
    public boolean insertAllArticles(List<Artical> lists){
        boolean flag = true;

        for (Artical artical : lists){
            flag = flag && insertArticle(artical);
        }

        return flag;
    }

    /**
     * 一次性插入一条数据 Article
     *
     * Insert one piece of data at a time
     * @param artical
     * @return
     */
    public boolean insertArticle(Artical artical){
        SQLiteDatabase database = null;
        boolean flag = false;
        try {
            database = sqLiteHelper.getWritableDatabase();
            database.beginTransaction();

            ContentValues values = new ContentValues();
            values.put("Title", "title");
            values.put("Content", "content");
            values.put("CommentNum", 5);
            database.insertOrThrow(SQLiteHelper.TABLE_ARTICLE, null, values);

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

    /**
     * 一次性插入一条数据 Article
     *
     * Insert one piece of data at a time
     * @param user
     * @return
     */
    public boolean insertUser(User user){
        SQLiteDatabase database = null;
        boolean flag = false;

        try {
            database = sqLiteHelper.getWritableDatabase();
            database.beginTransaction();

            ContentValues values = new ContentValues();
            values.put("NickName", user.getName());
            values.put("Groups", user.getGroup());
            values.put("Motto", user.getMotto());
            database.insertOrThrow(SQLiteHelper.TABLE_USER, null, values);

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

    /**
     * 执行自定义SQL语句
     *
     * Execute a custom SQL statement
     */
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

    public List<Artical> getAllArticles(){
        SQLiteDatabase database = null;
        Cursor cursor = null;

        try {
            database = sqLiteHelper.getReadableDatabase();
            cursor = database.query(SQLiteHelper.TABLE_ARTICLE, ARTICLE_COLNUMS,
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

    public List<User> getAllUsers(){
        SQLiteDatabase database = null;
        Cursor cursor = null;

        try {
            database = sqLiteHelper.getReadableDatabase();
            cursor = database.query(SQLiteHelper.TABLE_USER, USER_COLNUMS,
                    null, null, null, null, null);

            if (cursor.getCount() > 0){
                List<User> list = new ArrayList<>(cursor.getCount());
                while (cursor.moveToNext()){
                    list.add(parseUser(cursor));
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
        artical.setArticalTitleText(cursor.getString(cursor.getColumnIndex(ARTICLE_COLNUMS[0])));
        artical.setArticalContextText(cursor.getString(cursor.getColumnIndex(ARTICLE_COLNUMS[1])));
        artical.setCommentNum(cursor.getInt(cursor.getColumnIndex(ARTICLE_COLNUMS[2])));
        return artical;
    }

    private User parseUser(Cursor cursor){
        User user = new User();
        user.setName(cursor.getString(cursor.getColumnIndex(USER_COLNUMS[0])));
        user.setGroup(cursor.getString(cursor.getColumnIndex(USER_COLNUMS[1])));
        user.setMotto(cursor.getString(cursor.getColumnIndex(USER_COLNUMS[2])));
        return user;
    }

}
