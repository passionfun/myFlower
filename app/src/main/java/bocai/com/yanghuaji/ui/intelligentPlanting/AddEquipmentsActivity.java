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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;
import net.qiujuer.genius.ui.widget.Loading;

import org.greenrobot.eventbus.EventBus;
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
import bocai.com.yanghuaji.model.BindEquipmentModel;
import bocai.com.yanghuaji.model.EquipmentCard;
import bocai.com.yanghuaji.model.EquipmentModel;
import bocai.com.yanghuaji.model.EquipmentPhotoModel;
import bocai.com.yanghuaji.model.EquipmentRspModel;
import bocai.com.yanghuaji.model.LongToothRspModel;
import bocai.com.yanghuaji.model.MessageEvent;
import bocai.com.yanghuaji.model.PlantSeriesModel;
import bocai.com.yanghuaji.presenter.intelligentPlanting.AddEquipmentsRecylerContract;
import bocai.com.yanghuaji.presenter.intelligentPlanting.AddEquipmentsRecylerPresenter;
import bocai.com.yanghuaji.ui.main.MainActivity;
import bocai.com.yanghuaji.util.DateUtils;
import bocai.com.yanghuaji.util.LongToothUtil;
import bocai.com.yanghuaji.util.persistence.Account;
import bocai.com.yanghuaji.util.widget.EmptyView;
import butterknife.BindView;
import butterknife.OnClick;
import io.fog.fog2sdk.MiCODevice;
import io.fogcloud.easylink.helper.EasyLinkCallBack;
import io.fogcloud.fog_mdns.helper.SearchDeviceCallBack;
import xpod.longtooth.LongTooth;
import xpod.longtooth.LongToothAttachment;
import xpod.longtooth.LongToothServiceResponseHandler;
import xpod.longtooth.LongToothTunnel;

/**
 * 作者 yuanfei on 2017/12/8.
 * 邮箱 yuanfei221@126.com
 */

public class AddEquipmentsActivity extends Activity {
    @BindView(R.id.empty)
    EmptyView mEmptyView;

    @BindView(R.id.tv_title)
    TextView mTitle;

    @BindView(R.id.recycler)
    RecyclerView mRecyler;

    @BindView(R.id.tv_right)
    TextView mSave;

