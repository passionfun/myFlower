package bocai.com.yanghuajien.ui.intelligentPlanting;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import bocai.com.yanghuajien.R;
import bocai.com.yanghuajien.base.Application;
import bocai.com.yanghuajien.base.presenter.PresenterActivity;
import bocai.com.yanghuajien.model.BindEquipmentModel;
import bocai.com.yanghuajien.model.EquipmentInfoModel;
import bocai.com.yanghuajien.model.EquipmentRspModel;
import bocai.com.yanghuajien.model.LongToothRspModel;
import bocai.com.yanghuajien.model.UpdateVersionRspModel;
import bocai.com.yanghuajien.presenter.intelligentPlanting.EquipmentInfoContract;
import bocai.com.yanghuajien.presenter.intelligentPlanting.EquipmentInfoPresenter;
import bocai.com.yanghuajien.util.UiTool;
import bocai.com.yanghuajien.util.persistence.Account;
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

public class EquipmentInfoActivity extends PresenterActivity<EquipmentInfoContract.Presenter>
        implements EquipmentInfoContract.View {
    public static final String TAG = EquipmentInfoActivity.class.getName();
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

    @BindView(R.id.frame_update)
    FrameLayout mFramUpdate;

    @BindView(R.id.tv_latest_version)
    TextView tvLatestVersion;

    public static final String KEY_PLANT_BEAN = "KEY_PLANT_BEAN";
    private Map<String, String> map = new HashMap<>();
    private String id;
    private String mUUID;
    private String mLongToothId;
    private EquipmentRspModel.ListBean mPlantBean;


    //显示的入口
    public static void show(Context context, EquipmentRspModel.ListBean plantBean) {
        Intent intent = new Intent(context, EquipmentInfoActivity.class);
        intent.putExtra(KEY_PLANT_BEAN, plantBean);
        context.startActivity(intent);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_equipment_info;
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        mPlantBean = (EquipmentRspModel.ListBean) bundle.getSerializable(KEY_PLANT_BEAN);
        id = mPlantBean.getId();
        mUUID = mPlantBean.getPSIGN();
        mLongToothId = mPlantBean.getLTID();
        return super.initArgs(bundle);
    }

    @OnClick(R.id.img_back)
    void onBackClick() {
        finish();
    }

    Gson gson = new Gson();

    @OnClick(R.id.img_upgrade)
    void onUpdateClick() {
        //  501:有升级的新版本
        AlertDialog.Builder deleteDialog = new AlertDialog.Builder(EquipmentInfoActivity.this);
        deleteDialog.setTitle(Application.getStringText(R.string.have_new_version_ensure_update));
        deleteDialog.setPositiveButton(getResources().getString(R.string.ensure), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //升级操作
                doUpdate();
            }
        });
        deleteDialog.setNegativeButton(getResources().getString(R.string.cancel), null);
        deleteDialog.show();
    }

    //检查是否有新版本
    private void checkVersion() {
        /**
         * 是否有新版本请求格式
         * {
         * "CMD": "isUpdate",
         * "UUID": 1504608600
         * }
         */
        BindEquipmentModel model = new BindEquipmentModel("isUpdate", mUUID);
        String request = gson.toJson(model);
        //请求接口，判断是否需要更新
        LongTooth.request(mLongToothId, "longtooth", LongToothTunnel.LT_ARGUMENTS, request.getBytes(),
                0, request.getBytes().length, null, new LongToothServiceResponseHandler() {
                    @Override
                    public void handleServiceResponse(LongToothTunnel ltt, String ltid_str,
                                                      String service_str, int data_type, byte[] args,
                                                      LongToothAttachment attachment) {
                        if (args==null)
                            return;
                        String result = new String(args);
                        if (TextUtils.isEmpty(result)||!result.contains("CODE")) {
                            return;
                        }
                        LongToothRspModel longToothRspModel = gson.fromJson(result, LongToothRspModel.class);
                        Log.d(TAG, "update:" + result);
                        int code = longToothRspModel.getCODE();
                        switch (code) {
                            case 501:
                                //  501:有升级的新版本
                                Run.onUiAsync(new Action() {
                                    @Override
                                    public void call() {
                                        tvLatestVersion.setVisibility(View.GONE);
                                        imgUpgrade.setVisibility(View.VISIBLE);
                                    }
                                });
                                break;
                            default:
                                Run.onUiAsync(new Action() {
                                    @Override
                                    public void call() {
                                        tvLatestVersion.setVisibility(View.VISIBLE);
                                        imgUpgrade.setVisibility(View.GONE);
                                    }
                                });
                                break;
                        }
                    }
                });
    }
    private TimerTask task;

    //升级版本
    private void doUpdate() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.load_animation);
        mFramUpdate.setVisibility(View.VISIBLE);
        imgUpgrade.setVisibility(View.GONE);
        imgUpgrading.startAnimation(animation);
        //LongTooth.request(longToothId,"longtooth", LongToothTunnel.LT_ARGUMENTS,request.getBytes(),0,request.getBytes().length,null,new LongToothResponse());
        BindEquipmentModel model = new BindEquipmentModel("update", mUUID);
        String request = gson.toJson(model);
        //请求接口进行升级
        LongTooth.request(mLongToothId, "longtooth", LongToothTunnel.LT_ARGUMENTS, request.getBytes(),
                0, request.getBytes().length, null, new LongToothServiceResponseHandler() {
                    @Override
                    public void handleServiceResponse(LongToothTunnel ltt, String ltid_str,
                                                      String service_str, int data_type, byte[] args,
                                                      LongToothAttachment attachment) {
                        if (args==null)
                            return;
                        String result = new String(args);
                        if (TextUtils.isEmpty(result)||!result.contains("CODE")) {
                            return;
                        }
                        LongToothRspModel longToothRspModel = gson.fromJson(result, LongToothRspModel.class);
                        Log.d(TAG, "update: " + result);
                        int code = longToothRspModel.getCODE();
                        if (code == 0) {
                            Timer timer = new Timer();
                            timer.schedule(task =new TimerTask() {
                                @Override
                                public void run() {
                                    checkUpdateState();
                                }
                            },5000,5000);

                        } else {
                            Run.onUiAsync(new Action() {
                                @Override
                                public void call() {
                                    imgUpgrade.setVisibility(View.VISIBLE);
                                }
                            });
                            Application.showToast(Application.getStringText(R.string.update_failed_retry_later));
                        }
                    }
                });
    }

    private int times = 0;
    private void checkUpdateState() {
        final BindEquipmentModel model = new BindEquipmentModel("checkUpdateStat", mUUID);
        String request = gson.toJson(model);
        LongTooth.request(mLongToothId, "longtooth", LongToothTunnel.LT_ARGUMENTS, request.getBytes(),
                0, request.getBytes().length, null, new LongToothServiceResponseHandler() {
                    @Override
                    public void handleServiceResponse(LongToothTunnel ltt, String ltid_str,
                                                      String service_str, int data_type, byte[] args,
                                                      LongToothAttachment attachment) {
                        if (args==null)
                            return;
                        String result = new String(args);
                        if (TextUtils.isEmpty(result)||!result.contains("CODE")) {
                            return;
                        }
                        LongToothRspModel longToothRspModel = gson.fromJson(result, LongToothRspModel.class);
                        Log.d(TAG, "update:1111 " + result);
                        int code = longToothRspModel.getUpdateStat();
                        Log.d(TAG, "handleServiceResponse: "+code);
                        if (code == 1) {//code=1:正在升级
                            // 循环等待

                        } else if (code == 2) {//code=2:升级成功
                            // 更新版本号
                            task.cancel();
                            updateVersion();
                            Run.onUiAsync(new Action() {
                                @Override
                                public void call() {
                                    tvLatestVersion.setVisibility(View.VISIBLE);
                                    imgUpgrading.clearAnimation();
                                    mFramUpdate.setVisibility(View.GONE);
                                }
                            });
                            Application.showToast(R.string.update_success);
                        } else if (code == 3) {//code=3:升级失败
                            times++;
                            if (times > 2)  {
                                times = 0;
                                task.cancel();
                                Run.onUiAsync(new Action() {
                                    @Override
                                    public void call() {
                                        imgUpgrading.clearAnimation();
                                        mFramUpdate.setVisibility(View.GONE);
                                        imgUpgrade.setVisibility(View.VISIBLE);
                                    }
                                });
                                Application.showToast(R.string.update_failed);
                            }
                        }
                    }
                });
    }


    private void updateVersion() {
        final BindEquipmentModel model = new BindEquipmentModel("softVer", mUUID);
        String request = gson.toJson(model);
        LongTooth.request(mLongToothId, "longtooth", LongToothTunnel.LT_ARGUMENTS, request.getBytes(),
                0, request.getBytes().length, null, new LongToothServiceResponseHandler() {
                    @Override
                    public void handleServiceResponse(LongToothTunnel ltt, String ltid_str,
                                                      String service_str, int data_type, byte[] args,
                                                      LongToothAttachment attachment) {
                        if (args==null)
                            return;
                        String result = new String(args);
                        if (TextUtils.isEmpty(result)||!result.contains("CODE")) {
                            return;
                        }
                        LongToothRspModel longToothRspModel = gson.fromJson(result, LongToothRspModel.class);
                        Log.d(TAG, "update: " + result);
                        int code = longToothRspModel.getCODE();
                        if (code == 0) {
                            String newVersion = longToothRspModel.getSoftVer();
                            if (mPresenter==null||newVersion==null||id==null){
                                return;
                            }
                            mPresenter.updateVersion(Account.getToken(), newVersion, id);
                        }
                    }
                }
        );
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        UiTool.setBlod(mTitle);
        mTitle.setText(Application.getStringText(R.string.equipment_information));

        map.put("Token", Account.getToken());
        map.put("Id", id);
        mPresenter.equipmentInfo(map);
    }

    @Override
    public void equipmentInfoSuccess(EquipmentInfoModel model) {
        tvMacAddress.setText(model.getMac());
        if (!TextUtils.isEmpty(model.getSerialNum())){
            tvSerialNumber.setText(model.getSerialNum());
        }else {
            tvSerialNumber.setText(Application.getStringText(R.string.unknown));
        }
        tvEquipmentType.setText(model.getEquipName());
        tvVersion.setText(model.getVersion());
        updateVersion();
        checkVersion();
    }

    @Override
    public void updateVersionSuccess(UpdateVersionRspModel model) {
        tvVersion.setText(model.getVersion());
    }

    @Override
    protected EquipmentInfoContract.Presenter initPresenter() {
        return new EquipmentInfoPresenter(this);
    }
}
