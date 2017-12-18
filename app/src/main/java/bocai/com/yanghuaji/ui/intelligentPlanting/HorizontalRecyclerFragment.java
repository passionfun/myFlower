package bocai.com.yanghuaji.ui.intelligentPlanting;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.Activity;
import bocai.com.yanghuaji.base.Application;
import bocai.com.yanghuaji.base.GlideApp;
import bocai.com.yanghuaji.base.RecyclerAdapter;
import bocai.com.yanghuaji.base.common.Common;
import bocai.com.yanghuaji.base.presenter.PrensterFragment;
import bocai.com.yanghuaji.model.CheckboxStatusModel;
import bocai.com.yanghuaji.model.EquipmentDataModel;
import bocai.com.yanghuaji.model.EquipmentRspModel;
import bocai.com.yanghuaji.model.LedSetModel;
import bocai.com.yanghuaji.model.LedSetRspModel;
import bocai.com.yanghuaji.model.MessageEvent;
import bocai.com.yanghuaji.model.PlantStatusModel;
import bocai.com.yanghuaji.model.PlantStatusRspModel;
import bocai.com.yanghuaji.model.PushModel;
import bocai.com.yanghuaji.presenter.intelligentPlanting.IntelligentPlantContract;
import bocai.com.yanghuaji.presenter.intelligentPlanting.IntelligentPlantPresenter;
import bocai.com.yanghuaji.presenter.intelligentPlanting.MainRecylerContract;
import bocai.com.yanghuaji.presenter.intelligentPlanting.MainRecylerPresenter;
import bocai.com.yanghuaji.ui.intelligentPlanting.recyclerHelper.GalleryLayoutManager;
import bocai.com.yanghuaji.ui.intelligentPlanting.recyclerHelper.ScaleTransformer;
import bocai.com.yanghuaji.util.persistence.Account;
import bocai.com.yanghuaji.util.widget.EmptyView;
import butterknife.BindView;
import butterknife.OnClick;
import xpod.longtooth.LongTooth;
import xpod.longtooth.LongToothAttachment;
import xpod.longtooth.LongToothServiceResponseHandler;
import xpod.longtooth.LongToothTunnel;

/**
 * 作者 yuanfei on 2017/11/15.
 * 邮箱 yuanfei221@126.com
 */
public class HorizontalRecyclerFragment extends PrensterFragment<IntelligentPlantContract.Presenter>
        implements IntelligentPlantContract.View {
    @BindView(R.id.empty)
    EmptyView mEmptyView;
    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_current)
    TextView mCurrentNum;
    @BindView(R.id.tv_total)
    TextView mTotalNum;
    public static final String TAG = HorizontalRecyclerFragment.class.getName();
    public static final String HORIZONTALRECYLER_REFRESH = "HORIZONTALRECYLER_REFRESH";
    private RecyclerAdapter<EquipmentRspModel.ListBean> mAdapter;
    private Gson gson = new Gson();
    //搜索设备用
