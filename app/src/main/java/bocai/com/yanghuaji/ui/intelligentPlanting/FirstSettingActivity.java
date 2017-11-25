package bocai.com.yanghuaji.ui.intelligentPlanting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.presenter.PresenterActivity;
import bocai.com.yanghuaji.presenter.intelligentPlanting.FirstSettingContract;
import bocai.com.yanghuaji.presenter.intelligentPlanting.FirstSettingPresenter;
import bocai.com.yanghuaji.util.persistence.Account;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者 yuanfei on 2017/11/14.
 * 邮箱 yuanfei221@126.com
 */

public class FirstSettingActivity extends PresenterActivity<FirstSettingContract.Presenter>
        implements FirstSettingContract.View {
    @BindView(R.id.img_back)
    ImageView mImgBack;

    @BindView(R.id.et_input_equipment_name)
    EditText mEtEquipmentName;

    @BindView(R.id.et_input_plant_name)
    EditText mEtPlantName;


    public static String KEY_EQUIPMENT_NAME = "KEY_EQUIPMENT_NAME";
    public static String KEY_EQUIPMENT_ID = "KEY_EQUIPMENT_ID";
    public static String KEY_PLANT_NAME = "KEY_PLANT_NAME";
    public static String KEY_PLANT_ID = "KEY_PLANT_ID";
    private String mEquipmentName;
    private String mEquipmentId;
    private String mPlantName;
    private String mPlantId;

    //显示的入口
    public static void show(Context context,String equipmentName, String equipmentId) {
        Intent intent = new Intent(context, FirstSettingActivity.class);
        intent.putExtra(KEY_EQUIPMENT_NAME, equipmentName);
        intent.putExtra(KEY_EQUIPMENT_ID, equipmentId);
        context.startActivity(intent);
    }

    //显示的入口
    public static void show(Context context,String equipmentName, String equipmentId,String plantName,String plantId) {
        Intent intent = new Intent(context, FirstSettingActivity.class);
        intent.putExtra(KEY_EQUIPMENT_NAME, equipmentName);
        intent.putExtra(KEY_EQUIPMENT_ID, equipmentId);
        intent.putExtra(KEY_PLANT_NAME, plantName);
        intent.putExtra(KEY_PLANT_ID, plantId);
        context.startActivity(intent);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_first_setting;
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        mEquipmentId = bundle.getString(KEY_EQUIPMENT_ID);
        mEquipmentName = bundle.getString(KEY_EQUIPMENT_NAME);
        mPlantName = bundle.getString(KEY_PLANT_NAME);
        mPlantId = bundle.getString(KEY_PLANT_ID);
        return super.initArgs(bundle);
    }

    @Override
    protected void initData() {
        super.initData();
        mEtEquipmentName.setText(mEquipmentName);
        mEtPlantName.setText(mPlantName);
    }

    @OnClick(R.id.img_back)
    void onBackClick() {
        finish();
    }

    @OnClick(R.id.tv_save)
    void onSaveClick() {
        //第一次种植设置
        String token = Account.getToken();
        mPresenter.setup(token,mEquipmentName,mPlantName,mPlantId,mEquipmentId);
    }

    @Override
    public void setupSuccess() {

    }

    @Override
    protected FirstSettingContract.Presenter initPresenter() {
        return new FirstSettingPresenter(this);
    }
}
