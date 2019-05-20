package com.example.sht.homework.utils.bmobmanager;

import android.content.Context;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import com.example.sht.homework.activities.BaseActivity;
import com.example.sht.homework.activities.bases.UserDetailActivity;
import com.example.sht.homework.baseclasses.User;
import com.example.sht.homework.utils.MyLog;
import com.example.sht.homework.utils.MyToast;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

public class SingleObject extends BaseActivity {

    public static void getUser(final Context context, final String userId){
        BmobQuery<User> bmobQuery = new BmobQuery<>();
        bmobQuery.getObject(userId, new QueryListener<User>() {
            @Override
            public void done(User object,BmobException e) {
                if(e==null){
                    MyLog.Log(userId);
                    ((UserDetailActivity) context).setItemUser(object);
                    ((UserDetailActivity) context).getHandler().sendMessage(new Message());
                    MyToast.makeToast((AppCompatActivity) context, "查询成功");
                }else{
                    MyToast.makeToast((AppCompatActivity) context, "查询失败：" + e.getMessage());
                }
            }
        });
    }

}
