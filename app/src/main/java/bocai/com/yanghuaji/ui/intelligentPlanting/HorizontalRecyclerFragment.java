package bocai.com.yanghuaji.ui.intelligentPlanting;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.Application;
import bocai.com.yanghuaji.base.GlideApp;
import bocai.com.yanghuaji.base.RecyclerAdapter;
import bocai.com.yanghuaji.base.common.Common;
import bocai.com.yanghuaji.base.presenter.PrensterFragment;
import bocai.com.yanghuaji.model.EquipmentRspModel;
import bocai.com.yanghuaji.presenter.intelligentPlanting.IntelligentPlantContract;
import bocai.com.yanghuaji.presenter.intelligentPlanting.IntelligentPlantPresenter;
import bocai.com.yanghuaji.ui.intelligentPlanting.recyclerHelper.GalleryLayoutManager;
import bocai.com.yanghuaji.ui.intelligentPlanting.recyclerHelper.ScaleTransformer;
import bocai.com.yanghuaji.util.persistence.Account;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者 yuanfei on 2017/11/15.
 * 邮箱 yuanfei221@126.com
 */

public class HorizontalRecyclerFragment extends PrensterFragment<IntelligentPlantContract.Presenter>
        implements IntelligentPlantContract.View  {
    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;

    @BindView(R.id.tv_current)
    TextView mCurrentNum;

    @BindView(R.id.tv_total)
    TextView mTotalNum;

    private int page = 1;
    private RecyclerAdapter<EquipmentRspModel.ListBean> mAdapter;

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
                Log.d("test", Common.Constance.H5_BASE + "product.html?id="+ plantModel.getId());
                PlantingDateAct.show(getContext(), Common.Constance.H5_BASE + "product.html?id="+ plantModel.getId());
            }
        });
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
            mCurrentNum.setText(String.valueOf(position));
            return R.layout.item_main_horizontal;
        }

        @Override
        protected ViewHolder<EquipmentRspModel.ListBean> onCreateViewHolder(View root, int viewType) {

            return new MyViewHolder(root);
        }


        class MyViewHolder extends RecyclerAdapter.ViewHolder<EquipmentRspModel.ListBean> {
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

            private LinearLayout lldata;
            private EquipmentRspModel.ListBean  mModel;

            public MyViewHolder(View itemView) {
                super(itemView);
                lldata = itemView.findViewById(R.id.ll_data);
            }

            @Override
            protected void onBind(final EquipmentRspModel.ListBean plantModel) {
                mModel = plantModel;
                mEquipmentName.setText(plantModel.getEquipName());
                mPlantName.setText(plantModel.getPlantName());
                mGroupName.setText(plantModel.getGroupName());
                mTime.setText(plantModel.getDays()+"");
                GlideApp.with(getContext())
                        .load(plantModel.getPhoto())
                        .centerCrop()
                        .placeholder(R.mipmap.img_main_empty)
                        .into(mImage);
            }

            @OnClick(R.id.ll_data)
            void onDataClick(){
                Log.d("test",Common.Constance.H5_BASE + "product_data.html?id="+ mModel.getId());
                PlantingDateAct.show(getContext(), Common.Constance.H5_BASE + "product_data.html?id="+ mModel.getId());
            }


            @OnClick(R.id.img_setting)
            void onSettingClick(){
                AddPlantActivity.show(getContext(),"shc","1");
            }
        }


    }

}
