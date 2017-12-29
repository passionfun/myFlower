package bocai.com.yanghuaji.ui.intelligentPlanting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.Application;
import bocai.com.yanghuaji.base.common.Factory;
import bocai.com.yanghuaji.base.presenter.PresenterActivity;
import bocai.com.yanghuaji.model.BindEquipmentModel;
import bocai.com.yanghuaji.model.EquipmentCard;
import bocai.com.yanghuaji.model.EquipmentModel;
import bocai.com.yanghuaji.model.LongToothRspModel;
import bocai.com.yanghuaji.model.MessageEvent;
import bocai.com.yanghuaji.presenter.intelligentPlanting.ConnectContract;
import bocai.com.yanghuaji.presenter.intelligentPlanting.ConnectPresenter;
import bocai.com.yanghuaji.util.DateUtils;
import bocai.com.yanghuaji.util.UiTool;
import bocai.com.yanghuaji.util.persistence.Account;
import butterknife.BindView;
import butterknife.OnClick;
import io.fogcloud.sdk.easylink.api.EasyLink;
import io.fogcloud.sdk.easylink.helper.EasyLinkCallBack;
import io.fogcloud.sdk.easylink.helper.EasyLinkParams;
import io.fogcloud.sdk.mdns.api.MDNS;
import io.fogcloud.sdk.mdns.helper.SearchDeviceCallBack;
import xpod.longtooth.LongTooth;
import xpod.longtooth.LongToothAttachment;
import xpod.longtooth.LongToothServiceResponseHandler;
import xpod.longtooth.LongToothTunnel;

/**
 * 作者 yuanfei on 2017/11/22.
 * 邮箱 yuanfei221@126.com
 */

