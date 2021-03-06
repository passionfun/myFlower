package bocai.com.yanghuajien.ui.intelligentPlanting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import bocai.com.yanghuajien.R;
import bocai.com.yanghuajien.base.Activity;
import bocai.com.yanghuajien.base.Application;
import bocai.com.yanghuajien.model.PlantSeriesModel;
import bocai.com.yanghuajien.util.UiTool;
import bocai.com.yanghuajien.util.persistence.Account;
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

    @BindView(R.id.iv_show_psw)
    ImageView mShowPsw;

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
            Application.showToast(Application.getStringText(R.string.parameter_error));
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

    @OnClick(R.id.frame_show_pwd)
    void onShowPswClick(){
        if (mWifiPassword.getInputType() != InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
            mWifiPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            mShowPsw.setImageResource(R.mipmap.img_show_psw);
        } else {
            mWifiPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            mShowPsw.setImageResource(R.mipmap.img_hide_psw);
        }
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        UiTool.setBlod(mTitle);
        mTitle.setText(Application.getStringText(R.string.add_equipment));
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
            password="";
        }
        if (password.length()>0&&password.length()<8) {
            Application.showToast("密码必须大于8位");
            return;
        }
        Account.saveWifiPassword(this, ssid, password);
        if (isAddEquipments) {
            if (plantSeriesCard == null) {
                Application.showToast("系列信息为空");
                finish();
            }
            if (TextUtils.isEmpty(ssid)) {
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
