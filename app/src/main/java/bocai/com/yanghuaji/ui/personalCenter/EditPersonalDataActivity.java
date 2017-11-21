package bocai.com.yanghuaji.ui.personalCenter;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.Application;
import bocai.com.yanghuaji.base.GlideApp;
import bocai.com.yanghuaji.base.presenter.PresenterActivity;
import bocai.com.yanghuaji.media.GalleryFragment;
import bocai.com.yanghuaji.model.db.User;
import bocai.com.yanghuaji.presenter.personalCenter.EditPersonalDataContract;
import bocai.com.yanghuaji.presenter.personalCenter.EditPersonalDataPresenter;
import bocai.com.yanghuaji.util.ActivityUtil;
import bocai.com.yanghuaji.util.persistence.Account;
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

    final int DATE_DIALOG = 1;
    private int mYear, mMonth, mDay;


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
        mSex.setText("女");
        User user = Account.getUser();
        if (user != null) {
            mName.setText(user.getNickName());
            mSex.setText(user.getSex().equals("1") ? "男" : "女");
            mBirthday.setText(user.getBirthday());
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
                        mSex.setText("男");
                        popupWindow.dismiss();
                        break;
                    case R.id.tv_from_gallery:
                        // 女
                        mSex.setText("女");
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
        new GalleryFragment()
                .setListener(new GalleryFragment.OnSelectedListener() {
                    @Override
                    public void onSelectedImage(String path) {
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
                }).show(getSupportFragmentManager(), GalleryFragment.class.getName());
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
            mBirthday.setText(new StringBuffer().append(mYear).append("年").append(mMonth + 1).append("月").append(mDay).append("日"));
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
            Application.showToast("未知错误");
        }
    }

    private void modifyPortrait(String filePath) {
        Map<String, RequestBody> params = new HashMap<>();
        File file = new File(filePath);
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
