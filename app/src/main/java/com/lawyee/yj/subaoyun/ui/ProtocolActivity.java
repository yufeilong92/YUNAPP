package com.lawyee.yj.subaoyun.ui;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.lawyee.yj.subaoyun.R;
/**
 * @Author : YFL  is Creating a porject in YFPHILPS
 * @Email : yufeilong92@163.com
 * @Time :2016/12/19 16:25:16:25
 * @Purpose :webview调用
 */



public class ProtocolActivity extends BaseActivity {

    private WebView mMyWebView;


    @Override
    protected void initContenView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_protocol);
        initView();
    }

    private void initView() {
        mMyWebView = (WebView) findViewById(R.id.MyWebView);
        mMyWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        WebSettings settings = mMyWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setBuiltInZoomControls(true);
        settings.setSupportZoom(true);
        settings.setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
        mMyWebView.loadUrl("file:///android_asset/rules.html");

    }
}
