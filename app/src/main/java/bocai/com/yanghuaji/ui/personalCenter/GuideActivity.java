package bocai.com.yanghuaji.ui.personalCenter;

import android.content.Context;
import android.content.Intent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.Activity;
import butterknife.BindView;

/**
 * 作者 yuanfei on 2017/11/24.
 * 邮箱 yuanfei221@126.com
 */

public class GuideActivity extends Activity {
    @BindView(R.id.webView)
    WebView mWebView;


    //显示的入口
    public static void show(Context context) {
        context.startActivity(new Intent(context, GuideActivity.class));
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_guide;
    }

    @Override
    protected void initData() {
        super.initData();
        mWebView.loadUrl("http://121.41.128.239:8082/yhj/web/h5/yhj/single.html?id=0");


        //设置不用系统浏览器打开,直接显示在当前Webview
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }
}
