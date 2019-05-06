package com.example.joker.signinsystem.baseclasses;

import android.graphics.Bitmap;

import java.util.UUID;

import cn.bmob.v3.datatype.BmobFile;

public class CommentsArticals {

    private UUID mCommentId;
    private int commentImageId;
    private String commentText;
    private User linkUser;
    private BmobFile comtentImageFile;
    private Bitmap contentBitmap;

    public CommentsArticals(){
        mCommentId = UUID.randomUUID();
    }

    public UUID getmCommentId() {
        return mCommentId;
    }

    public void setmCommentId(UUID mCommentId) {
        this.mCommentId = mCommentId;
    }

    public int getCommentImageId() {
        return commentImageId;
    }

    public void setCommentImageId(int commentImageId) {
        this.commentImageId = commentImageId;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public User getLinkUser() {
        return linkUser;
    }

    public void setLinkUser(User linkUser) {
        this.linkUser = linkUser;
    }
}
