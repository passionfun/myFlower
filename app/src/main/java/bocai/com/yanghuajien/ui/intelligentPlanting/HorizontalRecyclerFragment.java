package bocai.com.yanghuajien.ui.intelligentPlanting;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothClass;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.load.data.mediastore.ThumbFetcher;
import com.bumptech.glide.util.LogTime;
import com.google.gson.Gson;
import com.uuzuche.lib_zxing.view.ViewfinderView;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.zip.GZIPOutputStream;

import bocai.com.yanghuajien.R;
import bocai.com.yanghuajien.base.Activity;
import bocai.com.yanghuajien.base.Application;
import boc.com.imgselector.GlideApp;
import bocai.com.yanghuajien.base.RecyclerAdapter;
import bocai.com.yanghuajien.base.common.Common;
import bocai.com.yanghuajien.base.presenter.PrensterFragment;
import bocai.com.yanghuajien.model.BindEquipmentModel;
import bocai.com.yanghuajien.model.CheckboxStatusModel;
import bocai.com.yanghuajien.model.DeviceFirmwareModel;
import bocai.com.yanghuajien.model.EquipmentDataModel;
import bocai.com.yanghuajien.model.EquipmentRspModel;
import bocai.com.yanghuajien.model.LedSetModel;
import bocai.com.yanghuajien.model.LedSetRspModel;
import bocai.com.yanghuajien.model.MessageEvent;
import bocai.com.yanghuajien.model.PlantStatusModel;
import bocai.com.yanghuajien.model.PlantStatusRspModel;
import bocai.com.yanghuajien.presenter.intelligentPlanting.IntelligentPlantContract;
import bocai.com.yanghuajien.presenter.intelligentPlanting.IntelligentPlantPresenter;
import bocai.com.yanghuajien.presenter.intelligentPlanting.MainRecylerContract;
import bocai.com.yanghuajien.presenter.intelligentPlanting.MainRecylerPresenter;
import bocai.com.yanghuajien.service.MyLongToothService;
import bocai.com.yanghuajien.ui.intelligentPlanting.recyclerHelper.GalleryLayoutManager;
import bocai.com.yanghuajien.ui.intelligentPlanting.recyclerHelper.ScaleTransformer;
import bocai.com.yanghuajien.ui.intelligentPlanting.zxing.ScanActivity;
import bocai.com.yanghuajien.ui.main.MainActivity;
import bocai.com.yanghuajien.util.ConstUtil;
import bocai.com.yanghuajien.util.LogUtil;
import bocai.com.yanghuajien.util.LongToothUtil;
import bocai.com.yanghuajien.util.UiTool;
import bocai.com.yanghuajien.util.persistence.Account;
import bocai.com.yanghuajien.util.widget.EmptyView;
import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnLongClick;
import xpod.longtooth.LongTooth;
import xpod.longtooth.LongToothAttachment;
import xpod.longtooth.LongToothServiceResponseHandler;
import xpod.longtooth.LongToothTunnel;

import static bocai.com.yanghuajien.ui.intelligentPlanting.HorizontalRecyclerFragmentHelper.LED_OFF;
import static bocai.com.yanghuajien.ui.intelligentPlanting.HorizontalRecyclerFragmentHelper.LED_ON;
import static bocai.com.yanghuajien.ui.intelligentPlanting.HorizontalRecyclerFragmentHelper.failTimes;
import static bocai.com.yanghuajien.ui.intelligentPlanting.HorizontalRecyclerFragmentHelper.update;
import static bocai.com.yanghuajien.ui.intelligentPlanting.VeticalRecyclerFragment.VERTICALRECYCLER_VISIABLE;

/**
 * 作者 yuanfei on 2017/11/15.
 * 邮箱 yuanfei221@126.com
 */
