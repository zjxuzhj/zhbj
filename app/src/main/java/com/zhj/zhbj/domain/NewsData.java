package com.zhj.zhbj.domain;

import java.util.ArrayList;

/**
 * Created by HongJay on 2016/7/16.
 */
public class NewsData {
    public int retcode;

    @Override
    public String toString() {
        return "NewsData{" +
                "data=" + data +
                '}';
    }

    public ArrayList<NewsMenuData> data;

    public class NewsMenuData {
        public ArrayList<NewsTabData> children;
        public String id;
        public String title;

        @Override
        public String toString() {
            return "NewsMenuData{" +
                    "children=" + children +
                    ", title='" + title + '\'' +
                    '}';
        }

        public int type;
        public String url;
    }

    public class NewsTabData {
        public String id;
        public String title;

        @Override
        public String toString() {
            return "NewsTabData{" +
                    "title='" + title + '\'' +
                    '}';
        }

        public int type;
        public String url;
    }
}
