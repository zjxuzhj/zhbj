package com.zhj.zhbj.domain;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by HongJay on 2017/4/3.
 */

public class news extends BmobObject {
    private Integer type;//新闻类型
    private BmobFile html;//新闻html

    public news(String title, BmobFile img, Integer id, String pubdate) {
        this.title = title;
        this.img = img;
        this.id = id;
        this.pubdate = pubdate;
    }

    public news(String tableName, String title, BmobFile img, Integer id, String pubdate) {
        super(tableName);
        this.title = title;
        this.img = img;
        this.id = id;
        this.pubdate = pubdate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String title;//新闻标题
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public BmobFile getHtml() {
        return html;
    }

    public void setHtml(BmobFile html) {
        this.html = html;
    }

    public BmobFile getImg() {
        return img;
    }

    public void setImg(BmobFile img) {
        this.img = img;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPubdate() {
        return pubdate;
    }

    public void setPubdate(String pubdate) {
        this.pubdate = pubdate;
    }

    private BmobFile img;//新闻图片
    private Integer id;
    private String 	pubdate;

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    private Integer score;

    public Integer getTabType() {
        return tabType;
    }

    public void setTabType(Integer tabType) {
        this.tabType = tabType;
    }

    private Integer tabType;

    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
