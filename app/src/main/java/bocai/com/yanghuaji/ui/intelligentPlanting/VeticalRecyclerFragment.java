package bocai.com.yanghuaji.ui.intelligentPlanting;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
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
import bocai.com.yanghuaji.presenter.intelligentPlanting.IntelligentPlantContract;
import bocai.com.yanghuaji.presenter.intelligentPlanting.IntelligentPlantPresenter;
import bocai.com.yanghuaji.presenter.intelligentPlanting.MainRecylerContract;
import bocai.com.yanghuaji.presenter.intelligentPlanting.MainRecylerPresenter;
import bocai.com.yanghuaji.ui.main.MainActivity;
import bocai.com.yanghuaji.util.persistence.Account;
import bocai.com.yanghuaji.util.widget.EmptyView;
import butterknife.BindView;
import butterknife.OnClick;
import xpod.longtooth.LongTooth;
import xpod.longtooth.LongToothAttachment;
import xpod.longtooth.LongToothServiceResponseHandler;
import xpod.longtooth.LongToothTunnel;

/**
 * 作者 yuanfei on 2017/11/25.
 * 邮箱 yuanfei221@126.com
 */

public class VeticalRecyclerFragment extends PrensterFragment<IntelligentPlantContract.Presenter>
        implements XRecyclerView.LoadingListener, IntelligentPlantContract.View {
    @BindView(R.id.empty)
    EmptyView mEmptyView;

    @BindView(R.id.recycler)
    XRecyclerView mRecycler;

    private static final String TAG = VeticalRecyclerFragment.class.getName();
    public static final String VERTICAL_RECYLER_REFRESH = "VERTICAL_RECYLER_REFRESH";
    private int page = 1;
    private RecyclerAdapter<EquipmentRspModel.ListBean> mAdapter;
    private Gson gson = new Gson();
//    //搜索设备用
//    private MiCODevice micodev;
//    //所有在线设备的mac集合
//    List<String> longtoothIds = new ArrayList<>();

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_vertical_recycler;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        EventBus.getDefault().register(this);
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
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
        mEmptyView.bind(mRecycler);
        mEmptyView.setEmptyImg(R.mipmap.img_equipment_empty);
        mEmptyView.setEmptyText(R.string.equipment_empty);
        setPlaceHolderView(mEmptyView);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fresh(MessageEvent messageEvent) {
        if (messageEvent.getMessage().equals(VERTICAL_RECYLER_REFRESH)) {
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
    public void onRefresh() {
        page = 1;
        mPresenter.getAllEquipments(Account.getToken(), "10", page + "");
    }

    @Override
    public void onLoadMore() {
        page++;
        mPresenter.getAllEquipments(Account.getToken(), "10", page + "");
    }

    @Override
    public void getAllEquipmentsSuccess(List<EquipmentRspModel.ListBean> listBeans) {
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

        @BindView(R.id.cb_led)
        CheckBox mLed;

        @BindView(R.id.cb_push)
        CheckBox mPush;

        @BindView(R.id.img_tent)
        ImageView mImgTent;

        @BindView(R.id.tv_temperature)
        TextView mTemperature;

        @BindView(R.id.tv_water_status)
        TextView mWaterStatus;

        @BindView(R.id.tv_led_status)
        TextView mLedStatus;

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

        private MainRecylerContract.Presenter mPresenter;

        public ViewHolder(View itemView) {
            super(itemView);
            new MainRecylerPresenter(this);
        }

        @Override
        protected void onBind(final EquipmentRspModel.ListBean plantModel) {
            mEquipmentName.setText(plantModel.getEquipName());
            mPlantName.setText(plantModel.getPlantName());
            mGroupName.setText(plantModel.getGroupName());
            mTime.setText(plantModel.getDays() + "");
            mLed.setChecked(plantModel.getLight().equals("1"));
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
                   getEquipmentData(plantModel);
                }
            };
            timer.schedule(task, 5000, 30000);
            setLed(plantModel);


            mPlantData.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Log.d("test", Common.Constance.H5_BASE + "product_data.html?id=" + plantModel.getId());
                    PlantingDateAct.show(getContext(), Common.Constance.H5_BASE + "product_data.html?id=" + plantModel.getId(), plantModel);
                }
            });

            mRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("test", Common.Constance.H5_BASE + "product.html?id=" + plantModel.getId());
                    PlantingDateAct.show(getContext(), Common.Constance.H5_BASE + "product.html?id=" + plantModel.getId(), plantModel);
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
                    plantModel.setPushStatus(mPush.isChecked()?"1":"0");
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
                    deleteDialog.setTitle("确定删除？");
                    deleteDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mPresenter.deleteEquipment(plantModel.getId());
                        }
                    });
                    deleteDialog.setNegativeButton("取消",null);
                    deleteDialog.show();
                }
            });

        }

        private void getEquipmentData(EquipmentRspModel.ListBean plantModel) {
            if (TextUtils.isEmpty(plantModel.getPSIGN()) ||
                    TextUtils.isEmpty(plantModel.getPid())) {
                //如果UUID或者植物id为空，则说明设备离线
                Run.onUiAsync(new Action() {
                    @Override
                    public void call() {
                        mImgTent.setVisibility(View.VISIBLE);
                    }
                });

                return;
            }

            final boolean isLedOn = mLed.isChecked();
            PlantStatusModel model = new PlantStatusModel(1, "getStatus", 1, Integer.parseInt(plantModel.getPSIGN()),
                    1, Integer.parseInt(plantModel.getPid()));
            String request = gson.toJson(model);
            LongTooth.request(plantModel.getLTID(), "longtooth", LongToothTunnel.LT_ARGUMENTS, request.getBytes(),
                    0, request.getBytes().length, null, new LongToothResponse(plantModel,isLedOn,mImgTent));
        }



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



        @OnClick(R.id.img_more)
        void onMoreClick() {
            mMoreRoot.setVisibility(View.VISIBLE);
        }

        @OnClick(R.id.img_close)
        void onCloseClick() {
            mMoreRoot.setVisibility(View.GONE);
        }



        @OnClick(R.id.tv_refresh)
        void onRefreshClick() {
            getEquipmentData(mData);
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
            mWaterStatus.setText(getStatus(model.getWstatus()));
            if (model.getLight() != null)
                mLedStatus.setText(model.getLight().equals("0") ? "关" : "开");
            mEcStatus.setText(getStatus(model.getEstatus()));
            if (model.getWstatus().equals("0")) {
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
        public void setCheckBoxSuccess(CheckboxStatusModel model) {
            EventBus.getDefault().post(new MessageEvent(HorizontalRecyclerFragment.HORIZONTALRECYLER_REFRESH));

        }

        @Override
        public void deleteEquipmentSuccess() {
            VeticalRecyclerFragment.this.mPresenter.getAllEquipments(Account.getToken(),"0","0");
            EventBus.getDefault().post(new MessageEvent(HorizontalRecyclerFragment.HORIZONTALRECYLER_REFRESH));
            EventBus.getDefault().post(new MessageEvent(MainActivity.MAIN_ACTIVITY_REFRESH));
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


        class LongToothResponse implements LongToothServiceResponseHandler {
            private EquipmentRspModel.ListBean mPlantModel;
            private boolean isLedOn;
            private ImageView mImgTent;
            private boolean isResp = false;

            public LongToothResponse(EquipmentRspModel.ListBean mPlantModel, boolean isLedOn, final ImageView imgTent) {
                this.mPlantModel = mPlantModel;
                this.isLedOn = isLedOn;
                this.mImgTent = imgTent;
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
                        mImgTent.setVisibility(View.VISIBLE);
                    }
                });
            }

            private void onLine() {
                Run.onUiAsync(new Action() {
                    @Override
                    public void call() {
                        mImgTent.setVisibility(View.INVISIBLE);
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
                Log.d(TAG, "handleServiceResponse: " + jsonContent);
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
                }else {
                    offLine();
                }
            }
        }

    }


}
