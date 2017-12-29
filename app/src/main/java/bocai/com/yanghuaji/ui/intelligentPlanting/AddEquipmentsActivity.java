package bocai.com.yanghuaji.ui.intelligentPlanting;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;
import net.qiujuer.genius.ui.widget.Loading;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.Activity;
import bocai.com.yanghuaji.base.Application;
import bocai.com.yanghuaji.base.GlideApp;
import bocai.com.yanghuaji.base.RecyclerAdapter;
import bocai.com.yanghuaji.base.common.Factory;
import bocai.com.yanghuaji.base.presenter.PresenterActivity;
import bocai.com.yanghuaji.model.AddEquipmentsModel;
import bocai.com.yanghuaji.model.BindEquipmentModel;
import bocai.com.yanghuaji.model.EquipmentCard;
import bocai.com.yanghuaji.model.EquipmentModel;
import bocai.com.yanghuaji.model.EquipmentPhotoModel;
import bocai.com.yanghuaji.model.EquipmentRspModel;
import bocai.com.yanghuaji.model.LongToothRspModel;
import bocai.com.yanghuaji.model.MessageEvent;
import bocai.com.yanghuaji.model.PlantSeriesModel;
import bocai.com.yanghuaji.presenter.intelligentPlanting.AddEquipmentsContract;
import bocai.com.yanghuaji.presenter.intelligentPlanting.AddEquipmentsPresenter;
import bocai.com.yanghuaji.presenter.intelligentPlanting.AddEquipmentsRecylerContract;
import bocai.com.yanghuaji.presenter.intelligentPlanting.AddEquipmentsRecylerPresenter;
import bocai.com.yanghuaji.ui.main.MainActivity;
import bocai.com.yanghuaji.util.DateUtils;
import bocai.com.yanghuaji.util.LongToothUtil;
import bocai.com.yanghuaji.util.UiTool;
import bocai.com.yanghuaji.util.persistence.Account;
import bocai.com.yanghuaji.util.widget.EmptyView;
import butterknife.BindView;
import butterknife.OnClick;
import io.fogcloud.sdk.easylink.api.EasyLink;
import io.fogcloud.sdk.easylink.helper.EasyLinkCallBack;
import io.fogcloud.sdk.easylink.helper.EasyLinkParams;
import io.fogcloud.sdk.mdns.api.MDNS;
import io.fogcloud.sdk.mdns.helper.SearchDeviceCallBack;
import xpod.longtooth.LongTooth;
import xpod.longtooth.LongToothAttachment;
import xpod.longtooth.LongToothServiceResponseHandler;
import xpod.longtooth.LongToothTunnel;

/**
 * 作者 yuanfei on 2017/12/8.
 * 邮箱 yuanfei221@126.com
 */

