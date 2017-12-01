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

import org.json.JSONArray;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.Activity;
import bocai.com.yanghuaji.base.Application;
import bocai.com.yanghuaji.base.common.Factory;
import butterknife.BindView;
import butterknife.OnClick;
import io.fog.fog2sdk.MiCODevice;
import io.fogcloud.easylink.helper.EasyLinkCallBack;
import io.fogcloud.fog_mdns.helper.SearchDeviceCallBack;
import xpod.longtooth.LongTooth;
import xpod.longtooth.LongToothAttachment;
import xpod.longtooth.LongToothEvent;
import xpod.longtooth.LongToothEventHandler;

/**
 * 作者 yuanfei on 2017/11/22.
 * 邮箱 yuanfei221@126.com
 */

public class ConnectActivity extends Activity {
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

    //显示的入口
    public static void show(Context context, String ssid, String password) {
        if (TextUtils.isEmpty(ssid) || TextUtils.isEmpty(password)) {
            Application.showToast("参数错误");
            return;
        }
        Intent intent = new Intent(context, ConnectActivity.class);
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
        return super.initArgs(bundle);
    }

    @OnClick(R.id.img_back)
    void onBackClick() {
        finish();
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        mTitle.setText("连接设备");
        Factory.runOnAsync(new Runnable() {
            @Override
            public void run() {
                try {
                    //启动长牙
                    LongTooth.start(Application.getInstance(),
                            2000110273,
                            1,
                            "30820126300D06092A864886F70D010101050003820113003082010E028201023030384645304233423539423931413943414435463341363735463632444645443333343739414132433337423543434333354239323733413330413241354244414539424344373142374334463944423237393430394139463235373245414534424133324141453334433133433036444645333937423531434636413743424143463638434446304432313945334644374442464341383032363645413730353039414239393230374246393735323435314133343943383530394135393232463038413531423344333037353035424646353139363234413835413842443742463634364230444438373944433542453131453230393443363132373944440206303130303031",
                            new LongToothHandler());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.load_animation);
        mAnimBg.startAnimation(animation);
        new CountDownTimer(61000, 1000) {
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
                Log.d("shc", "onSuccess配网: "+message);
            }

            @Override
            public void onFailure(int code, String message) {
                Application.showToast(message);
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
                ConnectFailedActivity.show(ConnectActivity.this);
                finish();
            }

            @Override
            public void onDevicesFind(int code, JSONArray deviceStatus) {
                super.onDevicesFind(code, deviceStatus);
                String content = deviceStatus.toString();
                Log.d("shc", "onDevicesFind: " + content);
                if (!TextUtils.isEmpty(content) && !content.equals("[]")) {
                    ConnectSuccessActivity.show(ConnectActivity.this, content);
                    micodev.stopEasyLink(null);
                    micodev.startSearchDevices(serviceName, null);
                    finish();
                }
            }
        });
    }





    private class LongToothHandler implements LongToothEventHandler {
        @Override
        public void handleEvent(int code, String ltid_str, String srv_str, byte[] msg, LongToothAttachment attachment) {
            if (code == LongToothEvent.EVENT_LONGTOOTH_STARTED) {
//                LongTooth.addService(ConnectActivity.class.getName(), new LongToothService());
            }
        }
    }




}
