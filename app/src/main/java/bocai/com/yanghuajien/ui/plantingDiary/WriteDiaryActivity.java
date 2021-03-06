package bocai.com.yanghuajien.ui.plantingDiary;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import boc.com.imgselector.utils.ImageSelectorUtils;
import bocai.com.yanghuajien.R;
import boc.com.imgselector.GlideApp;
import bocai.com.yanghuajien.base.Application;
import bocai.com.yanghuajien.base.RecyclerAdapter;
import bocai.com.yanghuajien.base.presenter.PresenterActivity;
import bocai.com.yanghuajien.model.ImageModel;
import bocai.com.yanghuajien.model.MessageEvent;
import bocai.com.yanghuajien.presenter.plantingDiary.WriteDiaryContract;
import bocai.com.yanghuajien.presenter.plantingDiary.WriteDiaryPresenter;
import bocai.com.yanghuajien.util.ActivityUtil;
import bocai.com.yanghuajien.util.BitmapUtils;
import bocai.com.yanghuajien.util.PermissionUtils;
import bocai.com.yanghuajien.util.ScalTool;
import bocai.com.yanghuajien.util.UiTool;
import bocai.com.yanghuajien.util.persistence.Account;
import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * 作者 yuanfei on 2017/11/13.
 * 邮箱 yuanfei221@126.com
 */

public class WriteDiaryActivity extends PresenterActivity<WriteDiaryContract.Presenter> implements WriteDiaryContract.View {
    @BindView(R.id.img_back)
    ImageView mImgBack;

    @BindView(R.id.ll_write_diary)
    LinearLayout mRootLayout;

    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    @BindView(R.id.et_content)
    EditText mContent;

    @BindView(R.id.tv_location)
    TextView mLocation;

