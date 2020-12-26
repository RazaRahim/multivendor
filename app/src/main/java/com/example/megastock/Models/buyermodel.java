package com.example.megastock.Models;

public class buyermodel {
    String name, shpename,  password, picUrl,phone;

    public buyermodel() {

    }


    public buyermodel(String name, String shpename, String phone, String password, String picUrl)
    {
        this.name = name;
        this.shpename = shpename;
        this.password = password;
        this.picUrl = picUrl;
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }





    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShpename() {
        return shpename;
    }

    public void setShpename(String shpename) {
        this.shpename = shpename;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }



    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
}
