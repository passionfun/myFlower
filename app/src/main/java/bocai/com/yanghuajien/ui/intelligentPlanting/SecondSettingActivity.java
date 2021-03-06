package bocai.com.yanghuajien.ui.intelligentPlanting;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import org.greenrobot.eventbus.EventBus;

import bocai.com.yanghuajien.R;
import bocai.com.yanghuajien.base.Application;
import bocai.com.yanghuajien.base.presenter.PresenterActivity;
import bocai.com.yanghuajien.model.CheckboxStatusModel;
import bocai.com.yanghuajien.model.EquipmentRspModel;
import bocai.com.yanghuajien.model.EquipmentSetupModel;
import bocai.com.yanghuajien.model.EquipmentSetupModel_Table;
import bocai.com.yanghuajien.model.MessageEvent;
import bocai.com.yanghuajien.model.PlantSettingModel;
import bocai.com.yanghuajien.model.PlantSettingModel_Table;
import bocai.com.yanghuajien.presenter.intelligentPlanting.SecondSettingContract;
import bocai.com.yanghuajien.presenter.intelligentPlanting.SecondSettingPresenter;
import bocai.com.yanghuajien.util.UiTool;
import bocai.com.yanghuajien.util.persistence.Account;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者 yuanfei on 2017/11/15.
 * 邮箱 yuanfei221@126.com
 */

public class SecondSettingActivity extends PresenterActivity<SecondSettingContract.Presenter>
        implements SecondSettingContract.View {

    @BindView(R.id.tv_title)
    TextView mTitle;

    @BindView(R.id.img_back)
    ImageView mImgBack;

    @BindView(R.id.cb_push)
    CheckBox mCbPush;

    public static final String DATA_DELETE_SUCCESS = "DATA_DELETE_SUCCESS";
    public static final String KEY_PLANT_BEAN = "KEY_PLANT_BEAN";
    private EquipmentRspModel.ListBean mPlantBean;


    //显示的入口
    public static void show(Context context, EquipmentRspModel.ListBean plantBean) {
        Intent intent = new Intent(context, SecondSettingActivity.class);
        intent.putExtra(KEY_PLANT_BEAN, plantBean);
        context.startActivity(intent);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_second_setting;
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        mPlantBean = (EquipmentRspModel.ListBean) bundle.getSerializable(KEY_PLANT_BEAN);
        return super.initArgs(bundle);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        UiTool.setBlod(mTitle);
        mTitle.setText(Application.getStringText(R.string.setting));
        mCbPush.setChecked(mPlantBean.getPushStatus().equals("1"));
        mCbPush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCbPush.isChecked()) {
                    //推送开
                    //type:类型区分   1光照状态   2消息推送状态
                    //status：状态  0关  1开
                    mPresenter.setCheckBox(Account.getToken(), "2", "1", mPlantBean.getId());
                } else {
                    //推送关
                    mPresenter.setCheckBox(Account.getToken(), "2", "0", mPlantBean.getId());
                }
            }
        });
    }

    @OnClick(R.id.img_back)
    void onBackClick() {
        finish();
    }

    @OnClick(R.id.tv_equipment_setting)
    void onEquipmentSettingClick() {
        EquipmentSettingActivity.show(this, mPlantBean);
        finish();
    }

    @OnClick(R.id.tv_plant_setting)
    void onPlantSettingClick() {
        PlantSettingActivity.show(this, mPlantBean);
//        finish();
    }

    @OnClick(R.id.tv_equipment_info)
    void onEquipmentInfoClick() {
        EquipmentInfoActivity.show(this, mPlantBean);
        finish();
    }

    @OnClick(R.id.tv_clear_data)
    void onClearDataClick() {
        // 清除数据
        AlertDialog.Builder deleteDialog = new AlertDialog.Builder(this);
        deleteDialog.setTitle(Application.getStringText(R.string.ensure_clear));
        deleteDialog.setPositiveButton(getResources().getString(R.string.ensure), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mPresenter.clearData(Account.getToken(), mPlantBean.getId());
            }
        });
        deleteDialog.setNegativeButton(getResources().getString(R.string.cancel), null);
        deleteDialog.show();

    }

    @Override
    public void clearDataSuccess() {
        EquipmentSetupModel model = SQLite.select()
                .from(EquipmentSetupModel.class)
                .where(EquipmentSetupModel_Table.Id.eq(mPlantBean.getId()))
                .querySingle();
        if (model!=null){
            model.delete();
        }
        PlantSettingModel plantModel = SQLite.select()
                .from(PlantSettingModel.class)
                .where(PlantSettingModel_Table.Id.eq(mPlantBean.getId()))
                .querySingle();
        if (plantModel!= null){
            plantModel.delete();
        }
        EventBus.getDefault().post(new MessageEvent(HorizontalRecyclerFragment.HORIZONTALRECYLER_REFRESH));
        EventBus.getDefault().post(new MessageEvent(VeticalRecyclerFragment.VERTICAL_RECYLER_REFRESH));
        finish();
    }


    @Override
    public void setCheckBoxSuccess(CheckboxStatusModel model) {
        EventBus.getDefault().post(new MessageEvent(HorizontalRecyclerFragment.HORIZONTALRECYLER_REFRESH));
        EventBus.getDefault().post(new MessageEvent(VeticalRecyclerFragment.VERTICAL_RECYLER_REFRESH));
    }

    @Override
    protected SecondSettingContract.Presenter initPresenter() {
        return new SecondSettingPresenter(this);
    }
}
