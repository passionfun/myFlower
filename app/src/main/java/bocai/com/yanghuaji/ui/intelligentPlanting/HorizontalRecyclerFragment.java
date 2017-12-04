package bocai.com.yanghuaji.ui.intelligentPlanting;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.Application;
import bocai.com.yanghuaji.base.GlideApp;
import bocai.com.yanghuaji.base.RecyclerAdapter;
import bocai.com.yanghuaji.base.common.Common;
import bocai.com.yanghuaji.base.presenter.PrensterFragment;
import bocai.com.yanghuaji.model.EquipmentRspModel;
import bocai.com.yanghuaji.model.PlantStatusModel;
import bocai.com.yanghuaji.model.PlantStatusRspModel;
import bocai.com.yanghuaji.presenter.intelligentPlanting.IntelligentPlantContract;
import bocai.com.yanghuaji.presenter.intelligentPlanting.IntelligentPlantPresenter;
import bocai.com.yanghuaji.ui.intelligentPlanting.recyclerHelper.GalleryLayoutManager;
import bocai.com.yanghuaji.ui.intelligentPlanting.recyclerHelper.ScaleTransformer;
import bocai.com.yanghuaji.util.persistence.Account;
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
    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_current)
    TextView mCurrentNum;
    @BindView(R.id.tv_total)
    TextView mTotalNum;
    private int page = 1;
    public static final String TAG = HorizontalRecyclerFragment.class.getName();
    private RecyclerAdapter<EquipmentRspModel.ListBean> mAdapter;
    private Gson gson = new Gson();
    String mEquipmentId;
    String mPlantId;
    String uuid;
    String longToothId;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_horizontal_recycler;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        GalleryLayoutManager layoutManager = new GalleryLayoutManager(GalleryLayoutManager.HORIZONTAL);
        layoutManager.attach(mRecyclerView, 1);
        layoutManager.setItemTransformer(new ScaleTransformer());
        mRecyclerView.setAdapter(mAdapter = new Adapter());
        mAdapter.setListener(new RecyclerAdapter.AdapterListenerImpl<EquipmentRspModel.ListBean>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder holder, EquipmentRspModel.ListBean plantModel) {
                Log.d("test", Common.Constance.H5_BASE + "product.html?id=" + plantModel.getId());
                String url = Common.Constance.H5_BASE + "product.html?id=" + plantModel.getId();
                PlantingDateAct.show(getContext(), url, mEquipmentId, mPlantId, uuid, longToothId );
            }
        });

        layoutManager.setOnItemSelectedListener(new GalleryLayoutManager.OnItemSelectedListener() {
            @Override
            public void onItemSelected(RecyclerView recyclerView, View item, int position) {
                mCurrentNum.setText((position + 1) + "");
            }
        });
        mCurrentNum.setText("1");

    }

    @Override
    protected void initData() {
        super.initData();
        page = 1;
        mPresenter.getAllEquipments(Account.getToken(), "10", page + "");
    }

    @Override
    public void getAllEquipmentsSuccess(List<EquipmentRspModel.ListBean> listBeans) {
        if (page == 1) {
            mAdapter.replace(listBeans);
        } else {
            if (listBeans != null && listBeans.size() == 0) {
                Application.showToast("没有更多");
            }
            mAdapter.add(listBeans);
        }
        mTotalNum.setText(String.valueOf(listBeans.size()));
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

        class MyViewHolder extends RecyclerAdapter.ViewHolder<EquipmentRspModel.ListBean> {
            @BindView(R.id.frame_setting)
            FrameLayout mSettingView;

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
            private EquipmentRspModel.ListBean mModel;
            private boolean isShowSetting = false;

            public MyViewHolder(View itemView) {
                super(itemView);
            }

            @Override
            protected void onBind(final EquipmentRspModel.ListBean plantModel) {
                mModel = plantModel;
                mEquipmentId = mModel.getId();
                mPlantId = mModel.getPid();
                uuid = mModel.getPSIGN();
                longToothId = mModel.getLTID();
                mSettingView.setVisibility(View.INVISIBLE);
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
                        PlantStatusModel model = new PlantStatusModel("1", "getStatus", "1", plantModel.getPSIGN(), "1", plantModel.getPid());
                        String request = gson.toJson(model);
                        LongTooth.request(plantModel.getLTID(), "longtooth", LongToothTunnel.LT_ARGUMENTS, request.getBytes(),
                                0, request.getBytes().length, null, new LongToothResponse());
                    }
                };
                timer.schedule(task, 5000, 5000);
            }

            @OnClick(R.id.ll_data)
            void onDataClick() {
                Log.d("test", Common.Constance.H5_BASE + "product_data.html?id=" + mModel.getId());
                String url = Common.Constance.H5_BASE + "product_data.html?id=" + mModel.getId();
                PlantingDateAct.show(getContext(), url, mEquipmentId, mPlantId, uuid, longToothId);
            }

            @OnClick(R.id.tv_setting_second)
            void onSecondSettingClick() {
                SecondSettingActivity.show(getContext(), mEquipmentId, mPlantId, uuid, longToothId);
            }

            @OnClick(R.id.img_setting)
            void onSettingClick() {
                Log.d(TAG, "run: " + mModel.getPSIGN());
                isShowSetting = !isShowSetting;
                if (isShowSetting) {
                    mSettingView.setVisibility(View.VISIBLE);
                    mSetting.setImageResource(R.mipmap.img_close_main_horizontal);
                } else {
                    mSettingView.setVisibility(View.GONE);
                    mSetting.setImageResource(R.mipmap.img_item_setting);
                }
            }
        }

        class LongToothResponse implements LongToothServiceResponseHandler {
            @Override
            public void handleServiceResponse(LongToothTunnel longToothTunnel, String s, String s1, int i, byte[] bytes, LongToothAttachment longToothAttachment) {
                Log.d("shc", "horizoontalResponse: " + new String(bytes));
                Application.showToast("horizontal" + new String(bytes));
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
