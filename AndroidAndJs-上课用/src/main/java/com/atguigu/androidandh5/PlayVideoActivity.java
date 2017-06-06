package com.atguigu.androidandh5;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class PlayVideoActivity extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);

        webView = (WebView)findViewById(R.id.webView);

        WebSettings settings = webView.getSettings();

        settings.setJavaScriptEnabled(true);
        //避免调系统的浏览器
        webView.setWebViewClient(new WebViewClient());

        webView.addJavascriptInterface(new AndroidAndJs(),"android");

        //javascript:android.playVideo(itemid, videourl, itemtitle);
        webView.loadUrl("file:///android_asset/RealNetJSCallJavaActivity.htm");
    }

    class AndroidAndJs {
        //js调用java 的方法，打开播放器播放视频
        public void playVideo(int itemid,String videourl,String itemtitle){
            Intent intent = new Intent();
            intent.setDataAndType(Uri.parse(videourl),"video/*");
            startActivity(intent);
        }
    }
}
