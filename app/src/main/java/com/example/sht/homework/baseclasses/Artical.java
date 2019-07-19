package com.example.sht.homework.baseclasses;

import android.graphics.Bitmap;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class Artical extends BmobObject {

    private int articalImageId;
    private int commentNum;
    private BmobFile articalImageFile;
    private Bitmap articlePhoto;
    private String articalTitleText;
    private String articalContextText;

    private List<CommentsArticals> articalComments;

    private User linkUser;
    private String mWriterId;//用于识别LostItem对象的Id,在构造方法中已经初始化

    public Artical(){

    }

    public String getmWriterId() {
        return mWriterId;
    }

    public void setmWriterId(String mWriterId) {
        this.mWriterId = mWriterId;
    }

    public int getArticalImageId() {
        return articalImageId;
    }

    public void setArticalImageId(int articalImageId) {
        this.articalImageId = articalImageId;
    }

    public String getArticalTitleText() {
        return articalTitleText;
    }

    public void setArticalTitleText(String articalTitleText) {
        this.articalTitleText = articalTitleText;
    }

    public String getArticalContextText() {
        return articalContextText;
    }

    public void setArticalContextText(String articalContextText) {
        this.articalContextText = articalContextText;
    }

    public List<CommentsArticals> getArticalComments() {
        return articalComments;
    }

    public void setArticalComments(List<CommentsArticals> articalComments) {
        this.articalComments = articalComments;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }

    public BmobFile getArticalImageFile() {
        return articalImageFile;
    }

    public void setArticalImageFile(BmobFile articalImageFile) {
        this.articalImageFile = articalImageFile;
    }

    public User getLinkUser() {
        return linkUser;
    }

    public void setLinkUser(User linkUser) {
        this.linkUser = linkUser;
    }

    public Bitmap getArticlePhoto() {
        return articlePhoto;
    }

    public void setArticlePhoto(Bitmap articlePhoto) {
        this.articlePhoto = articlePhoto;
    }
}
