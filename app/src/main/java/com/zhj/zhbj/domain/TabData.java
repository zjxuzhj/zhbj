package com.zhj.zhbj.domain;

import java.util.ArrayList;

/**
 * Created by HongJay on 2016/7/20.
 */
public class TabData {
    public  int retcode;
    public TabDetailData data;

    @Override
    public String toString() {
        return "TabData{" +
                "data=" + data +
                '}';
    }

    public class TabDetailData{
    public String title;
    public String more;
    public ArrayList<TabNewsData> news;
    public ArrayList<TopNewsData> topnews;

       @Override
       public String toString() {
           return "TabDetailData{" +
                   "title='" + title + '\'' +
                   ", news=" + news +
                   ", topnews=" + topnews +
                   '}';
       }

       /**
     * 新闻列表对象
     */
    public  class TabNewsData{
        public  String title;
        public  String url;
        public  String id;
        public  String listimage;
        public  String pubdate;
        public  String type;

        @Override
        public String toString() {
            return "TabNewsData{" +
                    "title='" + title + '\'' +
                    '}';
        }
    }

    /**
     * 头条新闻
     */
    public  class TopNewsData{
        public  String title;
        public  String url;
        public  String id;
        public  String topimage;
        public  String pubdate;
        public  String type;

        @Override
        public String toString() {
            return "TopNewsData{" +
                    "title='" + title + '\'' +
                    '}';
        }
    }
}
}
