package bocai.com.yanghuaji.ui.intelligentPlanting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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


    public static String KEY_EQUIPMENT_NAME = "KEY_EQUIPMENT_NAME";
    public static String KEY_EQUIPMENT_ID = "KEY_EQUIPMENT_ID";
    private String mEquipmentName;
    private String mEquipmentId;

    //显示的入口
    public static void show(Context context,String equipmentName, String equipmentId) {
        Intent intent = new Intent(context, FirstSettingActivity.class);
        intent.putExtra(KEY_EQUIPMENT_NAME, equipmentName);
        intent.putExtra(KEY_EQUIPMENT_ID, equipmentId);
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
        return super.initArgs(bundle);
    }

    @OnClick(R.id.img_back)
    void onBackClick() {
        finish();
    }

    @OnClick(R.id.tv_save)
    void onSaveClick() {
        //第一次种植设置
        String token = Account.getToken();
        mPresenter.setup(token,mEquipmentName,"","",mEquipmentId);
    }

    @Override
    public void setupSuccess() {

    }

    @Override
    protected FirstSettingContract.Presenter initPresenter() {
        return new FirstSettingPresenter(this);
    }
}
