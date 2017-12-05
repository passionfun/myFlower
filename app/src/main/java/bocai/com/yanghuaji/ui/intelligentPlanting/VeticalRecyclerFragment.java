package bocai.com.yanghuaji.ui.intelligentPlanting;

import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.GlideApp;
import bocai.com.yanghuaji.base.RecyclerAdapter;
import bocai.com.yanghuaji.base.common.Common;
import bocai.com.yanghuaji.base.presenter.PrensterFragment;
import bocai.com.yanghuaji.model.EquipmentRspModel;
import bocai.com.yanghuaji.model.PlantModel;
import bocai.com.yanghuaji.presenter.intelligentPlanting.IntelligentPlantContract;
import bocai.com.yanghuaji.presenter.intelligentPlanting.IntelligentPlantPresenter;
import bocai.com.yanghuaji.util.persistence.Account;
import bocai.com.yanghuaji.util.widget.EmptyView;
import butterknife.BindView;
import butterknife.OnClick;

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
    private List<PlantModel> mList = new ArrayList<>();
    private String mEquipmentId;
    private String mPlantId;
    private String uuid;
    private String longToothId;

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

        private EquipmentRspModel.ListBean mModel;


        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(final EquipmentRspModel.ListBean plantModel) {
            mModel = plantModel;
            mEquipmentId = mModel.getId();
            mPlantId = mModel.getPid();
            uuid = mModel.getPSIGN();
            longToothId = mModel.getLTID();
            mEquipmentName.setText(plantModel.getEquipName());
            mPlantName.setText(plantModel.getPlantName());
            mGroupName.setText(plantModel.getGroupName());
            mTime.setText(plantModel.getDays() + "");
            GlideApp.with(getContext())
                    .load(plantModel.getPhoto())
                    .centerCrop()
                    .placeholder(R.mipmap.img_main_empty)
                    .into(mImage);
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
            PlantingDateAct.show(getContext(), Common.Constance.H5_BASE + "product.html?id=" + mModel.getId(),mModel);
        }

        @OnClick(R.id.tv_setting)
        void onSettingClick() {
            SecondSettingActivity.show(getContext(),mModel);
        }

    }


}
