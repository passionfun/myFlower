package bocai.com.yanghuajien.ui.account;

import android.content.Context;
import android.content.Intent;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import bocai.com.yanghuajien.R;
import bocai.com.yanghuajien.base.Application;
import bocai.com.yanghuajien.base.presenter.PresenterActivity;
import bocai.com.yanghuajien.presenter.account.ForgetPasswordContract;
import bocai.com.yanghuajien.presenter.account.ForgetPasswordPresenter;
import bocai.com.yanghuajien.util.adapter.account.CountDownTimerUtils;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者 yuanfei on 2017/11/9.
 * 邮箱 yuanfei221@126.com
 */

public class ForgetPasswordActivity extends PresenterActivity<ForgetPasswordContract.Presenter>
        implements ForgetPasswordContract.View {
    @BindView(R.id.tv_title)
    TextView mTitle;
    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.et_verification_code)
    EditText etVerificationCode;
    @BindView(R.id.tv_get_verification_code)
    TextView tvGetVerificationCode;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.iv_show_psw)
    ImageView ivShowPsw;
    @BindView(R.id.et_re_password)
    EditText etRePassword;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;


    //显示的入口
    public static void show(Context context) {
        context.startActivity(new Intent(context, ForgetPasswordActivity.class));
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_forget_password;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        mTitle.setText("Recover Password");
    }

    @OnClick({R.id.img_back,R.id.btn_confirm,R.id.tv_get_verification_code,R.id.frame_show_pwd})
    void onViewClick(View view) {
        String email = etEmail.getText().toString();
        switch (view.getId()){
            case R.id.img_back:
                finish();
                break;
            case R.id.frame_show_pwd:
                //显示隐藏密码
                if (etPassword.getInputType() != InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    ivShowPsw.setImageResource(R.mipmap.img_show_psw);
                } else {
                    etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    ivShowPsw.setImageResource(R.mipmap.img_hide_psw);
                }
                break;
            case R.id.tv_get_verification_code:
                if (TextUtils.isEmpty(email)){
                    Application.showToast(Application.getStringText(R.string.email_can_not_empty));
                }else {
                    CountDownTimerUtils mCountDownTimerUtils = new CountDownTimerUtils(tvGetVerificationCode,
                            60000, 1000);
                    mCountDownTimerUtils.start();
                    mPresenter.getMsmCode(email);
                }
                break;
            case R.id.btn_confirm:
                //提交按钮
                String smsCode = etVerificationCode.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String rePassword = etRePassword.getText().toString().trim();
                if (TextUtils.isEmpty(email)){
                    Application.showToast(Application.getStringText(R.string.email_can_not_empty));
                    return;
                }
                if (TextUtils.isEmpty(smsCode)||
                        TextUtils.isEmpty(password)||
                        TextUtils.isEmpty(rePassword)){
                    Application.showToast(Application.getStringText(R.string.parameter_error));
                    return;
                }
                mPresenter.modifyPassword(email,smsCode,password,rePassword);
                break;
        }

    }





    @Override
    public void showLoading() {
        super.showLoading();
        btnConfirm.setEnabled(false);
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
        btnConfirm.setEnabled(true);
    }

    @Override
    public void modifyPasswordSuccess() {
        LoginActivity.show(this);
        finish();
    }

    @Override
    protected ForgetPasswordContract.Presenter initPresenter() {
        return new ForgetPasswordPresenter(this);
    }

}
