package com.example.sht.homework.baseclasses;

import android.graphics.Bitmap;

import cn.bmob.v3.datatype.BmobFile;

public class CommentsArticals {

    private String commentText;
    private BmobFile comtentImageFile;
    private Bitmap contentBitmap;

    private User linkUser;
    private String mWriterId;

    public CommentsArticals(){

    }

    public BmobFile getComtentImageFile() {
        return comtentImageFile;
    }

    public void setComtentImageFile(BmobFile comtentImageFile) {
        this.comtentImageFile = comtentImageFile;
    }

    public Bitmap getContentBitmap() {
        return contentBitmap;
    }

    public void setContentBitmap(Bitmap contentBitmap) {
        this.contentBitmap = contentBitmap;
    }

    public String getmWriterId() {
        return mWriterId;
    }

    public void setmWriterId(String mWriterId) {
        this.mWriterId = mWriterId;
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
