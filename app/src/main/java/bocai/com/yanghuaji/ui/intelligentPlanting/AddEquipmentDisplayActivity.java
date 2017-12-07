package bocai.com.yanghuaji.ui.intelligentPlanting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.Activity;
import bocai.com.yanghuaji.base.GlideApp;
import bocai.com.yanghuaji.util.ActivityUtil;
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

    public static final String KEY_SCAN_DATA = "KEY_SCAN_DATA";
    public static final String KEY_PHOTO_PATH = "KEY_PHOTO_PATH";
    private String mPhotoPath;
    private List<String> mScanData;

    //显示的入口
    public static void show(Context context,String photoPath, ArrayList<String> scanData) {
        Intent intent = new Intent(context, AddEquipmentDisplayActivity.class);
        intent.putExtra(KEY_PHOTO_PATH,photoPath);
        intent.putStringArrayListExtra(AddEquipmentDisplayActivity.KEY_SCAN_DATA,scanData);
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
        return super.initArgs(bundle);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        mTitle.setText("添加设备");
        GlideApp.with(this)
                .load(mPhotoPath)
                .centerCrop()
                .into(mEquipmentPhoto);
    }

    @OnClick(R.id.img_back)
    void onBackClick() {
        finish();
    }

    @OnClick(R.id.img_next)
    void onNextClick() {
        AddWifiActivity.show(this, (ArrayList<String>) mScanData);

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
