package com.example.joker.signinsystem.baseclasses;

import java.util.List;
import java.util.UUID;

import cn.bmob.v3.BmobObject;

public class Artical extends BmobObject {

    private UUID mId;//用于识别LostItem对象的Id,在构造方法中已经初始化
    private int articalImageId;
    private String articalText;
    private List<CommentsArticals> articalComments;

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

    public String getArticalText() {
        return articalText;
    }

    public void setArticalText(String articalText) {
        this.articalText = articalText;
    }

    public List<CommentsArticals> getArticalComments() {
        return articalComments;
    }

    public void setArticalComments(List<CommentsArticals> articalComments) {
        this.articalComments = articalComments;
    }
}
