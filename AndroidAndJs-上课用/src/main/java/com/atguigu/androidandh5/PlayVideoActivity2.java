package com.atguigu.androidandh5;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class PlayVideoActivity2 extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video2);

        webView = (WebView)findViewById(R.id.webView);

        WebSettings settings = webView.getSettings();

        settings.setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient());

        //android.playVideo(itemid, videourl, itemtitle);
        webView.addJavascriptInterface(new AndroidAndJs(),"android");

        webView.loadUrl("file:///android_asset/RealNetJSCallJavaActivity.htm");
    }


    private class AndroidAndJs {

        @JavascriptInterface
        public void playVideo(int itemid,String videourl,String itemtitle){
            Intent intent = new Intent();
            intent.setDataAndType(Uri.parse(videourl),"video/*");
            startActivity(intent);
        }

    }
}
