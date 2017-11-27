package bocai.com.yanghuaji.ui.intelligentPlanting;

import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.Application;
import bocai.com.yanghuaji.base.Fragment;
import bocai.com.yanghuaji.base.RecyclerAdapter;
import bocai.com.yanghuaji.base.common.Common;
import bocai.com.yanghuaji.model.PlantModel;
import butterknife.BindView;

/**
 * 作者 yuanfei on 2017/11/25.
 * 邮箱 yuanfei221@126.com
 */

public class VeticalRecyclerFragment extends Fragment implements XRecyclerView.LoadingListener {
    @BindView(R.id.recycler)
    XRecyclerView mRecycler;

    private RecyclerAdapter<PlantModel> mAdapter;
    private List<PlantModel> mList = new ArrayList<>();

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_vertical_recycler;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecycler.setAdapter(mAdapter = new RecyclerAdapter<PlantModel>() {
            @Override
            protected int getItemViewType(int position, PlantModel plantModel) {
                return R.layout.item_main_vertial;
            }

            @Override
            protected ViewHolder<PlantModel> onCreateViewHolder(View root, int viewType) {
                return new VeticalRecyclerFragment.ViewHolder(root);
            }
        });

        mRecycler.setPullRefreshEnabled(true);
        mRecycler.setLoadingMoreEnabled(true);
        mRecycler.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecycler.setLoadingMoreProgressStyle(ProgressStyle.Pacman);
        mRecycler.setLoadingListener(this);
        mAdapter.setListener(new RecyclerAdapter.AdapterListener<PlantModel>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder holder, PlantModel plantModel) {
                Log.d("test", Common.Constance.H5_BASE + "product.html?id="+ plantModel.getPlantType());
                PlantingDateAct.show(getContext(), Common.Constance.H5_BASE + "product.html?id="+ plantModel.getPlantType());
            }

            @Override
            public void onItemLongClick(RecyclerAdapter.ViewHolder holder, PlantModel plantModel) {

            }
        });
    }

    @Override
    protected void initData() {
        super.initData();

        for (int i = 0; i < 10; i++) {
            PlantModel model = new PlantModel();
            mList.add(model);
        }
        mAdapter.add(mList);
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {
        Application.showToast("no more");
    }


    class ViewHolder extends RecyclerAdapter.ViewHolder<PlantModel>{

        private LinearLayout lldata;

        public ViewHolder(View itemView) {
            super(itemView);
            lldata = itemView.findViewById(R.id.ll_data);
        }

        @Override
        protected void onBind(final PlantModel plantModel) {
            lldata.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("test",Common.Constance.H5_BASE + "product_data.html?id="+ plantModel.getPlantType());
                    PlantingDateAct.show(getContext(), Common.Constance.H5_BASE + "product_data.html?id="+ plantModel.getPlantType());
                }
            });
        }
    }



}
