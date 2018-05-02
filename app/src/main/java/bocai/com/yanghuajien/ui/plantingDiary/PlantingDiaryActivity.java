package bocai.com.yanghuajien.ui.plantingDiary;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import bocai.com.yanghuajien.R;
import boc.com.imgselector.GlideApp;
import bocai.com.yanghuajien.base.RecyclerAdapter;
import bocai.com.yanghuajien.base.presenter.PresenterActivity;
import bocai.com.yanghuajien.model.DiaryListModel;
import bocai.com.yanghuajien.model.EquipmentRspModel;
import bocai.com.yanghuajien.model.MessageEvent;
import bocai.com.yanghuajien.presenter.plantingDiary.PlantDiaryListContract;
import bocai.com.yanghuajien.presenter.plantingDiary.PlantDiaryListPresenter;
import bocai.com.yanghuajien.ui.intelligentPlanting.SecondSettingActivity;
import bocai.com.yanghuajien.util.DateUtils;
import bocai.com.yanghuajien.util.persistence.Account;
import bocai.com.yanghuajien.util.widget.EmptyView;
import butterknife.BindView;
import butterknife.OnClick;

import static bocai.com.yanghuajien.ui.plantingDiary.PlantingDiaryFragment.PLANTING_DIARY_REFRESH;

/**
 * 作者 yuanfei on 2017/12/18.
 * 邮箱 yuanfei221@126.com
 */

