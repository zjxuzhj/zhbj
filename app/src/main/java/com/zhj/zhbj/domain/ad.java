package com.zhj.zhbj.domain;

import cn.bmob.v3.BmobObject;

/**
 * Created by HongJay on 2017/4/14.
 */

public class ad extends BmobObject {
    private Integer weight;

    public product getPid() {
        return pid;
    }

    public void setPid(product pid) {
        this.pid = pid;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    private product pid;
}