public class ConnectActivity extends PresenterActivity<ConnectContract.Presenter>
        implements ConnectContract.View {
    @BindView(R.id.tv_title)
    TextView mTitle;

    @BindView(R.id.img_back)
    ImageView mImgBack;

    @BindView(R.id.tv_time)
    TextView mTime;

    @BindView(R.id.img_anim_bg)
    ImageView mAnimBg;


    public static final String KEY_SSID = "KEY_SSID";
    public static final String KEY_PASSWORD = "KEY_PASSWORD";
    public static final String TAG = ConnectActivity.class.getName();
    private String ssid;
    private String password;
    MDNS mdns = new MDNS(this);
    private CountDownTimer timer;
    private List<String> mScanData;

    //显示的入口
    public static void show(Context context, String ssid, String password, ArrayList<String> scanData) {
        if (TextUtils.isEmpty(ssid) || TextUtils.isEmpty(password)) {
            Application.showToast("参数错误");
            return;
        }
        Intent intent = new Intent(context, ConnectActivity.class);
        intent.putStringArrayListExtra(AddEquipmentDisplayActivity.KEY_SCAN_DATA, scanData);
        intent.putExtra(KEY_SSID, ssid);
        intent.putExtra(KEY_PASSWORD, password);
        context.startActivity(intent);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_connect;
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        ssid = bundle.getString(KEY_SSID);
        password = bundle.getString(KEY_PASSWORD);
        mScanData = getIntent().getStringArrayListExtra(AddEquipmentDisplayActivity.KEY_SCAN_DATA);
        return super.initArgs(bundle);
    }

    @OnClick(R.id.img_back)
    void onBackClick() {
        timer.cancel();
        finish();
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        UiTool.setBlod(mTitle);
        mTitle.setText("连接设备");
//        mPresenter.addEquipment(Account.getToken(), "666", "B0:F8:93:15:22:DB", "666",
//                "1.0.4", "1.1.2353.356.547", "1504608600", "WG301");
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.load_animation);
        mAnimBg.startAnimation(animation);

        timer = new CountDownTimer(61000, 1000) {
            @Override
            public void onTick(long l) {
                mTime.setText(l / 1000 + "");
            }

            @Override
            public void onFinish() {
                mTime.setText(0 + "");
                stopSearch();
                ConnectFailedActivity.show(ConnectActivity.this);
                finish();
            }
        }.start();
    }

    @Override
    protected void initData() {
        super.initData();
        EasyLink elink = new EasyLink(this);
        EasyLinkParams easylinkPara = new EasyLinkParams();
        easylinkPara.ssid = ssid;
        easylinkPara.password = password;
        easylinkPara.runSecond = 60000;
        easylinkPara.sleeptime = 20;

        elink.startEasyLink(easylinkPara, new EasyLinkCallBack() {
            @Override
            public void onSuccess(int code, String message) {
                Log.d(TAG, "sunhengchao111" + code + message);
            }

            @Override
            public void onFailure(int code, String message) {
                Log.d(TAG, "sunhengchao111" + code + message);
                Application.showToast(message);
                stopSearch();
                ConnectFailedActivity.show(ConnectActivity.this);
                finish();
            }
        });


        String serviceInfo = "_easylink._tcp.local.";
        mdns.startSearchDevices(serviceInfo, new SearchDeviceCallBack() {
            @Override
            public void onSuccess(int code, String message) {
                super.onSuccess(code, message);
            }

            @Override
            public void onFailure(int code, String message) {
                super.onFailure(code, message);
                Application.showToast(message);
                stopSearch();
                ConnectFailedActivity.show(ConnectActivity.this);
                finish();
            }

            @Override
            public void onDevicesFind(int code, JSONArray deviceStatus) {
                super.onDevicesFind(code, deviceStatus);
                String content = deviceStatus.toString();
                Log.d("sunhengchao", "onDevicesFind: " + content);
                if (!TextUtils.isEmpty(content) && !content.equals("[]")) {
                    jsonContent = content;
                    bindEquipment(content);
                }
            }
        });
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopSearch();
    }

    private Gson gson;
    private EquipmentModel mModel;
    private String longToothId;
    private String timeStamp;
    private String jsonContent;

    private void bindEquipment(String jsonContent) {
        gson = new Gson();
        List<EquipmentModel> equipmentModels = gson.fromJson(jsonContent, new TypeToken<List<EquipmentModel>>() {
        }.getType());
        for (EquipmentModel equipmentModel : equipmentModels) {
            //"B0:F8:93:10:CF:E6"
            String mac = equipmentModel.getMAC();
            String content = mac.replaceAll(":", "");
            if (mScanData == null) {
                return;
            }
            if (content.equals(mScanData.get(2)) && equipmentModel.get_$BOUNDSTATUS310().equals("notBound")) {
                stopSearch();
                //停止搜索设备
                Application.showToast("绑定中...");
                mModel = equipmentModel;
                Log.d("sunhengchao", "bind: " + mModel.toString());
                longToothId = equipmentModel.getLTID();
                timeStamp = DateUtils.getCurrentDateTimes();
                BindEquipmentModel model = new BindEquipmentModel("BR", timeStamp);
                String request = gson.toJson(model);
                LongTooth.request(longToothId, "longtooth", LongToothTunnel.LT_ARGUMENTS, request.getBytes(), 0, request.getBytes().length,
                        new SampleAttachment(), new LongToothResponse());
            } else if ((content.equals(mScanData.get(2)) && !equipmentModel.get_$BOUNDSTATUS310().equals("notBound"))) {
                Application.showToast("该设备已被绑定过");
                stopSearch();
                finish();
            }
        }
    }

    private void stopSearch() {
        if ( mdns!= null)
            mdns.stopSearchDevices(null);
        if (timer != null)
            timer.cancel();
    }

    @Override
    public void addEquipmentSuccess(EquipmentCard card) {
        timer.cancel();
        EventBus.getDefault().post(new MessageEvent(VeticalRecyclerFragment.VERTICAL_RECYLER_REFRESH));
        EventBus.getDefault().post(new MessageEvent(HorizontalRecyclerFragment.HORIZONTALRECYLER_REFRESH));
        ConnectSuccessActivity.show(ConnectActivity.this, jsonContent, card.getId(), card.getEquipName(), (ArrayList<String>) mScanData);
        finish();
    }

    @Override
    public void addEquipmentFailed() {
        timer.cancel();
        ConnectFailedActivity.show(this);
    }

    @Override
    protected ConnectContract.Presenter initPresenter() {
        return new ConnectPresenter(this);
    }


private class LongToothResponse implements LongToothServiceResponseHandler {
    private boolean isResp = false;

    public LongToothResponse() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!isResp) {
                    Run.onUiAsync(new Action() {
                        @Override
                        public void call() {
                            Application.showToast("绑定请求无响应");
                        }
                    });
                }
            }
        }, 6000);
    }

    @Override
    public void handleServiceResponse(LongToothTunnel ltt, String ltid_str,
                                      String service_str, int data_type, byte[] args,
                                      LongToothAttachment attachment) {
        String result = new String(args);
        Log.d("sunhengchao", "handleServiceResponse: " + new String(args));
        final LongToothRspModel longToothRspModel = gson.fromJson(result, LongToothRspModel.class);
        isResp = true;
        if (longToothRspModel.getCODE() == 0) {
            String mEquipmentName = mModel.getDEVNAME();
            String macAddress = mModel.getMAC();
            String token = Account.getToken();
            String serialNum = mScanData.get(1);
            String version = mModel.get_$FirmwareRev196();
            String series = mEquipmentName.substring(0, 5);
            if (mPresenter != null) {
                mPresenter.addEquipment(token, mEquipmentName, macAddress, serialNum, version, longToothId, timeStamp, series);
            }
        } else {
            Run.onUiAsync(new Action() {
                @Override
                public void call() {
                    Factory.decodeRspCode(longToothRspModel);
                }
            });
        }

    }
}

}
