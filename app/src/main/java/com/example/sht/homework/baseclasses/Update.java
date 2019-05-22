package com.example.sht.homework.baseclasses;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class Update extends BmobObject {

    private BmobFile APK;
    private String text,Code;

    public BmobFile getAPK() {
        return APK;
    }

    public void setAPK(BmobFile APK) {
        this.APK = APK;
    }


    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getapkUrl(){
        return APK.getFileUrl();
    }

}
