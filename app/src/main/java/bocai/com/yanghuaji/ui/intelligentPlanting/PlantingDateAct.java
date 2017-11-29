package bocai.com.yanghuaji.ui.intelligentPlanting;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.Activity;
import bocai.com.yanghuaji.util.ActivityUtil;
import butterknife.BindView;
import butterknife.OnClick;

public class PlantingDateAct extends Activity {


    private static String MID = "mid";
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.progress)
    ProgressBar progress;
    @BindView(R.id.webView)
    WebView webView;
    private String mUrl;

    //显示的入口
    public static void show(Context context, String  url) {
        Intent intent = new Intent(context, PlantingDateAct.class);
        intent.putExtra(MID,url);
        context.startActivity(intent);
    }
    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_planting_date;
    }

    @OnClick(R.id.img_back)
    void onBackClick(){
        finish();
    }

    @Override
    protected void initData() {
        super.initData();
        mUrl = getIntent().getExtras().getString(MID);
        ActivityUtil.initWebSetting(webView.getSettings());


        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d("test", url);
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
        //设置不用系统浏览器打开,直接显示在当前Webview

        Log.d("test",mUrl);
        webView.loadUrl(mUrl );
    }

}
