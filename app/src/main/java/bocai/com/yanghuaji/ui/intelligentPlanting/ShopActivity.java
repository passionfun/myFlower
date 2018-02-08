package bocai.com.yanghuaji.ui.intelligentPlanting;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.Activity;
import bocai.com.yanghuaji.util.ActivityUtil;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 商城
 * Created by shc on 2018/1/16.
 */

public class ShopActivity extends Activity {
    @BindView(R.id.webView)
    WebView mWebView;

    @BindView(R.id.progress)
    ProgressBar progress;

    //显示的入口
    public static void show(Context context) {
        Intent intent = new Intent(context, ShopActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_shop;
    }

    @Override
    protected void initData() {
        super.initData();
        ActivityUtil.initWebSetting(mWebView.getSettings());
//        mWebView.setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                Log.d("test", url);
//                view.loadUrl(url);
//                return true;
//            }
//        });
//        mWebView.setWebChromeClient(new WebChromeClient() {
//            @Override
//            public void onProgressChanged(WebView view, int newProgress) {
//                if (newProgress == 100) {
//                    progress.setVisibility(View.GONE);
//                } else {
//                    if (progress.getVisibility() == View.GONE) {
//                        progress.setVisibility(View.VISIBLE);
//                    }
//                    progress.setProgress(newProgress);
//                }
//                super.onProgressChanged(view, newProgress);
//            }
//        });
        mWebView.loadUrl("https://h5.youzan.com/v2/showcase/homepage?alias=1dnmgamr");
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
            mWebView.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @OnClick(R.id.img_back)
    void onBackClick() {
        if ( mWebView.canGoBack()) {
            mWebView.goBack();// 返回前一个页面
        }else {
            finish();
        }
    }
}
