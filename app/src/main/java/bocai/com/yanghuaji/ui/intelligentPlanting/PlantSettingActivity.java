package bocai.com.yanghuaji.ui.intelligentPlanting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import bocai.com.yanghuaji.model.EquipmentRspModel;
import bocai.com.yanghuaji.model.LedSetRspModel;
import bocai.com.yanghuaji.model.LifeCycleModel;
import bocai.com.yanghuaji.model.MessageEvent;
import bocai.com.yanghuaji.model.PlantRspModel;
import bocai.com.yanghuaji.model.PlantSettingModel;
import bocai.com.yanghuaji.model.PlantSettingModel_Table;
import bocai.com.yanghuaji.presenter.intelligentPlanting.PlantSettingContract;
import bocai.com.yanghuaji.presenter.intelligentPlanting.PlantSettingPresenter;
import bocai.com.yanghuaji.util.ActivityUtil;
import bocai.com.yanghuaji.util.UiTool;
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
    private boolean isPlantsetted = false;

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
        UiTool.setBlod(mTitle);
        mTitle.setText(Application.getStringText(R.string.plant_setting));
        tvRight.setVisibility(View.VISIBLE);
        pId=mPlantBean.getPid();
        id = mPlantBean.getId();
        mUUID = mPlantBean.getPSIGN();
        mLongToothId = mPlantBean.getLTID();
    }

    @Override
    protected void initData() {
        super.initData();
        tvPlantMethod.setEnabled(false);
//        PlantSettingModel model = SQLite.select()
//                .from(PlantSettingModel.class)
//                .where(PlantSettingModel_Table.Id.eq(id))
//                .querySingle();
//        if (model!=null){
//            plantMode = model.getPlantMode();
//            lifeCycle = model.getLifeCycle();
////            tvPlantMethod.setText(plantMode);
//            tvPlantCycle.setText(lifeCycle);
//            lid = model.getLid();
//            pMid = model.getPMid();
//        }
//        if (model==null||TextUtils.isEmpty(model.getPlantName())){
//            mTvPlantName.setText("去添加");
//            tvPlantMethod.setText("手动");
//            isPlantsetted = false;
//        }else {
//            mTvPlantName.setText(model.getPlantName());
//            tvPlantMethod.setText("智能");
//            isPlantsetted = true;
//        }
        if (!TextUtils.isEmpty(mPlantBean.getPlantName())){
            mTvPlantName.setText(mPlantBean.getPlantName());
            tvPlantMethod.setText("智能");
            plantMode = "智能";
            pMid = "14";
            isPlantsetted = true;
        }else {
            mTvPlantName.setText("去添加");
            tvPlantMethod.setText("手动");
            plantMode = "手动";
            pMid = "15";
            isPlantsetted = false;
        }
        tvPlantCycle.setText(mPlantBean.getLifeCycle());
        lifeCycle= mPlantBean.getLifeCycle();
        lid = mPlantBean.getLid();
    }

    @OnClick(R.id.img_back)
    void onBackClick() {
        finish();
    }

    @OnClick(R.id.tv_plant_method)
    void onMethod() {
//        mPresenter.plantMode();
    }

    @OnClick(R.id.tv_plant_cycle)
    void onCycle() {
        mPresenter.lifeCycle();
    }

    @OnClick(R.id.tv_plant_name)
    void onPlantNameClick() {
        if (!isPlantsetted){
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
        if (data!=null){
            mPlantcard = (PlantRspModel.PlantCard) data.getSerializableExtra(AddPlantActivity.KEY_PLANT_CARD);
            mTvPlantName.setText(mPlantcard.getPlantName());
            tvPlantMethod.setText("智能");
            pId = mPlantcard.getId();
        }
    }

    @OnClick(R.id.tv_right)
    void onSetupPlant() {
        plantName=mTvPlantName.getText().toString().trim();
        map.put("Token", Account.getToken());
        map.put("PlantMode", plantMode==null?"":plantMode);//种植模式
        map.put("PMid", pMid==null?"":pMid);//种植模式id
        map.put("PlantName", plantName);
        map.put("Pid", pId);//植物id
        map.put("LifeCycle", lifeCycle);//生长周期
        map.put("Lid", lid);//生长周期id
        map.put("Id", id);//设备id
        if (TextUtils.isEmpty(lid)){
            Application.showToast("生长周期不能id为空");
            return;
        }
        if (plantName.equals("去添加")||TextUtils.isEmpty(plantName)){
            Application.showToast("植物不能为空");
            return;
        }
        getAutoPara();
    }

    //从后台获取智能控制参数
    private void getAutoPara(){
            mPresenter.getAutoPara(id,pId,lid);
    }



    @Override
    public void setupPlantSuccess(PlantSettingModel model) {
        model.save();
        EventBus.getDefault().post(new MessageEvent(HorizontalRecyclerFragment.HORIZONTALRECYLER_REFRESH));
        EventBus.getDefault().post(new MessageEvent(VeticalRecyclerFragment.VERTICAL_RECYLER_REFRESH));
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
    final Gson gson = new Gson();
    private Timer timer = new Timer();
    @Override
    public void getAutoParaSuccess(List<AutoModel.ParaBean> paraBeans) {
        AutoParaModel model = new AutoParaModel("Auto",Integer.parseInt(pId),Integer.parseInt(mUUID),paraBeans);
        String request = gson.toJson(model);
        LongTooth.request(mLongToothId, "longtooth", LongToothTunnel.LT_ARGUMENTS, request.getBytes(),
                0, request.getBytes().length, null, new MyLongToothServiceResponseHandler());
    }

    private class MyLongToothServiceResponseHandler implements LongToothServiceResponseHandler {
        private boolean isRspSuccess = false;
        private boolean isReturn = false;
        public MyLongToothServiceResponseHandler() {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (!isRspSuccess){
                        hideLoading();
                        isReturn = true;
                        Application.showToast("设备无响应，保存失败");
                    }
                }
            },3000);
        }

        @Override
        public void handleServiceResponse(LongToothTunnel longToothTunnel, String s, String s1, int i,
                                          byte[] bytes, LongToothAttachment longToothAttachment) {
            if(bytes==null){
                return;
            }
            String jsonContent = new String(bytes);
            if (!jsonContent.contains("CODE")){
                return;
            }
            Log.d("shc", "handleServiceResponse: "+jsonContent);
            LedSetRspModel plantStatusRspModel = gson.fromJson(jsonContent, LedSetRspModel.class);
            if (plantStatusRspModel.getCODE()==0){
//                    Application.showToast("智能参数设置成功");
                isRspSuccess =true;
                hideLoading();
                if (!isReturn)
                    Run.onUiAsync(new Action() {
                        @Override
                        public void call() {
                            mPresenter.setupPlant(map);
                        }
                    });
            }else {
//                    Application.showToast("智能参数设置失败");
            }
        }
    }

    /**
     * 获取智能控制参数失败
     */
    @Override
    public void getAutoParaFailed() {
        Application.showToast("智能参数设置失败");
//        mPresenter.setupPlant(map);
    }

    @Override
    protected PlantSettingContract.Presenter initPresenter() {
        return new PlantSettingPresenter(this);
    }
}
