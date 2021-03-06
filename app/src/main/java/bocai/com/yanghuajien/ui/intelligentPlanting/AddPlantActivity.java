package bocai.com.yanghuajien.ui.intelligentPlanting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.List;

import bocai.com.yanghuajien.R;
import bocai.com.yanghuajien.base.Application;
import boc.com.imgselector.GlideApp;
import bocai.com.yanghuajien.base.RecyclerAdapter;
import bocai.com.yanghuajien.base.presenter.PresenterActivity;
import bocai.com.yanghuajien.model.EquipmentCard;
import bocai.com.yanghuajien.model.PlantRspModel;
import bocai.com.yanghuajien.presenter.intelligentPlanting.AddPlantContract;
import bocai.com.yanghuajien.presenter.intelligentPlanting.AddPlantPresenter;
import bocai.com.yanghuajien.ui.main.MainActivity;
import bocai.com.yanghuajien.util.ActivityUtil;
import bocai.com.yanghuajien.util.UiTool;
import bocai.com.yanghuajien.util.widget.EmptyView;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者 yuanfei on 2017/11/24.
 * 邮箱 yuanfei221@126.com
 */

public class AddPlantActivity extends PresenterActivity<AddPlantContract.Presenter>
        implements XRecyclerView.LoadingListener, AddPlantContract.View {
    @BindView(R.id.empty)
    EmptyView mEmptyView;

    @BindView(R.id.ll_root)
    LinearLayout mRoot;

    @BindView(R.id.tv_title)
    TextView mTitle;

    @BindView(R.id.img_back)
    ImageView mImgBack;

    @BindView(R.id.tv_right)
    TextView mSkip;

    @BindView(R.id.et_search)
    EditText mSearch;

    @BindView(R.id.recycler)
    XRecyclerView mRecyclerView;

    @BindView(R.id.ll_empty)
    LinearLayout mLinearlayout;

    private int page = 1;
    private RecyclerAdapter<PlantRspModel.PlantCard> mAdapter;
//    public static String KEY_EQUIPMENT_NAME = "KEY_EQUIPMENT_NAME";
//    public static String KEY_EQUIPMENT_ID = "KEY_EQUIPMENT_ID";
    public static String KEY_EQUIPMENT_CARD = "KEY_EQUIPMENT_CARD";
    public static String KEY_PLANT_CARD = "KEY_PLANT_CARD";

    private String mEquipmentName;
    private String mEquipmentId;
    private String mPlantName;
    private String mPlantId;
    private String className;
    private EquipmentCard mEquipmentCard;


    //显示的入口
    public static void show(Context context, EquipmentCard equipmentCard) {
        Intent intent = new Intent(context, AddPlantActivity.class);
        intent.putExtra(KEY_EQUIPMENT_CARD, equipmentCard);
//        intent.putExtra(KEY_EQUIPMENT_ID, equipmentId);
        context.startActivity(intent);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_add_plant;
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        mEquipmentCard = (EquipmentCard) bundle.getSerializable(KEY_EQUIPMENT_CARD);
        if (mEquipmentCard!=null){
            mEquipmentId = mEquipmentCard.getId();
            mEquipmentName = mEquipmentCard.getEquipName();
        }
//        mEquipmentId = bundle.getString(KEY_EQUIPMENT_ID);
//        mEquipmentName = bundle.getString(KEY_EQUIPMENT_NAME);
        className = bundle.getString(PlantSettingActivity.KEY_CLASS_NAME);
        return super.initArgs(bundle);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        UiTool.setBlod(mTitle);
        mTitle.setText(Application.getStringText(R.string.add_plant));
        if (!TextUtils.isEmpty(className)){
            mSkip.setVisibility(View.GONE);
        }else {
            mSkip.setVisibility(View.VISIBLE);
        }
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter = new RecyclerAdapter<PlantRspModel.PlantCard>() {
            @Override
            protected int getItemViewType(int position, PlantRspModel.PlantCard plantCard) {
                return R.layout.item_plant_search;
            }

            @Override
            protected ViewHolder onCreateViewHolder(View root, int viewType) {
                return new AddPlantActivity.ViewHolder(root);
            }
        });

        mRecyclerView.setPullRefreshEnabled(true);
        mRecyclerView.setLoadingMoreEnabled(true);
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.Pacman);
        mRecyclerView.setLoadingListener(this);

        mEmptyView.bind(mRecyclerView);
