package bocai.com.yanghuaji.ui.plantingDiary;

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

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.GlideApp;
import bocai.com.yanghuaji.base.RecyclerAdapter;
import bocai.com.yanghuaji.base.presenter.PrensterFragment;
import bocai.com.yanghuaji.model.DiaryListModel;
import bocai.com.yanghuaji.model.MessageEvent;
import bocai.com.yanghuaji.presenter.plantingDiary.PlantDiaryListContract;
import bocai.com.yanghuaji.presenter.plantingDiary.PlantDiaryListPresenter;
import bocai.com.yanghuaji.util.DateUtils;
import bocai.com.yanghuaji.util.UiTool;
import bocai.com.yanghuaji.util.persistence.Account;
import bocai.com.yanghuaji.util.widget.EmptyView;
import butterknife.BindView;
import butterknife.OnClick;


/**
 * 作者 yuanfei on 2017/11/9.
 * 邮箱 yuanfei221@126.com
 */

public class PlantingDiaryFragment extends PrensterFragment<PlantDiaryListContract.Presenter>
        implements PlantDiaryListContract.View, XRecyclerView.LoadingListener {
    @BindView(R.id.empty)
    EmptyView mEmptyView;

    @BindView(R.id.recycler_planting_diary)
    XRecyclerView mRecyclerView;

    @BindView(R.id.img_shadow)
    ImageView mShadow;



    @BindView(R.id.tv_diary_book)
    TextView mDiaryBook;



    private int page = 1;
    private RecyclerAdapter<DiaryListModel.DiaryModl> mAdapter;
    public static final String PLANTING_DIARY_REFRESH = "PLANTING_DIARY_REFRESH";

    public static PlantingDiaryFragment newInstance() {
        return new PlantingDiaryFragment();
    }


    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_planting_diary;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        UiTool.setBlod(mDiaryBook);
        EventBus.getDefault().register(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter = new RecyclerAdapter<DiaryListModel.DiaryModl>() {

            @Override
            protected int getItemViewType(int position, DiaryListModel.DiaryModl diaryListModel) {
                return R.layout.item_diary;
            }

            @Override
            protected ViewHolder onCreateViewHolder(View root, int viewType) {
                return new PlantingDiaryFragment.ViewHolder(root);
            }
        });
        mRecyclerView.setPullRefreshEnabled(true);
        mRecyclerView.setLoadingMoreEnabled(true);
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.Pacman);
        mRecyclerView.setLoadingListener(this);
        mEmptyView.bind(mRecyclerView,mShadow);
        mEmptyView.setEmptyImg(R.mipmap.status_diary_empty);
        mEmptyView.setEmptyText(R.string.diary_empty);
        setPlaceHolderView(mEmptyView);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(MessageEvent messageEvent){
        if (messageEvent.getMessage().equals(PLANTING_DIARY_REFRESH)){
            onRefresh();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onFirstInit() {
        super.onFirstInit();
    }

    @Override
    protected void initData() {
        super.initData();
        onRefresh();
        mEmptyView.triggerLoading();
    }

    @OnClick(R.id.bt_add_diary)
    void onGoToAddClick() {
        AddDiaryActivity.show(getContext());
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

    @Override
    public void onRefresh() {
        page = 1;
        mPresenter.getDiaryList(Account.getToken(), "10", page + "","");
    }

    @Override
    public void onLoadMore() {
        page++;
        mPresenter.getDiaryList(Account.getToken(), "10", page + "","");
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
            mTime.setText("更新日期:"+ DateUtils.timet(diaryListModel.getTimeline()));
            List<String> photos = diaryListModel.getPhotos();
            mFirst.setVisibility(View.INVISIBLE);
            mSecond.setVisibility(View.INVISIBLE);
            mThird.setVisibility(View.INVISIBLE);
            if (photos.size()==1) {
                mFirst.setVisibility(View.VISIBLE);
                GlideApp.with(getContext())
                        .load(photos.get(0))
                        .placeholder(R.mipmap.img_content_empty)
                        .centerCrop()
                        .into(mFirst);
            }else if (photos.size()==2){
                mFirst.setVisibility(View.VISIBLE);
                mSecond.setVisibility(View.VISIBLE);
                GlideApp.with(getContext())
                        .load(photos.get(0))
                        .placeholder(R.mipmap.img_content_empty)
                        .centerCrop()
                        .into(mFirst);
                GlideApp.with(getContext())
                        .load(photos.get(1))
                        .placeholder(R.mipmap.img_content_empty)
                        .centerCrop()
                        .into(mSecond);
            }else if (photos.size()==3){
                mFirst.setVisibility(View.VISIBLE);
                mSecond.setVisibility(View.VISIBLE);
                mThird.setVisibility(View.VISIBLE);
                GlideApp.with(getContext())
                        .load(photos.get(0))
                        .placeholder(R.mipmap.img_content_empty)
                        .centerCrop()
                        .into(mFirst);
                GlideApp.with(getContext())
                        .load(photos.get(1))
                        .placeholder(R.mipmap.img_content_empty)
                        .centerCrop()
                        .into(mSecond);
                GlideApp.with(getContext())
                        .load(photos.get(2))
                        .placeholder(R.mipmap.img_content_empty)
                        .centerCrop()
                        .into(mThird);
            }
        }

        @OnClick(R.id.img_write_diary)
        void onWriteDiaryClick() {
            DiaryListModel.DiaryModl diaryModl = mAdapter.getItems().get(getAdapterPosition()-1);
            WriteDiaryActivity.show(getContext(),diaryModl.getId());
        }
        @OnClick(R.id.ll_root)
        void onItemClick() {
            DiaryListModel.DiaryModl diaryModl = mAdapter.getItems().get(getAdapterPosition()-1);
            DiaryListActivity.show(getContext(),diaryModl.getId());
        }

    }


}

