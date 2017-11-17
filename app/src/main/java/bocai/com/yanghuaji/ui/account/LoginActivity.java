package bocai.com.yanghuaji.ui.account;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.regex.Pattern;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.Application;
import bocai.com.yanghuaji.base.common.Common;
import bocai.com.yanghuaji.base.presenter.PresenterActivity;
import bocai.com.yanghuaji.presenter.account.LoginContract;
import bocai.com.yanghuaji.presenter.account.LoginPresenter;
import bocai.com.yanghuaji.ui.main.MainActivity;
import bocai.com.yanghuaji.util.adapter.account.CountDownTimerUtils;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者 yuanfei on 2017/11/8.
 * 邮箱 yuanfei221@126.com
 */

public class LoginActivity extends PresenterActivity<LoginContract.Presenter>
        implements LoginContract.View {
    private boolean isPasswordLogin = false;
    @BindView(R.id.tv_verification_code_login)
    TextView mTvVerificationCodeLogin;

    @BindView(R.id.img_verification_code_login)
    ImageView mImgVerificationCodeLogin;

    @BindView(R.id.tv_password_login)
    TextView mTvPasswordLogin;

    @BindView(R.id.img_password_login)
    ImageView mImgPasswordLogin;

    @BindView(R.id.et_input_phone_number)
    EditText mEditInputPhoneNumber;

    @BindView(R.id.et_input_password)
    EditText mEditInputPassword;

    @BindView(R.id.tv_get_verification_code)
    TextView mTvGetVerification;

    @BindView(R.id.bt_login)
    Button mLogin;

    @BindView(R.id.tv_create_new_account)
    TextView mRegister;

    @BindView(R.id.tv_wechat_login)
    TextView mWechatLogin;


    //显示的入口
    public static void show(Context context) {
        context.startActivity(new Intent(context, LoginActivity.class));
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_login;
    }


    @OnClick(R.id.verification_code_login)
    void onVerificationLogin() {
        mTvVerificationCodeLogin.setTextColor(Color.parseColor("#87BC52"));
        mImgVerificationCodeLogin.setVisibility(View.VISIBLE);
        mTvPasswordLogin.setTextColor(Color.parseColor("#999999"));
        mImgPasswordLogin.setVisibility(View.INVISIBLE);
        mEditInputPhoneNumber.setHint(R.string.input_phone_number);
        mEditInputPassword.setHint(R.string.input_verification_code);
        mTvGetVerification.setText(R.string.get_verification_code);
        isPasswordLogin = false;
    }

    @OnClick(R.id.password_login)
    void onPasswordLogin() {
        mTvVerificationCodeLogin.setTextColor(Color.parseColor("#999999"));
        mImgVerificationCodeLogin.setVisibility(View.INVISIBLE);
        mTvPasswordLogin.setTextColor(Color.parseColor("#87BC52"));
        mImgPasswordLogin.setVisibility(View.VISIBLE);
        mEditInputPhoneNumber.setHint(R.string.input_account);
        mEditInputPassword.setHint(R.string.input_password);
        mTvGetVerification.setText(R.string.forgot_password);
        isPasswordLogin = true;
    }

    //  账户登录
    @OnClick(R.id.bt_login)
    void onSubmitClick() {
        String phone = mEditInputPhoneNumber.getText().toString();
        String password = mEditInputPassword.getText().toString();
        if (isPasswordLogin) {
            //密码登录
            mPresenter.passwordLogin(phone, password);
        } else {
            //手机验证码登录
            mPresenter.smsCodeLogin(phone, password);
        }

    }

    //  创建新账户
    @OnClick(R.id.tv_create_new_account)
    void onCreateNewAccount() {
        RegisterActivity.show(this);
    }

    // todo 微信登录
    @OnClick(R.id.tv_wechat_login)
    void onWechatLogin() {
        MainActivity.show(this);
    }

    //  获取验证码或者忘记密码
    @OnClick(R.id.tv_get_verification_code)
    void onGetVerification() {
        //  忘记密码
        if (isPasswordLogin) {
            ForgetPasswordActivity.show(this);
        } else {
            //获取验证码
            String phone = mEditInputPhoneNumber.getText().toString();
            if (!Pattern.matches(Common.Constance.REGEX_MOBILE, phone)) {
                Application.showToast("请输入正确的手机号");
            } else {
                CountDownTimerUtils mCountDownTimerUtils = new CountDownTimerUtils(mTvGetVerification, 60000, 1000);
                mCountDownTimerUtils.start();
                mPresenter.getMsmCode(phone);
            }

        }
    }

    @Override
    public void showLoading() {
        super.showLoading();
        mLogin.setEnabled(false);
        mRegister.setEnabled(false);
        mWechatLogin.setEnabled(false);
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
        mLogin.setEnabled(true);
        mRegister.setEnabled(true);
        mWechatLogin.setEnabled(true);
    }

    @Override
    public void loginSuccess() {
        MainActivity.show(this);
        finish();
    }

    @Override
    protected LoginContract.Presenter initPresenter() {
        return new LoginPresenter(this);
    }
}
