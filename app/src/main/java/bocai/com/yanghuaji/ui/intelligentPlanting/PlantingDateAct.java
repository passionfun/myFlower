package bocai.com.yanghuaji.ui.intelligentPlanting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.gson.Gson;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.util.Timer;
import java.util.TimerTask;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.Activity;
import bocai.com.yanghuaji.base.common.Common;
import bocai.com.yanghuaji.base.presenter.PresenterActivity;
import bocai.com.yanghuaji.model.BindEquipmentModel;
import bocai.com.yanghuaji.model.EquipmentDataModel;
import bocai.com.yanghuaji.model.EquipmentRspModel;
import bocai.com.yanghuaji.model.LongToothRspModel;
import bocai.com.yanghuaji.model.PlantStatusModel;
import bocai.com.yanghuaji.model.PlantStatusRspModel;
import bocai.com.yanghuaji.presenter.intelligentPlanting.MainRecylerContract;
import bocai.com.yanghuaji.presenter.intelligentPlanting.PlantDataPresenter;
import bocai.com.yanghuaji.presenter.intelligentPlanting.PlantingDataContract;
import bocai.com.yanghuaji.ui.plantingDiary.PlantingDiaryActivity;
import bocai.com.yanghuaji.util.ActivityUtil;
import bocai.com.yanghuaji.util.UiTool;
import bocai.com.yanghuaji.util.persistence.Account;
import bocai.com.yanghuaji.util.widget.EmptyView;
import butterknife.BindView;
import butterknife.OnClick;
import xpod.longtooth.LongTooth;
import xpod.longtooth.LongToothAttachment;
import xpod.longtooth.LongToothServiceResponseHandler;
import xpod.longtooth.LongToothTunnel;

