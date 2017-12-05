package bocai.com.yanghuaji.ui.intelligentPlanting;

import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import org.json.JSONArray;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.Application;
import bocai.com.yanghuaji.base.GlideApp;
import bocai.com.yanghuaji.base.RecyclerAdapter;
import bocai.com.yanghuaji.base.common.Common;
import bocai.com.yanghuaji.base.presenter.PrensterFragment;
import bocai.com.yanghuaji.model.EquipmentModel;
import bocai.com.yanghuaji.model.EquipmentRspModel;
import bocai.com.yanghuaji.model.LedSetModel;
import bocai.com.yanghuaji.model.LedSetRspModel;
import bocai.com.yanghuaji.model.PlantStatusModel;
import bocai.com.yanghuaji.model.PlantStatusRspModel;
import bocai.com.yanghuaji.presenter.intelligentPlanting.IntelligentPlantContract;
import bocai.com.yanghuaji.presenter.intelligentPlanting.IntelligentPlantPresenter;
import bocai.com.yanghuaji.util.persistence.Account;
import bocai.com.yanghuaji.util.widget.EmptyView;
import butterknife.BindView;
import butterknife.OnClick;
import io.fog.fog2sdk.MiCODevice;
import io.fogcloud.fog_mdns.helper.SearchDeviceCallBack;
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

    private int page = 1;
    private RecyclerAdapter<EquipmentRspModel.ListBean> mAdapter;
    private Gson gson = new Gson();
    //搜索设备用
    private MiCODevice micodev;
    //所有在线设备的mac集合
    List<String> longtoothIds;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_vertical_recycler;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
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

    @Override
    protected void initData() {
        super.initData();
        page = 1;
        mPresenter.getAllEquipments(Account.getToken(), "10", page + "");


        //开始搜索设备
        final String serviceName = "_easylink._tcp.local.";
        micodev = new MiCODevice(getContext());
        micodev.startSearchDevices(serviceName, new SearchDeviceCallBack() {
            @Override
            public void onDevicesFind(int code, JSONArray deviceStatus) {
                super.onDevicesFind(code, deviceStatus);
                String content = deviceStatus.toString();
                Log.d("shc", "onDevicesFind: " + content);
                if (!TextUtils.isEmpty(content) && !content.equals("[]")) {
                    String jsonContent = content;
                    micodev.stopSearchDevices( null);
                    List<EquipmentModel> equipmentModels = gson.fromJson(jsonContent, new TypeToken<List<EquipmentModel>>() {
                    }.getType());
                    for (EquipmentModel equipmentModel : equipmentModels) {
                        String longtoothId = equipmentModel.getLTID();
                        longtoothIds.add(longtoothId);
                    }
                }
            }
        });
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


    class ViewHolder extends RecyclerAdapter.ViewHolder<EquipmentRspModel.ListBean> {
        @BindView(R.id.ll_root)
        LinearLayout mRoot;

        @BindView(R.id.frame_more)
        FrameLayout mMoreRoot;

        @BindView(R.id.ll_data)
        LinearLayout lldata;

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

        @BindView(R.id.img_tent)
        ImageView mImgTent;

        private EquipmentRspModel.ListBean mModel;


        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(final EquipmentRspModel.ListBean plantModel) {
            mModel = plantModel;
            mEquipmentName.setText(plantModel.getEquipName());
            mPlantName.setText(plantModel.getPlantName());
            mGroupName.setText(plantModel.getGroupName());
            mTime.setText(plantModel.getDays() + "");
            GlideApp.with(getContext())
                    .load(plantModel.getPhoto())
                    .centerCrop()
                    .placeholder(R.mipmap.img_main_empty)
                    .into(mImage);

            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    Run.onUiAsync(new Action() {
                        @Override
                        public void call() {
                            mImgTent.setVisibility(isLineOff()?View.VISIBLE:View.INVISIBLE);
                        }
                    });
                    PlantStatusModel model = new PlantStatusModel("1", "getStatus", "1", plantModel.getPSIGN(), "1", plantModel.getPid());
                    String request = gson.toJson(model);
                    LongTooth.request(plantModel.getLTID(), "longtooth", LongToothTunnel.LT_ARGUMENTS, request.getBytes(),
                            0, request.getBytes().length, null, new LongToothResponse());
                }
            };
            timer.schedule(task, 5000, 30000);
            setLed();
        }

        private boolean isLineOff(){
            if (longtoothIds!=null&&longtoothIds.size()>0){
                longtoothIds.contains(mModel.getLTID());
                return false;
            }else {
                return true;
            }
        }

        private void setLed() {
            mLed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mLed.isChecked()){
                        LedSetModel model = new LedSetModel("On", mModel.getPSIGN());
                        String request = gson.toJson(model);
                        LongTooth.request(mModel.getLTID(), "longtooth", LongToothTunnel.LT_ARGUMENTS, request.getBytes(), 0, request.getBytes().length, null, new LongToothServiceResponseHandler() {
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
                    }else {
                        LedSetModel model = new LedSetModel("Off", mModel.getPSIGN());
                        String request = gson.toJson(model);
                        LongTooth.request(mModel.getLTID(), "longtooth", LongToothTunnel.LT_ARGUMENTS, request.getBytes(), 0, request.getBytes().length, null, new LongToothServiceResponseHandler() {
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

        @OnClick(R.id.ll_data)
        void onDataClick() {
            Log.d("test", Common.Constance.H5_BASE + "product_data.html?id=" + mModel.getId());
            PlantingDateAct.show(getContext(), Common.Constance.H5_BASE + "product_data.html?id=" + mModel.getId(), mModel);
        }

        @OnClick(R.id.img_more)
        void onMoreClick() {
            mMoreRoot.setVisibility(View.VISIBLE);
        }

        @OnClick(R.id.img_close)
        void onCloseClick() {
            mMoreRoot.setVisibility(View.GONE);
        }

        @OnClick(R.id.ll_root)
        void onItemClick() {
            Log.d("test", Common.Constance.H5_BASE + "product.html?id=" + mModel.getId());
            PlantingDateAct.show(getContext(), Common.Constance.H5_BASE + "product.html?id=" + mModel.getId(), mModel);
        }

        @OnClick(R.id.tv_setting)
        void onSettingClick() {
            SecondSettingActivity.show(getContext(), mModel);
        }


        class LongToothResponse implements LongToothServiceResponseHandler {
            @Override
            public void handleServiceResponse(LongToothTunnel longToothTunnel, String s, String s1, int i, byte[] bytes, LongToothAttachment longToothAttachment) {
                String jsonContent = new String(bytes);
                PlantStatusRspModel plantStatusRspModel = gson.fromJson(jsonContent, PlantStatusRspModel.class);
                if (plantStatusRspModel.getCODE() == 0) {
                    //获取数据成功
                    String temperature = plantStatusRspModel.getTemp();
                    String wagerState = plantStatusRspModel.getWaterStat();
                    String Ec = plantStatusRspModel.getEC();//植物的营养值

                }
            }
        }

    }


}
