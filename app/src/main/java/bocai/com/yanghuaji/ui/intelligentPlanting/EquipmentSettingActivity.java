package bocai.com.yanghuaji.ui.intelligentPlanting;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.presenter.PresenterActivity;
import bocai.com.yanghuaji.model.EquipmentRspModel;
import bocai.com.yanghuaji.model.EquipmentSetupModel;
import bocai.com.yanghuaji.model.EquipmentSetupModel_Table;
import bocai.com.yanghuaji.model.GroupRspModel;
import bocai.com.yanghuaji.presenter.intelligentPlanting.EquipmentSettingContract;
import bocai.com.yanghuaji.presenter.intelligentPlanting.EquipmentSettingPresenter;
import bocai.com.yanghuaji.util.ActivityUtil;
import bocai.com.yanghuaji.util.DateUtils;
import bocai.com.yanghuaji.util.persistence.Account;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者 yuanfei on 2017/11/15.
 * 邮箱 yuanfei221@126.com
 */

public class EquipmentSettingActivity extends PresenterActivity<EquipmentSettingContract.Presenter>
        implements EquipmentSettingContract.View {
    @BindView(R.id.scroll_root)
    ScrollView mRoot;

    @BindView(R.id.tv_title)
    TextView mTitle;

    @BindView(R.id.img_back)
    ImageView mImgBack;

    @BindView(R.id.tv_group)
    TextView mGroupName;
    @BindView(R.id.img_right)
    ImageView imgRight;
    @BindView(R.id.img_right_second)
    ImageView imgRightSecond;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_equipName)
    EditText etEquipName;
    @BindView(R.id.checkbox)
    CheckBox checkbox;
    @BindView(R.id.tv_lightStart)
    TextView tvLightStart;
    @BindView(R.id.tv_BanStart)
    TextView tvBanStart;
    @BindView(R.id.tv_BanStop)
    TextView tvBanStop;

    public static final String KEY_PLANT_BEAN = "KEY_PLANT_BEAN";
    private String mName, groupId, lightStart, banStart, banStop, equipmentId;
    //补光开始时间一天中的时间点，以秒为单位 比如下午8点30分 传的值为：20*60*60+30*60 =73800。
    private String mBengin;
    private Map<String, String> map = new HashMap<>();
    private String mUUID;
    private String mLongToothId;
    private String mNoDistrubStart,mNoDistrubEnd;
    private  EquipmentRspModel.ListBean mPlantBean;

    //显示的入口
    public static void show(Context context,EquipmentRspModel.ListBean plantBean) {
        Intent intent = new Intent(context, EquipmentSettingActivity.class);
        intent.putExtra(KEY_PLANT_BEAN,plantBean);
        context.startActivity(intent);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_equipment_setting;
    }

    @OnClick(R.id.img_back)
    void onBackClick() {
        finish();
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        mPlantBean = (EquipmentRspModel.ListBean) bundle.getSerializable(KEY_PLANT_BEAN);
        equipmentId = mPlantBean.getId();
        mUUID = mPlantBean.getPSIGN();
        mLongToothId = mPlantBean.getLTID();
        return super.initArgs(bundle);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        mTitle.setText("种植机设置");
        etEquipName.setText(mPlantBean.getEquipName());
        tvRight.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initData() {
        super.initData();
        EquipmentSetupModel equipmentSetupModel = SQLite.select()
                .from(EquipmentSetupModel.class)
                .where(EquipmentSetupModel_Table.Id.eq(equipmentId))
                .querySingle();
        if (equipmentSetupModel != null) {
            etEquipName.setText(equipmentSetupModel.getEquipName());
            // 	节水模式  0关  1开
            checkbox.setChecked(equipmentSetupModel.getWaterMode().equals("1"));
            tvLightStart.setText(equipmentSetupModel.getLightStart());
            tvBanStart.setText(equipmentSetupModel.getBanStart());
            tvBanStop.setText(equipmentSetupModel.getBanStop());
            mGroupName.setText(equipmentSetupModel.getGroupName());
            mBengin = equipmentSetupModel.getLightStart();
            banStart = equipmentSetupModel.getBanStart();
            banStop = equipmentSetupModel.getBanStop();
        }
    }

    @OnClick(R.id.tv_group)
    void onGroupClick() {
        mPresenter.getGroupList(Account.getToken());
    }

    @OnClick(R.id.tv_right)
    void onSave() {
        if (checkbox.isChecked()) {
            // 	节水模式  0关  1开
            map.put("WaterMode", "1");
        } else {
            map.put("WaterMode", "0");
        }
        map.put("Token", Account.getToken());
        map.put("EquipName", etEquipName.getText().toString().trim());
        map.put("BanStart", banStart==null?"":banStart);
        map.put("BanStop", banStop ==null?"":banStop);
        map.put("GroupId", groupId ==null?"":groupId);//分组id
        map.put("LightStart", lightStart ==null?"":lightStart);
        map.put("Id", equipmentId );// 	设备id
        //设置补光开启时间
        if (!TextUtils.isEmpty(mBengin)){
            EquipmentSettingHelper.setLightOn(mBengin,mUUID,mLongToothId);
        }

        if (!TextUtils.isEmpty(mNoDistrubStart)&&!TextUtils.isEmpty(mNoDistrubEnd)){
            EquipmentSettingHelper.setNoDisturb(mNoDistrubStart,mNoDistrubEnd,mUUID,mLongToothId);
        }
        mPresenter.setupEquipment(map);
    }

    @OnClick(R.id.tv_lightStart)
    void clickLightStart() {
        TimePickerDialog dialog = new TimePickerDialog(EquipmentSettingActivity.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        lightStart = DateUtils.formatTime(hour) + ":" + DateUtils.formatTime(minute);
                        mBengin = (hour*60*60+minute*60)+"";
                        tvLightStart.setText(lightStart);
                    }
                }, 6, 0, true);
        dialog.show();
