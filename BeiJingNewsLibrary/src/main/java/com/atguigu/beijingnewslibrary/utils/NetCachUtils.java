package com.atguigu.beijingnewslibrary.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2017/6/7.
 */

public class NetCachUtils {


    private final Handler handler;
    /**
     * 请求图片成功
     */
    public static final int SUCESS = 1;
    /*
    请求图片失败
     */
    public static final int FAIL = 2;
    private final LocalCachUtils localCachUtils;

    /**
     * 线程池类
     */
    private ExecutorService executorService;

    public NetCachUtils(Handler handler, LocalCachUtils localCachUtils) {
        this.handler = handler;
        this.localCachUtils = localCachUtils;
        executorService = Executors.newFixedThreadPool(10);
    }

    public void getBitmapFromNet(final String imageUrl, final int position) {

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                //使用原生的方法请求图片
                try {
                    HttpURLConnection urlConnection = (HttpURLConnection) new URL(imageUrl).openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setReadTimeout(5000);
                    urlConnection.setConnectTimeout(5000);
                    //得到请求码
                    int code = urlConnection.getResponseCode();
                    if(code == 200) {
                        //请求图片成功
                        InputStream is = urlConnection.getInputStream();
                        //把流转换成Bitmap
                        Bitmap bitmap = BitmapFactory.decodeStream(is);

                        //然后要发送给主线程
                        Message msg = Message.obtain();
                        msg.what =  SUCESS;
                        msg.obj = bitmap;
                        msg.arg1 = position;
                        handler.sendMessage(msg);

                        //在内存中保存一份
                        //在本地中保存一份
                        localCachUtils.putBitmap2Local(imageUrl,bitmap);
                    }

                } catch (IOException e) {
                    e.printStackTrace();

                    Message msg = Message.obtain();
                    msg.what = FAIL;//成功信息
                    msg.arg1 = position;
                    handler.sendMessage(msg);
                }
            }
        });

        //开启子线程请求网络
        /*new Thread(){
            public void run(){


            }
        }.start();*/
    }
}
