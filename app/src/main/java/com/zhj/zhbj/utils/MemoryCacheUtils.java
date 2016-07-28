package com.zhj.zhbj.utils;

import android.graphics.Bitmap;
import android.util.LruCache;

import java.util.HashMap;

/**
 * Created by HongJay on 2016/7/28.
 */
public class MemoryCacheUtils {
    private LruCache<String,Bitmap> mMemoryCache;
    public  MemoryCacheUtils(){
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        mMemoryCache=new LruCache<String,Bitmap>(maxMemory/8){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                int size = value.getRowBytes() * value.getHeight();// 返回bitmap占用的内存大小
                System.out.println("sizeof:" + size);
                return size;
            }
        };
    }
    public Bitmap getBitmapFromMemory(String url){
        return mMemoryCache.get(url);
    }
    public void setBitmapToMemory(String url,Bitmap bitmap){
        mMemoryCache.put(url, bitmap);
    }
}