public class AddEquipmentsActivity extends PresenterActivity<AddEquipmentsContract.Presenter>
        implements AddEquipmentsContract.View {
    @BindView(R.id.empty)
    EmptyView mEmptyView;

    @BindView(R.id.tv_title)
    TextView mTitle;

    @BindView(R.id.recycler)
    RecyclerView mRecyler;

    @BindView(R.id.tv_right)
    TextView mSave;

    @BindView(R.id.loading_add_equipments)
    Loading mLoadingAddEquipments;

    public static String KEY_PLANT_CARD = "KEY_PLANT_CARD";
    private String ssid;
    private String password;
    private RecyclerAdapter<EquipmentModel> mAdapter;
    private Gson gson = new Gson();
    //搜索设备用
    //所有在线设备的系列名称集合
    List<String> series = new ArrayList<>();
    //所有已经添加过的设备
    private List<EquipmentRspModel.ListBean> listBeans = Account.getListBeans();
    private PlantSeriesModel.PlantSeriesCard plantSeriesCard;
    //被选中要添加的设备集合
    List<EquipmentModel> equipmentModels = new ArrayList<>();
    MDNS mdns = new MDNS(this);
    private boolean isAllSuccess = true;
    EasyLink elink = new EasyLink(this);
    private boolean isSaveClicked = false;
    //显示的入口
    public static void show(Context context, String ssid, String password, PlantSeriesModel.PlantSeriesCard plantSeriesCard) {
        Intent intent = new Intent(context, AddEquipmentsActivity.class);
        intent.putExtra(KEY_PLANT_CARD, plantSeriesCard);
        intent.putExtra(ConnectActivity.KEY_SSID, ssid);
        intent.putExtra(ConnectActivity.KEY_PASSWORD, password);
        context.startActivity(intent);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_add_equipments;
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        ssid = bundle.getString(ConnectActivity.KEY_SSID);
        password = bundle.getString(ConnectActivity.KEY_PASSWORD);
        plantSeriesCard = (PlantSeriesModel.PlantSeriesCard) bundle.getSerializable(KEY_PLANT_CARD);
        return super.initArgs(bundle);
    }

    @OnClick(R.id.img_back)
    void onBackClick() {
        finish();
    }


    @OnClick(R.id.tv_right)
    void onConfirmClick() {
        if (isSaveClicked){
            Application.showToast("请返回重新添加");
            return;
        }
        showLoading();
        addEquipments();
    }

    private void addEquipments() {
        if (equipmentModels == null || equipmentModels.size() == 0) {
            hideLoading();
            Application.showToast("请选择要添加的设备");
            return;
        }
        isSaveClicked = true;
        final List<AddEquipmentsModel> models = new ArrayList<>();
        for (EquipmentModel equipmentModel : equipmentModels) {
            startBind(models, equipmentModel);
        }
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (mPresenter != null) {
                    if (models.size() > 0) {
                        String token = Account.getToken();
                        mPresenter.addEquipments(token, gson.toJson(models));
                    } else {
                        hideLoading();
                    }
                }
            }
        }, 5000);

    }


    public void startBind(List<AddEquipmentsModel> models, EquipmentModel mEquipmentModel) {
        final String timeStamp = DateUtils.getCurrentDateTimes();
        BindEquipmentModel model = new BindEquipmentModel("BR", timeStamp);
        final String request = gson.toJson(model);
        Log.d("sunhengchao", "startbind: " + request);
        LongTooth.request(mEquipmentModel.getLTID(), "longtooth", LongToothTunnel.LT_ARGUMENTS, request.getBytes(),
                0, request.getBytes().length,
                new SampleAttachment(), new MyLongToothServiceResponseHandler(models, mEquipmentModel,timeStamp));

    }


    class MyLongToothServiceResponseHandler implements LongToothServiceResponseHandler {
        private boolean isRsp = false;
        private List<AddEquipmentsModel> models;
        private EquipmentModel equipmentModel;
        private String timeStamp;

        public MyLongToothServiceResponseHandler(List<AddEquipmentsModel> models, final EquipmentModel equipmentModel,String timeStamp) {
            this.models = models;
            this.equipmentModel = equipmentModel;
            this.timeStamp = timeStamp;
            Timer timer = new Timer();
            isAllSuccess = true;
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (!isRsp) {
                        Application.showToast("绑定无响应");
                        EventBus.getDefault().post(new MessageEvent(equipmentModel.getLTID(),MessageEvent.FAILED));
                        isAllSuccess = false;
                    }
                }
            }, 5000);
        }


        @Override
        public void handleServiceResponse(LongToothTunnel ltt, String ltid_str,
                                          String service_str, int data_type, byte[] args,
                                          LongToothAttachment attachment) {
            String result = new String(args);
            if (TextUtils.isEmpty(result)) {
                return;
            }
            isRsp = true;
            Log.d("sunhengchao", "handleServiceResponse: " + new String(args));
            final LongToothRspModel longToothRspModel = gson.fromJson(result, LongToothRspModel.class);
            if (longToothRspModel.getCODE() == 0) {
                String mEquipmentName = equipmentModel.getDEVNAME();
                String macAddress = equipmentModel.getMAC();
                String serialNum = "";
                String version = equipmentModel.get_$FirmwareRev196();
                String series = mEquipmentName.substring(0, 5);
                AddEquipmentsModel model = new AddEquipmentsModel(mEquipmentName, macAddress, serialNum, version, equipmentModel.getLTID()
                        , timeStamp, series);
                models.add(model);
            } else {
                Run.onUiAsync(new Action() {
                    @Override
                    public void call() {
                        EventBus.getDefault().post(new MessageEvent(equipmentModel.getLTID(),MessageEvent.FAILED));
                        isAllSuccess = false;
                        Factory.decodeRspCode(longToothRspModel);
                    }
                });
            }
        }
    }


    @Override
    public void addEquipmentsSuccess(List<EquipmentCard> cards) {
        //通知主页面刷新数据
        EventBus.getDefault().post(new MessageEvent(HorizontalRecyclerFragment.HORIZONTALRECYLER_REFRESH));
        EventBus.getDefault().post(new MessageEvent(VeticalRecyclerFragment.VERTICAL_RECYLER_REFRESH));
        for (EquipmentCard card : cards) {
            EventBus.getDefault().post(new MessageEvent(card.getLTID(),MessageEvent.SUCCESS));
        }
        if (isAllSuccess) {
            MainActivity.show(this);
        }
    }

    @Override
    public void addEquipmentsFailed() {

        isAllSuccess = false;
    }


    @Override
    protected void initBefore() {
        super.initBefore();
    }

    @Override
    protected AddEquipmentsContract.Presenter initPresenter() {
        return new AddEquipmentsPresenter(this);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        UiTool.setBlod(mTitle);
        mTitle.setText("连接设备");
        mSave.setVisibility(View.VISIBLE);
        mSave.setText("保存");
        mRecyler.setLayoutManager(new LinearLayoutManager(this));
        mRecyler.setAdapter(mAdapter = new RecyclerAdapter<EquipmentModel>() {
            @Override
            protected int getItemViewType(int position, EquipmentModel equipmentModel) {
                return R.layout.item_add_equipments;
            }

            @Override
            protected ViewHolder<EquipmentModel> onCreateViewHolder(View root, int viewType) {
                return new MyViewHolder(root);
            }
        });
        mEmptyView.bind(mRecyler);
        mEmptyView.setEmptyImg(R.mipmap.img_equipment_empty);
        mEmptyView.setEmptyText(R.string.equipment_empty);
        setPlaceHolderView(mEmptyView);

        if (!LongToothUtil.isInitSuccess) {
            LongToothUtil.longToothInit();
        }


    }


    @Override
    protected void initData() {
        super.initData();
        mLoadingAddEquipments.setForegroundColor(Color.parseColor("#87BC52"));
        mLoadingAddEquipments.setVisibility(View.VISIBLE);
        mLoadingAddEquipments.start();
        EasyLinkParams easylinkPara = new EasyLinkParams();
        easylinkPara.ssid = ssid;
        easylinkPara.password = password;
        easylinkPara.runSecond = 60000;
        easylinkPara.sleeptime = 20;
        elink.startEasyLink(easylinkPara, new EasyLinkCallBack() {
            @Override
            public void onSuccess(int code, String message) {
                Log.d("sunhengchao", "onSuccess配网: " + message);
            }

            @Override
            public void onFailure(int code, String message) {
                Application.showToast(message);
                finish();
            }
        });
        String serviceInfo = "_easylink._tcp.local.";
        mdns.startSearchDevices(serviceInfo, new SearchDeviceCallBack() {
            @Override
            public void onSuccess(int code, String message) {
                super.onSuccess(code, message);
            }

            @Override
            public void onFailure(int code, String message) {
                super.onFailure(code, message);
                Application.showToast(message);
                finish();
            }

            @Override
            public void onDevicesFind(int code, JSONArray deviceStatus) {
                super.onDevicesFind(code, deviceStatus);
                String content = deviceStatus.toString();
                Log.d("sunhengchao", "onDevicesFind: " + content);
                if (!TextUtils.isEmpty(content) && !content.equals("[]")) {
                    String jsonContent = content;
                    List<EquipmentModel> equipmentModels = gson.fromJson(jsonContent, new TypeToken<List<EquipmentModel>>() {
                    }.getType());
                    for (final EquipmentModel equipmentModel : equipmentModels) {
                        String seriesName = equipmentModel.getDEVNAME();
                        //搜索所有搜索到该系列的设备，如果还没有添加过，并且设备没有被绑定过则显示
                        if (!series.contains(seriesName) && !isAdded(equipmentModel.getLTID())
                                && seriesName.startsWith(plantSeriesCard.getSeries())
                                && (equipmentModel.get_$BOUNDSTATUS310() != null &&
                                equipmentModel.get_$BOUNDSTATUS310().equals("notBound"))) {
                            series.add(seriesName);
                            Run.onUiAsync(new Action() {
                                @Override
                                public void call() {
                                    mAdapter.add(equipmentModel);
                                    mEmptyView.triggerOk();
                                }
                            });
                        }
                    }
                }
            }
        });



        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Run.onUiAsync(new Action() {
                    @Override
                    public void call() {
                        Log.d("shccall", "call: ");
                        mLoadingAddEquipments.stop();
                        mLoadingAddEquipments.setVisibility(View.INVISIBLE);
                        mdns.stopSearchDevices(null);
                        elink.stopEasyLink(null);
                        mEmptyView.triggerOkOrEmpty(mAdapter.getItemCount() > 0);
                    }
                });
            }
        };
        timer.schedule(task, 60000);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mdns.stopSearchDevices(null);
        elink.stopEasyLink(null);
    }

    private boolean isAdded(String longtoothId) {
        List<String> longtooths = new ArrayList<>();
        if (listBeans != null && listBeans.size() > 0) {
            for (EquipmentRspModel.ListBean listBean : listBeans) {
                longtooths.add(listBean.getLTID());
            }
            return longtooths.contains(longtoothId);
        } else {
            return false;
        }

    }


    class MyViewHolder extends RecyclerAdapter.ViewHolder<EquipmentModel> implements AddEquipmentsRecylerContract.View {
        @BindView(R.id.equipment_photo)
        ImageView mPhoto;

        @BindView(R.id.tv_equipment_name)
        TextView mName;

        @BindView(R.id.tv_mac)
        TextView mMac;

        @BindView(R.id.cb_add)
        CheckBox mCbAdd;

        @BindView(R.id.img_add_failed)
        ImageView mAddFailed;

        @BindView(R.id.img_add_success)
        ImageView mAddSuccess;

        private AddEquipmentsRecylerContract.Presenter mPresenter;

        public MyViewHolder(View itemView) {
            super(itemView);
            EventBus.getDefault().register(this);
            new AddEquipmentsRecylerPresenter(this);
        }


        @Subscribe(threadMode = ThreadMode.MAIN)
        public void onEidtPersonDataSuccess(MessageEvent messageEvent) {
            if (messageEvent.getMessage().equals(mData.getLTID())&&
                    messageEvent.getType().equals(MessageEvent.FAILED)) {
                mCbAdd.setChecked(false);
                mCbAdd.setVisibility(View.GONE);
                equipmentModels.remove(mData);
                mAddFailed.setVisibility(View.VISIBLE);
                mAddSuccess.setVisibility(View.GONE);
            }else if (messageEvent.getMessage().equals(mData.getLTID())&&
                    messageEvent.getType().equals(MessageEvent.SUCCESS)){
                mCbAdd.setVisibility(View.GONE);
                mAddFailed.setVisibility(View.GONE);
                mAddSuccess.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected void onBind(EquipmentModel equipmentModel) {
            mName.setText(equipmentModel.getDEVNAME());
            mMac.setText(equipmentModel.getMAC());
            String type = equipmentModel.getDEVNAME().substring(0, 4);
            mPresenter.getEquipmentPhoto("2", type);
        }

        @OnClick(R.id.cb_add)
        void onAddClick() {
            if (mCbAdd.isChecked()) {
                equipmentModels.add(mData);
            } else {
                equipmentModels.remove(mData);
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
        public void onConnectionConflict() {
            UiTool.onConnectionConflict(AddEquipmentsActivity.this);
        }

        @Override
        public void setPresenter(AddEquipmentsRecylerContract.Presenter presenter) {
            mPresenter = presenter;
        }

        @Override
        public void getEquipmentPhotoSuccess(EquipmentPhotoModel photoModel) {
            GlideApp.with(AddEquipmentsActivity.this)
                    .load(photoModel.getPhoto())
                    .centerCrop()
                    .placeholder(R.mipmap.img_content_empty)
                    .into(mPhoto);
        }


    }


}
