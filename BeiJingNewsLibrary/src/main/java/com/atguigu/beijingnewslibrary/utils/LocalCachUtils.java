package com.atguigu.beijingnewslibrary.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by Administrator on 2017/6/7.
 */

/**
 * 本地缓存工具类
 */
public class LocalCachUtils {

    private final MemoryCachUtils memoryCachUtils;

    public LocalCachUtils(MemoryCachUtils memoryCachUtils) {
        this.memoryCachUtils = memoryCachUtils;
    }

    public Bitmap getBitmap(String imageUrl) {
        try {
            //sdcard/beijingnews/ljsk;l;;llkkljhjjsk
            String dir = Environment.getExternalStorageDirectory() + "/beijingnews/";
            //文件名称
            String fileName = MD5Encoder.encode(imageUrl);
            //sdcard/beijingnews/ljsk;l;;llkkljhjjsk
            File file = new File(dir, fileName);

            if(file.exists()) {

                Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
                if(bitmap != null) {
                    memoryCachUtils.putBitmap2Memory(imageUrl,bitmap);
                }
                
                return bitmap;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }

    /**
     * 保存图片到本地
     *
     * @param imageUrl
     * @param bitmap
     */
    public void putBitmap2Local(String imageUrl, Bitmap bitmap) {
        try {
            //sdcard/beijingnews/ljsk;l;;llkkljhjjsk
            String dir = Environment.getExternalStorageDirectory() + "/beijingnews/";
            //文件名称
            String fileName = MD5Encoder.encode(imageUrl);
            //sdcard/beijingnews/ljsk;l;;llkkljhjjsk
            File file = new File(dir, fileName);
            //得到 //sdcard/beijingnews/
            File parentFile = file.getParentFile();
            if (!parentFile.exists()) {
                //创建多级目录
                parentFile.mkdirs();
            }

            //创建文件
            if (!file.exists()) {
                file.createNewFile();
            }

            Log.e("TAG","file=="+file.getAbsolutePath());
            //保存图片
            FileOutputStream fos = new FileOutputStream(file);
            //写入数据
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);

            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
