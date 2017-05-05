package com.evil.webbrowser.ui;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;

import com.evil.webbrowser.R;
import com.evil.webbrowser.adapter.MyDownloadListenter;
import com.evil.webbrowser.adapter.MyWebViewClient;
import com.evil.webbrowser.utils.AppUtils;
import com.evil.webbrowser.utils.SpUtils;
import com.evil.webbrowser.utils.StringUtils;
import com.fxc.base.AppActivity;

import static com.evil.webbrowser.R.id.iv_flush;

public class MainActivity
        extends AppActivity
{

    private WebView     mWebView;
    private ImageView   mIvBack;
    private ImageView   mIvGo;
    private ImageView   mIvHome;
    private ImageView   mIvSearch;
    private ImageView   mIvSetting;
    private WebSettings mWebSettings;
    private String[]    mPerms;
    private String      url;
    private ImageView   mIvOther;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mWebView = (WebView) findViewById(R.id.web_view);
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mIvGo = (ImageView) findViewById(R.id.iv_go);
        mIvHome = (ImageView) findViewById(R.id.iv_home);
        mIvSearch = (ImageView) findViewById(iv_flush);
        mIvSetting = (ImageView) findViewById(R.id.iv_setting);
        mPerms = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                              Manifest.permission.WRITE_EXTERNAL_STORAGE};
        initEvent();
        initData();
        mIvOther = (ImageView) findViewById(R.id.iv_other);
        mIvOther.setOnClickListener(this);
    }

    private void initEvent() {
        setOnClick(mIvBack, mIvGo, mIvHome, mIvSearch, mIvSetting);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.iv_back:
                //其中webView.canGoBack()在webView含有一个可后退的浏览记录时返回true
                if (mWebView.canGoBack()) {
                    mWebView.goBack();
                }
                break;
            case R.id.iv_go:
                if (mWebView.canGoForward()) {
                    mWebView.goForward();
                }
                break;
            case R.id.iv_home:
                mWebView.loadUrl("http://www.baidu.com");
                break;
            case R.id.iv_flush:
                refresh();
                break;
            case R.id.iv_setting:
                boolean js = SpUtils.getInfo(this, "js", true);
                SpUtils.save(this, "js", !js);
                //支持js
                mWebSettings.setJavaScriptEnabled(!js);
                //支持通过JS打开新窗口
                mWebSettings.setJavaScriptCanOpenWindowsAutomatically(!js);
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        requestPermissions(0x0001, mPerms);
    }

    private void initData() {
        url = getIntent().getStringExtra("url");
        //1.首先在WebView初始化时添加如下代码
        //        if (Build.VERSION.SDK_INT >= 19) {
        /*对系统API在19以上的版本作了兼容。因为4.4以上系统在onPageFinished时再恢复图片加载时,如果存在多张图片引用的是相同的src时，会只有一个image标签得到加载，因而对于这样的系统我们就先直接加载。*/
        mWebView.getSettings()
                .setLoadsImagesAutomatically(true);
        //        } else {
        //            mWebView.getSettings().setLoadsImagesAutomatically(false);
        //        }

        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.setDownloadListener(new MyDownloadListenter(this));
        mWebSettings = mWebView.getSettings();

        initSetting();

        if (StringUtils.isEmpty(url)) {
            //加载地址
            mWebView.loadUrl("http://www.baidu.com");
        } else {
            if (!url.startsWith("http")) {
                url = "http://" + url;
            }
            mWebView.loadUrl(url);
        }
    }

    /**
     * 初始化设置
     */
    private void initSetting() {
        setJs();
        setEncoding();
        //将图片调整到适合webview的大小
        setImageWide();
        setZoom();
        setWindowZoom();
        setImageLoad();
        //支持内容重新布局
        mWebSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        //设置可以访问文件
        mWebSettings.setAllowFileAccess(true);
        //当webview调用requestFocus时为webview设置节点
        mWebSettings.setNeedInitialFocus(true);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //其中webView.canGoBack()在webView含有一个可后退的浏览记录时返回true
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void requestPremissionsDefeat(int requestCode, @Nullable String[] permissions) {
        AppUtils.openSettingActivity(this);
    }

    /**
     * 刷新
     */
    public void refresh() {
        mWebView.reload();
    }

    /**
     * 设置js
     */
    public void setJs() {
        boolean js = SpUtils.getInfo(this, "js", true);
        //支持js
        mWebSettings.setJavaScriptEnabled(js);
        //支持通过JS打开新窗口
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(js);
    }

    /**
     * 设置编码
     */
    public void setEncoding() {
        String encoding = SpUtils.getInfo(this, "encoding", "utf-8");
        //设置默认编码
        mWebSettings.setDefaultTextEncodingName(encoding);
    }

    /**
     * 设置缩放
     */
    public void setZoom() {
        boolean zoom = SpUtils.getInfo(this, "zoom", true);
        //支持缩放
        mWebSettings.setSupportZoom(zoom);
        //设置支持缩放
        mWebSettings.setBuiltInZoomControls(zoom);
    }

    /**
     * 设置屏幕缩放
     */
    public void setWindowZoom() {
        boolean winzoom = SpUtils.getInfo(this, "winzoom", true);
        //缩放至屏幕的大小
        mWebSettings.setLoadWithOverviewMode(winzoom);
    }

    /**
     * 设置图片调整到适合webview的大小
     */
    public void setImageWide() {
        boolean imagewide = SpUtils.getInfo(this, "imagewide", false);
        //将图片调整到适合webview的大小
        mWebSettings.setUseWideViewPort(imagewide);
    }

    /**
     * 支持自动加载图片
     */
    public void setImageLoad() {
        boolean imageload = SpUtils.getInfo(this, "imageload", true);
        //支持自动加载图片
        mWebSettings.setLoadsImagesAutomatically(imageload);
    }
}
