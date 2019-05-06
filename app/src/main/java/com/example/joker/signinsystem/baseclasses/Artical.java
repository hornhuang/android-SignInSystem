package com.example.joker.signinsystem.baseclasses;

import java.util.List;
import java.util.UUID;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class Artical extends BmobObject {

    private UUID mId;//用于识别LostItem对象的Id,在构造方法中已经初始化
    private int articalImageId;
    private BmobFile articalImageFile;
    private String articalTitleText;
    private String articalContextText;
    private List<CommentsArticals> articalComments;
    private User linkUser;

    public Artical(){
        mId = UUID.randomUUID();
    }

    public UUID getmId() {
        return mId;
    }

    public void setmId(UUID mId) {
        this.mId = mId;
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
}
