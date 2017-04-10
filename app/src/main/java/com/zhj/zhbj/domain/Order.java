package com.zhj.zhbj.domain;

import java.util.Date;

import cn.bmob.v3.BmobObject;

/**
 * Created by HongJay on 2017/4/10.
 */

public class Order extends BmobObject {
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getSoldDate() {
        return soldDate;
    }

    public void setSoldDate(String soldDate) {
        this.soldDate = soldDate;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    private String uid;
    private String soldDate;
    private String pid;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    private String state;
}
