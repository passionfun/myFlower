package bocai.com.yanghuaji.ui.intelligentPlanting;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.presenter.PresenterActivity;
import bocai.com.yanghuaji.model.EquipmentSetupModel;
import bocai.com.yanghuaji.model.GroupRspModel;
import bocai.com.yanghuaji.presenter.intelligentPlanting.EquipmentSettingContract;
import bocai.com.yanghuaji.presenter.intelligentPlanting.EquipmentSettingPresenter;
import bocai.com.yanghuaji.util.ActivityUtil;
import bocai.com.yanghuaji.util.persistence.Account;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者 yuanfei on 2017/11/15.
 * 邮箱 yuanfei221@126.com
 */

public class EquipmentSettingActivity extends PresenterActivity<EquipmentSettingContract.Presenter>
        implements EquipmentSettingContract.View {
    public static final String EQUIPMENTID="equipmentId";
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

    private String mName, groupId,lightStart,banStart,banStop,equipmentId;
    private Map<String, String> map = new HashMap<>();

    //显示的入口
    public static void show(Context context,String equipmentId) {
        Intent intent=new Intent(context, EquipmentSettingActivity.class);
        intent.putExtra(EQUIPMENTID,equipmentId);
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
    protected void initWidget() {
        super.initWidget();
        mTitle.setText("种植机设置");
        tvRight.setVisibility(View.VISIBLE);
        equipmentId=getIntent().getStringExtra(EQUIPMENTID);
    }


    @OnClick(R.id.tv_group)
    void onGroupClick() {
        mPresenter.getGroupList(Account.getToken());
    }

    @OnClick(R.id.tv_right)
    void onSave(){
        if (checkbox.isChecked()){
            // 	节水模式  0关  1开
            map.put("WaterMode", "1");
        }else {
            map.put("WaterMode", "0");
        }
        map.put("Token", Account.getToken());
        if (TextUtils.isEmpty(etEquipName.getText().toString().trim())){
            Toast.makeText(EquipmentSettingActivity.this,"请输入设备名字",Toast.LENGTH_SHORT).show();
            return;
        }else {
            map.put("EquipName", etEquipName.getText().toString().trim());
        }

        if (TextUtils.isEmpty(banStart)){
            Toast.makeText(EquipmentSettingActivity.this,"请输入禁止光照开始时间",Toast.LENGTH_SHORT).show();
            return;
        }else {
            map.put("BanStart", banStart);
        }
        if (TextUtils.isEmpty(banStop)){
            Toast.makeText(EquipmentSettingActivity.this,"请输入禁止光照结束时间",Toast.LENGTH_SHORT).show();
            return;
        }else {
            map.put("BanStop",banStop );
        }
        if (TextUtils.isEmpty(groupId)){
            Toast.makeText(EquipmentSettingActivity.this,"请输入分组",Toast.LENGTH_SHORT).show();
            return;
        }else {
            map.put("GroupId", groupId);//分组id
        }
        if (TextUtils.isEmpty(lightStart)){
            Toast.makeText(EquipmentSettingActivity.this,"请输入光照开始时间",Toast.LENGTH_SHORT).show();
            return;
        }else {
            map.put("LightStart", lightStart);
        }

        map.put("Id", equipmentId);// 	设备id

        mPresenter.setupEquipment(map);
    }

    @OnClick(R.id.tv_lightStart)
    void clickLightStart(){
        TimePickerDialog dialog = new TimePickerDialog(EquipmentSettingActivity.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        lightStart=hour+":"+minute;
                        tvLightStart.setText(lightStart);
                    }
                }, 6, 0, true);
        dialog.show();
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setText("确定");
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setText("取消");
    }

    @OnClick(R.id.tv_BanStart)
    void clickBanStart(){
        TimePickerDialog dialog = new TimePickerDialog(EquipmentSettingActivity.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        banStart=hour+":"+minute;
                        tvBanStart.setText(banStart);
                    }
                }, 6, 0, true);
        dialog.show();
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setText("确定");
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setText("取消");
    }

    @OnClick(R.id.tv_BanStop)
    void clickBanStop(){
        TimePickerDialog dialog = new TimePickerDialog(EquipmentSettingActivity.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        banStop=hour+":"+minute;
                        tvBanStop.setText(banStop);
                    }
                }, 6, 0, true);
        dialog.show();
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setText("确定");
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setText("取消");
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
        finish();
    }

    @Override
    protected EquipmentSettingContract.Presenter initPresenter() {
        return new EquipmentSettingPresenter(this);
    }
}
