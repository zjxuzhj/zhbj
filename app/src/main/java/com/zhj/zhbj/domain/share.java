package com.zhj.zhbj.domain;

import cn.bmob.v3.BmobObject;

/**
 * Created by HongJay on 2017/4/19.
 */

public class share extends BmobObject {
    private User uid;
    private String time;

    public news getNid() {
        return nid;
    }

    public void setNid(news nid) {
        this.nid = nid;
    }

    public User getUid() {
        return uid;
    }

    public void setUid(User uid) {
        this.uid = uid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    private news nid;
}
