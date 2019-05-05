package com.example.joker.signinsystem.bmobmanager;

import android.widget.ImageView;


import com.example.joker.signinsystem.baseclasses.User;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

public class AvatarLoader extends ImageLoader{
    private User mUser;

    public AvatarLoader(ImageView imageView, User user) {
        super(imageView);
        mUser = user;
    }

    public void load(){
        BmobQuery<User> q=new BmobQuery<>();
        q.getObject(mUser.getObjectId(), new QueryListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if(user.getImageFile()!=null) {
                    setUrl(user.getImageFile().getUrl());
                    AvatarLoader.super.load();
                }
            }
        });
    }
}
