package bocai.com.yanghuaji.ui.plantingDiary;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.common.Common;
import bocai.com.yanghuaji.base.presenter.PresenterActivity;
import bocai.com.yanghuaji.model.DiaryCardModel;
import bocai.com.yanghuaji.model.MessageEvent;
import bocai.com.yanghuaji.presenter.plantingDiary.DiaryListContract;
import bocai.com.yanghuaji.presenter.plantingDiary.DiaryListPresenter;
import bocai.com.yanghuaji.util.ActivityUtil;
import bocai.com.yanghuaji.util.DateUtils;
import butterknife.BindView;
import butterknife.OnClick;

public class DiaryListActivity extends PresenterActivity<DiaryListContract.Presenter>
        implements DiaryListContract.View {
    @BindView(R.id.img_back)
    ImageView mImgBack;

    @BindView(R.id.ll_root)
    LinearLayout mRoot;

    @BindView(R.id.webView)
    WebView mWebview;

    @BindView(R.id.progress)
    ProgressBar progress;
    public static final String DIARY_LIST_REFRESH = "DIARY_LIST_REFRESH";
    public static final String KEY_DIARY_ID = "KEY_DIARY_ID";
    private String mDiaryId, mLoadUrl;
    private UMWeb mShareWeb;
    private UMShareListener mUmShareListener;

    //显示的入口
    public static void show(Context context, String diaryId) {
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDiaryItemDeleteSuccess(MessageEvent messageEvent) {
        if (messageEvent.getMessage().equals(DIARY_LIST_REFRESH)) {
            mWebview.reload();
        }
    }

    @Override
    protected void initWidget() {
        EventBus.getDefault().register(this);
        super.initWidget();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void initData() {
        super.initData();
        ActivityUtil.initWebSetting(mWebview.getSettings());
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
                Log.d("test", url);
                if (url.startsWith(Common.Constance.H5_BASE + "diary_info.html?")) {
                    DiaryDetailActivity.show(DiaryListActivity.this, url);
                }
                return true;
            }
        });

        mLoadUrl = Common.Constance.H5_BASE + "diary.html?id=" + mDiaryId;
        mWebview.loadUrl(mLoadUrl);

        mShareWeb = new UMWeb(mLoadUrl);
        mShareWeb.setTitle("养花机");//标题
        //todo 修改为正式图标和描述
        mShareWeb.setThumb(new UMImage(this, R.mipmap.img_connect_bg));  //缩略图
        mShareWeb.setDescription("养花机");//描述
        //分享的回调
        mUmShareListener = new UMShareListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {

            }

            @Override
            public void onResult(SHARE_MEDIA share_media) {
                Toast.makeText(DiaryListActivity.this, "分享成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                Toast.makeText(DiaryListActivity.this, "分享失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media) {

            }
        };
    }

    @OnClick(R.id.img_back)
    void onBackClick() {
        finish();
    }


    @OnClick(R.id.img_delete)
    void onDeleteClick() {
        //删除操作
        AlertDialog.Builder deleteDialog = new AlertDialog.Builder(this);
        deleteDialog.setTitle("确认删除该日记？");
        deleteDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mPresenter.deleteDiary(mDiaryId);
            }
        });
        deleteDialog.setNegativeButton("取消", null);
        deleteDialog.show();
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
                        popupWindow.dismiss();
                        // TODO 分享链接
                        break;
                    case R.id.img_share_qq:
                        //  分享到QQ
                        new ShareAction(DiaryListActivity.this)
                                .setPlatform(SHARE_MEDIA.QQ)//传入平台
                                .withMedia(mShareWeb)
                                .setCallback(mUmShareListener)//回调监听器
                                .share();
                        popupWindow.dismiss();
                        break;
                    case R.id.img_share_wechat:
                        //  分享到微信
                        new ShareAction(DiaryListActivity.this)
                                .setPlatform(SHARE_MEDIA.WEIXIN)//传入平台
                                .withMedia(mShareWeb)
                                .setCallback(mUmShareListener)//回调监听器
                                .share();
                        popupWindow.dismiss();
                        break;
                    case R.id.img_share_friends:
                        //  分享到朋友圈
                        new ShareAction(DiaryListActivity.this)
                                .setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)//传入平台
                                .withMedia(mShareWeb)
                                .setCallback(mUmShareListener)//回调监听器
                                .share();
                        popupWindow.dismiss();
                        break;
                }
            }
        });
        ActivityUtil.setBackgroundAlpha(this, 0.19f);
        popupWindow.showAtLocation(mRoot, Gravity.BOTTOM, 0, 30);
    }

    @OnClick(R.id.img_data_card)
    void onDataCardClick() {
        mPresenter.getDiaryData(mDiaryId);

    }


    @Override
    public void getDiaryDataSuccess(DiaryCardModel diaryCardModel) {
        DiaryContentCardPopupWindow popupWindow = new DiaryContentCardPopupWindow(this);
        ActivityUtil.setBackgroundAlpha(this, 0.19f);
        popupWindow.initData(diaryCardModel.getPhoto(), diaryCardModel.getPlantName(), diaryCardModel.getEquipName(),
                diaryCardModel.getPlace(), DateUtils.timet(diaryCardModel.getPlantTime()));
        popupWindow.showAtLocation(mRoot, Gravity.BOTTOM, 0, 30);
    }

    @Override
    public void deleteDiarySuccess() {
        EventBus.getDefault().post(new MessageEvent(PlantingDiaryFragment.PLANTING_DIARY_REFRESH));
        finish();
    }

    @Override
    protected DiaryListContract.Presenter initPresenter() {
        return new DiaryListPresenter(this);
    }
}
