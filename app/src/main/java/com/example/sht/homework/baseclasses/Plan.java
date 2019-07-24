package com.example.sht.homework.baseclasses;

import java.util.Date;

import cn.bmob.v3.BmobObject;

public class Plan extends BmobObject {

    private String course;

    private Date date;

    private String Uri;

    private User user;

    public Plan(){

    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getUri() {
        return Uri;
    }

    public void setUri(String uri) {
        Uri = uri;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
