package com.atguigu.beijingnewslibrary.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

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

    public NetCachUtils(Handler handler) {
        this.handler = handler;
    }

    public void getBitmapFromNet(final String imageUrl, final int position) {

        //开启子线程请求网络
        new Thread(){
            public void run(){
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
                    }


                } catch (IOException e) {
                    e.printStackTrace();

                    Message msg = Message.obtain();
                    msg.what = FAIL;//成功信息
                    msg.arg1 = position;
                    handler.sendMessage(msg);
                }

            }
        }.start();
    }
}
