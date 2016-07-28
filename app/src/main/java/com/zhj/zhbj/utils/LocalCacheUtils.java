package com.zhj.zhbj.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by HongJay on 2016/7/28.
 */
public class LocalCacheUtils {
    private static final String LOCAL_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/zhbj_cache";

    public Bitmap getBitmapFromLocal(String url) {
        try {
            String fileName = MD5Encoder.encode(url);  //用md5给文件命名。
            File file = new File(LOCAL_PATH, fileName);
            if(file.exists()){
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2;// 表示压缩比例，2表示宽高都压缩为原来的二分之一， 面积为四分之一
                options.inPreferredConfig = Bitmap.Config.RGB_565;// 设置bitmap的格式，565可以降低内存占用

                Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file), null, options);
                return bitmap;
            }else{
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setBitmapToLocal(String url, Bitmap bitmap) {
        try {
            String fileName = MD5Encoder.encode(url);  //用md5给文件命名。
            File file = new File(LOCAL_PATH, fileName);
            File parent = file.getParentFile();
            if (!parent.exists()) {
                parent.mkdirs();
            }
            //把图片持久化到本地
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100,
                    new FileOutputStream(file));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
