package bocai.com.yanghuaji.ui.intelligentPlanting;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.Fragment;
import bocai.com.yanghuaji.base.RecyclerAdapter;
import bocai.com.yanghuaji.base.common.Common;
import bocai.com.yanghuaji.model.PlantModel;
import bocai.com.yanghuaji.ui.intelligentPlanting.recyclerHelper.GalleryLayoutManager;
import bocai.com.yanghuaji.ui.intelligentPlanting.recyclerHelper.ScaleTransformer;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者 yuanfei on 2017/11/15.
 * 邮箱 yuanfei221@126.com
 */

public class HorizontalRecyclerFragment extends Fragment {

    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;

    @BindView(R.id.tv_current)
    TextView mCurrentNum;

    @BindView(R.id.tv_total)
    TextView mTotalNum;

    private List<PlantModel> mList = new ArrayList<>();

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_horizontal_recycler;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        for (int i = 0; i < 10; i++) {
            PlantModel model = new PlantModel();
            mList.add(model);
        }
        mTotalNum.setText(String.valueOf(mList.size()));
        GalleryLayoutManager layoutManager = new GalleryLayoutManager(GalleryLayoutManager.HORIZONTAL);
        layoutManager.attach(mRecyclerView, 1);
        layoutManager.setItemTransformer(new ScaleTransformer());
        Adapter adapter = new Adapter();
        adapter.add(mList);
        mRecyclerView.setAdapter(adapter);
        adapter.setListener(new RecyclerAdapter.AdapterListener<PlantModel>() {
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


    private class Adapter extends RecyclerAdapter<PlantModel> {

        @Override
        protected int getItemViewType(int position, PlantModel plantModel) {
            mCurrentNum.setText(String.valueOf(position));
            return R.layout.item_main_horizontal;
        }

        @Override
        protected ViewHolder<PlantModel> onCreateViewHolder(View root, int viewType) {

            return new ViewHoler(root);
        }
    }

    class ViewHoler extends RecyclerAdapter.ViewHolder<PlantModel> {
        @BindView(R.id.img_setting)
        ImageView mSetting;

        private LinearLayout lldata;
        public ViewHoler(View itemView) {
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


        @OnClick(R.id.img_setting)
        void onSettingClick(){
            AddPlantActivity.show(getContext(),"shc","1");
        }
    }


}
