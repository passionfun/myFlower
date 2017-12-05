package bocai.com.yanghuaji.ui.intelligentPlanting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.Activity;
import bocai.com.yanghuaji.model.EquipmentRspModel;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者 yuanfei on 2017/11/15.
 * 邮箱 yuanfei221@126.com
 */

public class SecondSettingActivity extends Activity {

    @BindView(R.id.tv_title)
    TextView mTitle;

    @BindView(R.id.img_back)
    ImageView mImgBack;

    public static final String KEY_PLANT_ID = "KEY_PLANT_ID";
    public static final String KEY_EQUIPMENT_ID = "KEY_EQUIPMENT_ID";
    public static final String KEY_UUID = "KEY_UUID";
    public static final String KEY_LONGTOOTH_ID = "KEY_LONGTOOTH_ID";
    public static final String KEY_PLANT_NAME = "KEY_PLANT_NAME";
    public static final String KEY_PLANT_BEAN = "KEY_PLANT_BEAN";
    private String mEquipmentId;
    private String mPlantId;
    private String mUUID;
    private String mLongToothId;
    private String mPlantName;
    private  EquipmentRspModel.ListBean mPlantBean;


    //显示的入口
    public static void show(Context context,EquipmentRspModel.ListBean plantBean) {
        Intent intent = new Intent(context, SecondSettingActivity.class);
//        intent.putExtra(KEY_EQUIPMENT_ID,equipmentId);
//        intent.putExtra(KEY_PLANT_ID,plantId);
//        intent.putExtra(KEY_UUID,uuid);
//        intent.putExtra(KEY_LONGTOOTH_ID,longToothId);
//        intent.putExtra(KEY_PLANT_NAME,plantName);
        intent.putExtra(KEY_PLANT_BEAN,plantBean);
        context.startActivity(intent);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_second_setting;
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
//        mEquipmentId = bundle.getString(KEY_EQUIPMENT_ID);
//        mPlantId = bundle.getString(KEY_PLANT_ID);
//        mUUID = bundle.getString(KEY_UUID);
//        mLongToothId = bundle.getString(KEY_LONGTOOTH_ID);
//        mPlantName = bundle.getString(KEY_PLANT_NAME);
        mPlantBean = (EquipmentRspModel.ListBean) bundle.getSerializable(KEY_PLANT_BEAN);
        return super.initArgs(bundle);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        mTitle.setText("设置");
    }

    @OnClick(R.id.img_back)
    void onBackClick() {
        finish();
    }

    @OnClick(R.id.tv_equipment_setting)
    void onEquipmentSettingClick() {
        // 往里面传 设备id
        EquipmentSettingActivity.show(this,mPlantBean);
    }

    @OnClick(R.id.tv_plant_setting)
    void onPlantSettingClick() {
        // 往里面传 植物id 和设备id
        PlantSettingActivity.show(this,mPlantBean);
    }

    @OnClick(R.id.tv_equipment_info)
    void onEquipmentInfoClick() {
        // 往里面传 设备id
        EquipmentInfoActivity.show(this,mPlantBean);
    }

}
