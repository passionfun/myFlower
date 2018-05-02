package bocai.com.yanghuajien.ui.account;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.regex.Pattern;

import bocai.com.yanghuajien.R;
import bocai.com.yanghuajien.base.Application;
import bocai.com.yanghuajien.base.common.Common;
import bocai.com.yanghuajien.base.presenter.PresenterActivity;
import bocai.com.yanghuajien.presenter.account.BindPhoneContract;
import bocai.com.yanghuajien.presenter.account.BindPhonePresenter;
import bocai.com.yanghuajien.ui.main.MainActivity;
import bocai.com.yanghuajien.util.adapter.account.CountDownTimerUtils;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者 yuanfei on 2017/11/8.
 * 邮箱 yuanfei221@126.com
 */

public class BindPhoneActivity extends PresenterActivity<BindPhoneContract.Presenter>
        implements BindPhoneContract.View {
    @BindView(R.id.img_back)
    ImageView mImgBack;

    @BindView(R.id.et_input_phone_number)
    EditText mEditInputPhoneNumber;

    @BindView(R.id.img_check)
    ImageView mImgCheck;

    @BindView(R.id.et_input_verification_code)
    EditText mEditInputVerificationCode;

    @BindView(R.id.tv_get_verification_code)
    TextView mTvGetVerification;

    public static final String KEY_OPEN_ID = "KEY_OPEN_ID";
    public static final String KEY_NAME = "KEY_NAME";
    public static final String KEY_PHOTO_URL = "KEY_PHOTO_URL";
    private String openId;
    private String name;
    private String photoUrl;
    private String msgCode;
    private String phone;


    //显示的入口
    public static void show(Context context, String photoUrl, String name, String openId) {
        Intent intent = new Intent(context, BindPhoneActivity.class);
        intent.putExtra(KEY_OPEN_ID, openId);
        intent.putExtra(KEY_NAME, name);
        intent.putExtra(KEY_PHOTO_URL, photoUrl);
        context.startActivity(intent);
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        openId = bundle.getString(KEY_OPEN_ID);
        name = bundle.getString(KEY_NAME);
        photoUrl = bundle.getString(KEY_PHOTO_URL);
        return super.initArgs(bundle);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_bind_phone;
    }

    @OnClick(R.id.img_back)
    void onBackClick() {
        finish();
    }

    //  获取验证码
    @OnClick(R.id.tv_get_verification_code)
    void onGetVerificationCodeClick() {
        phone  = mEditInputPhoneNumber.getText().toString();
        if (!Pattern.matches(Common.Constance.REGEX_MOBILE, phone)) {
            Application.showToast("请输入正确的手机号");
        } else {
            CountDownTimerUtils mCountDownTimerUtils = new CountDownTimerUtils(mTvGetVerification, 60000, 1000);
            mCountDownTimerUtils.start();
            mPresenter.getSmsCode(phone);
        }
    }

    //  绑定手机
    @OnClick(R.id.bt_bind)
    void onBindSubmit() {
        phone  = mEditInputPhoneNumber.getText().toString();
        msgCode = mEditInputVerificationCode.getText().toString();
        mPresenter.bindPhone(phone,msgCode,openId,photoUrl,name);
    }

    @Override
    public void getSmsCodeSuccess(String msg) {
//        msgCode = msg;
    }

    @Override
    public void bindPhoneSuccess() {
        MainActivity.show(this);
        finish();
    }

    @Override
    protected BindPhoneContract.Presenter initPresenter() {
        return new BindPhonePresenter(this);
    }
}
