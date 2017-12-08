package bocai.com.yanghuaji.ui.intelligentPlanting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.Application;
import bocai.com.yanghuaji.base.presenter.PresenterActivity;
import bocai.com.yanghuaji.model.AutoModel;
import bocai.com.yanghuaji.model.AutoParaModel;
import bocai.com.yanghuaji.model.EquipmentRspModel;
import bocai.com.yanghuaji.model.LedSetRspModel;
import bocai.com.yanghuaji.model.LifeCycleModel;
import bocai.com.yanghuaji.model.PlantRspModel;
import bocai.com.yanghuaji.model.PlantSettingModel;
import bocai.com.yanghuaji.model.PlantSettingModel_Table;
import bocai.com.yanghuaji.presenter.intelligentPlanting.PlantSettingContract;
import bocai.com.yanghuaji.presenter.intelligentPlanting.PlantSettingPresenter;
import bocai.com.yanghuaji.util.ActivityUtil;
import bocai.com.yanghuaji.util.persistence.Account;
import butterknife.BindView;
import butterknife.OnClick;
import xpod.longtooth.LongTooth;
import xpod.longtooth.LongToothAttachment;
import xpod.longtooth.LongToothServiceResponseHandler;
import xpod.longtooth.LongToothTunnel;

/**
 * 作者 yuanfei on 2017/11/21.
 * 邮箱 yuanfei221@126.com
 */

public class PlantSettingActivity extends PresenterActivity<PlantSettingContract.Presenter>
        implements PlantSettingContract.View {
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
    @BindView(R.id.tv_plant_name)
    TextView mTvPlantName;
    @BindView(R.id.tv_plant_cycle)
    TextView tvPlantCycle;
    @BindView(R.id.ll_root)
    LinearLayout llRoot;

    private Map<String, String> map = new HashMap<>();
    private String plantMode, pMid, plantName, pId, lifeCycle, lid, id;

    private String mUUID;
    private String mLongToothId;
    private EquipmentRspModel.ListBean mPlantBean;
    private PlantRspModel.PlantCard mPlantcard;
    public static final String KEY_PLANT_BEAN = "KEY_PLANT_BEAN";
    public static String KEY_CLASS_NAME = "KEY_CLASS_NAME";

    //显示的入口
    public static void show(Context context,EquipmentRspModel.ListBean plantBean) {
        Intent intent=new Intent(context, PlantSettingActivity.class);
        intent.putExtra(KEY_PLANT_BEAN,plantBean);
        context.startActivity(intent);
    }




    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_plant_setting;
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        mPlantBean = (EquipmentRspModel.ListBean) bundle.getSerializable(KEY_PLANT_BEAN);
        return super.initArgs(bundle);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        mTitle.setText("植物设置");
        tvRight.setVisibility(View.VISIBLE);
        pId=mPlantBean.getPid();
        id = mPlantBean.getId();
        mUUID = mPlantBean.getPSIGN();
        mLongToothId = mPlantBean.getLTID();
        mTvPlantName.setText(mPlantBean.getPlantName());
    }

    @Override
    protected void initData() {
        super.initData();
        PlantSettingModel model = SQLite.select()
                .from(PlantSettingModel.class)
                .where(PlantSettingModel_Table.Id.eq(id))
                .querySingle();
        if (model!=null){
            tvPlantMethod.setText(model.getPlantMode());
            tvPlantCycle.setText(model.getLifeCycle());
        }
    }

    @OnClick(R.id.img_back)
    void onBackClick() {
        finish();
    }

    @OnClick(R.id.tv_plant_method)
    void onMethod() {
        mPresenter.plantMode();
    }

    @OnClick(R.id.tv_plant_cycle)
    void onCycle() {
        mPresenter.lifeCycle();
    }

    @OnClick(R.id.tv_plant_name)
    void onPlantNameClick() {
        if (TextUtils.isEmpty(mTvPlantName.getText())){
            Intent intent = new Intent(this, AddPlantActivity.class);
            intent.putExtra(KEY_CLASS_NAME, PlantSettingActivity.class.getName());
            startActivityForResult(intent,1);
        }else {
            Application.showToast("已添加过植物");
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPlantcard = (PlantRspModel.PlantCard) data.getSerializableExtra(AddPlantActivity.KEY_PLANT_CARD);
        mTvPlantName.setText(mPlantcard.getPlantName());
        pId = mPlantcard.getId();
    }

    @OnClick(R.id.tv_right)
    void onSetupPlant() {
        plantName=mTvPlantName.getText().toString().trim();
        map.put("Token", Account.getToken());
        map.put("PlantMode", plantMode);//种植模式
        map.put("PMid", pMid);//种植模式id
        map.put("PlantName", plantName);
        map.put("Pid", pId);//植物id
        map.put("LifeCycle", lifeCycle);
        map.put("Lid", lid);//生长周期id
        map.put("Id", id);//设备id
        if (TextUtils.isEmpty(lid)){
            Application.showToast("生长周期不能为空");
            return;
        }
        getAutoPara();
    }

    //从后台获取智能控制参数
    private void getAutoPara(){
            mPresenter.getAutoPara(pId,lid);
    }



    @Override
    public void setupPlantSuccess(PlantSettingModel model) {
        model.save();
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
                    plantMode = listBean.getTitle();
                    pMid = listBean.getId();
                    tvPlantMethod.setText(listBean.getTitle());
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
                    tvPlantCycle.setText(listBean.getTitle());
                }
            });
        } else {
            Application.showToast("暂无数据");
        }
    }

    /**
     * 获取智能控制参数成功
     */
    @Override
    public void getAutoParaSuccess(List<AutoModel.ParaBean> paraBeans) {
        mPresenter.setupPlant(map);
        AutoParaModel model = new AutoParaModel("auto",Integer.parseInt(pId),Integer.parseInt(mUUID),paraBeans);
        final Gson gson = new Gson();
        String request = gson.toJson(model);
        LongTooth.request(mLongToothId, "longtooth", LongToothTunnel.LT_ARGUMENTS, request.getBytes(),
                0, request.getBytes().length, null, new LongToothServiceResponseHandler() {
            @Override
            public void handleServiceResponse(LongToothTunnel longToothTunnel, String s, String s1, int i,
                                              byte[] bytes, LongToothAttachment longToothAttachment) {
                String jsonContent = new String(bytes);
                LedSetRspModel plantStatusRspModel = gson.fromJson(jsonContent, LedSetRspModel.class);
                if (plantStatusRspModel.getCODE()==0){
                    Application.showToast("智能参数设置成功");
                }else {
                    Application.showToast("智能参数设置失败");
                }
            }
        });
    }
    /**
     * 获取智能控制参数失败
     */
    @Override
    public void getAutoParaFailed() {
        Application.showToast("智能参数设置失败");
        mPresenter.setupPlant(map);
    }

    @Override
    protected PlantSettingContract.Presenter initPresenter() {
        return new PlantSettingPresenter(this);
    }
}
