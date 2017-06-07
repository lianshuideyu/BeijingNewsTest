package com.atguigu.beijingnewslibrary.utils;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Created by Administrator on 2017/6/7.
 */

public class MemoryCachUtils {

    private LruCache<String,Bitmap> lruCache;

    public MemoryCachUtils() {
        //这个是得到系统分配给当前应用的8分之一空间，用于存储图片
        int maxSize = (int) (Runtime.getRuntime().maxMemory() / 8);
        lruCache = new LruCache<String,Bitmap>(maxSize){
            /**
             * 计算每张图片的大小
             * @param key
             * @param value
             * @return
             */
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight();
            }
        };

        //计算每张图片的大小


    }

    /**
     * 根据图片url获取内存的图片
     * @param imageUrl
     * @return
     */
    public Bitmap getBitmapFromMemory(String imageUrl) {
        return lruCache.get(imageUrl);
    }

    /**
     * 根据url存储图片到内存中
     * @param imageUrl
     * @param bitmap
     */
    public void putBitmap2Memory(String imageUrl, Bitmap bitmap) {
        lruCache.put(imageUrl,bitmap);
    }
}
