package bocai.com.yanghuaji.ui.intelligentPlanting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.Application;
import bocai.com.yanghuaji.base.presenter.PresenterActivity;
import bocai.com.yanghuaji.model.AutoModel;
import bocai.com.yanghuaji.model.AutoParaModel;
import bocai.com.yanghuaji.model.EquipmentCard;
import bocai.com.yanghuaji.model.LedSetRspModel;
import bocai.com.yanghuaji.model.LifeCycleModel;
import bocai.com.yanghuaji.model.PlantSettingModel;
import bocai.com.yanghuaji.presenter.intelligentPlanting.FirstSettingContract;
import bocai.com.yanghuaji.presenter.intelligentPlanting.FirstSettingPresenter;
import bocai.com.yanghuaji.ui.main.MainActivity;
import bocai.com.yanghuaji.util.ActivityUtil;
import bocai.com.yanghuaji.util.persistence.Account;
import butterknife.BindView;
import butterknife.OnClick;
import xpod.longtooth.LongTooth;
import xpod.longtooth.LongToothAttachment;
import xpod.longtooth.LongToothServiceResponseHandler;
import xpod.longtooth.LongToothTunnel;

/**
 * 作者 yuanfei on 2017/11/14.
 * 邮箱 yuanfei221@126.com
 */

public class FirstSettingActivity extends PresenterActivity<FirstSettingContract.Presenter>
        implements FirstSettingContract.View {
    @BindView(R.id.img_back)
    ImageView mImgBack;

    @BindView(R.id.et_input_equipment_name)
    EditText mEtEquipmentName;

    @BindView(R.id.et_input_plant_name)
    EditText mEtPlantName;

    @BindView(R.id.ll_root)
    LinearLayout llRoot;

    @BindView(R.id.tv_plant_cycle)
    TextView tvPlantCycle;


    //    public static String KEY_EQUIPMENT_NAME = "KEY_EQUIPMENT_NAME";
//    public static String KEY_EQUIPMENT_ID = "KEY_EQUIPMENT_ID";
    public static String KEY_PLANT_NAME = "KEY_PLANT_NAME";
    public static String KEY_PLANT_ID = "KEY_PLANT_ID";
    public static String KEY_EQUIPMENT_CARD = "KEY_EQUIPMENT_CARD";
    private String mEquipmentName;
    private String mEquipmentId;
    private String mPlantName;
    private String mPlantId;
    private EquipmentCard mEquipmentCard;
    private String lifeCycleId;
    private String lifeCycle;

    //显示的入口
    public static void show(Context context, EquipmentCard equipmentCard) {
        Intent intent = new Intent(context, FirstSettingActivity.class);
        intent.putExtra(KEY_EQUIPMENT_CARD, equipmentCard);
        context.startActivity(intent);
    }

    //显示的入口
    public static void show(Context context, EquipmentCard equipmentCard, String plantName, String plantId) {
        Intent intent = new Intent(context, FirstSettingActivity.class);
        intent.putExtra(KEY_EQUIPMENT_CARD, equipmentCard);
        intent.putExtra(KEY_PLANT_NAME, plantName);
        intent.putExtra(KEY_PLANT_ID, plantId);
        context.startActivity(intent);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_first_setting;
    }


    @OnClick(R.id.tv_plant_cycle)
    void onCycle() {
        mPresenter.lifeCycle();
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        mEquipmentCard = (EquipmentCard) bundle.getSerializable(KEY_EQUIPMENT_CARD);
        mEquipmentId = mEquipmentCard.getId();
        mEquipmentName = mEquipmentCard.getEquipName();
        mPlantName = bundle.getString(KEY_PLANT_NAME);
        mPlantId = bundle.getString(KEY_PLANT_ID);
        return super.initArgs(bundle);
    }

    @Override
    protected void initData() {
        super.initData();
        mEtEquipmentName.setText(mEquipmentName);
        mEtPlantName.setText(mPlantName);
    }

    @OnClick(R.id.img_back)
    void onBackClick() {
        finish();
    }

    @OnClick(R.id.tv_save)
    void onSaveClick() {
        //第一次种植设置
        if (TextUtils.isEmpty(lifeCycleId)) {
            Application.showToast("生长周期不能id为空");
            return;
        }
        mPresenter.getAutoPara(mEquipmentId, mPlantId, lifeCycleId);
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
                    lifeCycleId = listBean.getId();
                    lifeCycle = listBean.getTitle();
                    tvPlantCycle.setText(listBean.getTitle());
                }
            });
        } else {
            Application.showToast("暂无数据");
        }
    }

    @Override
    public void setupSuccess() {
        PlantSettingModel model = new PlantSettingModel();
        model.setPlantName(mEtPlantName.getText().toString());
        model.setId(mEquipmentId);
        model.save();
        MainActivity.show(this);
    }

    final Gson gson = new Gson();
    private Timer timer = new Timer();

    @Override
    public void getAutoParaSuccess(List<AutoModel.ParaBean> paraBeans) {
        AutoParaModel model = new AutoParaModel("Auto", Integer.parseInt(mPlantId), Integer.parseInt(mEquipmentCard.getPSIGN()), paraBeans);
        String request = gson.toJson(model);
        LongTooth.request(mEquipmentCard.getLTID(), "longtooth", LongToothTunnel.LT_ARGUMENTS, request.getBytes(),
                0, request.getBytes().length, null, new MyLongToothServiceResponseHandler());
    }

    private class MyLongToothServiceResponseHandler implements LongToothServiceResponseHandler {
        private boolean isRspSuccess = false;
        private boolean isReturn = false;

        public MyLongToothServiceResponseHandler() {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (!isRspSuccess) {
                        hideLoading();
                        isReturn = true;
                        Application.showToast("植物设置失败");
                        MainActivity.show(FirstSettingActivity.this);
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
                if (!isReturn)
                    Run.onUiAsync(new Action() {
                        @Override
                        public void call() {
                            String token = Account.getToken();
                            mPresenter.setup(token, mEquipmentName, mPlantName, mPlantId, mEquipmentId,lifeCycle,lifeCycleId);
                        }
                    });
            } else {
//                    Application.showToast("智能参数设置失败");
            }
        }
    }

    @Override
    public void getAutoParaFailed() {
        Application.showToast("植物设置失败");
        MainActivity.show(this);
        finish();
    }

    @Override
    protected FirstSettingContract.Presenter initPresenter() {
        return new FirstSettingPresenter(this);
    }
}