    @BindView(R.id.img_add_picture)
    ImageView mAdd;
    private static final int MY_PERMISSION_REQUEST_CODE = 10001;
    public static final int TAKE_PHOTO_REQUEST_ONE = 0x001;
    public static final int PHOTO_FROM_GALLERY = 0x002;
    public static final String KEY_DIARY_ID = "KEY_DIARY_ID";
    private String mDiaryId;
    private Uri imageUri;
    private RecyclerAdapter<String> mAdapter;
    private String mCityName = "";
    private String mLocationStr = "";
    private String[] storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    //可在其中解析amapLocation获取相应内容。
                    if (!TextUtils.isEmpty(aMapLocation.getAoiName())){
                        mLocationStr = aMapLocation.getAoiName();
                    }else if (!TextUtils.isEmpty(aMapLocation.getStreet())){
                        mLocationStr = aMapLocation.getStreet();
                    }else {
                        mLocationStr = aMapLocation.getCity();
                    }
                    mLocation.setText(mLocationStr);
                    mCityName = aMapLocation.getCity();
                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + aMapLocation.getErrorCode() + ", errInfo:"
                            + aMapLocation.getErrorInfo());
                    mLocation.setText(Application.getStringText(R.string.position_failed));
                    mLocationStr = "";
                    mCityName = "";
                }
            }
        }
    };
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;

    //显示的入口
    public static void show(Context context, String diaryId) {
        Intent intent = new Intent(context, WriteDiaryActivity.class);
        intent.putExtra(KEY_DIARY_ID, diaryId);
        context.startActivity(intent);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_write_diary;
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        mDiaryId = bundle.getString(KEY_DIARY_ID);
        return super.initArgs(bundle);

    }

    @Override
    protected void initWidget() {
        super.initWidget();
        EventBus.getDefault().register(this);
        mRecycler.setLayoutManager(new GridLayoutManager(this, 3));
        mRecycler.setAdapter(mAdapter = new RecyclerAdapter<String>() {

            @Override
            public void add(String s) {
                super.add(s);
                if (mAdapter.getItems().contains("add")) {
                    mAdapter.getItems().remove("add");
                }
                mAdapter.getItems().add("add");
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void add(String... dataList) {
                super.add(dataList);
                if (mAdapter.getItems().contains("add")) {
                    mAdapter.getItems().remove("add");
                }
                mAdapter.getItems().add("add");
                mAdapter.notifyDataSetChanged();
            }

            @Override
            protected int getItemViewType(int position, String s) {
                return R.layout.item_diary_photos;
            }

            @Override
            protected ViewHolder<String> onCreateViewHolder(View root, int viewType) {
                return new WriteDiaryActivity.ViewHolder(root);
            }
        });
        mAdapter.setListener(new RecyclerAdapter.AdapterListenerImpl<String>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder holder, String s) {
                if (mAdapter.getItems().size() <= 9) {
                    if (mAdapter.getItemCount() == (holder.getAdapterPosition() + 1)) {
                        showPop();
                    }
                }
            }
        });

        mLocation.setText(Application.getStringText(R.string.positioning));
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //获取一次定位结果：
        mLocationOption.setOnceLocation(true);
        //设置语言为英文
        mLocationOption.setGeoLanguage(AMapLocationClientOption.GeoLanguage.EN);
        //获取最近3s内精度最高的一次定位结果：
        mLocationOption.setOnceLocationLatest(true);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //申请定位权限
        new RxPermissions(this)
                .request(Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            mLocationClient.startLocation();
                        } else {
                            mLocation.setText(Application.getStringText(R.string.position_failed));
//                        getLocationFail();
                        }
                    }
                });
    }

    @OnClick(R.id.img_back)
    void onBackClick() {
        finish();
    }

    @OnClick(R.id.tv_location)
    void onLocationClick() {
        if (TextUtils.isEmpty(mCityName)) {
            Application.showToast(Application.getStringText(R.string.can_not_position_this_city));
            return;
        }
        LocationActivity.show(this, mCityName);
    }


    @OnClick(R.id.tv_save)
    void onSaveClick() {
        List<String> photoPaths = mAdapter.getItems();
        photoPaths.remove("add");
        uploadPhotos(photoPaths);
    }


    @OnClick(R.id.img_add_picture)
    void onAddPictureClick() {
        if (UiTool.isSoftShowing(this)) {
            UiTool.hideSoftInput(this,mAdd);
        }
        showPop();
    }

    private void showPop() {
        final SelectPicPopupWindow picPopupWindow = new SelectPicPopupWindow(this);
        picPopupWindow.setOnTtemClickListener(new SelectPicPopupWindow.ItemClickListener() {
            @Override
            public void onItemClick(View view) {
                switch (view.getId()) {
                    case R.id.tv_take_photo:
                        picPopupWindow.dismiss();
                        //  拍摄照片
                        new RxPermissions(WriteDiaryActivity.this)
                                .request(Manifest.permission.CAMERA)
                                .subscribe(new Consumer<Boolean>() {
                                    @Override
                                    public void accept(Boolean aBoolean) throws Exception {
                                        if (aBoolean) {
                                            imageUri = BitmapUtils.createImageUri(WriteDiaryActivity.this);
                                            Intent intent = new Intent();
                                            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                                            //如果不设置EXTRA_OUTPUT getData()  获取的是bitmap数据  是压缩后的
                                            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                                            startActivityForResult(intent, TAKE_PHOTO_REQUEST_ONE);
                                        } else {
                                            Application.showToast(Application.getStringText(R.string.please_open_camera_permission));
                                        }
                                    }
                                });
                        break;
                    case R.id.tv_from_gallery:
                        picPopupWindow.dismiss();
                        // 从相册选择照片
                        boolean isHavePermission = PermissionUtils.checkPermissionAllGranted(WriteDiaryActivity.this, storagePermissions);
                        if (!isHavePermission) {
                            //申请权限
                            ActivityCompat.requestPermissions(WriteDiaryActivity.this, storagePermissions, MY_PERMISSION_REQUEST_CODE);
                            return;
                        }
                        soSelectPhonts();
                        break;
                    case R.id.tv_cancel:
                        picPopupWindow.dismiss();
                        break;
                }
            }
        });
        ActivityUtil.setBackgroundAlpha(this, 0.19f);
        picPopupWindow.showAtLocation(mRootLayout, Gravity.BOTTOM, 0, 30);
    }

    private void soSelectPhonts() {
        //最多9张
        int currentCount = mAdapter.getItemCount()==0?0:mAdapter.getItemCount()-1;
        ImageSelectorUtils.openPhoto(this, PHOTO_FROM_GALLERY,false,9-currentCount);
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
                soSelectPhonts();
            } else {
                // 弹出对话框告诉用户需要权限的原因, 并引导用户去应用权限管理中手动打开权限按钮
                PermissionUtils.openAppDetails(this);
            }
        }
    }

    private void uploadPhotos(List<String> pathList) {
        Map<String, RequestBody> params = new HashMap<>();


        if (pathList != null && pathList.size() > 0) {
            for (int i = 0; i < pathList.size(); i++) {
//                File file = new File(pathList.get(i));
                File file = ScalTool.scal(pathList.get(i));
                RequestBody body = RequestBody.create(MediaType.parse("image/jpeg;charset=utf-8"), file);
                params.put("pic_head" + (i + 1) + "\";" + "filename=\"" + file, body);
            }
            mPresenter.addPhotos(params);
        } else {
            String content = mContent.getText().toString();
            if (TextUtils.isEmpty(content)){
                Application.showToast(Application.getStringText(R.string.photo_and_text_cannot_empty_same_time));
                return;
            }
            String token = Account.getToken();
            String diaryId = mDiaryId;
            mPresenter.writeDiary(token, content, mLocationStr, "", diaryId);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_PHOTO_REQUEST_ONE) {
            if (resultCode==0){
                return;
            }
            mAdapter.add(getRealFilePath(WriteDiaryActivity.this, imageUri));
            switchVisibility(true);
        }
        if (data==null){
            return;
        }
        if (resultCode == RESULT_CANCELED) {
            BitmapUtils.delteImageUri(WriteDiaryActivity.this, imageUri);
            return;
        }
        if (requestCode == PHOTO_FROM_GALLERY) {
            ArrayList<String> images = data.getStringArrayListExtra(ImageSelectorUtils.SELECT_RESULT);
            mAdapter.add(images.toArray(new String[0]));
            switchVisibility(true);
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

    private void switchVisibility(boolean isRecyclerVisibile) {
        if (isRecyclerVisibile) {
            mRecycler.setVisibility(View.VISIBLE);
            mAdd.setVisibility(View.INVISIBLE);
        } else {
            mRecycler.setVisibility(View.INVISIBLE);
            mAdd.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void addPhotosSuccess(List<ImageModel.AvatarBean> avatar) {
        String token = Account.getToken();
        String content = mContent.getText().toString();
        StringBuffer buffer = new StringBuffer();
        if (avatar != null && avatar.size() > 0) {
            for (ImageModel.AvatarBean avatarBean : avatar) {
                String photoId = avatarBean.getId() + "";
                buffer.append(photoId).append(",");
            }
        }
        String photosId = buffer.toString();
        String diaryId = mDiaryId;
        mPresenter.writeDiary(token, content, mLocationStr, photosId, diaryId);
    }

    @Override
    public void writeDiarySuccess() {
        finish();
    }

    @Override
    protected WriteDiaryContract.Presenter initPresenter() {
        return new WriteDiaryPresenter(this);
    }

    class ViewHolder extends RecyclerAdapter.ViewHolder<String> {
        @BindView(R.id.img_photo)
        ImageView photo;

        @BindView(R.id.img_delete)
        ImageView delete;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(String s) {

            if (s.equals("add")) {
                GlideApp.with(WriteDiaryActivity.this)
                        .load(R.mipmap.img_add_photos)
                        .centerCrop()
                        .into(photo);
                delete.setVisibility(View.GONE);
            } else {
                GlideApp.with(WriteDiaryActivity.this)
                        .load(s)
                        .centerCrop()
                        .into(photo);
                delete.setVisibility(View.VISIBLE);
            }
            //最多只能九张
            if (mAdapter.getItemCount() > 9) {
                if (s.equals("add")) {
                    photo.setVisibility(View.GONE);
                    delete.setVisibility(View.GONE);
                } else {
                    photo.setVisibility(View.VISIBLE);
                    delete.setVisibility(View.VISIBLE);
                }
            }
        }

        @OnClick(R.id.img_delete)
        void onDeleteClick() {
            mAdapter.remove(mAdapter.getItems().get(getAdapterPosition()));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        mLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
        mLocationClient.onDestroy();//销毁定位客户端，同时销毁本地定位服务。
    }

    @Subscribe
    public void getLocation(MessageEvent messageEvent) {
        if (messageEvent.getType().equals(LocationActivity.LOCATION)){
            mLocationStr = messageEvent.getMessage();
            if (TextUtils.isEmpty(mLocationStr)) {
                mLocation.setText(Application.getStringText(R.string.not_display));
            } else {
                mLocation.setText(mLocationStr);
            }
        }

    }
}