//        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setText("确定");
//        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setText("取消");
    }

    @OnClick(R.id.tv_BanStart)
    void clickBanStart() {
        TimePickerDialog dialog = new TimePickerDialog(EquipmentSettingActivity.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        banStart = DateUtils.formatTime(hour) + ":" + DateUtils.formatTime(minute);
                        mNoDistrubStart = (hour*60*60+minute*60)+"";
                        tvBanStart.setText(banStart);
                    }
                }, 6, 0, true);
        dialog.show();
    }

    @OnClick(R.id.tv_BanStop)
    void clickBanStop() {
        TimePickerDialog dialog = new TimePickerDialog(EquipmentSettingActivity.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        banStop = DateUtils.formatTime(hour) + ":" + DateUtils.formatTime(minute);
                        mNoDistrubEnd = (hour*60*60+minute*60)+"";
                        tvBanStop.setText(banStop);
                    }
                }, 6, 0, true);
        dialog.show();
    }


    @Override
    public void getGroupListSuccess(List<GroupRspModel.ListBean> groupCards) {
        GroupListPopupWindow popupWindow = new GroupListPopupWindow(this);
        ActivityUtil.setBackgroundAlpha(this, 0.19f);
        popupWindow.addData(groupCards);
        popupWindow.showAtLocation(mRoot, Gravity.CENTER, 0, 0);
        popupWindow.setOnSelectListener(new GroupListPopupWindow.SelectListener() {
            @Override
            public void selected(GroupRspModel.ListBean card) {
                mName = card.getGroupName();
                groupId = card.getId();
                mGroupName.setText(mName);
            }
        });
    }

    @Override
    public void setupEquipmentSuccess(EquipmentSetupModel model) {
        model.save();
        finish();
    }

    @Override
    protected EquipmentSettingContract.Presenter initPresenter() {
        return new EquipmentSettingPresenter(this);
    }
}
