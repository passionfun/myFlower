package bocai.com.yanghuaji.ui.intelligentPlanting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.presenter.PresenterActivity;
import bocai.com.yanghuaji.model.EquipmentInfoModel;
import bocai.com.yanghuaji.presenter.intelligentPlanting.EquipmentInfoContract;
import bocai.com.yanghuaji.presenter.intelligentPlanting.EquipmentInfoPresenter;
import bocai.com.yanghuaji.util.persistence.Account;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者 yuanfei on 2017/11/21.
 * 邮箱 yuanfei221@126.com
 */

public class EquipmentInfoActivity extends PresenterActivity<EquipmentInfoContract.Presenter>
        implements EquipmentInfoContract.View {

    public static final String EQUIPMENTID="equipmentId";

    @BindView(R.id.tv_title)
    TextView mTitle;

    @BindView(R.id.img_back)
    ImageView mImgBack;
    @BindView(R.id.img_right)
    ImageView imgRight;
    @BindView(R.id.img_right_second)
    ImageView imgRightSecond;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_mac_address)
    TextView tvMacAddress;
    @BindView(R.id.tv_serial_number)
    TextView tvSerialNumber;
    @BindView(R.id.tv_equipment_type)
    TextView tvEquipmentType;
    @BindView(R.id.tv_version)
    TextView tvVersion;
    @BindView(R.id.img_upgrading)
    ImageView imgUpgrading;
    @BindView(R.id.img_upgrade)
    ImageView imgUpgrade;

    private Map<String, String> map = new HashMap<>();
    private String id;

    //显示的入口
    public static void show(Context context,String equipmentId) {
        Intent intent=new Intent(context, EquipmentInfoActivity.class);
        intent.putExtra(EQUIPMENTID,equipmentId);
        context.startActivity(intent);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_equipment_info;
    }

    @OnClick(R.id.img_back)
    void onBackClick() {
        finish();
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        mTitle.setText("设备信息");
        id=getIntent().getStringExtra(EQUIPMENTID);
        map.put("Token", Account.getToken());
        map.put("Id", id);
        mPresenter.equipmentInfo(map);
    }

    @Override
    public void equipmentInfoSuccess(EquipmentInfoModel model) {
        tvMacAddress.setText(model.getMac());
        tvSerialNumber.setText(model.getSerialNum());
        tvEquipmentType.setText(model.getEquipName());
        tvVersion.setText(model.getVersion());
    }

    @Override
    protected EquipmentInfoContract.Presenter initPresenter() {
        return new EquipmentInfoPresenter(this);
    }
}
