package com.example.joker.signinsystem.baseclasses;

import android.graphics.Bitmap;

import java.io.File;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

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
    private BmobFile ImageFile; // 用户头像网址
    private Bitmap headIcon;
    private int mMondatTime =0;    // 周一在线时间
    private int mTuesdayTime =0;   // 周二在线时间
    private int mWednesdayTime =0; // 周三在线时间
    private int mThursdayTime =0;  // 周四在线时间
    private int mFridayTime =0;    // 周五在线时间
    private int mSaturdayTime =0;  // 周六在线时间
    private int mSundayTime =0;    // 周日在线时间
    private int mTotalTime =0;     // 总在线时间
    private int mYesturdayFlag=0;  // 昨天 判断是否更新 如果不一样就把今天变为0 同时把 flag 换成今天 -- 如果今天不是昨天 就把昨天赋值为今天

    private void summaryTotal(){
        mTotalTime = mMondatTime + mTuesdayTime +
                mWednesdayTime + mThursdayTime + mFridayTime + mSaturdayTime + mSundayTime ;
    }

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

    public BmobFile getImageFile() {
        return ImageFile;
    }

    public void setImageFile(BmobFile imageFile) {
        ImageFile = imageFile;
    }

    public String getMotto() {
        return motto;
    }

    public void setMotto(String motto) {
        this.motto = motto;
    }

    public int getmMondatTime() {
        return mMondatTime;
    }

    public void setmMondatTime(int mMondatTime) {
        this.mMondatTime = mMondatTime;
        summaryTotal();
    }

    public int getmTuesdayTime() {
        return mTuesdayTime;
    }

    public void setmTuesdayTime(int mTuesdayTime) {
        this.mTuesdayTime = mTuesdayTime;
        summaryTotal();
    }

    public int getmWednesdayTime() {
        return mWednesdayTime;
    }

    public void setmWednesdayTime(int mWednesdayTime) {
        this.mWednesdayTime = mWednesdayTime;
        summaryTotal();
    }

    public int getmThursdayTime() {
        return mThursdayTime;
    }

    public void setmThursdayTime(int mThursdayTime) {
        this.mThursdayTime = mThursdayTime;
        summaryTotal();
    }

    public int getmFridayTime() {
        return mFridayTime;
    }

    public void setmFridayTime(int mFridayTime) {
        this.mFridayTime = mFridayTime;
        summaryTotal();
    }

    public int getmSaturdayTime() {
        return mSaturdayTime;
    }

    public void setmSaturdayTime(int mSaturdayTime) {
        this.mSaturdayTime = mSaturdayTime;
        summaryTotal();
    }

    public int getmSundayTime() {
        return mSundayTime;
    }

    public void setmSundayTime(int mSundayTime) {
        this.mSundayTime = mSundayTime;
        summaryTotal();
    }

    public int getmTotalTime() {
        return mTotalTime;
    }

    public void setmTotalTime(int mTotalTime) {
        this.mTotalTime = mTotalTime;
    }

    public int getmYesturdayFlag() {
        return mYesturdayFlag;
    }

    public void setmYesturdayFlag(int mYesturdayFlag) {
        this.mYesturdayFlag = mYesturdayFlag;
    }

    public Bitmap getHeadIcon() {
        return headIcon;
    }

    public void setHeadIcon(Bitmap headIcon) {
        this.headIcon = headIcon;
    }
}
