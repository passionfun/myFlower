package bocai.com.yanghuaji.ui.intelligentPlanting;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bocai.zxinglibrary.android.CaptureActivity;
import com.bocai.zxinglibrary.bean.ZxingConfig;
import com.bocai.zxinglibrary.common.Constant;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.RecyclerAdapter;
import bocai.com.yanghuaji.base.presenter.PresenterActivity;
import bocai.com.yanghuaji.model.EquipmentPhotoModel;
import bocai.com.yanghuaji.model.PlantSeriesModel;
import bocai.com.yanghuaji.presenter.intelligentPlanting.AddEquipmentContract;
import bocai.com.yanghuaji.presenter.intelligentPlanting.AddEquuipmentPresenter;
import bocai.com.yanghuaji.util.PermissionUtils;
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

    private static final int MY_PERMISSION_REQUEST_CODE = 10000;
    private RecyclerAdapter<PlantSeriesModel.PlantSeriesCard> mAdapter;
    private int page = 1;
    private int REQUEST_CODE_SCAN = 111;
    private List<String> scanData;
    private boolean isAddEquipments = false;
    private PlantSeriesModel.PlantSeriesCard mPlantSeriesCard;
    private String[] camera = new String[]{Manifest.permission.CAMERA};

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
        mPresenter.getEquipmentSeries("10", page + "");
    }

    @OnClick(R.id.img_back)
    void onBackClick() {
        finish();
    }

    @OnClick(R.id.img_scan)
    void onScanClick() {
        boolean isHavePermission = PermissionUtils.checkPermissionAllGranted(this, camera);
        if (!isHavePermission) {
            //申请权限
            ActivityCompat.requestPermissions(this, camera, MY_PERMISSION_REQUEST_CODE);
            return;
        }

        doScan();

    }

    private void doScan() {
        Intent intent = new Intent(this, CaptureActivity.class);
        /**
         * ZxingConfig是配置类  可以设置是否显示底部布局，闪光灯，相册，是否播放提示音  震动等动能
         * 也可以不传这个参数
         * 不传的话  默认都为默认不震动  其他都为true
         */
        ZxingConfig config = new ZxingConfig();
        config.setPlayBeep(true);
        config.setShake(true);
        intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
        startActivityForResult(intent, REQUEST_CODE_SCAN);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSION_REQUEST_CODE) {
            boolean permission = true;
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    permission = false;
                    break;
                }
            }
            if (permission) {
                //  授权成功
                doScan();
            } else {
                // 弹出对话框告诉用户需要权限的原因, 并引导用户去应用权限管理中手动打开权限按钮
                PermissionUtils.openAppDetails(this);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {
                String content = data.getStringExtra(Constant.CODED_CONTENT);
                Log.d("shc", "扫描结果为: " + content);
                //型号，序列号，mac地址
                //WG101&8001F023412332&B0F89310CFE6
                String[] result = content.split("&");
                scanData = new ArrayList<>(Arrays.asList(result));
                String equipmentType = result[0];
                isAddEquipments = false;
                mPresenter.getEquipmentPhoto("1", equipmentType);
            }
        }
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
    public void getEquipmentPhotoSuccess(EquipmentPhotoModel photoModel) {
        if (isAddEquipments) {
            //多设备添加
            AddEquipmentDisplayActivity.show(this, photoModel.getPhoto(), mPlantSeriesCard);
        } else {
            //单设备添加
            AddEquipmentDisplayActivity.show(this, photoModel.getPhoto(), (ArrayList<String>) scanData);
        }
    }

    @Override
    protected AddEquipmentContract.Presenter initPresenter() {
        return new AddEquuipmentPresenter(this);
    }

    @Override
    public void onRefresh() {
        page = 1;
        mPresenter.getEquipmentSeries("10", page + "");
    }

    @Override
    public void onLoadMore() {
        page++;
        mPresenter.getEquipmentSeries("10", page + "");
    }


    class ViewHolder extends RecyclerAdapter.ViewHolder<PlantSeriesModel.PlantSeriesCard> {
        @BindView(R.id.ll_root)
        LinearLayout mRoot;

        @BindView(R.id.img_left)
        ImageView mImgLeft;

        @BindView(R.id.tv_name)
        TextView mName;


        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(final PlantSeriesModel.PlantSeriesCard plantSeriesCard) {
            if (getAdapterPosition() % 2 == 0) {
                mImgLeft.setBackgroundColor(Color.parseColor("#67B91A"));
            } else {
                mImgLeft.setBackgroundColor(Color.parseColor("#4F9818"));
            }
            mName.setText(plantSeriesCard.getTitle());
            mRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isAddEquipments = true;
                    mPlantSeriesCard = plantSeriesCard;
                    mPresenter.getEquipmentPhoto("1", plantSeriesCard.getSeries());
                }
            });
        }
    }


}
