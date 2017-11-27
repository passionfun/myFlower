package bocai.com.yanghuaji.ui.intelligentPlanting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.Application;
import bocai.com.yanghuaji.base.presenter.PresenterActivity;
import bocai.com.yanghuaji.model.LifeCycleModel;
import bocai.com.yanghuaji.model.PlantSettingModel;
import bocai.com.yanghuaji.presenter.intelligentPlanting.PlantSettingContract;
import bocai.com.yanghuaji.presenter.intelligentPlanting.PlantSettingPresenter;
import bocai.com.yanghuaji.util.ActivityUtil;
import bocai.com.yanghuaji.util.persistence.Account;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者 yuanfei on 2017/11/21.
 * 邮箱 yuanfei221@126.com
 */

public class PlantSettingActivity extends PresenterActivity<PlantSettingContract.Presenter>
        implements PlantSettingContract.View {
    public static final String PID="pid";
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

    @BindView(R.id.tv_plant_method)
    TextView tvPlantMethod;
    @BindView(R.id.et_input_password)
    EditText etInputPassword;
    @BindView(R.id.tv_plant_cycle)
    TextView tvPlantCycle;
    @BindView(R.id.ll_root)
    LinearLayout llRoot;

    private Map<String, String> map = new HashMap<>();
    private String plantMode, pMid, plantName, pId, lifeCycle, lid, id;

    //显示的入口
    public static void show(Context context,String pid,String equipmentId) {
        Intent intent=new Intent(context, PlantSettingActivity.class);
        intent.putExtra(PID,pid);
        intent.putExtra(EQUIPMENTID,equipmentId);
        context.startActivity(intent);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_plant_setting;
    }

    @OnClick(R.id.img_back)
    void onBackClick() {
        finish();
    }

    @OnClick(R.id.tv_plant_method)
    void onMethod() {
    }

    @OnClick(R.id.tv_plant_cycle)
    void onCycle() {
    }

    @OnClick(R.id.tv_right)
    void onSetupPlant() {
        plantName=etInputPassword.getText().toString().trim();
        map.put("Token", Account.getToken());
        map.put("PlantMode", plantMode);
        map.put("PMid", pMid);
        map.put("PlantName", plantName);
        map.put("Pid", pId);
        map.put("LifeCycle", lifeCycle);
        map.put("Lid", lid);
        map.put("Id", id);

        mPresenter.setupPlant(map);
    }


    @Override
    protected void initWidget() {
        super.initWidget();
        mTitle.setText("植物设置");
        tvRight.setVisibility(View.VISIBLE);
        pId=getIntent().getStringExtra(PID);
        id=getIntent().getStringExtra(EQUIPMENTID);
    }

    @Override
    public void setupPlantSuccess(PlantSettingModel model) {
        finish();
    }

    @Override
    public void plantModeSuccess(LifeCycleModel model) {
        if (model != null && model.getList().size() > 0) {
            ChoosePop popupWindow = new ChoosePop(this);
            ActivityUtil.setBackgroundAlpha(this, 0.19f);
            popupWindow.addData(model.getList());
            popupWindow.showAtLocation(llRoot, Gravity.CENTER, 0, 0);
            popupWindow.setOnSelectListener(new ChoosePop.SelectListener() {
                @Override
                public void selected(LifeCycleModel.ListBean listBean) {
                    plantMode = listBean.getId();
                    plantName = listBean.getTitle();
                }
            });
        } else {
            Application.showToast("暂无数据");
        }
    }

    @Override
    public void lifeCycleSuccess(LifeCycleModel model) {
        if (model != null && model.getList().size() > 0) {
            ChoosePop popupWindow = new ChoosePop(this);
            ActivityUtil.setBackgroundAlpha(this, 0.19f);
            popupWindow.addData(model.getList());
            popupWindow.showAtLocation(llRoot, Gravity.CENTER, 0, 0);
            popupWindow.setOnSelectListener(new ChoosePop.SelectListener() {
                @Override
                public void selected(LifeCycleModel.ListBean listBean) {
                    lid= listBean.getId();
                    lifeCycle = listBean.getTitle();
                }
            });
        } else {
            Application.showToast("暂无数据");
        }
    }

    @Override
    protected PlantSettingContract.Presenter initPresenter() {
        return new PlantSettingPresenter(this);
    }
}
