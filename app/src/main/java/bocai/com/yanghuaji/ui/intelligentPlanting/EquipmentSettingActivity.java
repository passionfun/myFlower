package bocai.com.yanghuaji.ui.intelligentPlanting;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.gson.Gson;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.Application;
import bocai.com.yanghuaji.base.presenter.PresenterActivity;
import bocai.com.yanghuaji.model.AutoModel;
import bocai.com.yanghuaji.model.AutoParaModel;
import bocai.com.yanghuaji.model.EquipmentInfoModel;
import bocai.com.yanghuaji.model.EquipmentRspModel;
import bocai.com.yanghuaji.model.EquipmentSetupModel;
import bocai.com.yanghuaji.model.EquipmentSetupModel_Table;
import bocai.com.yanghuaji.model.GroupRspModel;
import bocai.com.yanghuaji.model.LedSetRspModel;
import bocai.com.yanghuaji.model.MessageEvent;
import bocai.com.yanghuaji.presenter.intelligentPlanting.EquipmentSettingContract;
import bocai.com.yanghuaji.presenter.intelligentPlanting.EquipmentSettingPresenter;
import bocai.com.yanghuaji.ui.main.MainActivity;
import bocai.com.yanghuaji.util.ActivityUtil;
import bocai.com.yanghuaji.util.DateUtils;
import bocai.com.yanghuaji.util.UiTool;
import bocai.com.yanghuaji.util.persistence.Account;
import butterknife.BindView;
import butterknife.OnClick;
import xpod.longtooth.LongTooth;
import xpod.longtooth.LongToothAttachment;
import xpod.longtooth.LongToothServiceResponseHandler;
import xpod.longtooth.LongToothTunnel;

