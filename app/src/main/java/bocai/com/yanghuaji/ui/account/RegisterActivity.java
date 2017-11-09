package bocai.com.yanghuaji.ui.account;

import android.content.Context;
import android.content.Intent;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.Activity;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 注册页面
 * 作者 yuanfei on 2017/11/8.
 * 邮箱 yuanfei221@126.com
 */

public class RegisterActivity extends Activity {
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
    }

    @OnClick(R.id.img_back)
    void onBackClick() {
        finish();
    }

    // todo 获取验证码
    @OnClick(R.id.tv_get_verification_code)
    void onGetVerificationCodeClick() {

    }

    // todo 注册账户
    @OnClick(R.id.bt_register)
    void onRegisterSubmit() {

    }
}
