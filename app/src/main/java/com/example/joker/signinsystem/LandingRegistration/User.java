package com.example.joker.signinsystem.LandingRegistration;

import cn.bmob.v3.BmobUser;

//
public class User extends BmobUser {

        private String name;//用户名
        private String address;//密码
        private String fullname;//姓名
        private String group;//组别
        private String telephone;//电话

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



}
