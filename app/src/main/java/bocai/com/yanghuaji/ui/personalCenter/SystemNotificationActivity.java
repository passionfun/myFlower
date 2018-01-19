package bocai.com.yanghuaji.ui.personalCenter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.Activity;
import bocai.com.yanghuaji.base.common.Common;
import bocai.com.yanghuaji.util.ActivityUtil;
import bocai.com.yanghuaji.util.persistence.Account;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 系统通知
 * 作者 yuanfei on 2017/11/10.
 * 邮箱 yuanfei221@126.com
 */

public class SystemNotificationActivity extends Activity {
    @BindView(R.id.tv_title)
    TextView mTitle;

    @BindView(R.id.progress)
    ProgressBar progress;

    @BindView(R.id.web_view)
    WebView webView;

    @BindView(R.id.tv_right)
    TextView mRight;

    //显示的入口
    public static void show(Context context) {
        context.startActivity(new Intent(context, SystemNotificationActivity.class));
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_system_notification;
    }

    @Override
    protected void initData() {
        super.initData();
        mTitle.setVisibility(View.GONE);
        mRight.setVisibility(View.VISIBLE);
        mRight.setText("编辑");
        ActivityUtil.initWebSetting(webView.getSettings());
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d("test",url);
                view.loadUrl(url);
                return true;
            }

        });

        webView.setWebChromeClient(new WebChromeClient() {

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
        Log.d("test",Common.Constance.H5_BASE+"notice.html?token="+ Account.getToken());
        webView.loadUrl(Common.Constance.H5_BASE+"notice.html?token="+ Account.getToken());

    }

    @OnClick(R.id.img_back)
    void onBackClick() {
        finish();
    }

    @OnClick(R.id.tv_right)
    void onRightClick() {
        if (mRight.getText().equals("编辑")){
            mRight.setText("完成");
            webView.loadUrl("javascript:edit()");
        }else {
            mRight.setText("编辑");
            webView.loadUrl("javascript:editOk()");
        }
    }


}
