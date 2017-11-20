package bocai.com.yanghuaji.ui.plantingDiary;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.RecyclerAdapter;
import bocai.com.yanghuaji.base.presenter.PrensterFragment;
import bocai.com.yanghuaji.model.DiaryListModel;
import bocai.com.yanghuaji.presenter.plantingDiary.PlantDiaryListContract;
import bocai.com.yanghuaji.presenter.plantingDiary.PlantDiaryListPresenter;
import bocai.com.yanghuaji.util.persistence.Account;
import butterknife.BindView;
import butterknife.OnClick;


/**
 * 作者 yuanfei on 2017/11/9.
 * 邮箱 yuanfei221@126.com
 */

public class PlantingDiaryFragment extends PrensterFragment<PlantDiaryListContract.Presenter>
        implements PlantDiaryListContract.View {
    @BindView(R.id.recycler_planting_diary)
    RecyclerView mRecyclerView;

    private RecyclerAdapter<DiaryListModel.DiaryModl> mAdapter;

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
//        List<DiaryListModel.DiaryModl> list = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            DiaryListModel.DiaryModl model = new DiaryListModel.DiaryModl();
//            list.add(model);
//        }
//        mAdapter.add(list);
    }

    @Override
    protected void initData() {
        super.initData();
        mPresenter.getDiaryList(Account.getToken(),"3","1");
    }

    @OnClick(R.id.bt_add_diary)
    void onGoToAddClick() {
        AddDiaryActivity.show(getContext());
    }


    @Override
    public void getDiaryListSuccess(DiaryListModel model) {
        mAdapter.add(model.getList());
    }

    @Override
    protected PlantDiaryListContract.Presenter initPresenter() {
        return new PlantDiaryListPresenter(this);
    }


    class ViewHolder extends RecyclerAdapter.ViewHolder<DiaryListModel.DiaryModl> {
        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(DiaryListModel.DiaryModl diaryListModel) {

        }
    }
}
