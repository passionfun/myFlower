package bocai.com.yanghuajien.ui.plantingDiary;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import boc.com.imgselector.utils.ImageSelectorUtils;
import bocai.com.yanghuajien.R;
import bocai.com.yanghuajien.base.Application;
import boc.com.imgselector.GlideApp;
import bocai.com.yanghuajien.base.presenter.PresenterActivity;
import bocai.com.yanghuajien.model.EquipmentCard;
import bocai.com.yanghuajien.model.MessageEvent;
import bocai.com.yanghuajien.presenter.plantingDiary.AddDiaryContract;
import bocai.com.yanghuajien.presenter.plantingDiary.AddDiaryPresenter;
import bocai.com.yanghuajien.ui.personalCenter.EquipmentListPopupWindow;
import bocai.com.yanghuajien.util.ActivityUtil;
import bocai.com.yanghuajien.util.BitmapUtils;
import bocai.com.yanghuajien.util.DateUtils;
import bocai.com.yanghuajien.util.ScalTool;
import bocai.com.yanghuajien.util.UiTool;
import bocai.com.yanghuajien.util.persistence.Account;
import bocai.com.yanghuajien.util.widget.RoundAngleImageView;
import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * 作者 yuanfei on 2017/11/13.
 * 邮箱 yuanfei221@126.com
 */

public class AddDiaryActivity extends PresenterActivity<AddDiaryContract.Presenter> implements AddDiaryContract.View {
    public final static int PERMISSON_STORGE = 10;
    @BindView(R.id.scroll_root)
    ScrollView mRoot;

    @BindView(R.id.tv_title)
    TextView mTitle;

    @BindView(R.id.tv_right)
    TextView mSave;

    @BindView(R.id.img_back)
    ImageView mImgBack;

    @BindView(R.id.et_diary_name)
    EditText mEditInputDiaryName;


    @BindView(R.id.img_add_cover)
    RoundAngleImageView mCover;

