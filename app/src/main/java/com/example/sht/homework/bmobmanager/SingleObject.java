package com.example.sht.homework.bmobmanager;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.sht.homework.activities.BaseActivity;
import com.example.sht.homework.baseclasses.User;
import com.example.sht.homework.utils.MyLog;
import com.example.sht.homework.utils.MyToast;


import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

public class SingleObject extends BaseActivity {

    private User user;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

        }
    };

    public User getUser(final AppCompatActivity context, final String userId, User currentUser){
        BmobQuery<User> bmobQuery = new BmobQuery<>();
        bmobQuery.getObject(userId, new QueryListener<User>() {
            @Override
            public void done(User object,BmobException e) {
                if(e==null){
//                    currentUser = object;
                    MyToast.makeToast(context, "查询成功");
                }else{
                    MyToast.makeToast(context, "查询失败：" + e.getMessage());
                }
            }
        });
        return currentUser;
    }

}
