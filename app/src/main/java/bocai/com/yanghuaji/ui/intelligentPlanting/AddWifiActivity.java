package bocai.com.yanghuaji.ui.intelligentPlanting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.Activity;
import butterknife.BindView;
import butterknife.OnClick;
import io.fog.fog2sdk.MiCODevice;

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

    private MiCODevice micodev;
    private List<String> mScanData;

    //显示的入口
    public static void show(Context context,ArrayList<String> scanData) {
        Intent intent = new Intent(context, AddWifiActivity.class);
        intent.putStringArrayListExtra(AddEquipmentDisplayActivity.KEY_SCAN_DATA,scanData);
        context.startActivity(intent);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_add_wifi;
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        mScanData = getIntent().getStringArrayListExtra(AddEquipmentDisplayActivity.KEY_SCAN_DATA);
        return super.initArgs(bundle);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        mTitle.setText("添加设备");
        micodev = new MiCODevice(this);
        mWifiName.setText(micodev.getSSID());
    }

    @OnClick(R.id.img_back)
    void onBackClick() {
        finish();
    }


    @OnClick(R.id.img_next)
    void onNextClick() {
        String ssid = mWifiName.getText().toString();
        String password = mWifiPassword.getText().toString();
        ConnectActivity.show(this,ssid,password, (ArrayList<String>) mScanData);
    }


    @OnClick(R.id.tv_setting_wireless)
    void onSettingWirelessClick() {

    }

}
