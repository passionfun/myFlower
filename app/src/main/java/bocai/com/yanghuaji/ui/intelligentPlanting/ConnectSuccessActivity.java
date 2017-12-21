package bocai.com.yanghuaji.ui.intelligentPlanting;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.Application;
import bocai.com.yanghuaji.base.GlideApp;
import bocai.com.yanghuaji.base.presenter.PresenterActivity;
import bocai.com.yanghuaji.model.EquipmentModel;
import bocai.com.yanghuaji.model.EquipmentPhotoModel;
import bocai.com.yanghuaji.presenter.intelligentPlanting.ConnectSuccessContract;
import bocai.com.yanghuaji.presenter.intelligentPlanting.ConnectSuccessPresenter;
import bocai.com.yanghuaji.util.UiTool;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者 yuanfei on 2017/11/22.
 * 邮箱 yuanfei221@126.com
 */

public class ConnectSuccessActivity extends PresenterActivity<ConnectSuccessContract.Presenter>
        implements ConnectSuccessContract.View {
    @BindView(R.id.tv_title)
    TextView mTitle;

    @BindView(R.id.img_back)
    ImageView mImgBack;

    @BindView(R.id.tv_equipment_name)
    TextView mName;

    @BindView(R.id.tv_mac)
    TextView mMac;

    @BindView(R.id.tv_next)
    TextView mNext;

    @BindView(R.id.img_photo)
    ImageView mPhoto;

    public static final String KEY_JSON_CONTENT = "KEY_JSON_CONTENT";
    public static final String KEY_EQUIPMENT_ID = "KEY_EQUIPMENT_ID";
    public static final String KEY_EQUIPMENT_NAME = "KEY_EQUIPMENT_NAME";
    private String jsonContent;
    private String mEquipmentName;
    private String mEquipmentId;
    private List<String> mScanData;

    //显示的入口
    public static void show(Context context, String jsonString,String equipmentId,String equipmentName,ArrayList<String> scanData) {
        if (TextUtils.isEmpty(jsonString)) {
            Application.showToast("参数错误");
            return;
        }
        Intent intent = new Intent(context, ConnectSuccessActivity.class);
        intent.putStringArrayListExtra(AddEquipmentDisplayActivity.KEY_SCAN_DATA,scanData);
        intent.putExtra(KEY_JSON_CONTENT, jsonString);
        intent.putExtra(KEY_EQUIPMENT_ID, equipmentId);
        intent.putExtra(KEY_EQUIPMENT_NAME, equipmentName);

        context.startActivity(intent);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_connect_success;
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        mScanData = getIntent().getStringArrayListExtra(AddEquipmentDisplayActivity.KEY_SCAN_DATA);
        jsonContent = bundle.getString(KEY_JSON_CONTENT);
        mEquipmentId = bundle.getString(KEY_EQUIPMENT_ID);
        mEquipmentName = bundle.getString(KEY_EQUIPMENT_NAME);
        return super.initArgs(bundle);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        UiTool.setBlod(mTitle);
        mTitle.setText("连接设备");
        new CountDownTimer(4000, 1000) {
            @Override
            public void onTick(long l) {
                mNext.setText(l / 1000 + "s后进入下一步");
                SpannableString spannableString = new SpannableString(mNext.getText().toString());
                ForegroundColorSpan span = new ForegroundColorSpan(Color.parseColor("#87BC52"));

                spannableString.setSpan(span, 0, 2, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                mNext.setText(spannableString);
            }

            @Override
            public void onFinish() {
                AddPlantActivity.show(ConnectSuccessActivity.this,mEquipmentName,mEquipmentId);
            }
        }.start();

    }

    @Override
    protected void initData() {
        super.initData();
        Gson gson = new Gson();
        List<EquipmentModel> equipmentModels = gson.fromJson(jsonContent, new TypeToken<List<EquipmentModel>>() {
        }.getType());
        String macAddress = equipmentModels.get(0).getMAC();
        mName.setText(mEquipmentName);
        mMac.setText(macAddress);
        mPresenter.getEquipmentPhoto("2",mScanData.get(0));
    }

    @OnClick(R.id.img_back)
    void onBackClick() {
        finish();
    }

    @Override
    public void getEquipmentPhotoSuccess(EquipmentPhotoModel photoModel) {
        GlideApp.with(this)
                .load(photoModel.getPhoto())
                .placeholder(R.mipmap.img_content_empty)
                .centerCrop()
                .into(mPhoto);
    }

    @Override
    protected ConnectSuccessContract.Presenter initPresenter() {
        return new ConnectSuccessPresenter(this);
    }
}