//    private MiCODevice micodev;
//    //所有在线设备的longtoothId集合
//    List<String> longtoothIds = new ArrayList<>();
////    private boolean isLedOn = false;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_horizontal_recycler;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        EventBus.getDefault().register(this);
        GalleryLayoutManager layoutManager = new GalleryLayoutManager(GalleryLayoutManager.HORIZONTAL);
        layoutManager.attach(mRecyclerView, 1);
        layoutManager.setItemTransformer(new ScaleTransformer());
        mRecyclerView.setAdapter(mAdapter = new Adapter());
        mAdapter.setListener(new RecyclerAdapter.AdapterListenerImpl<EquipmentRspModel.ListBean>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder holder, EquipmentRspModel.ListBean plantModel) {
                Log.d("test", Common.Constance.H5_BASE + "product.html?id=" + plantModel.getId());
                String url = Common.Constance.H5_BASE + "product.html?id=" + plantModel.getId();
                PlantingDateAct.show(getContext(), url, plantModel);
            }
        });

        layoutManager.setOnItemSelectedListener(new GalleryLayoutManager.OnItemSelectedListener() {
            @Override
            public void onItemSelected(RecyclerView recyclerView, View item, int position) {
                mCurrentNum.setText((position + 1) + "");
            }
        });
        mCurrentNum.setText("1");
        mEmptyView.bind(mRecyclerView);
        mEmptyView.setEmptyImg(R.mipmap.img_equipment_empty);
        mEmptyView.setEmptyText(R.string.equipment_empty);
        setPlaceHolderView(mEmptyView);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fresh(MessageEvent messageEvent) {
        if (messageEvent.getMessage().equals(HORIZONTALRECYLER_REFRESH)) {
            mPresenter.getAllEquipments(Account.getToken(), "0", "0");
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
        mPresenter.getAllEquipments(Account.getToken(), "0", "0");
        mEmptyView.triggerLoading();
        //开始搜索设备
//        final String serviceName = "_easylink._tcp.local.";
//        micodev = new MiCODevice(getContext());
//        micodev.startSearchDevices(serviceName, new SearchDeviceCallBack() {
//            @Override
//            public void onDevicesFind(int code, JSONArray deviceStatus) {
//                super.onDevicesFind(code, deviceStatus);
//                String content = deviceStatus.toString();
//                Log.d("shc", "onDevicesFind: " + content);
//                if (!TextUtils.isEmpty(content) && !content.equals("[]")) {
//                    String jsonContent = content;
//                    micodev.stopSearchDevices(null);
//                    List<EquipmentModel> equipmentModels = gson.fromJson(jsonContent, new TypeToken<List<EquipmentModel>>() {
//                    }.getType());
//                    for (EquipmentModel equipmentModel : equipmentModels) {
//                        String longtoothId = equipmentModel.getLTID();
//                        longtoothIds.add(longtoothId);
//                    }
//                }
//            }
//        });


    }

    @Override
    public void getAllEquipmentsSuccess(List<EquipmentRspModel.ListBean> listBeans) {
        Account.setListBeans(listBeans);
        mAdapter.replace(listBeans);
        mTotalNum.setText(String.valueOf(listBeans.size()));
        mPlaceHolderView.triggerOkOrEmpty(mAdapter.getItemCount() > 0);
    }

    @Override
    protected IntelligentPlantContract.Presenter initPresenter() {
        return new IntelligentPlantPresenter(this);
    }

    class Adapter extends RecyclerAdapter<EquipmentRspModel.ListBean> {
        @Override
        protected int getItemViewType(int position, EquipmentRspModel.ListBean plantModel) {
            return R.layout.item_main_horizontal;
        }

        @Override
        protected ViewHolder<EquipmentRspModel.ListBean> onCreateViewHolder(View root, int viewType) {
            return new MyViewHolder(root);
        }

        class MyViewHolder extends RecyclerAdapter.ViewHolder<EquipmentRspModel.ListBean> implements MainRecylerContract.View {
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

            @BindView(R.id.cb_led)
            CheckBox mLed;

            @BindView(R.id.cb_push)
            CheckBox mPush;

            @BindView(R.id.tv_temperature)
            TextView mTemperature;

            @BindView(R.id.tv_water_status)
            TextView mWaterStatus;

            @BindView(R.id.tv_led_status)
            TextView mLedStatus;

            @BindView(R.id.tv_ec_status)
            TextView mEcStatus;

            @BindView(R.id.img_tem_arrow)
            ImageView mImgTemArrow;

            @BindView(R.id.ll_data)
            LinearLayout mPlantData;

            @BindView(R.id.tv_setting_second)
            TextView mSecondSetting;

            @BindView(R.id.tv_update)
            TextView mUpdate;

            private boolean isShowSetting = false;
            private MainRecylerContract.Presenter mPresenter;

            public MyViewHolder(View itemView) {
                super(itemView);

                new MainRecylerPresenter(this);
            }

            @Override
            protected void onBind(final EquipmentRspModel.ListBean plantModel) {
                mEquipmentName.setText(plantModel.getEquipName());
                mPlantName.setText(plantModel.getPlantName());
                mGroupName.setText(plantModel.getGroupName());
                mTime.setText(plantModel.getDays() + "");
                //台灯开关 0：关   1：开
                mLed.setChecked(plantModel.getLight().equals("1"));
                final boolean isLedOn = mLed.isChecked();
                //消息推送状态   0关   1开
                mPush.setChecked(plantModel.getPushStatus().equals("1"));
                GlideApp.with(getContext())
                        .load(plantModel.getPhoto())
                        .centerCrop()
                        .placeholder(R.mipmap.img_main_empty)
                        .into(mImage);
                Timer timer = new Timer();
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        //设置是否离线img
                        if (HorizontalRecyclerFragmentHelper.isHaveNewVersion(plantModel)) {
                            //设置有新版本
                        }

//                        Run.onUiAsync(new Action() {
//                            @Override
//                            public void call() {
////                                mOffLine.setVisibility(isLineOff(plantModel) ? View.VISIBLE : View.INVISIBLE);
////                                mFramOffline.setVisibility(isLineOff(plantModel) ? View.VISIBLE : View.INVISIBLE);
//                                mOffLine.setVisibility(View.INVISIBLE);
//                                mFramOffline.setVisibility(View.INVISIBLE);
//                            }
//                        });

                        if (TextUtils.isEmpty(plantModel.getPSIGN()) ||
                                TextUtils.isEmpty(plantModel.getPid())) {
                            Run.onUiAsync(new Action() {
                                @Override
                                public void call() {
                                    mOffLine.setVisibility(View.VISIBLE);
                                    mFramOffline.setVisibility(View.VISIBLE);
                                }
                            });

                            return;
                        }
                        PlantStatusModel model = new PlantStatusModel(1, "getStatus", 1, Integer.parseInt(plantModel.getPSIGN()),
                                1, Integer.parseInt(plantModel.getPid()));
                        String request = gson.toJson(model);
                        if (!TextUtils.isEmpty(plantModel.getLTID())) {
                            LongTooth.request(plantModel.getLTID(), "longtooth", LongToothTunnel.LT_ARGUMENTS, request.getBytes(),
                                    0, request.getBytes().length, null, new LongToothResponse(mPresenter, plantModel, isLedOn, mOffLine, mFramOffline));
                        }
                    }
                };
                timer.schedule(task, 5000, 30000);
                setLed(plantModel);

                mPlantData.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("test", Common.Constance.H5_BASE + "product_data.html?id=" + plantModel.getId());
                        String url = Common.Constance.H5_BASE + "product_data.html?id=" + plantModel.getId();
                        PlantingDateAct.show(getContext(), url, plantModel);
                    }
                });


                mSecondSetting.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        plantModel.setPushStatus(mPush.isChecked()?"1":"0");
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


