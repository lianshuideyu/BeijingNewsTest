package com.atguigu.androidandh5;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class CallPhoneActivity2 extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_phone2);
        webView = (WebView)findViewById(R.id.webView);

        WebSettings settings = webView.getSettings();

        settings.setJavaScriptEnabled(true);

        settings.setUseWideViewPort(true);

        webView.setWebViewClient(new WebViewClient());

        webView.addJavascriptInterface(new AndroidAndJs(),"Android");


        webView.loadUrl("file:///android_asset/JsCallJavaCallPhone.html");
    }


    private class AndroidAndJs {

        //show(jsondata)
        @JavascriptInterface
        public void showcontacts(){
            String json = "[{\"name\":\"啊\",\"phone\":\"12312\"}]";
            String i = "[{\"name\":\"名字\",\"phone\":\"23424\"}]";
            //调用js中的方法
            webView.loadUrl("javascript:show(" + "'" + i + "'" + ")");
        }
        //call(\""+ jsonobjs[y].phone
        @JavascriptInterface
        public void call(String phone){
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
            startActivity(intent);

        }
    }
}
