package bocai.com.yanghuaji.ui.plantingDiary;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import org.greenrobot.eventbus.EventBus;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.presenter.PresenterActivity;
import bocai.com.yanghuaji.model.DiaryDetailModel;
import bocai.com.yanghuaji.model.MessageEvent;
import bocai.com.yanghuaji.presenter.plantingDiary.DiaryDetailContract;
import bocai.com.yanghuaji.presenter.plantingDiary.DiaryDetailPresenter;
import bocai.com.yanghuaji.util.ActivityUtil;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者 yuanfei on 2017/11/13.
 * 邮箱 yuanfei221@126.com
 */

public class DiaryDetailActivity extends PresenterActivity<DiaryDetailContract.Presenter>
        implements DiaryDetailContract.View {
    @BindView(R.id.img_back)
    ImageView mImgBack;

    @BindView(R.id.ll_root)
    LinearLayout mRoot;

    @BindView(R.id.webView)
    WebView mWebview;

    @BindView(R.id.progress)
    ProgressBar progress;

    public static final String KEY_URL = "KEY_URL";
    public static final String DIARY_DELETE_SUCCESS = "DIARY_DELETE_SUCCESS";
    private String mUrl;
    private String mDiaryItemId;

    //显示的入口
    public static void show(Context context ,String url) {
        Intent intent = new Intent(context, DiaryDetailActivity.class);
        intent.putExtra(KEY_URL,url);
        context.startActivity(intent);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_diary_detail;
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        mUrl = bundle.getString(KEY_URL);
        //http://121.41.128.239:8082/yhj/web/h5/yhj/diary_info.html?id=15
        mDiaryItemId = mUrl.substring(mUrl.indexOf("=")+1,mUrl.length());
        return super.initArgs(bundle);
    }

    @Override
    protected void initData() {
        super.initData();
        ActivityUtil.initWebSetting(mWebview.getSettings());
        mWebview.setWebChromeClient(new WebChromeClient(){
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
        mWebview.loadUrl(mUrl);
    }

    @OnClick(R.id.img_back)
    void onBackClick() {
        finish();
    }

    @OnClick(R.id.img_share)
    void onShareClick() {
        mPresenter.getDiaryDetail(mDiaryItemId);
    }

    @OnClick(R.id.img_delete)
    void onDeleteClick() {
        //删除操作
        AlertDialog.Builder deleteDialog = new AlertDialog.Builder(this);
        deleteDialog.setTitle("确认删除该日记？");
        deleteDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mPresenter.deleteDiaryItem(mDiaryItemId);
            }
        });
        deleteDialog.setNegativeButton("取消", null);
        deleteDialog.show();
    }

    @Override
    public void getDiaryDetailSuccess(DiaryDetailModel diaryDetailModel) {
        ShareDiaryContentPopupWindow popupWindow = new ShareDiaryContentPopupWindow(this);
        popupWindow.setOnTtemClickListener(new ShareDiaryContentPopupWindow.ItemClickListener() {
            @Override
            public void onItemClick(View view) {
                switch (view.getId()) {
                    case R.id.img_save_picture:
                        // TODO 保存图片
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
        popupWindow.initData(diaryDetailModel.getPhotos().get(0),diaryDetailModel.getContent());
    }

    @Override
    public void deleteDiaryItemSuccess() {
        EventBus.getDefault().post(new MessageEvent(DIARY_DELETE_SUCCESS));
        finish();
    }

    @Override
    protected DiaryDetailContract.Presenter initPresenter() {
        return new DiaryDetailPresenter(this);
    }
}