public class HorizontalRecyclerFragment extends PrensterFragment<IntelligentPlantContract.Presenter>
        implements IntelligentPlantContract.View, MyLongToothService.MyLongToothListener {
    @BindView(R.id.empty)
    EmptyView mEmptyView;

    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;

    @BindView(R.id.tv_current)
    TextView mCurrentNum;

    @BindView(R.id.tv_total)
    TextView mTotalNum;
    private ImageView iv_recycle;
    private ImageView iv_bindExp;

    private TextView tv_cancelUpdateFirmware;
    private TextView tv_updateDeviceFirmware;
    public static final String TAG = HorizontalRecyclerFragment.class.getName();
    public static final String HORIZONTALRECYLER_REFRESH = "HORIZONTALRECYLER_REFRESH";
    public static final String HORIZONTALRECYLER_DELETE_SUCCESS = "HORIZONTALRECYLER_DELETE_SUCCESS";
    public static final String HORIZONTALRECYLER_VISIABLE = "HORIZONTALRECYLER_VISIABLE";
    public static final String PUMP_ON = "pumpOn";
    public static final String PUMP_OFF = "pumpOff";
    public static final String UNKNOWN = "- -";
    private RecyclerAdapter<EquipmentRspModel.ListBean> mAdapter;
    private List<EquipmentRspModel.ListBean> listBeans = null;

    private List<DeviceFirmwareModel> dfmList = null;
    private List<DeviceFirmwareModel> updateDeviceList = null;
    private List<DeviceFirmwareModel> checkedUpdateFirvDeviceList = null;//升级列表中选中升级的设备列表
    private List<DeviceFirmwareModel> uncheckedUpdateFirvDeviceList = null;//升级列表中未选中的设备列表
    private List<String> upgradeStateFailList = null;//升级状态失败
    private List<String> restartSuccessList = null;
    private List<DeviceFirmwareModel> sendUpgradeCMDSucessList = null;//发送升级命令之后执行成功
    private List<DeviceFirmwareModel> sendUpgradeCMDFailList = null;//发送升级命令之后执行失败
    private List<String> upgradeStateSuccessList = null;


    private String deviceSeries = "";
    private String deviceLtid = "";
    private String deviceUUID = "";
    private String returnDeviceLtid = "";
    private Gson gson = new Gson();
    private boolean hasNewFirmwarmDeviceInList = true;//固件更新列表有之前添加的设备（有新固件更新的设备列表）的标志位
    private boolean isFirstShowFirmwareDialog = true;
    private boolean enable = true;
    private boolean isRecycleOn = false;
    private boolean isPumpControl = false;
    private boolean isNeedLoadData = false;
    private boolean isUpgradeCmdSendSuccess = true;
    private boolean isRestartTimerOn = true;
    private boolean isUpgrading = true;
    private int deviceSize = 0;
    private int newVersionSize = 0;
    private int oldVersionSize = 0;
    private int progress = 0;
    private int updateStateType = -1;
    private Timer mUpdateTimer = null;
    private FirmwareUpdateAdapter firmwareUpdateAdapter = null;


    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_horizontal_recycler;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        EventBus.getDefault().register(this);
        GalleryLayoutManager layoutManager = new GalleryLayoutManager(GalleryLayoutManager.HORIZONTAL);
        //默认显示列表中的位置（0代表列表中的第一个）
        layoutManager.attach(mRecyclerView, 0);
        layoutManager.setItemTransformer(new ScaleTransformer());
        mRecyclerView.setAdapter(mAdapter = new Adapter());
        mAdapter.setListener(new RecyclerAdapter.AdapterListenerImpl<EquipmentRspModel.ListBean>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder holder, EquipmentRspModel.ListBean plantModel) {
                LogUtil.d(TAG, "种植标签页下（onItemClick注意关注id）:" + new Gson().toJson(plantModel));
                String url = Common.Constance.H5_BASE + "product.html?id=" + plantModel.getId();
                if (enable) {
                    enable = false;
                    PlantingDateAct.show(getContext(), url, plantModel);
                }
            }

        });
        //滑动横向recyclerview的回调监听
        layoutManager.setOnItemSelectedListener(new GalleryLayoutManager.OnItemSelectedListener() {
            @Override
            public void onItemSelected(RecyclerView recyclerView, View item, int position) {
                deviceSeries = listBeans.get(position).getSeries();
                deviceLtid = listBeans.get(position).getLTID();
                deviceUUID = listBeans.get(position).getPSIGN();
                LogUtil.d(TAG, "setOnItemSelectedListener position(=========================================当前选中了第):" + position + "项（deviceName）：" + deviceSeries + ",deviceLtid:" + deviceLtid + ",uuid:" + deviceUUID);
                //开始查询水循环的状态
                if (deviceSeries.equals("WG201")) {
                    isPumpControl = false;
                    sendPumpSetCmd(2);
                }

                mCurrentNum.setText(String.valueOf(position + 1));
                EventBus.getDefault().post(new MessageEvent(HORIZONTALRECYLER_VISIABLE, position));
                Account.setHorizonVisiablePosition(position);
            }
        });
        mCurrentNum.setText("1");
        mEmptyView.bind(mRecyclerView);
        mEmptyView.setEmptyImg(R.mipmap.img_equipment_empty);
        mEmptyView.setEmptyText(R.string.equipment_empty);
        setPlaceHolderView(mEmptyView);
    }

    @Override
    public void onResume() {
        enable = true;
        super.onResume();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        LogUtil.d(TAG, "onHiddenChanged executed:" + hidden);
        if (!hidden) {
            if (UiTool.isNetworkAvailable(getContext()) && isNeedLoadData) {
                isNeedLoadData = false;
                mPresenter.getAllEquipments(Account.getToken(), "0", "0");
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fresh(MessageEvent messageEvent) {
        if (messageEvent.getMessage().equals(HORIZONTALRECYLER_REFRESH)) {
            LogUtil.d(TAG, "fresh(hrf)收到消息了+++++++++++++++(equipment_list)+++++++get allequipment:" + HORIZONTALRECYLER_REFRESH);
            mPresenter.getAllEquipments(Account.getToken(), "0", "0");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (mRestartTimer != null) {
            LogUtil.d(TAG, "onDestroy mRestartTime cancel");
            mRestartTimer.cancel();
            mRestartTimer = null;
        }

        if (mUpdateTimer != null) {
            LogUtil.d(TAG, "onDestroy mupdateTime cancel");
            mUpdateTimer.cancel();
            mUpdateTimer = null;
        }
    }

    @Override
    public void showError(int str) {
        super.showError(str);
        isNeedLoadData = true;
    }

    @Override
    protected void initData() {
        super.initData();
        mPresenter.getAllEquipments(Account.getToken(), "0", "0");
        mEmptyView.triggerLoading();
    }

    private void clearData(){
        if (dfmList != null) {
            dfmList.clear();
            dfmList = null;
        }
        if (updateDeviceList != null) {
            updateDeviceList.clear();
            updateDeviceList = null;
        }
        if (checkedUpdateFirvDeviceList != null) {
            checkedUpdateFirvDeviceList.clear();
            checkedUpdateFirvDeviceList = null;
        }
        if (uncheckedUpdateFirvDeviceList != null) {
            uncheckedUpdateFirvDeviceList.clear();
            uncheckedUpdateFirvDeviceList = null;
        }
        if (upgradeStateFailList != null) {
            upgradeStateFailList.clear();
            upgradeStateFailList = null;
        }
        if (restartSuccessList != null) {
            restartSuccessList.clear();
            restartSuccessList = null;
        }
        if (sendUpgradeCMDSucessList != null) {
            sendUpgradeCMDSucessList.clear();
            sendUpgradeCMDSucessList = null;
        }
        if (sendUpgradeCMDFailList != null) {
            sendUpgradeCMDFailList.clear();
            sendUpgradeCMDFailList = null;
        }
        if (upgradeStateSuccessList != null) {
            upgradeStateSuccessList.clear();
            upgradeStateSuccessList = null;
        }
    }
    @Override
    public void getAllEquipmentsSuccess(List<EquipmentRspModel.ListBean> listBeans) {
        LogUtil.d(TAG, "getAllEquipmentSuccess:" + new Gson().toJson(listBeans));
        this.listBeans = listBeans;
        //fun add
        //发送检查设备版本的数据帧
        deviceSize = listBeans.size();

        Account.setListBeans(listBeans);
        mAdapter.replace(listBeans);
        mTotalNum.setText(String.valueOf(listBeans.size()));
        mPlaceHolderView.triggerOkOrEmpty(mAdapter.getItemCount() > 0);

        if (deviceSize > 0) {
            clearData();
            dfmList = new ArrayList<>();
            updateDeviceList = new ArrayList<>();
            checkedUpdateFirvDeviceList = new ArrayList<>();//升级列表中选中升级的设备列表
            uncheckedUpdateFirvDeviceList = new ArrayList<>();//升级列表中未选中的设备列表
            upgradeStateFailList = new ArrayList<>();//升级状态失败
            restartSuccessList = new ArrayList<>();
            sendUpgradeCMDSucessList = new ArrayList<>();//发送升级命令之后执行成功
            sendUpgradeCMDFailList = new ArrayList<>();//发送升级命令之后执行失败
            upgradeStateSuccessList = new ArrayList<>();

            for (int i = 0; i < deviceSize; i++) {
                BindEquipmentModel model = new BindEquipmentModel(ConstUtil.CMD_DEVICE_ISUPDATE, listBeans.get(i).getPSIGN());
                String deviceUpdateCmd = gson.toJson(model);
                LogUtil.d(TAG, "发送检查设备固件是否有新版本的数据帧--3PPPPPPPPPPPPPPPPPPPPPPPPPPPPPP----（isUpdate）：" + deviceUpdateCmd + ",LTID:" + listBeans.get(i).getLTID());
                MyLongToothService.sendDataFrame(getActivity(), ConstUtil.OP_ISUPDATE, listBeans.get(i).getLTID(), deviceUpdateCmd);
                MyLongToothService.setMyLongToothListener(this);
            }
        }
    }

    @Override
    protected IntelligentPlantContract.Presenter initPresenter() {
        return new IntelligentPlantPresenter(this);
    }

    @Override
    public void updateUI(Message msg) {
        mHandler.sendMessage(msg);
    }

    @Override
    public void timeOutMsg(Message msg) {

    }

    class Adapter extends RecyclerAdapter<EquipmentRspModel.ListBean> {
        @Override
        protected int getItemViewType(int position, EquipmentRspModel.ListBean plantModel) {
            return R.layout.item_main_horizontal;
        }

        @Override
        protected ViewHolder<EquipmentRspModel.ListBean> onCreateViewHolder(View root, int viewType) {
            LogUtil.d(TAG, "ViewHolder(onCreateViewHolder):" + viewType);
            return new MyViewHolder(root);
        }

        class MyViewHolder extends ViewHolder<EquipmentRspModel.ListBean> implements MainRecylerContract.View {
            @BindView(R.id.frame_setting)
            FrameLayout mSettingView;

            @BindView(R.id.frame_offliine)
            FrameLayout mFramOffline;

            @BindView(R.id.img_setting)
            ImageView mSetting;

            @BindView(R.id.tv_equipment_name)
            TextView mEquipmentName;

            @BindView(R.id.tv_plant_name)
            TextView mPlantName;

            @BindView(R.id.tv_group_name)
            TextView mGroupName;

            @BindView(R.id.tv_plant_time)
            TextView mTime;

            @BindView(R.id.image)
            ImageView mImage;

            @BindView(R.id.img_lineOff)
            ImageView mOffLine;

            @BindView(R.id.tv_led)
            TextView mLed;

            @BindView(R.id.cb_push)
            CheckBox mPush;

            @BindView(R.id.tv_temperature)
            TextView mTemperature;

            @BindView(R.id.tv_water_status)
            TextView mWaterStatus;

            @BindView(R.id.tv_led_status)
            TextView mLedMode;

            @BindView(R.id.tv_ec_status)
            TextView mEcStatus;

            @BindView(R.id.img_tem_arrow)
            ImageView mImgTemArrow;

            @BindView(R.id.ll_data)
            LinearLayout mPlantData;

            @BindView(R.id.ll_secSet)
            LinearLayout ll_secSet;
            @BindView(R.id.tv_setting_second)
            TextView mSecondSetting;

            @BindView(R.id.tv_update)
            TextView mUpdate;

            @BindView(R.id.tv_more)
            TextView tv_more;

            @BindView(R.id.img_refresh)
            ImageView mRefresh;

            @BindView(R.id.ll_recycle)
            LinearLayout ll_recycle;

            @BindView(R.id.img_waterRecycle)
            ImageView iv_waterRecycle;

            @BindView(R.id.iv_device_bind_exp)
            ImageView iv_deviceBindExp;

            private boolean isShowSetting = false;
            private MainRecylerContract.Presenter mPresenter;
            private TimerTask task;
            private Timer timer;
            private boolean isLedOn = false;

            public MyViewHolder(View itemView) {
                super(itemView);
                EventBus.getDefault().register(this);
                new MainRecylerPresenter(this);
            }

            @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
            public void fresh(MessageEvent messageEvent) {
                LogUtil.d(TAG, "Adapter(MyViewHolder)中收到消息啦+++++++++++++++++++++++++++++++++++++++++++++++++：" + new Gson().toJson(messageEvent));
                if (messageEvent.getMessage().equals(VeticalRecyclerFragment.VERTICALRECYCLER_DELETE_SUCCESS) &&
                        (messageEvent.getType().equals(mData.getLTID()))) {
                    if (task != null) {
                        task.cancel();
                    }
                } else if (messageEvent.getMessage().equals(VeticalRecyclerFragment.EQUIPMENT_LINE_ON) &&
                        (messageEvent.getType().equals(mData.getLTID()))) {
                    mOffLine.setVisibility(View.INVISIBLE);
                    mFramOffline.setVisibility(View.INVISIBLE);
                } else if (messageEvent.getMessage().equals(HorizontalRecyclerFragmentHelper.LED_ON) &&
                        (messageEvent.getType().equals(mData.getLTID()))) {
//                    iv_bindExp.setVisibility(View.GONE);
                    isLedOn = true;
                    mLed.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.mipmap.img_light_open_horizontal, 0, 0);
                } else if (messageEvent.getMessage().equals(HorizontalRecyclerFragmentHelper.LED_OFF) &&
                        (messageEvent.getType().equals(mData.getLTID()))) {
//                    iv_bindExp.setVisibility(View.GONE);
                    isLedOn = false;
                    mLed.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.mipmap.img_light_close_horizontal, 0, 0);
                } else if (messageEvent.getMessage().equals(PUMP_ON) &&
                        (messageEvent.getType().equals(mData.getLTID()))) {
//                    iv_bindExp.setVisibility(View.GONE);
                    isRecycleOn = true;
                    iv_waterRecycle.setImageResource(R.mipmap.v_water_recycle_on);
                } else if (messageEvent.getMessage().equals(PUMP_OFF) &&
                        (messageEvent.getType().equals(mData.getLTID()))) {
//                    iv_bindExp.setVisibility(View.GONE);
                    isRecycleOn = false;
                    iv_waterRecycle.setImageResource(R.mipmap.v_water_recycle_off);
                } else if (messageEvent.getMessage().equals(MainActivity.MAINACTIVITY_DESTROY)) {
                    if (task != null) {
                        task.cancel();
                    }
                } else if (messageEvent.getMessage().equals(HorizontalRecyclerFragmentHelper.UPDATE_SUCCESS)) {
//                    iv_bindExp.setVisibility(View.GONE);
                    mUpdate.setEnabled(false);
                    mUpdate.setText(Application.getStringText(R.string.newest_version));
                    mUpdate.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.mipmap.img_update_horizontal_nomal, 0, 0);
                } else if (messageEvent.getMessage().equals(HORIZONTALRECYLER_VISIABLE)) {
                    if (messageEvent.getPosition() == getAdapterPosition()) {
                        if (timer == null) {
                            timer = new Timer();
                        }
                        if (task == null) {
                            task = new TimerTask() {
                                @Override
                                public void run() {
                                    HorizontalRecyclerFragmentHelper.setLedSwitch(mData);
                                    HorizontalRecyclerFragmentHelper.setLedMode(mData, mLedMode, mPresenter);
                                    getEquipmentData(mData);
                                }
                            };
                        }
                        timer.schedule(task, 2000, 30000);
                    } else {
                        if (task != null) {
                            task.cancel();
                            task = null;
                        }
                    }
                } else if (messageEvent.getMessage().equals(VERTICALRECYCLER_VISIABLE)) {
                    if (task != null) {
                        task.cancel();
                        task = null;
                    }
                }
            }

            @Override
            protected void onBind(final EquipmentRspModel.ListBean plantModel) {
                LogUtil.d(TAG, "MyViewHolder onBind++++++++++++++++++++++++++++++++++++++++>:" + new Gson().toJson(plantModel));
                //初始化数据
                iv_recycle = iv_waterRecycle;
//                iv_bindExp = iv_deviceBindExp;
                isPumpControl = false;
                setDataSuccess(plantModel.buildEquipmentDataModel());
                mEquipmentName.setText(plantModel.getEquipName());
                mPlantName.setText(plantModel.getPlantName());
                mGroupName.setText(plantModel.getGroupName());
                mTime.setText(String.valueOf(plantModel.getDays()));
                String series = plantModel.getSeries();
                if (series.equals("WG201")) {
                    tv_more.setVisibility(View.GONE);
                    ll_recycle.setVisibility(View.VISIBLE);
                    //查询水泵的工作状态
                    deviceUUID = plantModel.getPSIGN();
                    sendPumpSetCmd(2);
                } else {
                    tv_more.setVisibility(View.VISIBLE);
                    ll_recycle.setVisibility(View.GONE);
                }

                //设置是否需要升级
                HorizontalRecyclerFragmentHelper.isHaveNewVersion(plantModel, mUpdate, true);

                //消息推送状态   0关   1开
                mPush.setChecked(plantModel.getPushStatus().equals("1"));
                GlideApp.with(getContext())
                        .load(plantModel.getPhoto())
                        .centerCrop()
                        .placeholder(R.mipmap.img_main_empty)
                        .into(mImage);
                setLed(plantModel);
//                if (timer==null){
//                    timer = new Timer();
//                }
//                if (task==null){
//                    task = new TimerTask() {
//                        @Override
//                        public void run() {
//                            HorizontalRecyclerFragmentHelper.setLedSwitch(mData);
//                            HorizontalRecyclerFragmentHelper.setLedMode(mData,mLedMode,mPresenter);
//                            getEquipmentData(mData);
//                        }
//                    };
//                }
//                timer.schedule(task, 2000, 30000);
                mPlantData.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        LogUtil.d(TAG, "ViewHolder的mPlantData的点击事件==============================：" + Common.Constance.H5_BASE + "product_data.html?id=" + plantModel.getId());
                        String url = Common.Constance.H5_BASE + "product_data.html?id=" + plantModel.getId();
                        PlantingDateAct.show(getContext(), url, plantModel);
                    }
                });
                mSecondSetting.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        plantModel.setPushStatus(mPush.isChecked() ? "1" : "0");
                        SecondSettingActivity.show(getContext(), plantModel);
                    }
                });

                mUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        HorizontalRecyclerFragmentHelper.update((Activity) getActivity(), plantModel);
                    }
                });

                mPush.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mPush.isChecked()) {
                            //推送开
                            //type:类型区分   1光照状态   2消息推送状态
                            //status：状态  0关  1开
                            mPresenter.setCheckBox(Account.getToken(), "2", "1", plantModel.getId());
                        } else {
                            //推送关
                            mPresenter.setCheckBox(Account.getToken(), "2", "0", plantModel.getId());
                        }
                    }
                });

            }


            private void getEquipmentData(EquipmentRspModel.ListBean plantModel) {
                if (TextUtils.isEmpty(plantModel.getPSIGN())) {
                    return;
                }
                PlantStatusModel model = new PlantStatusModel(1, "getStatus", 1, Integer.parseInt(plantModel.getPSIGN()),
                        1);
                String request = gson.toJson(model);
                LogUtil.d(TAG, "开始发送获取植物状态的设备名称：" + plantModel.getEquipName() + ",数据帧(getStatus)：" + request);
                if (!TextUtils.isEmpty(plantModel.getLTID())) {
                    LongTooth.request(plantModel.getLTID(), "longtooth", LongToothTunnel.LT_ARGUMENTS, request.getBytes(),
                            0, request.getBytes().length, null, new LongToothResponse(mPresenter, plantModel,
                                    isLedOn, mOffLine, mFramOffline, this));
                }
            }

            private void setLed(final EquipmentRspModel.ListBean plantModel) {

                mLed.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        UiTool.showLoading(getContext());
                        if (!isLedOn) {
                            mPresenter.setCheckBox(Account.getToken(), "1", "1", plantModel.getId());
                            LedSetModel model = new LedSetModel("On", plantModel.getPSIGN());
                            String request = gson.toJson(model);
                            LogUtil.d(TAG, "发送设备状态设置的数据帧（打开LED灯）：" + request);
                            LongTooth.request(plantModel.getLTID(), "longtooth", LongToothTunnel.LT_ARGUMENTS,
                                    request.getBytes(), 0, request.getBytes().length,
                                    null, new LongToothServiceResponseHandler() {
                                        @Override
                                        public void handleServiceResponse(LongToothTunnel longToothTunnel, String s, String s1,
                                                                          int i, byte[] bytes, LongToothAttachment longToothAttachment) {
                                            LogUtil.d(TAG, "LED handleServiceRespose:" + Thread.currentThread().getName());
                                            UiTool.hideLoading();
                                            if (bytes == null)
                                                return;
                                            String jsonContent = new String(bytes);
                                            LogUtil.d(TAG, "LED灯打开时数据帧返回：" + jsonContent);
                                            if (TextUtils.isEmpty(jsonContent) || !jsonContent.contains("CODE"))
                                                return;
                                            final LedSetRspModel plantStatusRspModel = gson.fromJson(jsonContent, LedSetRspModel.class);
                                            Run.onUiAsync(new Action() {
                                                @Override
                                                public void call() {
                                                    if (plantStatusRspModel.getCODE() == 0) {
//                                                        iv_bindExp.setVisibility(View.GONE);
                                                        Application.showToast(Application.getStringText(R.string.light_open_success));
                                                        isLedOn = true;
                                                        EventBus.getDefault().post(new MessageEvent(LED_ON, plantModel.getLTID()));
                                                        mLed.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.mipmap.img_light_open_horizontal,
                                                                0, 0);
                                                    } else {
//                                                        iv_bindExp.setVisibility(View.VISIBLE);
                                                        Application.showToast(Application.getStringText(R.string.light_open_failed_retry_later));
                                                    }
                                                }
                                            });
                                        }
                                    });
                        } else {
                            mPresenter.setCheckBox(Account.getToken(), "1", "0", plantModel.getId());
                            LedSetModel model = new LedSetModel("Off", plantModel.getPSIGN());
                            String request = gson.toJson(model);
                            LogUtil.d(TAG, "发送设备状态设置的数据帧（关闭LED灯）：" + request);
                            LongTooth.request(plantModel.getLTID(), "longtooth", LongToothTunnel.LT_ARGUMENTS, request.getBytes(), 0,
                                    request.getBytes().length,
                                    null, new LongToothServiceResponseHandler() {
                                        @Override
                                        public void handleServiceResponse(LongToothTunnel longToothTunnel, String s, String s1, int i, byte[] bytes, LongToothAttachment longToothAttachment) {
                                            UiTool.hideLoading();
                                            if (bytes == null)
                                                return;
                                            String jsonContent = new String(bytes);
                                            LogUtil.d(TAG, "LED灯关闭时数据帧返回：" + jsonContent);
                                            if (TextUtils.isEmpty(jsonContent) || !jsonContent.contains("CODE"))
                                                return;
                                            final LedSetRspModel plantStatusRspModel = gson.fromJson(jsonContent, LedSetRspModel.class);
                                            Run.onUiAsync(new Action() {
                                                @Override
                                                public void call() {
                                                    if (plantStatusRspModel.getCODE() == 0) {
//                                                        iv_bindExp.setVisibility(View.GONE);

                                                        Application.showToast(Application.getStringText(R.string.light_close_success));
                                                        EventBus.getDefault().post(new MessageEvent(LED_OFF, plantModel.getLTID()));
                                                        isLedOn = false;
                                                        mLed.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.mipmap.img_light_close_horizontal,
                                                                0, 0);
                                                    } else {
//                                                        iv_bindExp.setVisibility(View.VISIBLE);
                                                        Application.showToast(Application.getStringText(R.string.light_close_failed_retry_later));
                                                    }
                                                }
                                            });

                                        }
                                    });
                        }
                    }
                });

            }

            @OnLongClick(R.id.frame_root)
            boolean onItemLongClick() {
                AlertDialog.Builder deleteDialog = new AlertDialog.Builder(getContext());
                deleteDialog.setMessage(Application.getStringText(R.string.delete_will_reset_equipment_if_ensure));
                deleteDialog.setPositiveButton(getResources().getString(R.string.ensure), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mPresenter.deleteEquipment(mData.getId());
                    }
                });
                deleteDialog.setNegativeButton(getResources().getString(R.string.cancel), null);
                deleteDialog.show();
                return true;
            }

            @OnClick(R.id.img_setting)
            void onSettingClick() {
                isShowSetting = !isShowSetting;
                if (isShowSetting) {
                    LogUtil.d(TAG, "首页菜单打开deviceSeries:" + deviceSeries);
                    mSettingView.setVisibility(View.VISIBLE);
                    mSetting.setImageResource(R.mipmap.img_close_main_horizontal);
                } else {
                    LogUtil.d(TAG, "首页菜单关闭(deviceSeries):" + deviceSeries);
                    mSettingView.setVisibility(View.GONE);
                    mSetting.setImageResource(R.mipmap.img_item_setting);
                }

            }

            @OnClick(R.id.ll_recycle)
            void onRecycleClick() {
                UiTool.showLoading(getContext());
                iv_recycle = iv_waterRecycle;
                isPumpControl = true;
                if (isRecycleOn) {
                    LogUtil.d(TAG, "horizon pump state is on ,turn off pump");
                    sendPumpSetCmd(0);
                } else {
                    LogUtil.d(TAG, "horizon pump state is off ,turn on pump");
                    sendPumpSetCmd(1);
                }
//                Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.recycle_animation);
//                iv_waterRecycle.startAnimation(animation);
//
//                Timer timer = new Timer();
//                timer.schedule(new TimerTask() {
//                    @Override
//                    public void run() {
//                        Run.onUiAsync(new Action() {
//                            @Override
//                            public void call() {
//                                iv_waterRecycle.clearAnimation();
//                            }
//                        });
//                    }
//                }, 3000);
            }


            @OnClick(R.id.liner_refresh)
            void onRefreshClick() {
                //设置是否需要升级
                HorizontalRecyclerFragmentHelper.isHaveNewVersion(mData, mUpdate, true);
                Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.load_animation);
                mRefresh.startAnimation(animation);
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Run.onUiAsync(new Action() {
                            @Override
                            public void call() {
                                mRefresh.clearAnimation();
                            }
                        });
                    }
                }, 3000);
                getEquipmentData(mData);
            }

            @Override
            public void showError(int str) {

            }

            Dialog dialog;

            @Override
            public void showLoading() {
                dialog = UiTool.createLoadingDialog(getContext());
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }

            @Override
            public void hideLoading() {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }

            @Override
            public void onConnectionConflict() {
                Account.logOff(getContext());
                UiTool.onConnectionConflict(getContext());
            }

            @Override
            public void setPresenter(MainRecylerContract.Presenter presenter) {
                mPresenter = presenter;
            }

            private void init() {
                mTemperature.setText("- -");
                mWaterStatus.setText("- -");
                mLedMode.setText("- -");
                mEcStatus.setText("- -");
                mImgTemArrow.setImageResource(0);
            }

            @Override
            public void setDataSuccess(EquipmentDataModel model) {
                mRefresh.clearAnimation();
                iv_waterRecycle.clearAnimation();
                Log.d(TAG, "setDataSuccess: " + model.toString());
                mTemperature.setText(model.getDegree() + "°C");
                mEcStatus.setText(getStatus(model.getEstatus()));
                //如果不支持营养功能，则把图标设置为灰色
                if (getStatus(model.getEstatus()).equals(UNKNOWN)) {
                    mEcStatus.setCompoundDrawablesRelativeWithIntrinsicBounds(R.mipmap.img_ec_normal, 0, 0, 0);
                    mEcStatus.setTextColor(Color.parseColor("#dadada"));
                } else {
                    mEcStatus.setCompoundDrawablesRelativeWithIntrinsicBounds(R.mipmap.img_ec, 0, 0, 0);
                    mEcStatus.setTextColor(Color.parseColor("#9FD166"));
                }
                //如果不支持水位功能，则把图标设置为灰色
                if (HorizontalRecyclerFragmentHelper.getWaStatus(model.getWater()).equals(UNKNOWN)) {
                    mWaterStatus.setCompoundDrawablesRelativeWithIntrinsicBounds(R.mipmap.img_water_lever_normal, 0, 0, 0);
                    mWaterStatus.setTextColor(Color.parseColor("#dadada"));
                } else {
                    mWaterStatus.setCompoundDrawablesRelativeWithIntrinsicBounds(R.mipmap.img_water_lever, 0, 0, 0);
                    mWaterStatus.setTextColor(Color.parseColor("#FBB179"));
                }
                mWaterStatus.setText(HorizontalRecyclerFragmentHelper.getWaStatus(model.getWater()));
                mImgTemArrow.setVisibility(View.VISIBLE);
                if (model.getDstatus().equals("0")) {
                    //温度过低
                    mImgTemArrow.setImageResource(R.mipmap.img_trending_down);
                } else if (model.getDstatus().equals("2")) {
                    //温度过高
                    mImgTemArrow.setImageResource(R.mipmap.img_temperature_trending_up);
                } else {
                    mImgTemArrow.setImageResource(0);
                }
            }

            @Override
            public void deleteEquipmentSuccess() {
                HorizontalRecyclerFragment.this.mPresenter.getAllEquipments(Account.getToken(), "0", "0");
                timer.cancel();
                task.cancel();
                //通知MainActivity刷新页面
                EventBus.getDefault().post(new MessageEvent(MainActivity.MAIN_ACTIVITY_REFRESH));
                //通知verticalfragment停止task
                EventBus.getDefault().post(new MessageEvent(HORIZONTALRECYLER_DELETE_SUCCESS, mData.getLTID()));
                //通知Vertical刷新页面
                EventBus.getDefault().post(new MessageEvent(VeticalRecyclerFragment.VERTICAL_RECYLER_REFRESH));
                //设备设置为出厂状态
                LogUtil.d(TAG, "设备删除成功，开始发送恢复出厂设置的数据帧");
                HorizontalRecyclerFragmentHelper.equipmentReset(mData);
            }

            private String getStatus(String code) {
                switch (code) {
                    case "0":
                        return Application.getStringText(R.string.too_low);
                    case "1":
                        return Application.getStringText(R.string.normal);
                    case "2":
                        return Application.getStringText(R.string.too_high);
                    default:
                        return UNKNOWN;
                }
            }
        }

        private int times = 0;
        Timer timer = new Timer();

        class LongToothResponse implements LongToothServiceResponseHandler {

            private MainRecylerContract.Presenter mPresenter;
            private EquipmentRspModel.ListBean mPlantModel;
            private boolean isLedOn;
            private ImageView mOffLine;
            private FrameLayout mFrameOffLine;
            private boolean isResp = false;

            public LongToothResponse(MainRecylerContract.Presenter mPresenter, final EquipmentRspModel.ListBean plantModel,
                                     boolean isLedOn, final ImageView offLine, FrameLayout frameOffLine, final MyViewHolder myViewHolder) {
                this.mPresenter = mPresenter;
                mPlantModel = plantModel;
                this.isLedOn = isLedOn;
                this.mOffLine = offLine;
                this.mFrameOffLine = frameOffLine;
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        Log.d(TAG, "getStatus次数: " + times);
                        if (!isResp) {
                            times++;
                            if (TextUtils.isEmpty(plantModel.getPSIGN())) {
                                return;
                            }
                            PlantStatusModel model = new PlantStatusModel(1, "getStatus", 1, Integer.parseInt(plantModel.getPSIGN()),
                                    1);
                            String request = gson.toJson(model);
                            if (!TextUtils.isEmpty(plantModel.getLTID())) {
                                LongTooth.request(plantModel.getLTID(), "longtooth", LongToothTunnel.LT_ARGUMENTS, request.getBytes(),
                                        0, request.getBytes().length, null, LongToothResponse.this);
                            }
                        }
                        //如果没有三次没有数据返回，则认为设备离线
                        if (times > 2) {
                            times = 0;
                            offLine();
                        }
                    }
                };
                timer.schedule(task, 6000);
            }

            private void offLine() {
                Run.onUiAsync(new Action() {
                    @Override
                    public void call() {
                        mOffLine.setVisibility(View.VISIBLE);
                        mFrameOffLine.setVisibility(View.VISIBLE);
                    }
                });
            }

            private void onLine() {
                Run.onUiAsync(new Action() {
                    @Override
                    public void call() {
                        EventBus.getDefault().post(new MessageEvent(VeticalRecyclerFragment.EQUIPMENT_LINE_ON, mPlantModel.getLTID()));
                        mOffLine.setVisibility(View.INVISIBLE);
                        mFrameOffLine.setVisibility(View.INVISIBLE);
                    }
                });
            }

            @Override
            public void handleServiceResponse(LongToothTunnel longToothTunnel, String s, String s1, int i,
                                              byte[] bytes, LongToothAttachment longToothAttachment) {
                times = 0;
                isResp = true;
                if (bytes == null) {
                    offLine();
                    return;
                }
                String jsonContent = new String(bytes);
                if (!jsonContent.contains("CODE")) {
                    offLine();
                    return;
                }
                Log.d(TAG, "handleServiceResponse: " + mPlantModel.getLTID() + ":" + mPlantModel.getEquipName() + ":" + jsonContent);
                PlantStatusRspModel plantStatusRspModel = gson.fromJson(jsonContent, PlantStatusRspModel.class);
                if (plantStatusRspModel.getCODE() == 0) {
                    onLine();
                    //获取数据成功
                    String temperature = plantStatusRspModel.getTemp();
                    String wagerState = plantStatusRspModel.getWaterStat();
                    String Ec = plantStatusRspModel.getEC();//植物的营养值
                    String isLihtOn = isLedOn ? "0" : "1";
                    if (mPresenter == null || temperature == null || wagerState == null || Ec == null) {
                        return;
                    }
                    mPresenter.setData(Account.getToken(), mPlantModel.getMac(), temperature, wagerState, isLihtOn, Ec);
                }
            }
        }
    }

    /**
     * 水循环泵操作
     */
    public void sendPumpSetCmd(int pumpState) {
        JSONObject pumpSet = null;
        try {
            pumpSet = new JSONObject();
            pumpSet.put("CMD", ConstUtil.CMD_PUMP_SET);
            pumpSet.put("UUID", deviceUUID);
            if (pumpState == 0) {//关闭
                pumpSet.put("SWITCH", "Off");
                pumpSet.put("DURATION", 100);//100S
            } else if (pumpState == 1) {//打开
                pumpSet.put("SWITCH", "On");
                pumpSet.put("DURATION", 100);//100S
            } else if (pumpState == 2) {//查询

            }
            String pumpSetCmd = String.valueOf(pumpSet);//PUMPSET CMD:{"CMD":"pumpSet","UUID":1534061416,"SWITCH":"On","DURATION":100}
            LogUtil.d(TAG, "PUMPSET CMD:" + pumpSetCmd + ",deviceLtid:" + deviceLtid + ",deviceUuid:" + deviceUUID);
            MyLongToothService.sendDataFrame(getActivity(), ConstUtil.OP_PUMP_SET, deviceLtid, pumpSetCmd);
            MyLongToothService.setMyLongToothListener(this);
        } catch (JSONException e) {
            e.printStackTrace();
            LogUtil.d(TAG, "sendPumpSetCmd json exp:" + e.getMessage());
        }
    }

    private Timer mRestartTimer = null;
    private MyPumpSetHandler mHandler = new MyPumpSetHandler(this);

    private class MyPumpSetHandler extends Handler {
        private WeakReference<HorizontalRecyclerFragment> weakReference = null;

        private MyPumpSetHandler(HorizontalRecyclerFragment mFragment) {
            weakReference = new WeakReference<HorizontalRecyclerFragment>(mFragment);
        }

        @Override
        public void handleMessage(Message msg) {
            HorizontalRecyclerFragment horizontalRecyclerFragment = weakReference.get();
            if (horizontalRecyclerFragment == null) {
                return;
            }
            LogUtil.d(TAG, "handle message……………………………………………………………………:" + msg.obj.toString());
            int code = -1;
            JSONObject joMsgRes = null;
            try {
                String msgRes = msg.obj.toString();
                if (msgRes.contains("==")) {
                    String[] resArr = msgRes.split("==");
                    joMsgRes = new JSONObject(resArr[0]);
                    returnDeviceLtid = resArr[1];
                    code = joMsgRes.getInt("CODE");
                    LogUtil.d(TAG, "pump message:" + resArr[0] + ",ltid:" + resArr[1]);

                    switch (msg.what) {
                        case ConstUtil.GET_DEVICE_FIRMWARE_VERSION_VERSION://设备重启，发送查询设备版本号的数据帧（之前是查询设备的工作状态），以确保设备重启成功
                            String sVer = joMsgRes.getString("softVer");
                            if (code == 0) {
                                LogUtil.d(TAG,"3ppp查询设备版本号成功："+sVer+",ltid:"+returnDeviceLtid);
                                for(int i = 0;i < checkedUpdateFirvDeviceList.size();i++){
                                    if(returnDeviceLtid.equals(checkedUpdateFirvDeviceList.get(i).getLtid())){
                                        LogUtil.d(TAG,"3ppp版本检测成功了========："+sVer);
                                        if(checkedUpdateFirvDeviceList.get(i).getType() == 0){//升级之前的版本号
                                            LogUtil.d(TAG,"3ppp版本检测成功了");
                                            checkedUpdateFirvDeviceList.get(i).setOldVerSoft(sVer);
                                        }else{//升级之后的版本号

                                            checkedUpdateFirvDeviceList.get(i).setNewVerSoft(sVer);
                                            LogUtil.d(TAG, "haha设备3ppp升级成功并且重启成功啦&&&…………………………………………………………：" + returnDeviceLtid);
                                            String oldVer = checkedUpdateFirvDeviceList.get(i).getOldVerSoft();
                                            String newVer = checkedUpdateFirvDeviceList.get(i).getNewVerSoft();
                                            LogUtil.d(TAG,"3ppp升级成功啦，对比版本号oldVer："+oldVer+",newVer:"+newVer);
                                            boolean verFlag = compareVer(oldVer,newVer);
                                            if (verFlag) {
                                                if (!restartSuccessList.contains(returnDeviceLtid)) {
                                                    //设备升级成功后添加升级成功后的长牙id
                                                    restartSuccessList.add(returnDeviceLtid);
                                                }
                                                progress = 100;
                                                updateStateType = 6;
                                            }else{
                                                progress = 95;
                                                updateStateType = 2;
                                            }
                                            checkedUpdateFirvDeviceList.get(i).setState(updateStateType);
                                            checkedUpdateFirvDeviceList.get(i).setProgress(progress);
                                            checkedUpdateFirvDeviceList.get(i).setUpgradeSuccess(true);
                                            if (checkedUpdateFirvDeviceList.size() == restartSuccessList.size()) {
                                                isUpgrading = false;
                                                LogUtil.d(TAG, "设备升级3ppp成功并且重启成功了，选择升级设备的数量全部升级重启成功");
                                                tv_updateDeviceFirmware.setEnabled(true);
                                                if (mRestartTimer != null) {
                                                    mRestartTimer.cancel();
                                                    mRestartTimer = null;
                                                    LogUtil.d(TAG, "3ppp设备升级并重启成功！关闭定时器");
                                                }
                                                LogUtil.d(TAG, "3ppp queryVersion success and getOpMode success##############################################:" + returnDeviceLtid);
                                            } else {
                                                LogUtil.d(TAG, "3ppp设备升级成功并且重启成功了，部分重启成功");
                                            }
                                        }
                                        firmwareUpdateAdapter.notifyDataSetChanged();
                                    }
                                }
                            } else {
                                LogUtil.d(TAG, "3ppp设备升级成功但是设备重启失败啦…………………………………………………………:" + returnDeviceLtid+",code:"+code);
                            }
                            break;
                        case ConstUtil.PUMP_SET_SUCCESS:
                            UiTool.hideLoading();
                            //{ "SWITCH": "Off", "DURATION": 0, "CODE": 0 }
                            if (code == 0) {
//                                iv_bindExp.setVisibility(View.GONE);
                                String pumpState = joMsgRes.getString("SWITCH");
                                LogUtil.d(TAG, "code == 0");
                                if (pumpState.equals("Off")) {
                                    isRecycleOn = false;
                                    iv_recycle.setImageResource(R.mipmap.h_water_recycle_off);
                                    if (isPumpControl) {
                                        Application.showToast(Application.getStringText(R.string.pump_set_off_success_text));
                                    } else {
                                        LogUtil.d(TAG, "水泵状态查询成功：关闭");
                                    }
                                    EventBus.getDefault().post(new MessageEvent(PUMP_OFF, returnDeviceLtid));
                                } else {
                                    isRecycleOn = true;
                                    iv_recycle.setImageResource(R.mipmap.h_water_recycle_on);
                                    if (isPumpControl) {
                                        Application.showToast(Application.getStringText(R.string.pump_set_on_success_text));
                                    } else {
                                        LogUtil.d(TAG, "水泵状态查询成功：开启");
                                    }
                                    EventBus.getDefault().post(new MessageEvent(PUMP_ON, returnDeviceLtid));
                                }
                            } else {
                                LogUtil.d(TAG, "水泵设置的数据帧的返回值code == 1");
//                                iv_bindExp.setVisibility(View.VISIBLE);
                                if (isPumpControl) {
                                    Application.showToast(Application.getStringText(R.string.pump_set_fail_text));
                                }
                            }
                            break;
                        case ConstUtil.CHECK_UPDATE_SUCCESS:
                            for (int i = 0; i < deviceSize; i++) {
                                if (listBeans.get(i).getLTID().equals(returnDeviceLtid)) {
                                    if (code == 501) {
                                        int updateSize = updateDeviceList.size();
                                        if (updateSize > 0) {
                                            for (int j = 0; j < updateSize; j++) {
                                                if (returnDeviceLtid.equals(updateDeviceList.get(j).getLtid())) {
                                                    LogUtil.d(TAG, "3ppp(isUpdate数据帧返回有新版本)固件更新列表中有相同的设备，不添加到列表中NNNNNNNNNNN");
                                                    hasNewFirmwarmDeviceInList = true;
                                                } else {
                                                    LogUtil.d(TAG, "3ppp（isUpdate数据帧返回有新版本）固件更新列表中没有相同的设备，添加到列表中YYYYYYYYYYY");
                                                    hasNewFirmwarmDeviceInList = false;
                                                    break;
                                                }
                                            }
                                        } else {
                                            LogUtil.d(TAG, "3ppp（有新版本isUpdate数据帧返回）初始状态设备列表中没有设备固件更新initial");
                                            LogUtil.d(TAG, "3ppp（isUpdate数据帧返回）固件更新列表中没有相同的设备，添加到列表中YYYYYYYYYYY，开始添加啦…………………………………………");
                                            String deviceName = listBeans.get(i).getEquipName();
                                            String dUUID =  listBeans.get(i).getPSIGN();
                                            String dLtid = listBeans.get(i).getLTID();
                                            String imgUrl = listBeans.get(i).getPhoto();
                                            DeviceFirmwareModel dfm = new DeviceFirmwareModel(0,0,0,deviceName, dUUID, dLtid, imgUrl,"0.0.0","0.0.0", true, false);
                                            dfmList.add(dfm);
                                            updateDeviceList.add(dfm);
                                            sendQueryDeviceFirmrevCMD(0,dUUID,dLtid);
                                            newVersionSize++;
                                        }
                                        if (!hasNewFirmwarmDeviceInList) {
                                            LogUtil.d(TAG, "3ppp（isUpdate数据帧返回）固件更新列表中没有相同的设备，添加到列表中YYYYYYYYYYY，开始添加啦…………………………………………");
                                            String deviceName = listBeans.get(i).getEquipName();
                                            String dUUID =  listBeans.get(i).getPSIGN();
                                            String dLtid = listBeans.get(i).getLTID();
                                            String imgUrl = listBeans.get(i).getPhoto();

                                            DeviceFirmwareModel dfm = new DeviceFirmwareModel(0,0,0,deviceName,dUUID,dLtid ,imgUrl ,"0.0.0","0.0.0", true, false);
                                            dfmList.add(dfm);
                                            updateDeviceList.add(dfm);
                                            sendQueryDeviceFirmrevCMD(0,dUUID,dLtid);
                                            newVersionSize++;
                                        }
                                    } else {
                                        LogUtil.d(TAG, "3ppp（isUpdate数据帧返回）check update handler===没有可以升级的版本====================>" + code);
                                        oldVersionSize++;
                                    }
                                }
                            }
                            LogUtil.d(TAG, "3ppp（isUpdate数据帧返回）固件更新列表的size：" + upgradeStateSuccessList.size() + ",newVersionSize:" + newVersionSize);
                            if (newVersionSize > 0) {
                                LogUtil.d(TAG, "3ppp（isUpdate数据帧返回）有新版本的固件可以升级:" + "newVersionSize:" + newVersionSize + "+oldVersionSize:" + oldVersionSize + ",=" + deviceSize);
                                if (isFirstShowFirmwareDialog) {
                                    LogUtil.d(TAG, "3ppp（isUpdate数据帧返回）首次弹出检测设备固件更新的对话框，……………………………………");
                                    isFirstShowFirmwareDialog = false;
                                    setUpdateFirmAdapter();
                                } else {
                                    LogUtil.d(TAG, "3ppp（isUpdate数据帧返回）不是首次弹出检测设备固件更新的对话框，………………………………");
                                    firmwareUpdateAdapter.notifyDataSetChanged();
                                }
                            } else {
                                LogUtil.d(TAG, "3ppp（isUpdate数据帧返回）新版本的固件设备数量为0:oldVersionSize:" + oldVersionSize + ",newVersionSize：" + newVersionSize);
                            }
                        break;
                        case ConstUtil.DEVICE_UPDATING:
                            if (code == 0) {
                                for (int i = 0; i < checkedUpdateFirvDeviceList.size(); i++) {
                                    if (checkedUpdateFirvDeviceList.get(i).getLtid().equals(returnDeviceLtid)) {
                                        LogUtil.d(TAG, "3ppp（update数据帧返回）发送升级数据帧的返回code = 0 update device ltid:" + returnDeviceLtid);
                                        sendUpgradeCMDSucessList.add(checkedUpdateFirvDeviceList.get(i));
                                    }
                                }
                                //升级命令发送成功，开启定时器
                                if (isUpgradeCmdSendSuccess) {
                                    LogUtil.d(TAG, "3ppp（update数据帧返回）升级命令发送成功,开启定时器");
                                    isUpgradeCmdSendSuccess = false;
                                    //升级命令发送成功，每隔一段时间检测设备升级状态
                                    mUpdateTimer = new Timer();
                                    mUpdateTimer.schedule(new TimerTask() {
                                        @Override
                                        public void run() {
                                            LogUtil.d(TAG, "3ppp（update数据帧返回）升级命令发送成功,定时器启动了………………开始发送查询升级状态的数据帧");

                                            if (sendUpgradeCMDSucessList.size() > 0) {//发送升级命令成功的设备
                                                boolean hasUpgradeCmd = false;
                                                for (int i = 0; i < sendUpgradeCMDSucessList.size(); i++) {
                                                    if (upgradeStateSuccessList.size() > 0) {//设备升级状态为升级成功的设备列表
                                                        for (int j = 0; j < upgradeStateSuccessList.size(); j++) {
                                                            if (upgradeStateSuccessList.get(j).equals(sendUpgradeCMDSucessList.get(i).getLtid())) {
                                                                hasUpgradeCmd = true;
                                                                break;
                                                            } else {
                                                                hasUpgradeCmd = false;
                                                            }
                                                        }
                                                        if (hasUpgradeCmd) {
                                                            sendUpgradeCMDSucessList.remove(sendUpgradeCMDSucessList.get(i));
                                                        } else {
                                                            sendUpdateFirmwareCMD(2, sendUpgradeCMDSucessList.get(i).getLtid(), sendUpgradeCMDSucessList.get(i).getUuid());
                                                        }
                                                    } else {
                                                        sendUpdateFirmwareCMD(2, sendUpgradeCMDSucessList.get(i).getLtid(), sendUpgradeCMDSucessList.get(i).getUuid());
                                                    }
                                                }
                                            }
                                        }
                                    }, 5*1000L, 5*1000L);
                                }
                            } else {
                                for (int i = 0; i < checkedUpdateFirvDeviceList.size(); i++) {
                                    if (checkedUpdateFirvDeviceList.get(i).getLtid().equals(returnDeviceLtid)) {
                                        LogUtil.d(TAG, "3ppp（update数据帧返回）发送升级数据帧 update device fail ltid:" + returnDeviceLtid);
                                        sendUpgradeCMDFailList.add(checkedUpdateFirvDeviceList.get(i));
                                    }
                                }
                                LogUtil.d(TAG, "3ppp（update数据帧返回）发送升级数据帧的返回code update device ltid:" + returnDeviceLtid + ",code=" + code+",sendUpgradeCMDFailList:"+new Gson().toJson(sendUpgradeCMDFailList));
                            }

                            break;
                        case ConstUtil.DEVICE_UPDATE_STATE:
                            LogUtil.d(TAG, "3ppp(checkUpdateStat数据帧返回) DEVICE_UPDATE_STATE&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
                            final int updateStat = joMsgRes.getInt("updateStat");
                            if (code == 0) {
                                for (int i = 0; i < checkedUpdateFirvDeviceList.size(); i++) {
                                    if (checkedUpdateFirvDeviceList.get(i).getLtid().equals(returnDeviceLtid)) {
                                        if (updateStat == 1) {//升级成功
                                            progress += 5;
                                            if (progress > 50) {
                                                progress = 60;
                                            }
                                            updateStateType = 1;
                                            LogUtil.d(TAG, returnDeviceLtid + "3ppp(checkUpdateStat数据帧返回)升级中…………………………………………");
                                        } else if (updateStat == 2) {//最近一次升级成功
                                            //升级状态为成功
                                            updateStateType = 2;
                                            progress = 70;
                                            if (!upgradeStateSuccessList.contains(returnDeviceLtid)) {
                                                upgradeStateSuccessList.add(returnDeviceLtid);
                                            }
                                            //对升级状态为成功的设备进行定时发送检测设备状态的数据帧（设备升级成功需要重启，检测重启成功的标志是发送检测设备状态的数据帧有返回）
                                            if (isRestartTimerOn) {
                                                isRestartTimerOn = false;
                                                if (mRestartTimer == null) {
                                                    LogUtil.d(TAG, "3ppp（checkUpdateStat数据帧）定时器第一次开启(checkUpdateStat数据帧返回)…………………………………………");
                                                    mRestartTimer = new Timer();
                                                    mRestartTimer.schedule(new TimerTask() {
                                                        @Override
                                                        public void run() {
                                                            if (restartSuccessList.size() > 0) {
                                                                //重启成功了之后，删掉之前升级状态为成功的设备
                                                                LogUtil.d(TAG, "3ppp(checkUpdateStat数据帧)重启成功的设备，升级状态为成功的设备");
                                                                for (int u = 0; u < restartSuccessList.size(); u++) {
                                                                    if(upgradeStateSuccessList.size() > 0){
                                                                        for (int w = 0; w < upgradeStateSuccessList.size(); w++) {
                                                                            if (restartSuccessList.get(u).equals(upgradeStateSuccessList.get(w))) {
                                                                                upgradeStateSuccessList.remove(upgradeStateSuccessList.get(w));
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                            if (upgradeStateSuccessList.size() > 0) {
                                                                for (int j = 0; j < upgradeStateSuccessList.size(); j++) {
                                                                    for (int k = 0; k < checkedUpdateFirvDeviceList.size(); k++) {
                                                                        if (upgradeStateSuccessList.get(j).equals(checkedUpdateFirvDeviceList.get(k).getLtid())) {
                                                                            progress += 5;
                                                                            if (progress > 88) {
                                                                                progress = 90;
                                                                            }

                                                                            String upgradeDeviceLtid = checkedUpdateFirvDeviceList.get(k).getLtid();
                                                                            String devUuid = checkedUpdateFirvDeviceList.get(k).getUuid();
                                                                            checkedUpdateFirvDeviceList.get(k).setType(1);
                                                                            sendQueryDeviceFirmrevCMD(checkedUpdateFirvDeviceList.get(k).getType(),devUuid,upgradeDeviceLtid);//发送检测设备固件版本号

                                                                            final int index = k;
                                                                            Run.onUiSync(new Action() {
                                                                                @Override
                                                                                public void call() {
                                                                                    LogUtil.d(TAG, Thread.currentThread().getName() + "<<3ppp(checkUpdateStat数据帧返回)每隔10s通知一次适配器刷新进度：" + progress);
                                                                                    checkedUpdateFirvDeviceList.get(index).setState(updateStateType);
                                                                                    checkedUpdateFirvDeviceList.get(index).setProgress(progress);
                                                                                    firmwareUpdateAdapter.notifyDataSetChanged();
                                                                                }
                                                                            });
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }, 18 * 1000L, 10 * 1000L);
                                                }
                                            }

                                            LogUtil.d(TAG, "3ppp升级状态为成功(checkUpdateStat数据帧返回)……………………………………………………………………" + returnDeviceLtid);
                                            //设备升级成功，要进行重启，定时获取工作模式，如果获取工作模式成功就说明设备重启成功
                                        } else if (updateStat == 3) {//最近一次升级失败
                                            updateStateType = 3;//升级失败

                                            if (!upgradeStateFailList.contains(returnDeviceLtid)) {
                                                upgradeStateFailList.add(returnDeviceLtid);
                                            }

                                            LogUtil.d(TAG,"3ppp最近一次升级失败(checkUpdateStat数据帧返回)……………………………………" +returnDeviceLtid );
                                            tv_updateDeviceFirmware.setEnabled(true);
                                            progress = 0;
                                            //选择升级的设备列表的大小等于升级状态为失败的设备列表数量
                                            LogUtil.d(TAG,"3ppp(最近一次升级失败(checkUpdateStat数据帧返回)checkedUpdateFirvDeviceList:"+checkedUpdateFirvDeviceList.size()+",upgradeStateFailList:"+upgradeStateFailList.size());
                                            if(checkedUpdateFirvDeviceList.size() == upgradeStateFailList.size()){
                                                if (mUpdateTimer != null) {
                                                    LogUtil.d(TAG, "3ppp最近一次升级失败（选择升级的设备列表的数量等于升级状态失败的说设备列表的数量）……………(checkUpdateStat数据帧返回)………………………mUpdateTimer cancel");
                                                    mUpdateTimer.cancel();
                                                    mUpdateTimer = null;
                                                }
                                            }
                                        }
                                        checkedUpdateFirvDeviceList.get(i).setState(updateStateType);
                                        checkedUpdateFirvDeviceList.get(i).setProgress(progress);
                                        firmwareUpdateAdapter.notifyDataSetChanged();
                                    }
                                }
                            }else{
                                LogUtil.d(TAG,"3ppp（checkUpdateStat数据帧返回的code值为："+code);
                            }


                            break;
                        default:
                            break;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                LogUtil.d(TAG, "hander EXP:" + e.getMessage());
            }
        }
    }

    /**
     * 比较设备版本号
     * @param oldVer
     * @param newVer
     * @return true为升级成功，false为升级失败
     */
    private boolean  compareVer(String oldVer, String newVer) {
        LogUtil.d(TAG,"3ppp compareVer:(oldVer)："+oldVer+",newVer:"+newVer);
        if(TextUtils.isEmpty(oldVer) && TextUtils.isEmpty(newVer)){
            LogUtil.d(TAG,"oldVer and newVer is null");
            return false;
        }
        if(oldVer.contains(".") && newVer.contains(".")){
            String oldStr = oldVer.replaceAll("[.]", "");
            String newStr = newVer.replaceAll("[.]", "");
            int oldSoftVer = Integer.parseInt(oldStr);
            int newSoftVer = Integer.parseInt(newStr);
            LogUtil.d(TAG,"升级之前和之后的版本号进行对比：oldSoftVer:"+oldSoftVer+",newSoftVer:"+newSoftVer);
            if (newSoftVer > oldSoftVer) {
                return true;
            }else if(newSoftVer == oldSoftVer){
                return false;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }

    /**
     * 发送查询设备版本号
     * @param type
     * @param dUUID
     * @param dLtid
     */
    private void sendQueryDeviceFirmrevCMD(int type,String dUUID, String dLtid) {
        BindEquipmentModel model = new BindEquipmentModel(ConstUtil.CMD_FIRMWARE_VERSION, dUUID);
        final String verCMD = gson.toJson(model);
        if(type == 0){
            LogUtil.d(TAG,"3ppp开始发送固件升级之前（before）的版本号的数据帧");
            LogUtil.d(TAG, "3ppp开始发送固件升级之前（before）的版本号的数据帧发送查询设备固件版本的的CMD:" + verCMD+",ltid:"+ dLtid);
        }else{
            LogUtil.d(TAG,"3ppp开始发送固件升级之后（after）的版本号的数据帧");
            LogUtil.d(TAG, "3ppp开始发送固件升级之后（after）的版本号的数据帧发送查询设备固件版本的的CMD:" + verCMD+",ltid:"+ dLtid);
        }
        MyLongToothService.sendDataFrame(getActivity(),ConstUtil.OP_DEVICE_FIRMWARE_VERSION,dLtid,verCMD);
    }

    class FirmwareUpdateAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return updateDeviceList.size();
        }

        @Override
        public Object getItem(int position) {
            return updateDeviceList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itmeView = convertView;
            FirmwareViewHolder vm = null;

            if (itmeView == null) {
                itmeView = LayoutInflater.from(getContext()).inflate(R.layout.item_update_details, parent, false);
                vm = new FirmwareViewHolder();

                vm.tv_firmwareDeviceName = itmeView.findViewById(R.id.tv_updateDeviceName);
                vm.cb = itmeView.findViewById(R.id.cb_updateFirmware);
                vm.pb = itmeView.findViewById(R.id.pb_firmwareUpdate);
                vm.tv_state = itmeView.findViewById(R.id.tv_state);
                vm.iv_firmwareDevice = itmeView.findViewById(R.id.iv_firmwareDevice);

                itmeView.setTag(vm);
            } else {
                vm = (FirmwareViewHolder) itmeView.getTag();
            }

            final DeviceFirmwareModel dfm = dfmList.get(position);

            vm.tv_firmwareDeviceName.setText(dfm.getDeviceName());
            vm.cb.setChecked(dfm.isChecked());
            switch (dfm.getState()){
                case 1:
                    vm.tv_state.setText("Downloading");

                    vm.pb.setVisibility(View.VISIBLE);
                    vm.cb.setEnabled(false);
                    break;
                case 2:
                    vm.tv_state.setText("Restarting");

                    vm.pb.setVisibility(View.VISIBLE);
                    vm.cb.setEnabled(false);
                    break;
                case 3:
                    vm.tv_state.setText("Failed");
                    dfm.setUpgradeSuccess(false);
                    vm.cb.setEnabled(true);
                    vm.pb.setVisibility(View.INVISIBLE);
                    break;
                case 4:
                    vm.tv_state.setText("Preparing");

                    vm.pb.setVisibility(View.VISIBLE);
                    vm.cb.setEnabled(false);
                    break;
                case 6:
                    dfm.setUpgradeSuccess(true);
                    vm.tv_state.setText("Success");

                    vm.pb.setVisibility(View.VISIBLE);
                    vm.cb.setEnabled(false);
                    break;
                    default:
                        vm.cb.setEnabled(true);
                        vm.pb.setVisibility(View.INVISIBLE);
                        break;
            }

            vm.pb.setProgress(dfm.getProgress());
            vm.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        LogUtil.d(TAG, "Checked");
                        dfm.setChecked(true);
                    } else {
                        dfm.setChecked(false);
                        LogUtil.d(TAG, "unChecked");
                    }
                }
            });

            GlideApp.with(getContext())
                    .load(dfm.getPicUrl())
                    .centerCrop()
                    .placeholder(R.mipmap.img_main_empty)
                    .into(vm.iv_firmwareDevice);
            LogUtil.d(TAG, "需要升级的设备信息：" + new Gson().toJson(updateDeviceList));
            return itmeView;
        }

        class FirmwareViewHolder {
            TextView tv_firmwareDeviceName;
            CheckBox cb;
            ProgressBar pb;
            TextView tv_state;
            ImageView iv_firmwareDevice;
        }
    }
    private void setUpdateFirmAdapter() {
        final AlertDialog upgradeDialog = new AlertDialog.Builder(getActivity()).create();
        View view = getLayoutInflater().inflate(R.layout.item_update_firmware, null);
        ListView lv = view.findViewById(R.id.lv_device_firmware);
        tv_cancelUpdateFirmware = view.findViewById(R.id.tv_cancel);
        tv_updateDeviceFirmware = view.findViewById(R.id.tv_update);
        LogUtil.d(TAG, "check device update success 501(deviceSize):" + deviceSize + ",newVersionSize:" + newVersionSize + ",oldVersionSize:" + oldVersionSize);

        upgradeDialog.setView(view);
        upgradeDialog.setCancelable(false);

        firmwareUpdateAdapter = new FirmwareUpdateAdapter();
        lv.setAdapter(firmwareUpdateAdapter);

        tv_cancelUpdateFirmware.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upgradeDialog.dismiss();
            }
        });

        tv_updateDeviceFirmware.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (updateDeviceList != null && updateDeviceList.size() > 0) {

                    LogUtil.d(TAG, "需要升级的设备信息(updateDeviceList):" + new Gson().toJson(updateDeviceList));
                    for (int i = 0; i < updateDeviceList.size(); i++) {
                        if (updateDeviceList.get(i).isChecked()) {
                            int checkSize = checkedUpdateFirvDeviceList.size();
                            boolean hasCheckDevice = false;//是否有选中升级的设备
                            if (checkSize > 0) {
                                for (int j = 0; j < checkSize; j++) {
                                    if (checkedUpdateFirvDeviceList.get(j).getLtid().equals(updateDeviceList.get(i).getLtid())) {
                                        hasCheckDevice = true;
                                    } else {
                                        hasCheckDevice = false;
                                        break;
                                    }
                                }
                                if (!hasCheckDevice) {//有选中升级的设备
                                    checkedUpdateFirvDeviceList.add(updateDeviceList.get(i));
                                }
                            } else {
                                checkedUpdateFirvDeviceList.add(updateDeviceList.get(i));
                            }
                        } else {
                            boolean hasUnCheckedDevice = false;//是否有没有选中升级的设备
                            int uncheckSize = uncheckedUpdateFirvDeviceList.size();
                            if (uncheckSize > 0) {
                                for (int k = 0; k < uncheckSize; k++) {
                                    if (uncheckedUpdateFirvDeviceList.get(k).getLtid().equals(updateDeviceList.get(i).getLtid())) {
                                        hasUnCheckedDevice = true;
                                    } else {
                                        hasUnCheckedDevice = false;
                                        break;
                                    }
                                }
                                if (!hasUnCheckedDevice){
                                    uncheckedUpdateFirvDeviceList.add(updateDeviceList.get(i));
                                }
                            } else {
                                uncheckedUpdateFirvDeviceList.add(updateDeviceList.get(i));
                            }
                        }
                    }
                    if(isUpgrading){
                        if(restartSuccessList.size() == checkedUpdateFirvDeviceList.size()){
                            tv_updateDeviceFirmware.setEnabled(true);
                            updateStateType = 6;
                            Application.showToast(R.string.upgrade_checked_device_success_text);
                        }else{
                            tv_updateDeviceFirmware.setEnabled(false);
                            if(restartSuccessList.size() > 0){

                            }else{
                                for(int i = 0;i < checkedUpdateFirvDeviceList.size();i++){
                                    updateStateType = 4;
                                    checkedUpdateFirvDeviceList.get(i).setState(updateStateType);
                                }
                            }
                        }
                        firmwareUpdateAdapter.notifyDataSetChanged();
                    }

                    if (checkedUpdateFirvDeviceList.size() > 0) {
                        for (int u = 0; u < checkedUpdateFirvDeviceList.size(); u++) {
                            if(!checkedUpdateFirvDeviceList.get(u).isUpgradeSuccess()){
                                LogUtil.d(TAG,"该设备待升级了，开始发送升级的数据帧啦："+checkedUpdateFirvDeviceList.get(u).getLtid()+",设备名称："+checkedUpdateFirvDeviceList.get(u).getDeviceName());
                                sendUpdateFirmwareCMD(1, checkedUpdateFirvDeviceList.get(u).getLtid(), checkedUpdateFirvDeviceList.get(u).getUuid());
                            }else{
                                LogUtil.d(TAG,"该设备已经升级成功了，不要再次发送升级的数据帧啦："+checkedUpdateFirvDeviceList.get(u).getLtid()+",设备名称："+checkedUpdateFirvDeviceList.get(u).getDeviceName());
                            }
                        }

                        LogUtil.d(TAG,"3ppppp checkedUpdateFirvDeviceList.size:"+checkedUpdateFirvDeviceList.size()+",restartSuccessList.size():"+restartSuccessList.size());

                    } else {
                        Application.showToast(R.string.update_device_not_checked_text);
                    }
                }
            }
        });
        upgradeDialog.show();
    }


    private void sendUpdateFirmwareCMD(int type, String ltid, String uuid) {
        BindEquipmentModel cmd = null;
        if (type == 1) {//升级
            cmd = new BindEquipmentModel(ConstUtil.CMD_UPDATE_FIMEWARE, uuid);
            String deviceUpdateCmd = gson.toJson(cmd);
            LogUtil.d(TAG, "发送升级数据帧（update）%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%：" + deviceUpdateCmd);
            MyLongToothService.sendDataFrame(getActivity(), ConstUtil.OP_UPDATE_FIRMWARM, ltid, deviceUpdateCmd);
            MyLongToothService.setMyLongToothListener(this);
        } else {
            //检查升级状态
            cmd = new BindEquipmentModel(ConstUtil.CMD_UPDATE_STATE, uuid);
            String deviceUpdateCmd = gson.toJson(cmd);
            LogUtil.d(TAG, "发送检查升级状态的数据帧（checkUpdateStat）%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%：" + deviceUpdateCmd);
            MyLongToothService.sendDataFrame(getActivity(), ConstUtil.OP_UPDATE_STATE, ltid, deviceUpdateCmd);
            MyLongToothService.setMyLongToothListener(this);
        }


    }
}
