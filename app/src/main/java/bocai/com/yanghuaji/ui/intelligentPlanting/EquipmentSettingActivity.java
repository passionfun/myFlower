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
import bocai.com.yanghuaji.base.Application;
import bocai.com.yanghuaji.base.presenter.PresenterActivity;
import bocai.com.yanghuaji.model.EquipmentInfoModel;
import bocai.com.yanghuaji.model.EquipmentRspModel;
import bocai.com.yanghuaji.model.EquipmentSetupModel;
import bocai.com.yanghuaji.model.EquipmentSetupModel_Table;
import bocai.com.yanghuaji.model.GroupRspModel;
import bocai.com.yanghuaji.presenter.intelligentPlanting.EquipmentSettingContract;
import bocai.com.yanghuaji.presenter.intelligentPlanting.EquipmentSettingPresenter;
import bocai.com.yanghuaji.util.ActivityUtil;
import bocai.com.yanghuaji.util.DateUtils;
import bocai.com.yanghuaji.util.UiTool;
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
    private String mNoDistrubStart, mNoDistrubEnd;
    private EquipmentRspModel.ListBean mPlantBean;
    //禁止光照最长时间
    private int leastNoLedTime=8;

    //显示的入口
    public static void show(Context context, EquipmentRspModel.ListBean plantBean) {
        Intent intent = new Intent(context, EquipmentSettingActivity.class);
        intent.putExtra(KEY_PLANT_BEAN, plantBean);
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
        UiTool.setBlod(mTitle);
        mTitle.setText("种植机设置");
        etEquipName.setText(mPlantBean.getEquipName());
        tvRight.setVisibility(View.VISIBLE);
        map.put("Token", Account.getToken());
        map.put("Id", mPlantBean.getId());
        mPresenter.equipmentInfo(map);
        if (mPlantBean.getSeries().equals("WG101")){
            checkbox.setChecked(false);
            checkbox.setEnabled(false);
        }
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
            lightStart = equipmentSetupModel.getLightStart();
            banStart = equipmentSetupModel.getBanStart();
            banStop = equipmentSetupModel.getBanStop();
            if (!TextUtils.isEmpty(banStart)&&!TextUtils.isEmpty(banStop)){
                mNoDistrubStart = DateUtils.getTimeSecond(banStart)+"";
                mNoDistrubEnd = DateUtils.getTimeSecond(banStop)+"";
            }
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
        map.put("BanStart", banStart == null ? "" : banStart);
        map.put("BanStop", banStop == null ? "" : banStop);
        map.put("GroupId", groupId == null ? "" : groupId);//分组id
        map.put("LightStart", lightStart == null ? "" : lightStart);
        map.put("Id", equipmentId);// 	设备id
        //设置补光开启时间
        if (!TextUtils.isEmpty(mBengin)) {
            if (!TextUtils.isEmpty(banStop) && !TextUtils.isEmpty(banStart)&& !TextUtils.isEmpty(lightStart)){
                int noDisStrart = DateUtils.getTimeSecondNoZone(banStart);
                int noDisStop = DateUtils.getTimeSecondNoZone(banStop);
                int begin = DateUtils.getTimeSecondNoZone(lightStart);
                if (begin>=noDisStrart&&begin<=noDisStop){
                    Application.showToast("开启时间不能再禁止时间之间");
                    return;
                }
            }
            EquipmentSettingHelper.setLightOn(mBengin, mUUID, mLongToothId);
        }
        //设置免打扰时间
        if (!TextUtils.isEmpty(mNoDistrubStart) && !TextUtils.isEmpty(mNoDistrubEnd)) {
            if ((DateUtils.getTimeSecondNoZone(banStop) -DateUtils.getTimeSecondNoZone(banStart) )<=0){
                Application.showToast("请选择正确的时间");
                return;
            }
            if ((DateUtils.getTimeSecondNoZone(banStop) -DateUtils.getTimeSecondNoZone(banStart) )>leastNoLedTime*3600){
                Application.showToast("禁止光照时间不能超过"+leastNoLedTime+"小时");
                return;
            }else {
                EquipmentSettingHelper.setNoDisturb(mNoDistrubStart, mNoDistrubEnd, mUUID, mLongToothId);
            }
        }else if ((TextUtils.isEmpty(mNoDistrubStart) && !TextUtils.isEmpty(mNoDistrubEnd))||(!TextUtils.isEmpty(mNoDistrubStart) && TextUtils.isEmpty(mNoDistrubEnd))){
            Application.showToast("请选择正确的时间");
            return;
        }
        mPresenter.setupEquipment(map);
    }

    @OnClick(R.id.tv_lightStart)
    void clickLightStart() {
        int lastHour =6;
        int lastMinute = 0;
        if (!TextUtils.isEmpty(lightStart)){
            int[] hourAndMinute = DateUtils.getHourAndMinute(lightStart);
            lastHour = hourAndMinute[0];
            lastMinute = hourAndMinute[1];
        }
        TimePickerDialog dialog = new TimePickerDialog(EquipmentSettingActivity.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        lightStart = DateUtils.formatTime(hour) + ":" + DateUtils.formatTime(minute);
                        //转换为格林尼治时间
                        int needHour;
                        if ((hour-8)<0||((hour-8)==0&&minute==0)){
                            needHour = hour-8+24;
                        }else {
                            needHour = hour-8;
                        }
                        mBengin = (needHour * 60 * 60 + minute * 60) + "";
                        tvLightStart.setText(lightStart);
                    }
                }, lastHour, lastMinute, true);
        dialog.show();
    }

    @OnClick(R.id.tv_BanStart)
    void clickBanStart() {
        int lastHour =6;
        int lastMinute = 0;
        if (!TextUtils.isEmpty(banStart)){
            int[] hourAndMinute = DateUtils.getHourAndMinute(banStart);
            lastHour = hourAndMinute[0];
            lastMinute = hourAndMinute[1];
        }
        TimePickerDialog dialog = new TimePickerDialog(EquipmentSettingActivity.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        banStart = DateUtils.formatTime(hour) + ":" + DateUtils.formatTime(minute);
                        int needHour;
                        if ((hour-8)<0||((hour-8)==0&&minute==0)){
                            needHour = hour-8+24;
                        }else {
                            needHour = hour-8;
                        }

                        mNoDistrubStart = (needHour * 60 * 60 + minute * 60) + "";
                        tvBanStart.setText(banStart);
                    }
                }, lastHour, lastMinute, true);
        dialog.show();
    }

    @OnClick(R.id.tv_BanStop)
    void clickBanStop() {
        int lastHour =6;
        int lastMinute = 0;
        if (!TextUtils.isEmpty(banStop)){
            int[] hourAndMinute = DateUtils.getHourAndMinute(banStop);
            lastHour = hourAndMinute[0];
            lastMinute = hourAndMinute[1];
        }
        TimePickerDialog dialog = new TimePickerDialog(EquipmentSettingActivity.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        banStop = DateUtils.formatTime(hour) + ":" + DateUtils.formatTime(minute);
                        int needHour;
                        if ((hour-8)<0||((hour-8)==0&&minute==0)){
                            needHour = hour-8+24;
                        }else {
                            needHour = hour-8;
                        }
                        mNoDistrubEnd = (needHour * 60 * 60 + minute * 60) + "";
                        tvBanStop.setText(banStop);
                    }
                }, lastHour, lastMinute, true);
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
    public void equipmentInfoSuccess(EquipmentInfoModel model) {
        leastNoLedTime = Integer.valueOf(model.getBanTime());
    }

    @Override
    protected EquipmentSettingContract.Presenter initPresenter() {
        return new EquipmentSettingPresenter(this);
    }
}
