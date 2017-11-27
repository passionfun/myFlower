package bocai.com.yanghuaji.ui.plantingDiary;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.common.Common;
import bocai.com.yanghuaji.base.presenter.BaseContract;
import bocai.com.yanghuaji.base.presenter.PresenterActivity;
import bocai.com.yanghuaji.util.ActivityUtil;
import butterknife.BindView;
import butterknife.OnClick;

public class DiaryListActivity extends PresenterActivity {
    @BindView(R.id.img_back)
    ImageView mImgBack;

    @BindView(R.id.ll_root)
    LinearLayout mRoot;

    @BindView(R.id.webView)
    WebView mWebview;

    @BindView(R.id.progress)
    ProgressBar progress;

    public static final String KEY_DIARY_ID = "KEY_DIARY_ID";
    private String mDiaryId;

    //显示的入口
    public static void show(Context context,String diaryId) {
        Intent intent = new Intent(context, DiaryListActivity.class);
        intent.putExtra(KEY_DIARY_ID, diaryId);
        context.startActivity(intent);
    }



    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_diary_list;
    }


    @Override
    protected boolean initArgs(Bundle bundle) {
        mDiaryId = bundle.getString(KEY_DIARY_ID);
        return super.initArgs(bundle);

    }

    @Override
    protected void initData() {
        super.initData();
        ActivityUtil.initWebSetting(mWebview.getSettings());

//        mWebview.loadUrl("http://www.baidu.com/");


        mWebview.setWebChromeClient(new WebChromeClient() {
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
        mWebview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d("test",url);
                view.loadUrl(url);
                return true;
            }
        });

        mWebview.loadUrl(Common.Constance.H5_BASE+"diary.html?id="+mDiaryId);
    }

    @OnClick(R.id.img_back)
    void onBackClick() {
        finish();
    }

    @OnClick(R.id.img_share)
    void onShareClick() {
        final ShareDiaryListPopupWindow popupWindow = new ShareDiaryListPopupWindow(this);
        popupWindow.setOnTtemClickListener(new ShareDiaryListPopupWindow.ItemClickListener() {
            @Override
            public void onItemClick(View view) {
                switch (view.getId()) {
                    case R.id.tv_cancel:
                        popupWindow.dismiss();
                        break;
                    case R.id.img_copy_link:
                        mWebview.loadUrl("http://www.baidu.com/");
                        popupWindow.dismiss();
                        // TODO 分享链接
                        break;
                    case R.id.img_share_qq:
                        // TODO 分享到QQ
                        break;
                    case R.id.img_share_wechat:
                        // TODO 分享到微信
                        break;
                    case R.id.img_share_friends:
                        // TODO 分享到朋友圈
                        break;
                }
            }
        });
        ActivityUtil.setBackgroundAlpha(this, 0.19f);
        popupWindow.showAtLocation(mRoot, Gravity.BOTTOM,0,0);
    }

    @OnClick(R.id.img_data_card)
    void onDataCardClick() {
        DiaryContentCardPopupWindow popupWindow = new DiaryContentCardPopupWindow(this);
        ActivityUtil.setBackgroundAlpha(this, 0.19f);
        popupWindow.showAtLocation(mRoot, Gravity.BOTTOM,0,0);
    }


    @Override
    protected BaseContract.Presenter initPresenter() {
        return null;
    }
}