import static bocai.com.yanghuaji.ui.intelligentPlanting.HorizontalRecyclerFragment.HORIZONTALRECYLER_REFRESH;
import static bocai.com.yanghuaji.ui.intelligentPlanting.VeticalRecyclerFragment.VERTICAL_RECYLER_REFRESH;

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
    private int leastNoLedTime = 8;

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
        if (mPlantBean.getSeries().equals("WG101")) {
            checkbox.setChecked(false);
            checkbox.setEnabled(false);
        } else {
            checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (TextUtils.isEmpty(mPlantBean.getPid()) || TextUtils.isEmpty(mPlantBean.getLid())) {
                        checkbox.setChecked(false);
                        Application.showToast("植物设置不完善，不可开启节水模式");
                    }
                }
            });
        }
    }

    @Override
    protected void initData() {
        super.initData();
        mGroupName.setText(mPlantBean.getGroupName());
//        EquipmentSetupModel equipmentSetupModel = SQLite.select()
//                .from(EquipmentSetupModel.class)
//                .where(EquipmentSetupModel_Table.Id.eq(equipmentId))
//                .querySingle();
//        if (equipmentSetupModel != null) {
//            etEquipName.setText(equipmentSetupModel.getEquipName());
//            // 	节水模式  0关  1开
//            checkbox.setChecked(equipmentSetupModel.getWaterMode().equals("1"));
//            tvLightStart.setText(equipmentSetupModel.getLightStart());
//            tvBanStart.setText(equipmentSetupModel.getBanStart());
//            tvBanStop.setText(equipmentSetupModel.getBanStop());
////            mGroupName.setText(equipmentSetupModel.getGroupName());
//            lightStart = equipmentSetupModel.getLightStart();
//            banStart = equipmentSetupModel.getBanStart();
//            banStop = equipmentSetupModel.getBanStop();
//            if (!TextUtils.isEmpty(banStart)&&!TextUtils.isEmpty(banStop)){
//                mNoDistrubStart = DateUtils.getTimeSecond(banStart)+"";
//                mNoDistrubEnd = DateUtils.getTimeSecond(banStop)+"";
//            }
//        }
    }

    @OnClick(R.id.tv_group)
    void onGroupClick() {
        mPresenter.getGroupList(Account.getToken());
    }

    //补光开启是否设置成功
    public static boolean isSetLightOnSuccess = false;
    //免打扰是否设置成功
    public static boolean isSetNoDisturbSuccess = false;

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
        if (!TextUtils.isEmpty(banStop) && !TextUtils.isEmpty(banStart) && !TextUtils.isEmpty(lightStart)) {
            int noDisStrart = DateUtils.getTimeSecondNoZone(banStart);
            int noDisStop = DateUtils.getTimeSecondNoZone(banStop);
            int begin = DateUtils.getTimeSecondNoZone(lightStart);
            if (begin >= noDisStrart && begin <= noDisStop) {
                Application.showToast("开启时间不能再禁止时间之间");
                return;
            }
        }
        if ((TextUtils.isEmpty(mNoDistrubStart) && !TextUtils.isEmpty(mNoDistrubEnd)) ||
                (!TextUtils.isEmpty(mNoDistrubStart) && TextUtils.isEmpty(mNoDistrubEnd))) {
            Application.showToast("请选择正确的时间");
            return;
        }
        //设置免打扰时间
        if (!TextUtils.isEmpty(mNoDistrubStart) && !TextUtils.isEmpty(mNoDistrubEnd)) {
            if ((DateUtils.getTimeSecondNoZone(banStop) - DateUtils.getTimeSecondNoZone(banStart)) <= 0) {
                //禁止光照时间为隔天的情况
                mNoDistrubEnd = Integer.valueOf(mNoDistrubEnd) + 24 * 3600 + "";
                if (Integer.valueOf(mNoDistrubEnd) - Integer.valueOf(mNoDistrubStart) > leastNoLedTime * 3600) {
                    Application.showToast("禁止光照时间不能超过" + leastNoLedTime + "小时");
                    return;
                }
            }
            if ((DateUtils.getTimeSecondNoZone(banStop) - DateUtils.getTimeSecondNoZone(banStart)) > leastNoLedTime * 3600) {
                Application.showToast("禁止光照时间不能超过" + leastNoLedTime + "小时");
                return;
            }
        } else {
            isSetNoDisturbSuccess = true;
        }

        if (!TextUtils.isEmpty(mBengin)) {
            EquipmentSettingHelper.setLightOn(mBengin, mUUID, mLongToothId);
        } else {
            isSetLightOnSuccess = true;
        }
        if (!TextUtils.isEmpty(mNoDistrubStart) && !TextUtils.isEmpty(mNoDistrubEnd)) {
            EquipmentSettingHelper.setNoDisturb(mNoDistrubStart, mNoDistrubEnd, mUUID, mLongToothId);
        }
        showLoading();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {

                hideLoading();
                if (isSetNoDisturbSuccess && isSetLightOnSuccess) {
                    Run.onUiAsync(new Action() {
                        @Override
                        public void call() {
                            mPresenter.setupEquipment(map);
                        }
                    });
                } else {
                    Application.showToast("设置失败");
                }
            }
        }, 3500);

