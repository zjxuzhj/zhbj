package com.zhj.zhbj.utils;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.zhj.zhbj.R;

/**
 * Created by HongJay on 2016/7/27.
 */
public class MyBitmapUtils {
    NetCacheUtils netCacheUtils;
    LocalCacheUtils localCacheUtils;
    MemoryCacheUtils memoryCacheUtils;
    public  MyBitmapUtils(){
        localCacheUtils=new LocalCacheUtils();
        memoryCacheUtils = new MemoryCacheUtils();
        netCacheUtils=new NetCacheUtils(localCacheUtils,memoryCacheUtils);

    }
    public void display(ImageView ivPic,String url){
        ivPic.setImageResource(R.drawable.news_pic_default);
        //内存缓存
        Bitmap bitmap = memoryCacheUtils.getBitmapFromMemory(url);
        if(bitmap!=null){
            ivPic.setImageBitmap(bitmap);
            System.out.println("从内存缓存读取图片...");
            return;
        }

        //本地缓存

        bitmap = localCacheUtils.getBitmapFromLocal(url);
        if(bitmap!=null){
            ivPic.setImageBitmap(bitmap);
            memoryCacheUtils.setBitmapToMemory(url,bitmap);
            System.out.println("从本地缓存读取图片...");
            return;
        }
        //从网络读取
        netCacheUtils.getBitmapFromNet(ivPic,url);
    }
}