    @BindView(R.id.tv_equipment_name)
    TextView mEquipmentName;
    @BindView(R.id.img_right)
    ImageView imgRight;
    @BindView(R.id.img_right_second)
    ImageView imgRightSecond;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_planting_time)
    TextView tvPlantingTime;

    private String mCoverId;
    private String mName,plantTime;
    private String mEquipmentId;
    private Map<String, String> map = new HashMap<>();
    private Uri imageUri;
    private int REQUEST_CODE = 1008;


    //显示的入口
    public static void show(Context context) {
        context.startActivity(new Intent(context, AddDiaryActivity.class));
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_add_diary;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        UiTool.setBlod(mTitle);
        mTitle.setText(Application.getStringText(R.string.add_diary));
        mSave.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.img_back)
    void onBackClick() {
        finish();
    }

    @OnClick(R.id.img_add_cover)
    void onAddCoverClick() {
        if (UiTool.isSoftShowing(this)) {
            UiTool.hideSoftInput(this,tvPlantingTime);
        }
        if (ContextCompat.checkSelfPermission(AddDiaryActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            //应用还未获取读取本地文件 的权限，询问是否允许
            ActivityCompat.requestPermissions(AddDiaryActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, PERMISSON_STORGE);
        } else {
            final SelectPicPopupWindow picPopupWindow = new SelectPicPopupWindow(this);
            picPopupWindow.setOnTtemClickListener(new SelectPicPopupWindow.ItemClickListener() {
                @Override
                public void onItemClick(View view) {
                    switch (view.getId()) {
                        case R.id.tv_take_photo:
                            imageUri = BitmapUtils.createImageUri(AddDiaryActivity.this);
                            Intent intent = new Intent();
                            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                            //如果不设置EXTRA_OUTPUT getData()  获取的是bitmap数据  是压缩后的
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                            startActivityForResult(intent, WriteDiaryActivity.TAKE_PHOTO_REQUEST_ONE);
                            picPopupWindow.dismiss();
                            break;
                        case R.id.tv_from_gallery:
                            ImageSelectorUtils.openPhoto(AddDiaryActivity.this, REQUEST_CODE, false, 1);
                            picPopupWindow.dismiss();
                            break;
                        case R.id.tv_cancel:
                            picPopupWindow.dismiss();
                            break;
                    }
                }
            });
            ActivityUtil.setBackgroundAlpha(this, 0.19f);
            picPopupWindow.showAtLocation(mRoot, Gravity.BOTTOM, 0, 30);
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        String coverPhotoPath = "";
        if (requestCode == WriteDiaryActivity.TAKE_PHOTO_REQUEST_ONE) {
            if (resultCode==0){
                return;
            }
            coverPhotoPath = getRealFilePath(AddDiaryActivity.this, imageUri);
        }

        if (requestCode == REQUEST_CODE&&data!=null) {
            ArrayList<String> stringArrayListExtra = data.getStringArrayListExtra(ImageSelectorUtils.SELECT_RESULT);
            if (stringArrayListExtra!=null){
                coverPhotoPath = stringArrayListExtra.get(0);
            }
        }

        if (!TextUtils.isEmpty(coverPhotoPath)){
            GlideApp.with(AddDiaryActivity.this)
                            .load(coverPhotoPath)
                            .centerCrop()
                            .into(mCover);
            loadCover(coverPhotoPath);
        }
    }

    public String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }


    private void loadCover(String filePath) {
        Map<String, RequestBody> params = new HashMap<>();
        //File file = new File(filePath);
        File file = ScalTool.scal(filePath);
        RequestBody body = RequestBody.create(MediaType.parse("image/jpeg;charset=utf-8"), file);
        params.put("pic_head" + "\";" + "filename=\"" + file, body);
        mPresenter.loadCover(params);
    }

    @OnClick(R.id.tv_equipment_name)
    void onEquipmentNameClick() {
        mPresenter.getEquipmentList(Account.getToken());
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {

            // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
            View v = getCurrentFocus();

            if (isShouldHideInput(v, ev)) {
                hideSoftInput(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }
    /**
     * 多种隐藏软件盘方法的其中一种
     *
     * @param token
     */
    private void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
     *
     * @param view
     * @param event
     * @return
     */
    private boolean isShouldHideInput(View view, MotionEvent event) {
        if (view != null && (view instanceof EditText)) {
            int[] l = { 0, 0 };
            view.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + view.getHeight(), right = left
                    + view.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
        return false;
    }

    @OnClick(R.id.tv_right)
    void onSaveClick() {
        //非空判断自己写
        map.put("Token", Account.getToken());
        map.put("Photo", mCoverId==null?"":mCoverId);
        map.put("BookName", mEditInputDiaryName.getText().toString());
        map.put("EquipName", mName==null?"":mName);
        map.put("Eid", mEquipmentId==null?"":mEquipmentId);
        map.put("PlantTime", plantTime==null?"":plantTime);
        mPresenter.addDiary(map);
    }

    @Override
    public void addDiarySuccess() {
        EventBus.getDefault().post(new MessageEvent(PlantingDiaryFragment.PLANTING_DIARY_REFRESH));
        finish();
    }

    @Override
    public void loadCoverSuccess(String coverId) {
        mCoverId = coverId;
    }

    @Override
    public void getEquipmentListSuccess(List<EquipmentCard> cards) {
        if (cards != null && cards.size() > 0) {
            EquipmentListPopupWindow popupWindow = new EquipmentListPopupWindow(this);
            ActivityUtil.setBackgroundAlpha(this, 0.19f);
            popupWindow.addData(cards);
            popupWindow.showAtLocation(mRoot, Gravity.CENTER, 0, 0);
            popupWindow.setOnSelectListener(new EquipmentListPopupWindow.SelectListener() {
                @Override
                public void selected(EquipmentCard card) {
                    mName = card.getEquipName();
                    plantTime= DateUtils.timet(card.getPlantTime());
                    mEquipmentId = card.getId();
                    mEquipmentName.setText(mName);
                    tvPlantingTime.setText(plantTime);
                }
            });
        } else {
            Application.showToast(Application.getStringText(R.string.have_no_equipment));
        }

    }

    @Override
    protected AddDiaryContract.Presenter initPresenter() {
        return new AddDiaryPresenter(this);
    }

}
