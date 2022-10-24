package com.ecare.smartmeal.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.StringUtils;
import com.ecare.smartmeal.R;
import com.ecare.smartmeal.base.SimpleActivity;
import com.ecare.smartmeal.config.Constants;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * NeighborhoodLongevity
 * <p>
 * Created by xuminmin on 12/15/21.
 * Email: iminminxu@gmail.com
 */
public class WebPageActivity extends SimpleActivity {

    //标题
    @BindView(R.id.tv_title)
    TextView tvTitle;
    //加载进度
    @BindView(R.id.pb_progress)
    ProgressBar pbProgress;
    //WebView
    @BindView(R.id.wv_web)
    WebView mWebView;
    //传递的参数
    private String mTitle;
    private String mUrl;

    @Override
    protected int getLayoutId() {
        return R.layout.act_web_page;
    }

    @Override
    protected void initVariables() {
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        mTitle = intent.getStringExtra(Constants.IT_TITLE);
        mUrl = intent.getStringExtra(Constants.IT_URL);
    }

    @Override
    protected void initViews(@Nullable Bundle savedInstanceState) {
        //设置标题
        tvTitle.setText(mTitle);
        //初始化WebView
        initWebView();
    }

    /**
     * 初始化WebView
     */
    private void initWebView() {
        //WebSettings
        WebSettings webSettings = mWebView.getSettings();
        //设置WebView是否允许执行JavaScript脚本，默认false，不允许
        webSettings.setJavaScriptEnabled(true);
        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合WebView的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        // 设置脚本是否允许自动打开弹窗，默认false，不允许
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        //设置是否开启DOM存储API权限，默认false，未开启，设置为true，WebView能够使用DOM storage API
        webSettings.setDomStorageEnabled(true);
        //设置页面缓存模式
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        //设置WebView底层的布局算法
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        //设置允许混合模式
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        //设置JSBridge
        // mWebView.addJavascriptInterface(new JSBridge(this), "yinqingli");
        // 处理各种通知 & 请求事件
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("http") || url.startsWith("https")) {
                    return false;
                } else {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                        return true;
                    } catch (Exception e) {
                        return false;
                    }
                }
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                pbProgress.setVisibility(newProgress == 100 ? View.GONE : View.VISIBLE);
                pbProgress.setProgress(newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (StringUtils.isEmpty(mTitle)) {
                    tvTitle.setText(title);
                }
            }
        });
    }

    /**
     * 点击事件
     *
     * @param view view
     */
    @OnClick({R.id.dtv_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dtv_back:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    protected void doBusiness() {
        //加载网页
        mWebView.loadUrl(mUrl);
    }

    /**
     * 返回键处理
     */
    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 避免WebView内存泄露
     */
    @Override
    public void onDestroy() {
        if (mWebView != null) {
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebView.clearHistory();
            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy();
    }
}
