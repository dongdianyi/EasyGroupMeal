package gzkj.easygroupmeal.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import gzkj.easygroupmeal.R;
import gzkj.easygroupmeal.base.BaseActivity;

public class WebActivity extends BaseActivity {

    @BindView(R.id.webview)
    WebView webview;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.progressbar)
    ProgressBar progressBar;
    private String url;

    @Override
    public int intiLayout() {
        return R.layout.activity_web;
    }

    @SuppressLint("JavascriptInterface")
    @Override
    public void initData() {
        hideTopUIMenu();
        url = getIntent().getStringExtra("url");
        title.setText(getIntent().getStringExtra("title"));
        setWeb(url);
    }

    private void setWeb(String url) {
        webview.setHorizontalScrollBarEnabled(false);//水平不显示
        webview.setVerticalScrollBarEnabled(false); //垂直不显示
        webview.addJavascriptInterface(this, "android");//添加js监听 这样html就能调用客户端
        webview.setWebChromeClient(webChromeClient);
        webview.setWebViewClient(webViewClient);
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);//允许使用js
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);//不使用缓存，只从网络获取数据.
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        //不显示webview缩放按钮
        webSettings.setDisplayZoomControls(false);
        webSettings.setLoadWithOverviewMode(true);

        DisplayMetrics metrics = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int mDensity = metrics.densityDpi;
        logger("densityDpi = " + mDensity);

        if (mDensity == 240) {

            webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);

        } else if (mDensity == 160) {

            webSettings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);

        } else if(mDensity == 120) {

            webSettings.setDefaultZoom(WebSettings.ZoomDensity.CLOSE);

        }else if(mDensity == DisplayMetrics.DENSITY_XHIGH){

            webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);

        }else if (mDensity == DisplayMetrics.DENSITY_TV){

            webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);

        }else{

            webSettings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);

        }
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setUseWideViewPort(true);
        webview.loadUrl(url);

    }

    @Override
    public void setListener() {

    }

    @OnClick(R.id.back)
    public void onViewClicked() {
        if (webview.canGoBack()) {
            // /点击返回按钮的时候判断有没有上一页
            webview.goBack(); // goBack()表示返回webView的上一页面
        } else {
            finish();
        }
    }

    private WebViewClient webViewClient = new WebViewClient() {
        @Override
        public void onPageFinished(WebView view, String url) {
            //页面加载完成

            //提取网页标题显示
//            String titleWeb = view.getTitle();
//            if (!TextUtils.isEmpty(titleWeb)) {
//                title.setText(titleWeb);
//            }
            progressBar.setVisibility(View.GONE);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            //页面开始加载
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            logger("拦截url:" + url);
            if (url.equals("http://www.google.com/")) {
                toastShort("国内不能访问google,拦截该url");
                return true;//表示我已经处理过了
            }
            return super.shouldOverrideUrlLoading(view, url);
        }
    };
    //WebChromeClient主要辅助WebView处理Javascript的对话框、网站图标、网站title、加载进度等
    private WebChromeClient webChromeClient = new WebChromeClient() {
        //不支持js的alert弹窗，需要自己监听然后通过dialog弹窗
        @Override
        public boolean onJsAlert(WebView webView, String url, String message, JsResult result) {
            AlertDialog.Builder localBuilder = new AlertDialog.Builder(webView.getContext());
            localBuilder.setMessage(message).setPositiveButton("确定", null);
            localBuilder.setCancelable(false);
            localBuilder.create().show(); //注意: //必须要这一句代码:
            //result.confirm()表示: //处理结果为确定状态同时唤醒WebCore线程 //否则不能继续点击按钮
            result.confirm();
            return true;
        }

        //获取网页标题
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            logger("网页标题:" + title);
        } //加载进度回调

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            progressBar.setProgress(newProgress);
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        logger("是否有上一个页面:" + webview.canGoBack());
        if (webview.canGoBack() && keyCode == KeyEvent.KEYCODE_BACK) {
            // /点击返回按钮的时候判断有没有上一页
            webview.goBack(); // goBack()表示返回webView的上一页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @JavascriptInterface //仍然必不可少
    public void getClient(String str) {
        logger("html调用客户端:" + str);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy(); //释放资源
        webview.destroy();
        webview = null;
    }

}


