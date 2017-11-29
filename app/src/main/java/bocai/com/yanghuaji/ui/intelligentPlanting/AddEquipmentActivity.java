package bocai.com.yanghuaji.ui.intelligentPlanting;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.List;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.RecyclerAdapter;
import bocai.com.yanghuaji.base.presenter.PresenterActivity;
import bocai.com.yanghuaji.model.PlantSeriesModel;
import bocai.com.yanghuaji.presenter.intelligentPlanting.AddEquipmentContract;
import bocai.com.yanghuaji.presenter.intelligentPlanting.AddEquuipmentPresenter;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者 yuanfei on 2017/11/14.
 * 邮箱 yuanfei221@126.com
 */

public class AddEquipmentActivity extends PresenterActivity<AddEquipmentContract.Presenter>
        implements AddEquipmentContract.View, XRecyclerView.LoadingListener {
    @BindView(R.id.img_back)
    ImageView mImgBack;

    @BindView(R.id.recycler)
    XRecyclerView mRecyler;

    private RecyclerAdapter<PlantSeriesModel.PlantSeriesCard> mAdapter;
    private int page = 1;

    //显示的入口
    public static void show(Context context) {
        context.startActivity(new Intent(context, AddEquipmentActivity.class));
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_add_equipment;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        mRecyler.setLayoutManager(new LinearLayoutManager(this));
        mRecyler.setAdapter(mAdapter = new RecyclerAdapter<PlantSeriesModel.PlantSeriesCard>() {
            @Override
            protected int getItemViewType(int position, PlantSeriesModel.PlantSeriesCard plantSeriesCard) {
                return R.layout.item_equipment_series_list;
            }

            @Override
            protected ViewHolder<PlantSeriesModel.PlantSeriesCard> onCreateViewHolder(View root, int viewType) {
                return new AddEquipmentActivity.ViewHolder(root);
            }
        });

        mRecyler.setPullRefreshEnabled(true);
        mRecyler.setLoadingMoreEnabled(true);
        mRecyler.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyler.setLoadingMoreProgressStyle(ProgressStyle.Pacman);
        mRecyler.setLoadingListener(this);

    }

    @Override
    protected void initData() {
        super.initData();
        page = 1;
        mPresenter.getEquipmentSeries("10",page+"");
    }

    @OnClick(R.id.img_back)
    void onBackClick() {
        finish();
    }

    @OnClick(R.id.img_scan)
    void onScanClick() {
        AddEquipmentDisplayActivity.show(this);
    }

    @Override
    public void getEquipmentSeriesSuccess(List<PlantSeriesModel.PlantSeriesCard> cards) {
        if (page == 1) {
            mRecyler.refreshComplete();
            mAdapter.replace(cards);
        } else {
            mRecyler.loadMoreComplete();
            mAdapter.add(cards);
        }

    }

    @Override
    protected AddEquipmentContract.Presenter initPresenter() {
        return new AddEquuipmentPresenter(this);
    }

    @Override
    public void onRefresh() {
        page = 1;
        mPresenter.getEquipmentSeries("10",page+"");
    }

    @Override
    public void onLoadMore() {
        page++;
        mPresenter.getEquipmentSeries("10",page+"");
    }


    class ViewHolder extends RecyclerAdapter.ViewHolder<PlantSeriesModel.PlantSeriesCard> {
        @BindView(R.id.img_left)
        ImageView mImgLeft;

        @BindView(R.id.tv_name)
        TextView mName;


        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(PlantSeriesModel.PlantSeriesCard plantSeriesCard) {
            if (getAdapterPosition() % 2 == 0) {
                mImgLeft.setBackgroundColor(Color.parseColor("#67B91A"));
            } else {
                mImgLeft.setBackgroundColor(Color.parseColor("#4F9818"));
            }
            mName.setText(plantSeriesCard.getTitle());
        }
    }


}
