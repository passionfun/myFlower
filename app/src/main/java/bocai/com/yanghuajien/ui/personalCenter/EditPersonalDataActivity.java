package bocai.com.yanghuajien.ui.personalCenter;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yalantis.ucrop.UCrop;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import boc.com.imgselector.utils.ImageSelectorUtils;
import bocai.com.yanghuajien.R;
import bocai.com.yanghuajien.base.Application;
import boc.com.imgselector.GlideApp;
import bocai.com.yanghuajien.base.presenter.PresenterActivity;
import bocai.com.yanghuajien.model.MessageEvent;
import bocai.com.yanghuajien.model.db.User;
import bocai.com.yanghuajien.presenter.personalCenter.EditPersonalDataContract;
import bocai.com.yanghuajien.presenter.personalCenter.EditPersonalDataPresenter;
import bocai.com.yanghuajien.ui.plantingDiary.SelectPicPopupWindow;
import bocai.com.yanghuajien.ui.plantingDiary.WriteDiaryActivity;
import bocai.com.yanghuajien.util.ActivityUtil;
import bocai.com.yanghuajien.util.BitmapUtils;
import bocai.com.yanghuajien.util.PermissionUtils;
import bocai.com.yanghuajien.util.ScalTool;
import bocai.com.yanghuajien.util.UiTool;
import bocai.com.yanghuajien.util.persistence.Account;
import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * 作者 yuanfei on 2017/11/10.
 * 邮箱 yuanfei221@126.com
 */

