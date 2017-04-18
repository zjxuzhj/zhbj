package com.zhj.zhbj.domain;

import cn.bmob.v3.BmobObject;

/**
 * Created by HongJay on 2017/4/17.
 */

public class comment extends BmobObject {
    private User uid;
    private String time;
    private news nid;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public news getNid() {
        return nid;
    }

    public void setNid(news nid) {
        this.nid = nid;
    }

    private String message;
}