//        mEmptyView.setEmptyImg(R.mipmap.img_equipment_empty);
//        mEmptyView.setEmptyText(R.string.equipment_empty);
        setPlaceHolderView(mEmptyView);
    }

    @Override
    protected void initData() {
        super.initData();
        //默认开始一次搜索
        mEmptyView.triggerLoading();
        mPresenter.search("", "10", page + "");
        mSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                page = 1;
                mPresenter.search(textView.getText().toString(), "10", page + "");
                //隐藏软键盘
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(mSearch.getWindowToken(), 0);
                }
                return false;
            }
        });
    }

    @OnClick(R.id.img_back)
    void onBackClick() {
        finish();
    }

    @OnClick(R.id.img_delete)
    void onDeleteClick() {
        mSearch.setText("");
    }


    @OnClick(R.id.tv_right)
    void onSkipClick() {
        MainActivity.show(this);
        AddPlantActivity.this.finish();
    }


    @OnClick(R.id.bt_common_plant)
    void onCommonPlantClick() {
        mPresenter.searchCommonPlant();
    }

    @Override
    public void onRefresh() {
        page = 1;
        String keyword = mSearch.getText().toString();
        mPresenter.search(keyword, "10", page + "");
    }

    @Override
    public void onLoadMore() {
        page++;
        String keyword = mSearch.getText().toString();
        mPresenter.search(keyword, "10", page + "");
    }

    @Override
    public void searchSuccess(List<PlantRspModel.PlantCard> cards) {
        mEmptyView.triggerOk();
        if (page == 1) {
            mRecyclerView.refreshComplete();
            mAdapter.replace(cards);
        } else {
            if (cards == null || cards.size() == 0) {
                Application.showToast("没有更多");
            }
            mRecyclerView.loadMoreComplete();
            mAdapter.add(cards);
        }

        if (mAdapter.getItems().size() == 0) {
            mRecyclerView.setVisibility(View.GONE);
            mLinearlayout.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            mLinearlayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void searchCommonPlantSuccess(List<PlantRspModel.PlantCard> cards) {
        if (cards != null && cards.size() > 0) {
            CommonPlantListPopupWindow popupWindow = new CommonPlantListPopupWindow(this);
            ActivityUtil.setBackgroundAlpha(this, 0.19f);
            popupWindow.addData(cards);
            popupWindow.showAtLocation(mRoot, Gravity.CENTER, 0, 0);
            popupWindow.setOnSelectListener(new CommonPlantListPopupWindow.SelectListener() {
                @Override
                public void selected(PlantRspModel.PlantCard card) {
                    mPlantName = card.getPlantName();
                    mPlantId = card.getId();
                    if (TextUtils.isEmpty(className)){
                        FirstSettingActivity.show(AddPlantActivity.this,mEquipmentCard, mPlantName, mPlantId);
                    }else {
                        Intent intent = new Intent();
                        intent.putExtra(KEY_PLANT_CARD,card);
                        setResult(2,intent);
                    }
                    AddPlantActivity.this.finish();
                }
            });
        } else {
            Application.showToast("暂无数据");
        }
    }

    @Override
    protected AddPlantContract.Presenter initPresenter() {
        return new AddPlantPresenter(this);
    }


    class ViewHolder extends RecyclerAdapter.ViewHolder<PlantRspModel.PlantCard> {
        @BindView(R.id.frame_root)
        FrameLayout mroot;

        @BindView(R.id.image)
        ImageView mImage;

        @BindView(R.id.tv_name)
        TextView mName;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(PlantRspModel.PlantCard plantCard) {
            GlideApp.with(AddPlantActivity.this)
                    .load(plantCard.getPhoto())
                    .centerCrop()
                    .into(mImage);
            mName.setText(plantCard.getPlantName());
        }

        @OnClick(R.id.frame_root)
        void onItemClick() {
            PlantRspModel.PlantCard plantCard = mAdapter.getItems().get(getAdapterPosition() - 1);
            String plantName = plantCard.getPlantName();
            String plantId = plantCard.getId();
            if (TextUtils.isEmpty(className)){
                FirstSettingActivity.show(AddPlantActivity.this, mEquipmentCard, plantName, plantId);
            }else {
                Intent intent = new Intent();
                intent.putExtra(KEY_PLANT_CARD,plantCard);
                setResult(2,intent);
            }
            AddPlantActivity.this.finish();
        }


    }
}
