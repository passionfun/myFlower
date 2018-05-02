package bocai.com.yanghuajien.ui.intelligentPlanting;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;

import bocai.com.yanghuajien.R;
import bocai.com.yanghuajien.base.common.Common;
import bocai.com.yanghuajien.util.ActivityUtil;

/**
 * 作者 yuanfei on 2017/11/14.
 * 邮箱 yuanfei221@126.com
 */

public class ShowStatePopupWindow extends PopupWindow {
    private ImageView mConfirm, mClose;
    private ItemClickListener mListener;

    public ShowStatePopupWindow(final Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_pop_show_state, null);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setContentView(view);
        mConfirm = view.findViewById(R.id.img_confirm);
        mClose = view.findViewById(R.id.img_close);
        WebView webView = view.findViewById(R.id.webView);
        ProgressBar progress = view.findViewById(R.id.progress);
        initWeb(webView,progress);
        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onItemClick(view);
            }
        });
        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowStatePopupWindow.this.dismiss();
            }
        });
        this.setBackgroundDrawable(new BitmapDrawable());
        this.setOutsideTouchable(true);
        this.setFocusable(true);
        this.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                ActivityUtil.setBackgroundAlpha((Activity) context, 1f);
            }
        });
    }

    private void initWeb(WebView webView, final ProgressBar progress) {
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

        webView.loadUrl(Common.Constance.H5_BASE +"single.html?id="+3);
    }

    public void setOnTtemClickListener(ItemClickListener listener) {
        mListener = listener;
    }

    interface ItemClickListener {
        void onItemClick(View view);
    }

}
