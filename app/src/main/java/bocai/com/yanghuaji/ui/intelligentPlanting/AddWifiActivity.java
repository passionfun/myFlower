package bocai.com.yanghuaji.ui.intelligentPlanting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.Activity;
import bocai.com.yanghuaji.base.Application;
import bocai.com.yanghuaji.model.PlantSeriesModel;
import bocai.com.yanghuaji.util.UiTool;
import bocai.com.yanghuaji.util.persistence.Account;
import butterknife.BindView;
import butterknife.OnClick;
import io.fogcloud.sdk.easylink.api.EasyLink;

/**
 * 作者 yuanfei on 2017/11/14.
 * 邮箱 yuanfei221@126.com
 */

public class AddWifiActivity extends Activity {
    @BindView(R.id.tv_title)
    TextView mTitle;

    @BindView(R.id.img_back)
    ImageView mImgBack;

    @BindView(R.id.et_wifi_name)
    EditText mWifiName;

    @BindView(R.id.et_wifi_password)
    EditText mWifiPassword;

    @BindView(R.id.img_next)
    ImageView mNext;

    //    private MiCODevice micodev;
    private EasyLink elink;
    private List<String> mScanData;
    PlantSeriesModel.PlantSeriesCard plantSeriesCard;
    private static boolean isAddEquipments = false;
    private String ssid;

    //显示的入口(单设备)
    public static void show(Context context, ArrayList<String> scanData) {
        Intent intent = new Intent(context, AddWifiActivity.class);
        intent.putStringArrayListExtra(AddEquipmentDisplayActivity.KEY_SCAN_DATA, scanData);
        isAddEquipments = false;
        context.startActivity(intent);
    }

    //显示的入口(多设备添加)
    public static void show(Context context, PlantSeriesModel.PlantSeriesCard plantSeriesCard) {
        Intent intent = new Intent(context, AddWifiActivity.class);
        if (plantSeriesCard == null) {
            Application.showToast("参数异常");
            return;
        }
        intent.putExtra(AddEquipmentsActivity.KEY_PLANT_CARD, plantSeriesCard);
        isAddEquipments = true;
        context.startActivity(intent);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_add_wifi;
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        mScanData = getIntent().getStringArrayListExtra(AddEquipmentDisplayActivity.KEY_SCAN_DATA);
        plantSeriesCard = (PlantSeriesModel.PlantSeriesCard) bundle.getSerializable(AddEquipmentsActivity.KEY_PLANT_CARD);
        return super.initArgs(bundle);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        UiTool.setBlod(mTitle);
        mTitle.setText("添加设备");
//        micodev = new MiCODevice(this);
        elink = new EasyLink(this);

        mWifiName.setText(elink.getSSID());
        ssid = mWifiName.getText().toString();
        mWifiPassword.setText(Account.getWifiPassword(this, ssid));
    }

    @OnClick(R.id.img_back)
    void onBackClick() {
        finish();
    }


    @OnClick(R.id.img_next)
    void onNextClick() {
        String password = mWifiPassword.getText().toString();
        ssid = mWifiName.getText().toString();
        if (TextUtils.isEmpty(password)) {
            Application.showToast("请输入WiFi密码");
            return;
        }
        Account.saveWifiPassword(this, ssid, password);
        if (isAddEquipments) {
            if (plantSeriesCard == null) {
                Application.showToast("系列信息为空");
                finish();
            }
            if (TextUtils.isEmpty(ssid) || TextUtils.isEmpty(password)) {
                Application.showToast("参数异常");
                return;
            }
            AddEquipmentsActivity.show(this, ssid, password, plantSeriesCard);
        } else {
            ConnectActivity.show(this, ssid, password, (ArrayList<String>) mScanData);
        }

        mNext.setEnabled(false);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mNext.setEnabled(true);
    }

    @OnClick(R.id.tv_setting_wireless)
    void onSettingWirelessClick() {
        //跳转WiFi页面
        Intent intent = new Intent();
        intent.setAction("android.net.wifi.PICK_WIFI_NETWORK");
        startActivity(intent);
    }

}
