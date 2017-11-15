package bocai.com.yanghuaji.ui.account;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.regex.Pattern;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.common.Common;
import bocai.com.yanghuaji.base.presenter.PresenterActivity;
import bocai.com.yanghuaji.presenter.RegisterContract;
import bocai.com.yanghuaji.presenter.account.RegisterPresenter;
import bocai.com.yanghuaji.util.adapter.TextWatcherAdapter;
import bocai.com.yanghuaji.util.adapter.account.CountDownTimerUtils;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 注册页面
 * 作者 yuanfei on 2017/11/8.
 * 邮箱 yuanfei221@126.com
 */

public class RegisterActivity extends PresenterActivity<RegisterContract.Presenter>
        implements RegisterContract.View {
    @BindView(R.id.tv_title)
    TextView mTitle;

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

    @BindView(R.id.et_input_password)
    EditText mEditInputPassword;

    @BindView(R.id.et_confirm_password)
    EditText mEditConfirmPasswrd;


    //显示的入口
    public static void show(Context context) {
        context.startActivity(new Intent(context, RegisterActivity.class));
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        mTitle.setText(R.string.phone_register);
        initEditContent();
    }

    private void initEditContent() {
        mEditInputPhoneNumber.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                String phone = s.toString().trim();
                // 校验手机号
                if (Pattern.matches(Common.Constance.REGEX_MOBILE, phone)){
                    mImgCheck.setVisibility(View.VISIBLE);
                }else {
                    mImgCheck.setVisibility(View.GONE);
                }
            }
        });
    }

    @OnClick(R.id.img_back)
    void onBackClick() {
        finish();
    }

    // todo 获取验证码
    @OnClick(R.id.tv_get_verification_code)
    void onGetVerificationCodeClick() {
        CountDownTimerUtils mCountDownTimerUtils = new CountDownTimerUtils(mTvGetVerification, 60000, 1000);
        mCountDownTimerUtils.start();
        String phone = mEditInputPhoneNumber.getText().toString();
        mPresenter.getSmsCode(phone);
    }

    // todo 注册账户
    @OnClick(R.id.bt_register)
    void onRegisterSubmit() {

    }

    @Override
    public void getVerifiCationcodeSuccess() {

    }

    @Override
    public void registerSuccess() {

    }

    @Override
    protected RegisterContract.Presenter initPresenter() {
        return new RegisterPresenter(this);
    }
}
