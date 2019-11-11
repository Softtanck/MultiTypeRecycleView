package com.cheweishi.android.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.ProgressBar;

import com.cheweishi.android.utils.LogHelper;

/**
 * WebView
 * <p>
 * eg:
 * <p>
 * WebViewView webViewUtil = new WebViewView();
 * webViewUtil.setWebView(mWebView); webViewUtil.setContext(mContext);
 * webViewUtil.setJS(true); webViewUtil.setSelfAdaption();
 * webViewUtil.setNoCache();
 * webViewUtil.openUrl(getIntent().getExtras().getString("url"));
 * webViewUtil.alarm404();
 *
 * @author WG
 */

public class WebViewView {
    private WebView mWebView;
    private Context mContext;

    private static final int DEFAULT_PROGRESS = 30;

    /**
     * 设置webview
     */

    public void setWebView(WebView webView) {
        mWebView = webView;
    }

    /**
     * 打开URL
     *
     * @param url
     */
    public void openUrl(String url) {
        mWebView.loadUrl(url);
    }

    /**
     * 网页提醒
     */
    public void alarm404() {
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {

                super.onReceivedError(view, errorCode, description, failingUrl);
                LogHelper.d("web onReceivedError");
                String data = "Page NO FOUND！";
                view.loadUrl("javascript:document.body.innerHTML=\"" + data + "\"");
            }
        });
    }

    /**
     * 显示进度
     */

    public void showShowProgress(Activity activity) {
        activity.getWindow().requestFeature(Window.FEATURE_PROGRESS);
    }

    /**
     * 设置进度
     */

    public void setProgress(final ProgressBar progress) {
        mWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    progress.setVisibility(View.INVISIBLE);
                } else {
                    if (View.INVISIBLE == progress.getVisibility()) {
                        progress.setVisibility(View.VISIBLE);
                    }

                    if (newProgress > DEFAULT_PROGRESS)
                        progress.setProgress(newProgress);
//                    else
//                        progress.setProgress(newProgress);
                }
//                ((Activity) mContext).setProgress(newProgress);
                super.onProgressChanged(view, newProgress);
            }

        });
    }

    /**
     * 启动JAVASCRIPT
     *
     * @param flag
     */
    @SuppressLint("SetJavaScriptEnabled")
    public void setJS(boolean flag) {
        mWebView.getSettings().setJavaScriptEnabled(flag);
    }

    /**
     * 禁止缓存
     */
    @SuppressLint("SetJavaScriptEnabled")
    public void setNoCache() {
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
    }

    /**
     * 加载进来的页面自适应手机屏幕
     */
    @SuppressLint("SetJavaScriptEnabled")
    public void setSelfAdaption() {
        mWebView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setUseWideViewPort(true);
//        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setBuiltInZoomControls(false);
        mWebView.setInitialScale(25);
    }

    public void setContext(Context mContext) {
        this.mContext = mContext;
    }

}
