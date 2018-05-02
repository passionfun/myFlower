package bocai.com.yanghuajien.ui.personalCenter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import bocai.com.yanghuajien.R;
import bocai.com.yanghuajien.base.Activity;
import bocai.com.yanghuajien.base.common.Common;
import bocai.com.yanghuajien.util.ActivityUtil;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者 yuanfei on 2017/11/24.
 * 邮箱 yuanfei221@126.com
 */

public class GuideActivity extends Activity {
    private static final String TYPE = "type";
    @BindView(R.id.webView)
    WebView mWebView;

    @BindView(R.id.progress)
    ProgressBar progress;

    @BindView(R.id.img_back)
    ImageView imgBack;

    private int mType;

    //显示的入口
    public static void show(Context context,int type) {
        Intent intent = new Intent(context, GuideActivity.class);
        intent.putExtra(TYPE,type);
        context.startActivity(intent);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_guide;
    }

    @Override
    protected void initData() {
        super.initData();
        mType = getIntent().getExtras().getInt(TYPE);
        ActivityUtil.initWebSetting(mWebView.getSettings());


        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d("test",url);
                view.loadUrl(url);
                return true;
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    progress.setVisibility(View.GONE);
                } else {
                    if (progress.getVisibility() == View.GONE) {
                        progress.setVisibility(View.VISIBLE);
                    }
                    progress.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }
        });
        //设置不用系统浏览器打开,直接显示在当前Webview

        Log.d("test",Common.Constance.H5_BASE +"single.html?id="+mType);
        mWebView.loadUrl(Common.Constance.H5_BASE +"single.html?id="+mType);
    }


    @OnClick(R.id.img_back)
    void onBackClick() {
        finish();
    }
}