//        //如果没有设置过生命周期，则默认值为“0”
//        if (TextUtils.isEmpty(mPlantBean.getPid())||mPlantBean.getLid().equals("0")){
//            mPresenter.setupEquipment(map);
//        }else {
//            mPresenter.getAutoPara(equipmentId,mPlantBean.getPid(),mPlantBean.getLid());
//        }
    }

    @OnClick(R.id.tv_lightStart)
    void clickLightStart() {
        int lastHour = 6;
        int lastMinute = 0;
        if (!TextUtils.isEmpty(lightStart)) {
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
                        if ((hour - 8) < 0 || ((hour - 8) == 0 && minute == 0)) {
                            needHour = hour - 8 + 24;
                        } else {
                            needHour = hour - 8;
                        }
                        mBengin = (needHour * 60 * 60 + minute * 60) + "";
                        tvLightStart.setText(lightStart);
                    }
                }, lastHour, lastMinute, true);
        dialog.show();
    }

    @OnClick(R.id.tv_BanStart)
    void clickBanStart() {
        int lastHour = 6;
        int lastMinute = 0;
        if (!TextUtils.isEmpty(banStart)) {
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
                        if ((hour - 8) < 0 || ((hour - 8) == 0 && minute == 0)) {
                            needHour = hour - 8 + 24;
                        } else {
                            needHour = hour - 8;
                        }

                        mNoDistrubStart = (needHour * 60 * 60 + minute * 60) + "";
                        tvBanStart.setText(banStart);
                    }
                }, lastHour, lastMinute, true);
        dialog.show();
    }

    @OnClick(R.id.tv_BanStop)
    void clickBanStop() {
        int lastHour = 6;
        int lastMinute = 0;
        if (!TextUtils.isEmpty(banStop)) {
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
                        if ((hour - 8) < 0 || ((hour - 8) == 0 && minute == 0)) {
                            needHour = hour - 8 + 24;
                        } else {
                            needHour = hour - 8;
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
        EventBus.getDefault().post(new MessageEvent(MainActivity.MAIN_ACTIVITY_REFRESH));
        EventBus.getDefault().post(new MessageEvent(VERTICAL_RECYLER_REFRESH));
        EventBus.getDefault().post(new MessageEvent(HORIZONTALRECYLER_REFRESH));
        model.save();
        if (mPlantBean.getWaterMode().equals(checkbox.isChecked() ? "1" : "0")) {
            //节水模式状态没有改变，则不用再次下发智能参数
            finish();
        } else {
            //节水模式状态发生改变，重新下发智能参数
            mPresenter.getAutoPara(equipmentId, mPlantBean.getPid(), mPlantBean.getLid());
        }

//        finish();
    }


    @Override
    public void equipmentInfoSuccess(EquipmentInfoModel model) {

        etEquipName.setText(model.getEquipName());
        // 	节水模式  0关  1开
        checkbox.setChecked(model.getWaterMode().equals("1"));
        tvLightStart.setText(model.getLightStart());
        tvBanStart.setText(model.getBanStart());
        tvBanStop.setText(model.getBanStop());
        lightStart = model.getLightStart();
        banStart = model.getBanStart();
        banStop = model.getBanStop();
        if (!TextUtils.isEmpty(banStart) && !TextUtils.isEmpty(banStop)) {
            mNoDistrubStart = DateUtils.getTimeSecond(banStart) + "";
            mNoDistrubEnd = DateUtils.getTimeSecond(banStop) + "";
        }
        leastNoLedTime = Integer.valueOf(model.getBanTime());
    }

    final Gson gson = new Gson();
    private Timer timer = new Timer();

    @Override
    public void getAutoParaSuccess(List<AutoModel.ParaBean> paraBeans) {
        AutoParaModel model = new AutoParaModel("Auto", Integer.parseInt(mPlantBean.getPid()),
                Integer.parseInt(mUUID), paraBeans);
        String request = gson.toJson(model);
        LongTooth.request(mLongToothId, "longtooth", LongToothTunnel.LT_ARGUMENTS, request.getBytes(),
                0, request.getBytes().length, null, new MyLongToothServiceResponseHandler());
    }


    private class MyLongToothServiceResponseHandler implements LongToothServiceResponseHandler {
        private boolean isRspSuccess = false;

        //        private boolean isReturn = false;
        public MyLongToothServiceResponseHandler() {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (!isRspSuccess) {
                        hideLoading();
//                        isReturn = true;
                        Application.showToast("设备无响应，节水状态下发失败");
                        finish();
                    }
                }
            }, 3000);
        }

        @Override
        public void handleServiceResponse(LongToothTunnel longToothTunnel, String s, String s1, int i,
                                          byte[] bytes, LongToothAttachment longToothAttachment) {
            if (bytes == null) {
                return;
            }
            String jsonContent = new String(bytes);
            if (!jsonContent.contains("CODE")) {
                return;
            }
            Log.d("shc", "handleServiceResponse: " + jsonContent);
            LedSetRspModel plantStatusRspModel = gson.fromJson(jsonContent, LedSetRspModel.class);
            if (plantStatusRspModel.getCODE() == 0) {
//                    Application.showToast("智能参数设置成功");
                isRspSuccess = true;
                hideLoading();
                finish();
//                if (!isReturn)
//                    Run.onUiAsync(new Action() {
//                        @Override
//                        public void call() {
//                            mPresenter.setupEquipment(map);
//                        }
//                    });
            } else {
//                    Application.showToast("智能参数设置失败");
            }
        }
    }

    @Override
    public void getAutoParaFailed() {
        Application.showToast("获取智能参数失败，节水模式下发失败");
        finish();
    }

    @Override
    protected EquipmentSettingContract.Presenter initPresenter() {
        return new EquipmentSettingPresenter(this);
    }
}
