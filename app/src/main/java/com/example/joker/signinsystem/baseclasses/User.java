package com.example.joker.signinsystem.baseclasses;

import android.graphics.Bitmap;

import java.io.File;

import cn.bmob.v3.BmobUser;

//
public class User extends BmobUser {

    /*
    数据成员
     */
    private String name;     // 用户名
    private String address;  // 密码
    private String fullname; // 姓名
    private String group;    // 组别
    private String motto;    // 座右铭
    private String telephone;// 电话
    private Bitmap headIcon; // 用户头像
    private File ImageUrl;   // 用户头像网址

    /*
    get() & set() 方法
     */
    public String getTelephone() {
        return telephone;
    }
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getGroup() {
        return group;
    }
    public void setGroup(String group) {
        this.group = group;
    }

    public String getFullname() {
        return fullname;
    }
    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public Bitmap getHeadIcon() {
        return headIcon;
    }

    public void setHeadIcon(Bitmap headIcon) {
        this.headIcon = headIcon;
    }

    public File getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(File imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getMotto() {
        return motto;
    }

    public void setMotto(String motto) {
        this.motto = motto;
    }
}