    public static String KEY_PLANT_CARD = "KEY_PLANT_CARD";
    private String ssid;
    private String password;
    private RecyclerAdapter<EquipmentModel> mAdapter;
    private Gson gson = new Gson();
    //搜索设备用
    private MiCODevice micodev;
    //所有在线设备的系列名称集合
    List<String> series = new ArrayList<>();
    //所有已经添加过的设备
    private List<EquipmentRspModel.ListBean> listBeans = Account.getListBeans();
    private PlantSeriesModel.PlantSeriesCard plantSeriesCard;

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
        // 添加完成，跳转主页面
        MainActivity.show(this);
    }

    @Override
    protected void initBefore() {
        super.initBefore();
    }

    @Override
    protected void initWidget() {
        super.initWidget();
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
        mEmptyView.triggerLoading();
        micodev = new MiCODevice(this);
        //开始配网
        micodev.startEasyLink(ssid, password, true, 60000, 20, "", "", new EasyLinkCallBack() {
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

        //开始搜索设备
        final String serviceName = "_easylink._tcp.local.";
        micodev.startSearchDevices(serviceName, new SearchDeviceCallBack() {
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
                        //搜索所有搜索到该系列的设备，如果还没有添加过，则显示
                        if (!series.contains(seriesName) && !isAdded(equipmentModel.getLTID())
                                && seriesName.startsWith(plantSeriesCard.getSeries())) {
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
                        micodev.stopEasyLink(null);
                        micodev.stopSearchDevices(null);
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
        micodev.stopEasyLink(null);
        micodev.stopSearchDevices(null);
    }

    private boolean isAdded(String longtoothId) {
        List<String> longtoothS = new ArrayList<>();
        if (listBeans != null && listBeans.size() > 0) {
            for (EquipmentRspModel.ListBean listBean : listBeans) {
                longtoothS.add(listBean.getLTID());
            }
            if (longtoothS.contains(longtoothId)) {
                return true;
            } else {
                return false;
            }
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

        @BindView(R.id.img_add)
        ImageView mAdd;

        @BindView(R.id.img_add_success)
        ImageView mAddSuccess;

        @BindView(R.id.loading)
        Loading mLoading;


        private AddEquipmentsRecylerContract.Presenter mPresenter;
        private Gson gson;
        private EquipmentModel mEquipmentModel;


        public MyViewHolder(View itemView) {
            super(itemView);
            new AddEquipmentsRecylerPresenter(this);
        }

        @Override
        protected void onBind(EquipmentModel equipmentModel) {
            mEquipmentModel = equipmentModel;
            mName.setText(equipmentModel.getDEVNAME());
            mMac.setText(equipmentModel.getMAC());
            String type = equipmentModel.getDEVNAME().substring(0, 4);
            mPresenter.getEquipmentPhoto("2", type);
        }

        @OnClick(R.id.img_add)
        void onAddClick() {
            mAdd.setVisibility(View.GONE);
            mAddSuccess.setVisibility(View.GONE);
            mLoading.setVisibility(View.VISIBLE);
            mLoading.setForegroundColor(Color.parseColor("#75B62B"));
            mLoading.start();
            startBind();
        }

        private void startBind() {
            final String timeStamp = DateUtils.getCurrentDateTimes();
            BindEquipmentModel model = new BindEquipmentModel("BR", timeStamp);
            gson = new Gson();
            final String request = gson.toJson(model);
            Log.d("sunhengchao", "startbind: " + request);
            //mEquipmentModel.getLTID()   "2000110256.1.2353.24.219"
            Run.onUiAsync(new Action() {
                @Override
                public void call() {
                    Toast.makeText(AddEquipmentsActivity.this, "request:" + request + "\n" + "LTID:" + mEquipmentModel.getLTID(), Toast.LENGTH_LONG).show();
                }
            });
            LongTooth.request(mEquipmentModel.getLTID(), "longtooth", LongToothTunnel.LT_ARGUMENTS, request.getBytes(), 0, request.getBytes().length,
                    new SampleAttachment(), new LongToothServiceResponseHandler() {
                        @Override
                        public void handleServiceResponse(LongToothTunnel ltt, String ltid_str,
                                                          String service_str, int data_type, byte[] args,
                                                          LongToothAttachment attachment) {
                            String result = new String(args);
                            Log.d("sunhengchao", "handleServiceResponse: " + new String(args));
                            LongToothRspModel longToothRspModel = gson.fromJson(result, LongToothRspModel.class);
                            if (longToothRspModel.getCODE() == 0) {
                                String mEquipmentName = mEquipmentModel.getDEVNAME();
                                String macAddress = mEquipmentModel.getMAC();
                                String token = Account.getToken();
                                String serialNum = "";
                                String version = mEquipmentModel.get_$FirmwareRev196();
                                String series = mEquipmentName.substring(0, 5);
                                if (mPresenter != null) {
                                    mPresenter.addEquipment(token, mEquipmentName, macAddress, serialNum, version, mEquipmentModel.getLTID(), timeStamp, series);
                                }
                            } else {
                                Run.onUiAsync(new Action() {
                                    @Override
                                    public void call() {
                                        Application.showToast(R.string.add_failed);
                                        mLoading.stop();
                                        mAdd.setVisibility(View.VISIBLE);
                                        mAddSuccess.setVisibility(View.GONE);
                                        mLoading.setVisibility(View.GONE);
                                    }
                                });
                            }
                        }
                    });

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

        @Override
        public void addEquipmentSuccess(EquipmentCard card) {
            mLoading.stop();
            mAdd.setVisibility(View.GONE);
            mAddSuccess.setVisibility(View.VISIBLE);
            mLoading.setVisibility(View.GONE);
            EventBus.getDefault().post(new MessageEvent(HorizontalRecyclerFragment.HORIZONTALRECYLER_REFRESH));
        }

        @Override
        public void addEquipmentFailed() {
            mLoading.stop();
            mAdd.setVisibility(View.VISIBLE);
            mAddSuccess.setVisibility(View.GONE);
            mLoading.setVisibility(View.GONE);
        }

    }


}