//            private boolean isLineOff(EquipmentRspModel.ListBean plantModel) {
//                return !(longtoothIds != null && longtoothIds.size() > 0
//                        && longtoothIds.contains(plantModel.getLTID()));
//            }

            private void setLed(final EquipmentRspModel.ListBean plantModel) {

                mLed.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mLed.isChecked()) {
                            mPresenter.setCheckBox(Account.getToken(), "1", "1", plantModel.getId());
                            LedSetModel model = new LedSetModel("On", plantModel.getPSIGN());
                            String request = gson.toJson(model);
                            LongTooth.request(plantModel.getLTID(), "longtooth", LongToothTunnel.LT_ARGUMENTS, request.getBytes(), 0, request.getBytes().length,
                                    null, new LongToothServiceResponseHandler() {
                                        @Override
                                        public void handleServiceResponse(LongToothTunnel longToothTunnel, String s, String s1, int i, byte[] bytes, LongToothAttachment longToothAttachment) {
                                            String jsonContent = new String(bytes);
                                            LedSetRspModel plantStatusRspModel = gson.fromJson(jsonContent, LedSetRspModel.class);
                                            if (plantStatusRspModel.getCODE() == 0) {
                                                Application.showToast("LED开启成功");
                                            } else {
                                                Application.showToast("LED开启失败,稍后再试");
                                            }
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
                                            String jsonContent = new String(bytes);
                                            LedSetRspModel plantStatusRspModel = gson.fromJson(jsonContent, LedSetRspModel.class);
                                            if (plantStatusRspModel.getCODE() == 0) {
                                                Application.showToast("LED关闭成功");
                                            } else {
                                                Application.showToast("LED关闭失败,稍后再试");
                                            }
                                        }
                                    });
                        }
                    }
                });

            }


            @OnClick(R.id.img_setting)
            void onSettingClick() {
                isShowSetting = !isShowSetting;
                if (isShowSetting) {
                    mSettingView.setVisibility(View.VISIBLE);
                    mSetting.setImageResource(R.mipmap.img_close_main_horizontal);
                } else {
                    mSettingView.setVisibility(View.GONE);
                    mSetting.setImageResource(R.mipmap.img_item_setting);
                }
            }

            @Override
            public void showError(int str) {

            }

            @Override
            public void showLoading() {

            }

            @Override
            public void hideLoading() {

            }

            @Override
            public void setPresenter(MainRecylerContract.Presenter presenter) {
                mPresenter = presenter;
            }

            @Override
            public void setDataSuccess(EquipmentDataModel model) {
                mTemperature.setText(model.getDegree());
                if (model.getLight() != null) {
                    mLedStatus.setText(model.getLight().equals("0") ? "关" : "开");
                }
                mEcStatus.setText(getStatus(model.getEstatus()));
                mWaterStatus.setText(getStatus(model.getWstatus()));
                if (model.getWstatus().equals("0")) {
                    //温度过低
                    mImgTemArrow.setImageResource(R.mipmap.img_trending_down);
                    PushModel pushModel = new PushModel("push", "sys101");
                    HorizontalRecyclerFragmentHelper.push(model, pushModel);
                } else if (model.getDstatus().equals("2")) {
                    //温度过高
                    mImgTemArrow.setImageResource(R.mipmap.img_temperature_trending_up);
                    PushModel pushModel = new PushModel("push", "sys102");
                    HorizontalRecyclerFragmentHelper.push(model, pushModel);
                } else {
                    mImgTemArrow.setImageResource(0);
                }
                if (model.getEstatus().equals("0")) {
                    //营养过低
                    PushModel pushModel = new PushModel("push", "sys202");
                    HorizontalRecyclerFragmentHelper.push(model, pushModel);
                } else if (model.getDstatus().equals("2")) {
                    //营养过高
                    PushModel pushModel = new PushModel("push", "sys201");
                    HorizontalRecyclerFragmentHelper.push(model, pushModel);
                }
                if (model.getWstatus().equals("0")) {
                    //水位过低
                    PushModel pushModel = new PushModel("push", "sys301");
                    HorizontalRecyclerFragmentHelper.push(model, pushModel);
                } else if (model.getDstatus().equals("2")) {
                    //水位过高
                    PushModel pushModel = new PushModel("push", "sys302");
                    HorizontalRecyclerFragmentHelper.push(model, pushModel);
                }
            }

            @Override
            public void setCheckBoxSuccess(CheckboxStatusModel model) {
                EventBus.getDefault().post(new MessageEvent(VeticalRecyclerFragment.VERTICAL_RECYLER_REFRESH));
            }

            @Override
            public void deleteEquipmentSuccess() {

            }

            private String getStatus(String code) {
                switch (code) {
                    case "0":
                        return "过低";
                    case "1":
                        return "正常";
                    case "2":
                        return "过高";
                    default:
                        return "未知";
                }
            }


        }

        class LongToothResponse implements LongToothServiceResponseHandler {

            private MainRecylerContract.Presenter mPresenter;
            private EquipmentRspModel.ListBean mPlantModel;
            private boolean isLedOn;
            private ImageView mOffLine;
            private FrameLayout mFrameOffLine;
            private boolean isResp = false;

            public LongToothResponse(MainRecylerContract.Presenter mPresenter, EquipmentRspModel.ListBean plantModel, boolean isLedOn, final ImageView offLine, FrameLayout frameOffLine) {
                this.mPresenter = mPresenter;
                mPlantModel = plantModel;
                this.isLedOn = isLedOn;
                this.mOffLine = offLine;
                this.mFrameOffLine = frameOffLine;
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        if (!isResp){
                            offLine();
                        }
                    }
                };
                Timer timer = new Timer();
                timer.schedule(task, 15000);
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
                        mOffLine.setVisibility(View.INVISIBLE);
                        mFrameOffLine.setVisibility(View.INVISIBLE);
                    }
                });
            }

            @Override
            public void handleServiceResponse(LongToothTunnel longToothTunnel, String s, String s1, int i, byte[] bytes, LongToothAttachment longToothAttachment) {
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
                Log.d(TAG, "handleServiceResponse: " + jsonContent);
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
}
