package com.evil.webbrowser.adapter;

import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 *  @项目名： WebBrowser
 *  @包名： com.evil.webbrowser.adapter
 *  @创建者: Noah.冯
 *  @时间: 12:27
 *  @描述： TODO
 */
public class MyWebViewClient extends WebViewClient {
    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        handler.proceed();  // 接受信任所有网站的证书
        // handler.cancel();   // 默认操作 不处理
        // handler.handleMessage(null);  // 可做其他处理
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        if (!view.getSettings().getLoadsImagesAutomatically()) {
            view.getSettings().setLoadsImagesAutomatically(true);
        }
    }
}