public class PlantingDateAct extends PresenterActivity<PlantingDataContract.Presenter> implements PlantingDataContract.View {
    private static String MID = "mid";
    @BindView(R.id.img_back)
    ImageView imgBack;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.progress)
    ProgressBar progress;

    @BindView(R.id.webView)
    WebView webView;

    @BindView(R.id.empty)
    EmptyView mEmpty;

    public static final String TAG = PlantingDateAct.class.getName();
    private String mUrl;
    private  EquipmentRspModel.ListBean mPlantBean;
    private Gson gson = new Gson();
    Timer timer = new Timer();

    //显示的入口
    public static void show(Context context, String url,EquipmentRspModel.ListBean plantBean) {
        Intent intent = new Intent(context, PlantingDateAct.class);
        intent.putExtra(MID, url);
        intent.putExtra(SecondSettingActivity.KEY_PLANT_BEAN,plantBean);
        context.startActivity(intent);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_planting_date;
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        mPlantBean = (EquipmentRspModel.ListBean) bundle.getSerializable(SecondSettingActivity.KEY_PLANT_BEAN);
        return super.initArgs(bundle);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        isHaveNewVersion(mPlantBean);
        mEmpty.bind(webView);
    }

    private void isHaveNewVersion(EquipmentRspModel.ListBean plantModel){
        BindEquipmentModel model = new BindEquipmentModel("isUpdate", plantModel.getPSIGN());
        String request = gson.toJson(model);
        LongTooth.request(plantModel.getLTID(), "longtooth", LongToothTunnel.LT_ARGUMENTS, request.getBytes(),
                0, request.getBytes().length, null,new  LongToothServiceResponseHandler(){
                    @Override
                    public void handleServiceResponse(LongToothTunnel ltt, String ltid_str,
                                                      String service_str, int data_type, byte[] args,
                                                      LongToothAttachment attachment) {
                        if (args==null)
                            return;
                        String result = new String(args);
                        if (TextUtils.isEmpty(result)||!result.contains("CODE")) {
                            return;
                        }
                        LongToothRspModel longToothRspModel = gson.fromJson(result, LongToothRspModel.class);
                        Log.d(TAG, "update:" + result);
                        int code = longToothRspModel.getCODE();
                        switch (code) {
                            case 501:
                                //  501:有升级的新版本
                                mPresenter.setUpdateStatus(mPlantBean.getMac(),"1");
                                break;
                            default:
                                mPresenter.setUpdateStatus(mPlantBean.getMac(),"0");
                                break;
                        }
                    }
                });
    }

    @OnClick(R.id.img_back)
    void onBackClick() {
        finish();
    }

    @OnClick(R.id.img_setting)
    void onSettingClick() {
        SecondSettingActivity.show(this,mPlantBean);
        finish();
    }

    @OnClick(R.id.img_wifi)
    void onWifiClick() {
        // TODO 重新配网
//        AddWifiActivity
    }

    @Override
    protected void initData() {
        super.initData();

        if (!UiTool.isNetworkAvailable(this)){
            mEmpty.setEmptyText(R.string.network_unavailable);
            mEmpty.triggerEmpty();
            return;
        }
        mUrl = getIntent().getExtras().getString(MID);
        ActivityUtil.initWebSetting(webView.getSettings());


        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d("testshc", url);
                if (url.startsWith(Common.Constance.H5_BASE+"write")){
                    PlantingDiaryActivity.show(PlantingDateAct.this,mPlantBean);
                }else if (url.startsWith(Common.Constance.H5_BASE+"upgrade")){
                    HorizontalRecyclerFragmentHelper.update(PlantingDateAct.this, mPlantBean);
                } else {
                    view.loadUrl(url);
                }
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

        Log.d("test", mUrl);


        if (mUrl.contains("product.html")){
            //  5秒刷新

            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    Run.onUiAsync(new Action() {
                        @Override
                        public void call() {
                            webView.loadUrl("javascript:updata()");
                        }
                    });

                    if (TextUtils.isEmpty(mPlantBean.getPSIGN()) ||
                            TextUtils.isEmpty(mPlantBean.getPid())) {
                        return;
                    }
                    PlantStatusModel model = new PlantStatusModel(1, "getStatus", 1, Integer.parseInt(mPlantBean.getPSIGN()),
                            1);
                    String request = gson.toJson(model);
                    if (!TextUtils.isEmpty(mPlantBean.getLTID())) {
                        LongTooth.request(mPlantBean.getLTID(), "longtooth", LongToothTunnel.LT_ARGUMENTS, request.getBytes(),
                                0, request.getBytes().length, null,
                                new LongToothResponse( mPlantBean));
                    }
                }
            };

            timer.schedule(task, 5000, 5000);

        }

        webView.loadUrl(mUrl);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

    @Override
    public void setDataSuccess(EquipmentDataModel model) {

    }

    @Override
    protected PlantingDataContract.Presenter initPresenter() {
        return new PlantDataPresenter(this);
    }


    class LongToothResponse implements LongToothServiceResponseHandler {

        private EquipmentRspModel.ListBean mPlantModel;


    public LongToothResponse( EquipmentRspModel.ListBean plantModel) {

        mPlantModel = plantModel;
    }





    @Override
    public void handleServiceResponse(LongToothTunnel longToothTunnel, String s, String s1, int i, byte[] bytes, LongToothAttachment longToothAttachment) {
        if (bytes == null) {
            return;
        }
        String jsonContent = new String(bytes);
        if (TextUtils.isEmpty(jsonContent)||!jsonContent.contains("CODE")) {
            return;
        }
        Log.d(TAG, "handleServiceResponse: " + jsonContent);
        PlantStatusRspModel plantStatusRspModel = gson.fromJson(jsonContent, PlantStatusRspModel.class);
        if (plantStatusRspModel.getCODE() == 0) {
            //获取数据成功
            String temperature = plantStatusRspModel.getTemp();
            String wagerState = plantStatusRspModel.getWaterStat();
            String Ec = plantStatusRspModel.getEC();//植物的营养值
            String isLihtOn = mPlantModel.getLight();
            if (mPresenter==null||temperature==null||wagerState==null||isLihtOn==null||Ec==null){
                return;
            }
            mPresenter.setData(Account.getToken(), mPlantModel.getMac(), temperature, wagerState, isLihtOn, Ec);
        }

    }
}

}
