package com.example.joker.signinsystem.baseclasses;

import java.util.UUID;

public class CommentsArticals {

    private UUID mCommentId;
    private int commentImageId;
    private String commentText;

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

}
