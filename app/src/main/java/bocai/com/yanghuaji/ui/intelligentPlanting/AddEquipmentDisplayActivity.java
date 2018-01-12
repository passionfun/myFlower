package bocai.com.yanghuaji.ui.intelligentPlanting;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.Activity;
import boc.com.imgselector.GlideApp;
import bocai.com.yanghuaji.model.PlantSeriesModel;
import bocai.com.yanghuaji.util.ActivityUtil;
import bocai.com.yanghuaji.util.PermissionUtils;
import bocai.com.yanghuaji.util.UiTool;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者 yuanfei on 2017/11/14.
 * 邮箱 yuanfei221@126.com
 */

public class AddEquipmentDisplayActivity extends Activity {
    @BindView(R.id.tv_title)
    TextView mTitle;

    @BindView(R.id.scroll_root)
    ScrollView mRoot;

    @BindView(R.id.img_back)
    ImageView mImgBack;

    @BindView(R.id.img_photo)
    ImageView mEquipmentPhoto;

    @BindView(R.id.cb_led)
    CheckBox mLedStatus;

    @BindView(R.id.img_next)
    ImageView mNext;

    public static final String KEY_SCAN_DATA = "KEY_SCAN_DATA";
    public static final String KEY_PHOTO_PATH = "KEY_PHOTO_PATH";
    private static final int MY_PERMISSION_REQUEST_CODE = 10008;
    private String mPhotoPath;
    private List<String> mScanData;
    private PlantSeriesModel.PlantSeriesCard plantSeriesCard;
    private static boolean isAddEquipments = false;
    private String[] phoneState = new String[]{Manifest.permission.READ_PHONE_STATE,Manifest.permission.WRITE_EXTERNAL_STORAGE};

    //显示的入口(单设备添加)
    public static void show(Context context,String photoPath, ArrayList<String> scanData) {
        Intent intent = new Intent(context, AddEquipmentDisplayActivity.class);
        intent.putExtra(KEY_PHOTO_PATH,photoPath);
        intent.putStringArrayListExtra(AddEquipmentDisplayActivity.KEY_SCAN_DATA,scanData);
        isAddEquipments = false;
        context.startActivity(intent);
    }

    //显示的入口（多设备添加）
    public static void show(Context context,String photoPath, PlantSeriesModel.PlantSeriesCard plantSeriesCard) {
        Intent intent = new Intent(context, AddEquipmentDisplayActivity.class);
        intent.putExtra(KEY_PHOTO_PATH,photoPath);
        intent.putExtra(AddEquipmentsActivity.KEY_PLANT_CARD,plantSeriesCard);
        isAddEquipments = true;
        context.startActivity(intent);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_add_equipment_display;
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        mPhotoPath = bundle.getString(KEY_PHOTO_PATH);
        mScanData = getIntent().getStringArrayListExtra(AddEquipmentDisplayActivity.KEY_SCAN_DATA);
        plantSeriesCard = (PlantSeriesModel.PlantSeriesCard) bundle.getSerializable(AddEquipmentsActivity.KEY_PLANT_CARD);
        return super.initArgs(bundle);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        UiTool.setBlod(mTitle);
        mTitle.setText("添加设备");
        Log.d("gif", "initWidget: "+mPhotoPath);
        if (mPhotoPath.endsWith(".gif")){
            GlideApp.with(this)
                    .asGif()
                    .load(mPhotoPath)
                    .centerCrop()
                    .placeholder(R.mipmap.img_diary_cover_empty)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(mEquipmentPhoto);
        }else {
            GlideApp.with(this)
                    .load(mPhotoPath)
                    .centerCrop()
                    .into(mEquipmentPhoto);
        }

        mNext.setEnabled(false);
    }

    @OnClick(R.id.img_back)
    void onBackClick() {
        finish();
    }

    @OnClick(R.id.cb_led)
    void onLedStatusClick(){
        if (mLedStatus.isChecked()){
            mNext.setEnabled(true);
        }else {
            mNext.setEnabled(false);
        }
    }

    @OnClick(R.id.img_next)
    void onNextClick() {
        boolean isHavePhoneStatePermission = PermissionUtils.checkPermissionAllGranted(this, phoneState);
        if (!isHavePhoneStatePermission) {
            //申请权限
            ActivityCompat.requestPermissions(this, phoneState, MY_PERMISSION_REQUEST_CODE);
            return;
        }
     doNext();

    }

    private void doNext(){
        if (isAddEquipments){
            AddWifiActivity.show(this,plantSeriesCard);
            finish();
        }else {
            AddWifiActivity.show(this, (ArrayList<String>) mScanData);
            finish();
        }
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
            if (!permission) {
                // 弹出对话框告诉用户需要权限的原因, 并引导用户去应用权限管理中手动打开权限按钮
                PermissionUtils.openAppDetails(this);
            }else {
                doNext();
            }
        }
    }


    @OnClick(R.id.tv_show_pop)
    void onShowPopClick() {
        ShowStatePopupWindow popupWindow = new ShowStatePopupWindow(this);
        popupWindow.setOnTtemClickListener(new ShowStatePopupWindow.ItemClickListener() {
            @Override
            public void onItemClick(View view) {
                // 确认点击时候触发
            }
        });

        ActivityUtil.setBackgroundAlpha(this, 0.19f);
        popupWindow.showAtLocation(mRoot, Gravity.CENTER,0,0);
    }

}