public class EditPersonalDataActivity extends PresenterActivity<EditPersonalDataContract.Presenter>
        implements EditPersonalDataContract.View {
    @BindView(R.id.ll_root)
    LinearLayout mRootLayout;

    @BindView(R.id.img_add_portrait)
    CircleImageView mPortrait;

    @BindView(R.id.tv_sex)
    TextView mSex;

    @BindView(R.id.tv_birthday)
    TextView mBirthday;

    @BindView(R.id.et_input_name)
    EditText mName;

    private static final int MY_PERMISSION_REQUEST_CODE = 10002;
    private String[] storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    public static final String MODIFY_PERSONAL_DATA_SUCCESS = "MODIFY_PERSONAL_DATA_SUCCESS";
    final int DATE_DIALOG = 1;
    private int mYear, mMonth, mDay;
    private Uri imageUri;


    // 头像的本地路径
    private String mPortraitPath;

    private String mPortraitId;

    //显示的入口
    public static void show(Context context) {
        context.startActivity(new Intent(context, EditPersonalDataActivity.class));
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_edit_personal_data;
    }

    @Override
    protected void initData() {
        super.initData();
        User user = Account.getUser();
        if (user != null) {
            mName.setText(user.getNickName());
            if (user.getSex() != null) {
                switch (user.getSex()) {
                    case "0":
                        mSex.setText(Application.getStringText(R.string.secrecy));
                        break;
                    case "1":
                        mSex.setText(Application.getStringText(R.string.male));
                        break;
                    default:
                        mSex.setText(Application.getStringText(R.string.female));
                        break;
                }
            }
            if (!TextUtils.isEmpty(user.getBirthday())) {
                mBirthday.setText(user.getBirthday());
            } else {
                mBirthday.setText("");
            }
            GlideApp.with(this)
                    .load(user.getRelativePath())
                    .centerCrop()
                    .into(mPortrait);
        }

    }

    @OnClick(R.id.img_back)
    void onBackClick() {
        finish();
    }


    @OnClick(R.id.tv_sex)
    void onSexClick() {
        final SexSelectPopupWindow popupWindow = new SexSelectPopupWindow(this);
        popupWindow.setOnTtemClickListener(new SexSelectPopupWindow.ItemClickListener() {
            @Override
            public void onItemClick(View view) {
                switch (view.getId()) {
                    case R.id.tv_take_photo:
                        // 男
                        mSex.setText(Application.getStringText(R.string.male));
                        popupWindow.dismiss();
                        break;
                    case R.id.tv_from_gallery:
                        // 女
                        mSex.setText(Application.getStringText(R.string.female));
                        popupWindow.dismiss();
                        break;
                    case R.id.tv_secret:
                        // 保密
                        mSex.setText(Application.getStringText(R.string.secrecy));
                        popupWindow.dismiss();
                        break;
                }
            }
        });
        ActivityUtil.setBackgroundAlpha(this, 0.19f);
        popupWindow.showAtLocation(mRootLayout, Gravity.CENTER, 0, 0);
    }

    @OnClick(R.id.tv_birthday)
    void onBirthdayClick() {
        Calendar ca = Calendar.getInstance();
        mYear = ca.get(Calendar.YEAR);
        mMonth = ca.get(Calendar.MONTH);
        mDay = ca.get(Calendar.DAY_OF_MONTH);
        showDialog(DATE_DIALOG);
    }

    @OnClick(R.id.bt_save)
    void onSaveSubmit() {
        String token = Account.getToken();
        String portraitId = mPortraitId;
        String name = mName.getText().toString();
        String set = mSex.getText().toString();
        String birthday = mBirthday.getText().toString();
        mPresenter.modifyData(token, portraitId, name, set, birthday);
    }


    @OnClick(R.id.img_add_portrait)
    void onPortraitClick() {
        if (UiTool.isSoftShowing(this)) {
            UiTool.hideSoftInput(this,mName);
        }
        boolean isHavePermission = PermissionUtils.checkPermissionAllGranted(EditPersonalDataActivity.this, storagePermissions);
        if (!isHavePermission) {
            //申请权限
            ActivityCompat.requestPermissions(EditPersonalDataActivity.this, storagePermissions, MY_PERMISSION_REQUEST_CODE);
            return;
        }

        final SelectPicPopupWindow picPopupWindow = new SelectPicPopupWindow(this);
        picPopupWindow.setOnTtemClickListener(new SelectPicPopupWindow.ItemClickListener() {
            @Override
            public void onItemClick(View view) {
                switch (view.getId()) {
                    case R.id.tv_take_photo:
                        imageUri = BitmapUtils.createImageUri(EditPersonalDataActivity.this);
                        Intent intent = new Intent();
                        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                        //如果不设置EXTRA_OUTPUT getData()  获取的是bitmap数据  是压缩后的
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(intent, WriteDiaryActivity.TAKE_PHOTO_REQUEST_ONE);
                        picPopupWindow.dismiss();
                        break;
                    case R.id.tv_from_gallery:
                        doSelectPhoto();
                        picPopupWindow.dismiss();
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

    private int REQUEST_CODE = 10006;

    private void doSelectPhoto() {
        ImageSelectorUtils.openPhoto(this, REQUEST_CODE, false, 1);

//        new GalleryFragment()
//                .setListener(new GalleryFragment.OnSelectedListener() {
//                    @Override
//                    public void onSelectedImage(String path) {
//                       cropPhoto(path);
//                    }
//                }).show(getSupportFragmentManager(), GalleryFragment.class.getName());
    }

    private void cropPhoto(String path) {
        UCrop.Options options = new UCrop.Options();
        // 设置图片处理的格式JPEG
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        // 设置压缩后的图片精度
        options.setCompressionQuality(96);
        File dPath = Application.getPortraitTmpFile();

        UCrop.of(Uri.fromFile(new File(path)), Uri.fromFile(dPath))
                .withAspectRatio(1, 1) // 1比1比例
                .withMaxResultSize(520, 520) // 返回最大的尺寸
                .withOptions(options)
                .start(EditPersonalDataActivity.this);
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
                onPortraitClick();
            } else {
                // 弹出对话框告诉用户需要权限的原因, 并引导用户去应用权限管理中手动打开权限按钮
                PermissionUtils.openAppDetails(this);
            }
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DATE_DIALOG) {
            return new DatePickerDialog(this, mdateListener, mYear, mMonth, mDay);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener mdateListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            mBirthday.setText(new StringBuffer().append(mYear).append("-")
                    .append(mMonth + 1).append("-")
                    .append(mDay));
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            // 通过UCrop得到对应的Uri
            final Uri resultUri = UCrop.getOutput(data);
            modifyPortrait(resultUri.getPath());
            if (resultUri != null) {
                loadPortrait(resultUri);
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            Application.showToast(Application.getStringText(R.string.unknown_error));
        }

        if (requestCode == WriteDiaryActivity.TAKE_PHOTO_REQUEST_ONE) {
            if (resultCode == 0) {
                return;
            }
            cropPhoto(getRealFilePath(EditPersonalDataActivity.this, imageUri));
        }

        if (requestCode == REQUEST_CODE && data != null) {
            ArrayList<String> stringArrayListExtra = data.getStringArrayListExtra(ImageSelectorUtils.SELECT_RESULT);
            if (stringArrayListExtra != null) {
                cropPhoto(stringArrayListExtra.get(0));
            }
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


    private void modifyPortrait(String filePath) {
        Log.d("shc", "modifyPortrait: "+filePath);
        Map<String, RequestBody> params = new HashMap<>();
//        File file = new File(filePath);
        File file = ScalTool.scal(filePath);
        RequestBody body = RequestBody.create(MediaType.parse("image/jpeg;charset=utf-8"), file);
        params.put("pic_head" + "\";" + "filename=\"" + file, body);
        mPresenter.modifyPortrait(params);
    }


    /**
     * 加载Uri到当前的头像中
     */
    private void loadPortrait(Uri uri) {
        // 得到头像地址
        mPortraitPath = uri.getPath();
        Log.d("shc", "onActivityResult: " + mPortraitPath);
        GlideApp.with(this)
                .load(uri)
                .centerCrop()
                .into(mPortrait);
    }

    @Override
    public void modifyDataSuccess() {
        Application.showToast("Amend successfully");
        EventBus.getDefault().post(new MessageEvent(MODIFY_PERSONAL_DATA_SUCCESS));
        finish();
    }

    @Override
    public void modifyPortraitSuccess(String portraitId) {
        mPortraitId = portraitId;
    }

    @Override
    protected EditPersonalDataContract.Presenter initPresenter() {
        return new EditPersonalDataPresenter(this);
    }
}
