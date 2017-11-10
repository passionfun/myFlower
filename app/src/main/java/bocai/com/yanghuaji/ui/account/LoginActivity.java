package bocai.com.yanghuaji.ui.account;

import android.graphics.Color;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.Activity;
import bocai.com.yanghuaji.ui.main.MainActivity;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者 yuanfei on 2017/11/8.
 * 邮箱 yuanfei221@126.com
 */

public class LoginActivity extends Activity {
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






    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
    }

    @OnClick(R.id.verification_code_login)
    void onVerificationLogin(){
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
    void onPasswordLogin(){
        mTvVerificationCodeLogin.setTextColor(Color.parseColor("#999999"));
        mImgVerificationCodeLogin.setVisibility(View.INVISIBLE);
        mTvPasswordLogin.setTextColor(Color.parseColor("#87BC52"));
        mImgPasswordLogin.setVisibility(View.VISIBLE);
        mEditInputPhoneNumber.setHint(R.string.input_account);
        mEditInputPassword.setHint(R.string.input_password);
        mTvGetVerification.setText(R.string.forgot_password);
        isPasswordLogin = true;
    }

    // todo 账户登录
    @OnClick(R.id.bt_login)
    void onSubmitClick(){
        MainActivity.show(this);
    }

    // todo 创建新账户
    @OnClick(R.id.tv_create_new_account)
    void onCreateNewAccount(){
        RegisterActivity.show(this);
    }

    // todo 微信登录
    @OnClick(R.id.tv_wechat_login)
    void onWechatLogin(){

    }

    // todo 获取验证码或者忘记密码
    @OnClick(R.id.tv_get_verification_code)
    void onGetVerification(){
        //忘记密码
        if (isPasswordLogin){
        ForgetPasswordActivity.show(this);
        }else {
            //获取验证码
        }
    }
}
