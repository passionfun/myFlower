package bocai.com.yanghuajien.ui.intelligentPlanting;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import org.eclipse.jetty.security.authentication.ClientCertAuthenticator;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import bocai.com.yanghuajien.R;
import bocai.com.yanghuajien.base.Activity;
import bocai.com.yanghuajien.base.Application;
import boc.com.imgselector.GlideApp;
import bocai.com.yanghuajien.base.RecyclerAdapter;
import bocai.com.yanghuajien.base.common.Common;
import bocai.com.yanghuajien.base.presenter.PrensterFragment;
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
import bocai.com.yanghuajien.ui.main.MainActivity;
import bocai.com.yanghuajien.util.ConstUtil;
import bocai.com.yanghuajien.util.LogUtil;
import bocai.com.yanghuajien.util.UiTool;
import bocai.com.yanghuajien.util.persistence.Account;
import bocai.com.yanghuajien.util.widget.EmptyView;
import butterknife.BindView;
import butterknife.OnClick;
import xpod.longtooth.LongTooth;
import xpod.longtooth.LongToothAttachment;
import xpod.longtooth.LongToothServiceResponseHandler;
import xpod.longtooth.LongToothTunnel;

import static bocai.com.yanghuajien.ui.intelligentPlanting.HorizontalRecyclerFragment.HORIZONTALRECYLER_VISIABLE;
import static bocai.com.yanghuajien.ui.intelligentPlanting.HorizontalRecyclerFragmentHelper.LED_OFF;
import static bocai.com.yanghuajien.ui.intelligentPlanting.HorizontalRecyclerFragmentHelper.LED_ON;

/**
 * 作者 yuanfei on 2017/11/25.
 * 邮箱 yuanfei221@126.com
 */

