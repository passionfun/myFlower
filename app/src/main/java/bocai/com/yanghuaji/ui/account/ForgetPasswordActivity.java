package bocai.com.yanghuaji.ui.account;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.Activity;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者 yuanfei on 2017/11/9.
 * 邮箱 yuanfei221@126.com
 */

public class ForgetPasswordActivity extends Activity {
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
    EditText mEditConfirmPassword;

    @BindView(R.id.bt_register)
    Button mSave;

    @BindView(R.id.ll_consent_agreement)
    LinearLayout mConsentAgreement;



    //显示的入口
    public static void show(Context context) {
        context.startActivity(new Intent(context, ForgetPasswordActivity.class));
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        mTitle.setText(R.string.forget_password);
        mSave.setText(R.string.save);
        mConsentAgreement.setVisibility(View.INVISIBLE);
    }

    @OnClick(R.id.img_back)
    void onBackClick() {
        finish();
    }

    // todo 获取验证码
    @OnClick(R.id.tv_get_verification_code)
    void onGetVerificationCodeClick() {

    }

    // todo 保存新密码
    @OnClick(R.id.bt_register)
    void onSaveSubmit() {

    }
}
