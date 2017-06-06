package com.atguigu.androidandh5;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class CallPhoneActivity extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_phone);

        webView = (WebView)findViewById(R.id.webView);

        WebSettings settings = webView.getSettings();

        settings.setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient());

        webView.addJavascriptInterface(new AndroidAndJs(),"Android");

        webView.loadUrl("file:///android_asset/JsCallJavaCallPhone.html");
    }


    class AndroidAndJs {
        //该方法被js调用，显示数据
        @JavascriptInterface
        public void showcontacts(){
//            String json = "[{\"name\":\"阿福\", \"phone\":\"18600012345\"}]";
            String json = "[{\"name\":\"啊\",\"phone\":\"12312\"}]";
            //show(jsondata)
            webView.loadUrl("javascript:show(" + "'" + json + "'" + ")");
        }

        //javascript:Android.call(\""+ jsonobjs[y].phone
        @JavascriptInterface
        public void call(String phone){
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
            startActivity(intent);

        }

    }
}
