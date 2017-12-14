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

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

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
import bocai.com.yanghuaji.util.persistence.Account;
import butterknife.BindView;
import butterknife.OnClick;
import io.fog.fog2sdk.MiCODevice;
import io.fogcloud.easylink.helper.EasyLinkCallBack;
import io.fogcloud.fog_mdns.helper.SearchDeviceCallBack;
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
    private String ssid;
    private String password;
    private MiCODevice micodev;
    private CountDownTimer timer;
    private List<String> mScanData;
    //显示的入口
    public static void show(Context context, String ssid, String password,ArrayList<String> scanData) {
        if (TextUtils.isEmpty(ssid) || TextUtils.isEmpty(password)) {
            Application.showToast("参数错误");
            return;
        }
        Intent intent = new Intent(context, ConnectActivity.class);
        intent.putStringArrayListExtra(AddEquipmentDisplayActivity.KEY_SCAN_DATA,scanData);
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
    protected void initBefore() {
        super.initBefore();
        //初始化长牙
        Factory.runOnAsync(new Runnable() {
            @Override
            public void run() {
                try {
                    //启动长牙
                    LongTooth.setRegisterHost("114.215.170.184", 53180);
                    LongTooth.start(Application.getInstance(),
                            2000110273,
                            1,
                            "30820126300D06092A864886F70D010101050003820113003082010E028201023030384645304233423539423931413943414435463341363735463632444645443333343739414132433337423543434333354239323733413330413241354244414539424344373142374334463944423237393430394139463235373245414534424133324141453334433133433036444645333937423531434636413743424143463638434446304432313945334644374442464341383032363645413730353039414239393230374246393735323435314133343943383530394135393232463038413531423344333037353035424646353139363234413835413842443742463634364230444438373944433542453131453230393443363132373944440206303130303031",
                            null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    protected void initWidget() {
        super.initWidget();
        mTitle.setText("连接设备");
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.load_animation);
        mAnimBg.startAnimation(animation);

        timer =new CountDownTimer(61000, 1000) {
            @Override
            public void onTick(long l) {
                mTime.setText(l / 1000 + "");
            }

            @Override
            public void onFinish() {
                mTime.setText(0 + "");
                ConnectFailedActivity.show(ConnectActivity.this);
                finish();
            }
        }.start();
    }

    @Override
    protected void initData() {
        super.initData();
        micodev = new MiCODevice(this);
        //开始配网
        micodev.startEasyLink(ssid, password, true, 60000, 20, "", "", new EasyLinkCallBack() {
            @Override
            public void onSuccess(int code, String message) {
                Log.d("shc", "onSuccess配网: " + message);
            }

            @Override
            public void onFailure(int code, String message) {
                Application.showToast(message);
                timer.cancel();
                ConnectFailedActivity.show(ConnectActivity.this);
                finish();
            }
        });
        //开始搜索设备
        final String serviceName = "_easylink._tcp.local.";
        micodev.startSearchDevices(serviceName, new SearchDeviceCallBack() {
            @Override
            public void onFailure(int code, String message) {
                super.onFailure(code, message);
                Application.showToast(message);
                timer.cancel();
                ConnectFailedActivity.show(ConnectActivity.this);
                finish();
            }

            @Override
            public void onDevicesFind(int code, JSONArray deviceStatus) {
                super.onDevicesFind(code, deviceStatus);
                String content = deviceStatus.toString();
                Log.d("shc", "onDevicesFind: " + content);
                if (!TextUtils.isEmpty(content) && !content.equals("[]")) {
                    jsonContent = content;
                    bindEquipment(content);
                    micodev.stopEasyLink(null);
                }
            }
        });
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
            String content = mac.replaceAll(":","");
            if (content.equals(mScanData.get(2))){
                //停止搜索设备
                micodev.stopSearchDevices( null);
                mModel = equipmentModel;
                Log.d("shc", "bind: "+mModel.toString());
                longToothId = equipmentModel.getLTID();
                timeStamp = DateUtils.getCurrentDateTimes();
                BindEquipmentModel model = new BindEquipmentModel("BR", timeStamp);
                String request = gson.toJson(model);
                LongTooth.request(longToothId,"longtooth",LongToothTunnel.LT_ARGUMENTS,request.getBytes(),0,request.getBytes().length,
                        new SampleAttachment(),new LongToothResponse());
            }
        }
    }

    @Override
    public void addEquipmentSuccess(EquipmentCard card) {
        timer.cancel();
        EventBus.getDefault().post(new MessageEvent(SecondSettingActivity.DATA_DELETE_SUCCESS));
        ConnectSuccessActivity.show(ConnectActivity.this, jsonContent,card.getId(),card.getEquipName(), (ArrayList<String>) mScanData);
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

        @Override
        public void handleServiceResponse(LongToothTunnel ltt, String ltid_str,
                                          String service_str, int data_type, byte[] args,
                                          LongToothAttachment attachment) {
            String result = new String(args);
            Log.d("shc", "handleServiceResponse: "+new String(args));
            LongToothRspModel longToothRspModel = gson.fromJson(result, LongToothRspModel.class);
            if (longToothRspModel.getCODE()==0){
                String mEquipmentName = mModel.getDEVNAME();
                String macAddress = mModel.getMAC();
                String token = Account.getToken();
                String serialNum = mScanData.get(1);
                String version = mModel.get_$FirmwareRev196();
                if (mPresenter != null){
                    mPresenter.addEquipment(token,mEquipmentName,macAddress,serialNum,version,longToothId,timeStamp);
                }
            }

        }
    }

}
