package com.example.gf.seventhdemo.JavaBean;

import org.litepal.crud.LitePalSupport;

public class Linkman extends LitePalSupport {

    private String name;    //联系人姓名
    private String phoneNumber;     //联系人电话号码
    int id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getId() {
        return id;
    }
}