public class PlantingDiaryActivity extends PresenterActivity<PlantDiaryListContract.Presenter> implements
        XRecyclerView.LoadingListener, PlantDiaryListContract.View {
    @BindView(R.id.img_back)
    ImageView mImgBack;

    @BindView(R.id.empty)
    EmptyView mEmptyView;

    @BindView(R.id.recycler_planting_diary)
    XRecyclerView mRecyclerView;
    private int page = 1;
    private RecyclerAdapter<DiaryListModel.DiaryModl> mAdapter;
    private  EquipmentRspModel.ListBean mPlantBean;



    //显示的入口
    public static void show(Context context, EquipmentRspModel.ListBean plantBean) {
        Intent intent = new Intent(context, PlantingDiaryActivity.class);
        intent.putExtra(SecondSettingActivity.KEY_PLANT_BEAN,plantBean);
        context.startActivity(intent);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_planting_diary;
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        mPlantBean = (EquipmentRspModel.ListBean) bundle.getSerializable(SecondSettingActivity.KEY_PLANT_BEAN);
        return super.initArgs(bundle);
    }

    @OnClick(R.id.img_back)
    void onBackClick() {
        finish();
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        EventBus.getDefault().register(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter = new RecyclerAdapter<DiaryListModel.DiaryModl>() {

            @Override
            protected int getItemViewType(int position, DiaryListModel.DiaryModl diaryListModel) {
                return R.layout.item_diary;
            }

            @Override
            protected ViewHolder onCreateViewHolder(View root, int viewType) {
                return new PlantingDiaryActivity.ViewHolder(root);
            }
        });
        mRecyclerView.setPullRefreshEnabled(true);
        mRecyclerView.setLoadingMoreEnabled(true);
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.Pacman);
        mRecyclerView.setLoadingListener(this);
        mEmptyView.bind(mRecyclerView);
        mEmptyView.setEmptyImg(R.mipmap.status_diary_empty);
        mEmptyView.setEmptyText(R.string.diary_empty);
        setPlaceHolderView(mEmptyView);
    }


    @Override
    protected void initData() {
        super.initData();
        onRefresh();
        mEmptyView.triggerLoading();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(MessageEvent messageEvent) {
        if (messageEvent.getMessage().equals(PLANTING_DIARY_REFRESH)) {
            onRefresh();
        }
    }


    @OnClick(R.id.bt_add_diary)
    void onGoToAddClick() {
        AddDiaryActivity.show(this);
    }


    @Override
    public void onRefresh() {
        page = 1;
        mPresenter.getDiaryList(Account.getToken(), "10", page + "",mPlantBean.getId());
    }

    @Override
    public void onLoadMore() {
        page++;
        mPresenter.getDiaryList(Account.getToken(), "10", page + "",mPlantBean.getId());
    }

    @Override
    public void getDiaryListSuccess(DiaryListModel model) {
        if (page == 1) {
            mRecyclerView.refreshComplete();
            mAdapter.replace(model.getList());
        } else {
            mRecyclerView.loadMoreComplete();
            mAdapter.add(model.getList());
        }
        mPlaceHolderView.triggerOkOrEmpty(mAdapter.getItemCount() > 0);
    }

    @Override
    protected PlantDiaryListContract.Presenter initPresenter() {
        return new PlantDiaryListPresenter(this);
    }

    class ViewHolder extends RecyclerAdapter.ViewHolder<DiaryListModel.DiaryModl> {
        @BindView(R.id.ll_root)
        LinearLayout mRoot;

        @BindView(R.id.tv_diary_title)
        TextView mTitle;

        @BindView(R.id.tv_modify_time)
        TextView mTime;

        @BindView(R.id.img_first)
        ImageView mFirst;

        @BindView(R.id.img_second)
        ImageView mSecond;

        @BindView(R.id.img_third)
        ImageView mThird;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(DiaryListModel.DiaryModl diaryListModel) {
            mTitle.setText(diaryListModel.getBookName());
            mTime.setText("更新日期:" + DateUtils.timet(diaryListModel.getTimeline()));
            List<String> photos = diaryListModel.getPhotos();
            mFirst.setVisibility(View.INVISIBLE);
            mSecond.setVisibility(View.INVISIBLE);
            mThird.setVisibility(View.INVISIBLE);
            if (photos.size() == 1) {
                mFirst.setVisibility(View.VISIBLE);
                GlideApp.with(PlantingDiaryActivity.this)
                        .load(photos.get(0))
                        .placeholder(R.mipmap.img_content_empty)
                        .centerCrop()
                        .into(mFirst);
            } else if (photos.size() == 2) {
                mFirst.setVisibility(View.VISIBLE);
                mSecond.setVisibility(View.VISIBLE);
                GlideApp.with(PlantingDiaryActivity.this)
                        .load(photos.get(0))
                        .placeholder(R.mipmap.img_content_empty)
                        .centerCrop()
                        .into(mFirst);
                GlideApp.with(PlantingDiaryActivity.this)
                        .load(photos.get(1))
                        .placeholder(R.mipmap.img_content_empty)
                        .centerCrop()
                        .into(mSecond);
            } else if (photos.size() == 3) {
                mFirst.setVisibility(View.VISIBLE);
                mSecond.setVisibility(View.VISIBLE);
                mThird.setVisibility(View.VISIBLE);
                GlideApp.with(PlantingDiaryActivity.this)
                        .load(photos.get(0))
                        .placeholder(R.mipmap.img_content_empty)
                        .centerCrop()
                        .into(mFirst);
                GlideApp.with(PlantingDiaryActivity.this)
                        .load(photos.get(1))
                        .placeholder(R.mipmap.img_content_empty)
                        .centerCrop()
                        .into(mSecond);
                GlideApp.with(PlantingDiaryActivity.this)
                        .load(photos.get(2))
                        .placeholder(R.mipmap.img_content_empty)
                        .centerCrop()
                        .into(mThird);
            }
        }

        @OnClick(R.id.img_write_diary)
        void onWriteDiaryClick() {
            DiaryListModel.DiaryModl diaryModl = mAdapter.getItems().get(getAdapterPosition() - 1);
            WriteDiaryActivity.show(PlantingDiaryActivity.this, diaryModl.getId());
        }

        @OnClick(R.id.ll_root)
        void onItemClick() {
            DiaryListModel.DiaryModl diaryModl = mAdapter.getItems().get(getAdapterPosition() - 1);
            DiaryListActivity.show(PlantingDiaryActivity.this, diaryModl.getId());
        }

    }


}

