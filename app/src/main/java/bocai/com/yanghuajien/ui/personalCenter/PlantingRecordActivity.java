package bocai.com.yanghuajien.ui.personalCenter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import bocai.com.yanghuajien.R;
import bocai.com.yanghuajien.base.Activity;
import bocai.com.yanghuajien.base.common.Common;
import bocai.com.yanghuajien.util.ActivityUtil;
import bocai.com.yanghuajien.util.UiTool;
import bocai.com.yanghuajien.util.persistence.Account;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 种植记录
 * 作者 yuanfei on 2017/11/10.
 * 邮箱 yuanfei221@126.com
 */

public class PlantingRecordActivity extends Activity {
    @BindView(R.id.tv_title)
    TextView mTitle;

    //    @BindView(R.id.bt_planting)
//    Button mBtPlanting;
//
//    @BindView(R.id.bt_planted)
//    Button mBtPlanted;
    @BindView(R.id.progress)
    ProgressBar progress;
    @BindView(R.id.web_view)
    WebView webView;

    //显示的入口
    public static void show(Context context) {
        context.startActivity(new Intent(context, PlantingRecordActivity.class));
    }


    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_planting_record;
    }

    @Override
    protected void initData() {
        super.initData();
        UiTool.setBlod(mTitle);
        mTitle.setText("种植记录");
        ActivityUtil.initWebSetting(webView.getSettings());
        webView.loadUrl(Common.Constance.H5_BASE+"record.html?token="+ Account.getToken());
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
    }

    @OnClick(R.id.img_back)
    void onBackClick() {
        finish();
    }

//    @OnClick(R.id.bt_planting)
//    void onPlantingClick() {
//        mBtPlanting.setTextColor(Color.parseColor("#FFFFFF"));
//        mBtPlanted.setTextColor(Color.parseColor("#999999"));
//        mBtPlanting.setBackgroundResource(R.mipmap.img_plant_record_selected);
//        mBtPlanted.setBackgroundResource(R.drawable.bt_plant_record_bg);
//    }
//
//    @OnClick(R.id.bt_planted)
//    void onPlantedClick() {
//        mBtPlanted.setTextColor(Color.parseColor("#FFFFFF"));
//        mBtPlanting.setTextColor(Color.parseColor("#999999"));
//        mBtPlanted.setBackgroundResource(R.mipmap.img_plant_record_selected);
//        mBtPlanting.setBackgroundResource(R.drawable.bt_plant_record_bg);
//    }

}