public class VeticalRecyclerFragment extends PrensterFragment<IntelligentPlantContract.Presenter>
        implements XRecyclerView.LoadingListener, IntelligentPlantContract.View, MyLongToothService.MyLongToothListener {
    @BindView(R.id.empty)
    EmptyView mEmptyView;
    @BindView(R.id.recycler)
    XRecyclerView mRecycler;
    private static final String TAG = VeticalRecyclerFragment.class.getName();
    public static final String VERTICAL_RECYLER_REFRESH = "VERTICAL_RECYLER_REFRESH";
    private int page = 1;
    private RecyclerAdapter<EquipmentRspModel.ListBean> mAdapter;
    private Gson gson = new Gson();
    public static final String VERTICALRECYCLER_DELETE_SUCCESS = "VERTICALRECYCLER_DELETE_SUCCESS";
    public static final String EQUIPMENT_LINE_ON = "EQUIPMENT_LINE_ON";
    public static final String VERTICALRECYCLER_VISIABLE = "VERTICALRECYCLER_VISIABLE";
    public static final String PUMP_ON = "pumpOn";
    public static final String PUMP_OFF = "pumpOff";
    private boolean isNeedLoadData = false;
    private boolean isPumpOn = false;
    private static boolean isPumpControl = false;
    private String deviceSeries = "";
    private String deviceLtid = "";
    private String deviceUUID = "";
    private String pumpLtid = "";
    private ImageView iv_recycle;
    private ImageView iv_bindExp;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_vertical_recycler;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        EventBus.getDefault().register(this);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecycler.setLayoutManager(linearLayoutManager);
        mRecycler.setAdapter(mAdapter = new RecyclerAdapter<EquipmentRspModel.ListBean>() {
            @Override
            protected int getItemViewType(int position, EquipmentRspModel.ListBean plantModel) {
                return R.layout.item_main_vertial;
            }

            @Override
            protected ViewHolder<EquipmentRspModel.ListBean> onCreateViewHolder(View root, int viewType) {
                return new VeticalRecyclerFragment.ViewHolder(root);
            }
        });
        mRecycler.setPullRefreshEnabled(true);
        mRecycler.setLoadingMoreEnabled(true);
        mRecycler.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecycler.setLoadingMoreProgressStyle(ProgressStyle.Pacman);
        mRecycler.setLoadingListener(this);
        mRecycler.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    LogUtil.d(TAG, "verticalRecyclerView 下拉刷新啦 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                    EventBus.getDefault().postSticky(new MessageEvent(VERTICALRECYCLER_VISIABLE, linearLayoutManager.findFirstVisibleItemPosition()));
                }
            }
        });
        mEmptyView.bind(mRecycler);
        mEmptyView.setEmptyImg(R.mipmap.img_equipment_empty);
        mEmptyView.setEmptyText(R.string.equipment_empty);
        setPlaceHolderView(mEmptyView);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        LogUtil.d(TAG,"onHiddenChanged executed:"+hidden);
        if (!hidden && isNeedLoadData) {
            if (UiTool.isNetworkAvailable(getContext())) {
                isNeedLoadData = false;
                onRefresh();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fresh(MessageEvent messageEvent) {
        if (messageEvent.getMessage().equals(VERTICAL_RECYLER_REFRESH)) {
            LogUtil.d(TAG,"收到消息啦（vrf）get allequipment+++++++++++++++++++++++++++++++++"+new Gson().toJson(messageEvent));
            onRefresh();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void initData() {
        super.initData();
        page = 1;
        mPresenter.getAllEquipments(Account.getToken(), "10", page + "");
        mEmptyView.triggerLoading();
    }

    @Override
    public void showError(int str) {
        super.showError(str);
        isNeedLoadData = true;
    }

    @Override
    public void onRefresh() {
        page = 1;
        if (mPresenter != null)
            mPresenter.getAllEquipments(Account.getToken(), "10", page + "");
    }

    @Override
    public void onLoadMore() {
        page++;
        mPresenter.getAllEquipments(Account.getToken(), "10", page + "");
    }

    @Override
    public void getAllEquipmentsSuccess(List<EquipmentRspModel.ListBean> listBeans) {
        LogUtil.d(TAG,"verticalFragment getAllEquipmentsSuccess:"+new Gson().toJson(listBeans));
        if (page == 1) {
            mRecycler.refreshComplete();
            mAdapter.replace(listBeans);
        } else {
            mRecycler.loadMoreComplete();
            mAdapter.add(listBeans);
        }
        mPlaceHolderView.triggerOkOrEmpty(mAdapter.getItemCount() > 0);
    }

    @Override
    protected IntelligentPlantContract.Presenter initPresenter() {
        return new IntelligentPlantPresenter(this);
    }

    @Override
    public void updateUI(Message msg) {
        mVerticalPumpHandler.sendMessage(msg);
    }

    @Override
    public void timeOutMsg(Message msg) {

    }


    class ViewHolder extends RecyclerAdapter.ViewHolder<EquipmentRspModel.ListBean> implements MainRecylerContract.View {
        @BindView(R.id.ll_root)
        LinearLayout mRoot;

        @BindView(R.id.frame_more)
        FrameLayout mMoreRoot;

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

        @BindView(R.id.tv_led)
        TextView mLed;

        @BindView(R.id.cb_push)
        CheckBox mPush;

        @BindView(R.id.img_tent)
        ImageView mImgTent;

        @BindView(R.id.tv_temperature)
        TextView mTemperature;

        @BindView(R.id.tv_water_status)
        TextView mWaterStatus;

        @BindView(R.id.tv_led_status)
        TextView mLedMode;

        @BindView(R.id.tv_ec_status)
        TextView mEcStatus;

        @BindView(R.id.ll_data)
        LinearLayout mPlantData;

        @BindView(R.id.tv_update)
        TextView mUpdate;

        @BindView(R.id.tv_setting)
        TextView mSecondSetting;

        @BindView(R.id.btn_delete)
        Button mDelete;

        @BindView(R.id.img_refresh)
        ImageView mRefresh;

        @BindView(R.id.tv_more)
        TextView tv_more;

        @BindView(R.id.img_waterRecycle)
        ImageView iv_waterRecycle;

        @BindView(R.id.ll_recycle)
        LinearLayout ll_recycle;

        @BindView(R.id.iv_device_bind_exp)
        ImageView iv_bindException;

        private MainRecylerContract.Presenter mPresenter;
        private TimerTask task;
        private boolean isLedOn = false;
        private Timer timer;

        public ViewHolder(View itemView) {
            super(itemView);
            EventBus.getDefault().register(this);
            new MainRecylerPresenter(this);
        }

        @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
        public void fresh(MessageEvent messageEvent) {
            LogUtil.d(TAG,"收到消息了（vrf ViewHolder）："+new Gson().toJson(messageEvent));
            if (messageEvent.getMessage().equals(EQUIPMENT_LINE_ON) &&
                    (messageEvent.getType().equals(mData.getLTID()))) {
                mImgTent.setVisibility(View.INVISIBLE);
            } else if (messageEvent.getMessage().equals(HorizontalRecyclerFragment.HORIZONTALRECYLER_DELETE_SUCCESS) &&
                    (messageEvent.getType().equals(mData.getLTID()))) {
                task.cancel();
                timer.cancel();
            } else if (messageEvent.getMessage().equals(LED_ON) &&
                    (messageEvent.getType().equals(mData.getLTID()))) {
//                iv_bindExp.setVisibility(View.GONE);
                isLedOn = true;
                mLed.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.mipmap.img_light_open, 0, 0);
            } else if (messageEvent.getMessage().equals(HorizontalRecyclerFragmentHelper.LED_OFF) &&
                    (messageEvent.getType().equals(mData.getLTID()))) {
//                iv_bindExp.setVisibility(View.GONE);
                isLedOn = false;
                mLed.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.mipmap.img_light_close, 0, 0);
            } else if (messageEvent.getMessage().equals(PUMP_ON) &&
                    (messageEvent.getType().equals(mData.getLTID()))) {
//                iv_bindExp.setVisibility(View.GONE);
                isPumpOn = true;
                iv_waterRecycle.setImageResource(R.mipmap.v_water_recycle_on);
            } else if (messageEvent.getMessage().equals(PUMP_OFF) &&
                    (messageEvent.getType().equals(mData.getLTID()))) {
//                iv_bindExp.setVisibility(View.GONE);
                isPumpOn = false;
                iv_waterRecycle.setImageResource(R.mipmap.v_water_recycle_off);
            } else if (messageEvent.getMessage().equals(MainActivity.MAINACTIVITY_DESTROY)) {
                if (task != null) {
                    task.cancel();
                }
            } else if (messageEvent.getMessage().equals(HorizontalRecyclerFragmentHelper.UPDATE_SUCCESS)) {
                mUpdate.setText(Application.getStringText(R.string.newest_version));
                mUpdate.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.mipmap.img_update_vertical_nomal, 0, 0);
                mUpdate.setEnabled(false);
            } else if (messageEvent.getMessage().equals(VERTICALRECYCLER_VISIABLE)) {
                if (getAdapterPosition() <= (messageEvent.getPosition() + 2) && getAdapterPosition() >= messageEvent.getPosition()) {
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
                        timer.schedule(task, 2000, 30000);
                    }
                } else {
                    if (task != null) {
                        task.cancel();
                        task = null;
                    }
                    if (timer != null) {
                        timer.cancel();
                        timer = null;
                    }
                }
            } else if (messageEvent.getMessage().equals(HORIZONTALRECYLER_VISIABLE)) {
                if (task != null) {
                    task.cancel();
                    task = null;
                }
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }
            }
        }

        @Override
        protected void onBind(final EquipmentRspModel.ListBean plantModel) {
            //初始化数据
            iv_recycle = iv_waterRecycle;
//            iv_bindExp = iv_bindException;
            setDataSuccess(plantModel.buildEquipmentDataModel());
            mEquipmentName.setText(plantModel.getEquipName());
            mPlantName.setText(plantModel.getPlantName());
            mGroupName.setText(plantModel.getGroupName());
            mTime.setText(String.valueOf(plantModel.getDays()));
            deviceSeries = plantModel.getSeries();
            if (deviceSeries.equals("WG201")) {
                isPumpControl = false;
                deviceLtid = plantModel.getLTID();
                deviceUUID = plantModel.getPSIGN();
                LogUtil.d(TAG,"onBind(deviceSeries:deviceLtid:deviceUUID):"+deviceSeries+",deviceLtid:"+deviceLtid+",deviceUUID:"+deviceUUID);
                sendPumpSetCmd(2);
                tv_more.setVisibility(View.GONE);
                ll_recycle.setVisibility(View.VISIBLE);
            } else {
                tv_more.setVisibility(View.VISIBLE);
                ll_recycle.setVisibility(View.GONE);
            }

            //设置是否需要升级
            HorizontalRecyclerFragmentHelper.isHaveNewVersion(plantModel, mUpdate, false);
            mPush.setChecked(plantModel.getPushStatus().equals("1"));
            GlideApp.with(getContext())
                    .load(plantModel.getPhoto())
                    .centerCrop()
                    .placeholder(R.mipmap.img_main_empty)
                    .into(mImage);
            setLed(plantModel);
            mPlantData.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG,"Vertical mPlantData onclick:" + Common.Constance.H5_BASE + "product_data.html?id=" + plantModel.getId());
                    if (deviceSeries.equals("WG201")) {
                        LogUtil.d(TAG,"Vertical onclick pump test");
                    }else{
                        LogUtil.d(TAG,"Vertical onclick h5 page");
                        PlantingDateAct.show(getContext(), Common.Constance.H5_BASE + "product_data.html?id=" + plantModel.getId(), plantModel);
                    }
                }
            });
            mRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("test", Common.Constance.H5_BASE + "product.html?id=" + plantModel.getId());
                    PlantingDateAct.show(getContext(), Common.Constance.H5_BASE + "product.html?id=" + plantModel.getId(), plantModel);
                }
            });
            ll_recycle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UiTool.showLoading(getContext());
                    iv_recycle = iv_waterRecycle;
                    isPumpControl = true;
                    deviceLtid = plantModel.getLTID();
                    deviceUUID = plantModel.getPSIGN();
                    if(isPumpOn){
                        LogUtil.d(TAG,"vertical pump state is on ,turn off pump");
                        sendPumpSetCmd(0);
                    }else{
                        LogUtil.d(TAG,"vertical pump state is off ,turn on pump");
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
            });
            mUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    HorizontalRecyclerFragmentHelper.update((Activity) getActivity(), plantModel);
                }
            });

            mSecondSetting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    plantModel.setPushStatus(mPush.isChecked() ? "1" : "0");
                    SecondSettingActivity.show(getContext(), plantModel);
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

            mDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder deleteDialog = new AlertDialog.Builder(getContext());
                    deleteDialog.setMessage(Application.getStringText(R.string.delete_will_reset_equipment_if_ensure));
                    deleteDialog.setPositiveButton(getResources().getString(R.string.ensure), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mPresenter.deleteEquipment(plantModel.getId());
                        }
                    });
                    deleteDialog.setNegativeButton(getResources().getString(R.string.cancel), null);
                    deleteDialog.show();
                }
            });
        }

        private void getEquipmentData(EquipmentRspModel.ListBean plantModel) {
            Log.d(TAG, "getEquipmentData: " + plantModel.getEquipName());
            if (TextUtils.isEmpty(plantModel.getPSIGN())) {
                return;
            }
            PlantStatusModel model = new PlantStatusModel(1, "getStatus", 1, Integer.parseInt(plantModel.getPSIGN()),
                    1);
            String request = gson.toJson(model);
            LongTooth.request(plantModel.getLTID(), "longtooth", LongToothTunnel.LT_ARGUMENTS, request.getBytes(),
                    0, request.getBytes().length, null, new LongToothResponse(plantModel, isLedOn, mImgTent));
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
                        LogUtil.d(TAG,"led set ltid:"+plantModel.getLTID()+",name:"+plantModel.getEquipName()+",deviceUUID:"+plantModel.getPSIGN());
                        LongTooth.request(plantModel.getLTID(), "longtooth", LongToothTunnel.LT_ARGUMENTS, request.getBytes(), 0, request.getBytes().length,
                                null, new LongToothServiceResponseHandler() {
                                    @Override
                                    public void handleServiceResponse(LongToothTunnel longToothTunnel, String s, String s1, int i, byte[] bytes, LongToothAttachment longToothAttachment) {
                                        UiTool.hideLoading();
                                        if (bytes == null)
                                            return;
                                        String jsonContent = new String(bytes);
                                        if (TextUtils.isEmpty(jsonContent) || !jsonContent.contains("CODE"))
                                            return;
                                        final LedSetRspModel plantStatusRspModel = gson.fromJson(jsonContent, LedSetRspModel.class);

                                        Run.onUiAsync(new Action() {
                                            @Override
                                            public void call() {
                                                if (plantStatusRspModel.getCODE() == 0) {
//                                                    iv_bindExp.setVisibility(View.GONE);
                                                    Application.showToast(Application.getStringText(R.string.light_open_success));
                                                    isLedOn = true;
                                                    EventBus.getDefault().post(new MessageEvent(LED_ON, plantModel.getLTID()));

                                                    mLed.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.mipmap.img_light_open,
                                                            0, 0);
                                                } else {
//                                                    iv_bindExp.setVisibility(View.VISIBLE);
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
                        LongTooth.request(plantModel.getLTID(), "longtooth", LongToothTunnel.LT_ARGUMENTS, request.getBytes(), 0, request.getBytes().length,
                                null, new LongToothServiceResponseHandler() {
                                    @Override
                                    public void handleServiceResponse(LongToothTunnel longToothTunnel, String s, String s1, int i, byte[] bytes, LongToothAttachment longToothAttachment) {
                                        UiTool.hideLoading();
                                        if (bytes == null)
                                            return;
                                        String jsonContent = new String(bytes);
                                        if (TextUtils.isEmpty(jsonContent) || !jsonContent.contains("CODE"))
                                            return;
                                        final LedSetRspModel plantStatusRspModel = gson.fromJson(jsonContent, LedSetRspModel.class);

                                        Run.onUiAsync(new Action() {
                                            @Override
                                            public void call() {
                                                if (plantStatusRspModel.getCODE() == 0) {
//                                                    iv_bindExp.setVisibility(View.GONE);
                                                    Application.showToast(Application.getStringText(R.string.light_close_success));
                                                    EventBus.getDefault().post(new MessageEvent(LED_OFF, plantModel.getLTID()));
                                                    isLedOn = false;
                                                    mLed.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.mipmap.img_light_close,
                                                            0, 0);
                                                } else {
//                                                    iv_bindExp.setVisibility(View.VISIBLE);
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


        @OnClick(R.id.img_more)
        void onMoreClick() {
            mMoreRoot.setVisibility(View.VISIBLE);
        }

        @OnClick(R.id.img_close)
        void onCloseClick() {
            mMoreRoot.setVisibility(View.GONE);
        }

        @OnClick(R.id.liner_refresh)
        void onRefreshClick() {
            //设置是否需要升级
            HorizontalRecyclerFragmentHelper.isHaveNewVersion(mData, mUpdate, false);
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
            UiTool.onConnectionConflict(getContext());
        }

        @Override
        public void setPresenter(MainRecylerContract.Presenter presenter) {
            mPresenter = presenter;
        }

        @Override
        public void setDataSuccess(EquipmentDataModel model) {
            mRefresh.clearAnimation();
            mTemperature.setText(model.getDegree() + "°C");
            mWaterStatus.setText(HorizontalRecyclerFragmentHelper.getWaStatus(model.getWater()));
            //如果不支持营养功能，则把图标设置为灰色
            if (getStatus(model.getEstatus()).equals(HorizontalRecyclerFragment.UNKNOWN)) {
                mEcStatus.setCompoundDrawablesRelativeWithIntrinsicBounds(R.mipmap.img_ec_normal, 0, 0, 0);
                mEcStatus.setTextColor(Color.parseColor("#dadada"));
            } else {
                mEcStatus.setCompoundDrawablesRelativeWithIntrinsicBounds(R.mipmap.img_ec, 0, 0, 0);
                mEcStatus.setTextColor(Color.parseColor("#9FD166"));
            }
            //如果不支持水位功能，则把图标设置为灰色
            if (HorizontalRecyclerFragmentHelper.getWaStatus(model.getWater()).equals(HorizontalRecyclerFragment.UNKNOWN)) {
                mWaterStatus.setCompoundDrawablesRelativeWithIntrinsicBounds(R.mipmap.img_water_lever_normal, 0, 0, 0);
                mWaterStatus.setTextColor(Color.parseColor("#dadada"));
            } else {
                mWaterStatus.setCompoundDrawablesRelativeWithIntrinsicBounds(R.mipmap.img_water_lever, 0, 0, 0);
                mWaterStatus.setTextColor(Color.parseColor("#FBB179"));
            }
            if (model.getLight() != null) {

            }

            mEcStatus.setText(getStatus(model.getEstatus()));
            if (model.getDstatus().equals("0")) {
                //温度过低
                mTemperature.setCompoundDrawablesRelativeWithIntrinsicBounds(R.mipmap.img_temperature, 0,
                        R.mipmap.img_trending_down, 0);
            } else if (model.getDstatus().equals("2")) {
                //温度过高
                mTemperature.setCompoundDrawablesRelativeWithIntrinsicBounds(R.mipmap.img_temperature,
                        0, R.mipmap.img_temperature_trending_up, 0);
            } else {
                mTemperature.setCompoundDrawablesRelativeWithIntrinsicBounds(R.mipmap.img_temperature,
                        0, 0, 0);
            }
        }

        @Override
        public void deleteEquipmentSuccess() {
            VeticalRecyclerFragment.this.mPresenter.getAllEquipments(Account.getToken(), "0", "0");
            //通知Horizontal停止timertask
            EventBus.getDefault().post(new MessageEvent(VERTICALRECYCLER_DELETE_SUCCESS, mData.getLTID()));
            //通知Horizontal刷新页面
            EventBus.getDefault().post(new MessageEvent(HorizontalRecyclerFragment.HORIZONTALRECYLER_REFRESH));
            //通知MainActivity刷新页面
            EventBus.getDefault().post(new MessageEvent(MainActivity.MAIN_ACTIVITY_REFRESH));
            //fun add
            if(timer != null){
                timer.cancel();
                task.cancel();
            }
//            timer.cancel();
//            task.cancel();
            //设备设置为出厂状态
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
                    return HorizontalRecyclerFragment.UNKNOWN;
            }
        }

        private int times = 0;

        class LongToothResponse implements LongToothServiceResponseHandler {
            private EquipmentRspModel.ListBean mPlantModel;
            private boolean isLedOn;
            private ImageView mImgTent;
            private boolean isResp = false;

            public LongToothResponse(final EquipmentRspModel.ListBean mPlantModel, final boolean isLedOn,
                                     final ImageView imgTent) {
                this.mPlantModel = mPlantModel;
                this.isLedOn = isLedOn;
                this.mImgTent = imgTent;
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        if (!isResp) {
                            Log.d(TAG, "run: " + times);
                            times++;
                            if (TextUtils.isEmpty(mPlantModel.getPSIGN())) {
                                return;
                            }
                            PlantStatusModel model = new PlantStatusModel(1, "getStatus", 1, Integer.parseInt(mPlantModel.getPSIGN()),
                                    1);
                            String request = gson.toJson(model);
                            LongTooth.request(mPlantModel.getLTID(), "longtooth", LongToothTunnel.LT_ARGUMENTS, request.getBytes(),
                                    0, request.getBytes().length, null, LongToothResponse.this);
                            //如果三次请求无数据返回，则认为设备离线
                            if (times > 3) {
                                offLine();
                                times = 0;
                            }
                        }
                    }
                };
//                Timer timer = new Timer();
                timer.schedule(task, 6000);
            }


            private void offLine() {
                Run.onUiAsync(new Action() {
                    @Override
                    public void call() {
                        mImgTent.setVisibility(View.VISIBLE);
                    }
                });
            }

            private void onLine() {
                Run.onUiAsync(new Action() {
                    @Override
                    public void call() {
                        EventBus.getDefault().post(new MessageEvent(EQUIPMENT_LINE_ON, mPlantModel.getLTID()));
                        mImgTent.setVisibility(View.INVISIBLE);
                    }
                });
            }


            @Override
            public void handleServiceResponse(LongToothTunnel longToothTunnel, String s, String s1, int i, byte[] bytes, LongToothAttachment longToothAttachment) {
                isResp = true;
                times = 0;
                if (bytes == null) {
                    offLine();
                    return;
                }
                String jsonContent = new String(bytes);
                Log.d(TAG, "handleServiceResponse: " + mPlantModel.getLTID() + ":" + jsonContent);
                if (!jsonContent.contains("CODE")) {
                    offLine();
                    return;
                }
                PlantStatusRspModel plantStatusRspModel = gson.fromJson(jsonContent, PlantStatusRspModel.class);
                if (plantStatusRspModel.getCODE() == 0) {
                    onLine();
                    //获取数据成功
                    String temperature = plantStatusRspModel.getTemp();
                    String wagerState = plantStatusRspModel.getWaterStat();
                    String Ec = plantStatusRspModel.getEC();//植物的营养值
                    String isLihtOn = isLedOn ? "0" : "1";
                    mPresenter.setData(Account.getToken(), mPlantModel.getMac(), temperature, wagerState, isLihtOn, Ec);
                } else {
                    offLine();
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
            LogUtil.d(TAG, "PUMPSET CMD(deviceLtid:deviceUUID):" + pumpSetCmd+"==>"+deviceLtid+",deviceUUID:"+deviceUUID);
            MyLongToothService.sendDataFrame(getActivity(), ConstUtil.OP_PUMP_SET, deviceLtid, pumpSetCmd);
            MyLongToothService.setMyLongToothListener(this);
        } catch (JSONException e) {
            e.printStackTrace();
            LogUtil.d(TAG, "sendPumpSetCmd json exp:" + e.getMessage());
        }
    }

    private MyPumpSetHandler mVerticalPumpHandler = new MyPumpSetHandler(this);

    private class MyPumpSetHandler extends Handler {
        private WeakReference<VeticalRecyclerFragment> weakReference = null;

        private MyPumpSetHandler(VeticalRecyclerFragment mFragment) {
            weakReference = new WeakReference<>(mFragment);
        }

        @Override
        public void handleMessage(Message msg) {
            VeticalRecyclerFragment veticalRecyclerFragment = weakReference.get();
            if (veticalRecyclerFragment == null) {
                return;
            }
            LogUtil.d(TAG, "pump set handleMessage:" + msg.toString() + "\n" + msg.obj.toString());
            if (msg.what == ConstUtil.PUMP_SET_SUCCESS) {
                try {
                    UiTool.hideLoading();
                    String msgRes = msg.obj.toString();
                    String[] resArr = msgRes.split("==");
                    pumpLtid = resArr[1];

                    JSONObject joMsgRes = new JSONObject(resArr[0]);
                    int code = joMsgRes.getInt("CODE");
                    //{ "SWITCH": "Off", "DURATION": 0, "CODE": 0 }

                    if (code == 0) {
//                        iv_bindExp.setVisibility(View.GONE);
                        String pumpState = joMsgRes.getString("SWITCH");
                        if (pumpState.equals("Off")) {
                            isPumpOn = false;
                            iv_recycle.setImageResource(R.mipmap.v_water_recycle_off);
                            if(isPumpControl){
                                Application.showToast(Application.getStringText(R.string.pump_set_off_success_text));
                            }else{
                                LogUtil.d(TAG,"水泵状态查询成功：关闭");
                            }
                            EventBus.getDefault().post(new MessageEvent(PUMP_OFF,pumpLtid));
                        } else {
                            isPumpOn = true;
                            iv_recycle.setImageResource(R.mipmap.v_water_recycle_on);
                            if (isPumpControl) {
                                Application.showToast(Application.getStringText(R.string.pump_set_on_success_text));
                            }else{
                                LogUtil.d(TAG,"水泵状态查询成功：开启");
                            }
                            EventBus.getDefault().post(new MessageEvent(PUMP_ON,pumpLtid));
                        }
                    } else {
//                        iv_bindExp.setVisibility(View.VISIBLE);
                        if(isPumpControl){
                            Application.showToast(Application.getStringText(R.string.pump_set_fail_text));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
