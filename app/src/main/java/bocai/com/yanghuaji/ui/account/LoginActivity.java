package bocai.com.yanghuaji.ui.account;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.Application;
import bocai.com.yanghuaji.base.common.Common;
import bocai.com.yanghuaji.base.common.Factory;
import bocai.com.yanghuaji.base.presenter.PresenterActivity;
import bocai.com.yanghuaji.model.EquipmentConfigModel;
import bocai.com.yanghuaji.presenter.account.LoginContract;
import bocai.com.yanghuaji.presenter.account.LoginPresenter;
import bocai.com.yanghuaji.ui.intelligentPlanting.SampleAttachment;
import bocai.com.yanghuaji.ui.main.MainActivity;
import bocai.com.yanghuaji.util.PermissionUtils;
import bocai.com.yanghuaji.util.adapter.account.CountDownTimerUtils;
import bocai.com.yanghuaji.util.persistence.Account;
import butterknife.BindView;
import butterknife.OnClick;
import xpod.longtooth.LongTooth;
import xpod.longtooth.LongToothAttachment;
import xpod.longtooth.LongToothEvent;
import xpod.longtooth.LongToothEventHandler;
import xpod.longtooth.LongToothServiceRequestHandler;
import xpod.longtooth.LongToothTunnel;

/**
 * 作者 yuanfei on 2017/11/8.
 * 邮箱 yuanfei221@126.com
 */

public class LoginActivity extends PresenterActivity<LoginContract.Presenter>
        implements LoginContract.View {
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

    public static final String TAG = LoginActivity.class.getName();
    private boolean isPasswordLogin = false;
    private String openId;
    private String name;
    private String photoUrl;
    private String[] phoneState = new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final int MY_PERMISSION_REQUEST_CODE = 10009;


    //显示的入口
    public static void show(Context context) {
        context.startActivity(new Intent(context, LoginActivity.class));
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        boolean isHavePhoneStatePermission = PermissionUtils.checkPermissionAllGranted(this, phoneState);
        if (!isHavePhoneStatePermission) {
            //申请权限
            ActivityCompat.requestPermissions(this, phoneState, MY_PERMISSION_REQUEST_CODE);

        }
        return super.initArgs(bundle);

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
            if (!permission) {
                // 弹出对话框告诉用户需要权限的原因, 并引导用户去应用权限管理中手动打开权限按钮
                Application.showToast("没有授权，程序无法使用");
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        finish();
                    }
                },2000);

            }
        }
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        if (Account.isLogin()) {
            MainActivity.show(this);
            finish();
        }
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

    //  微信登录
    @OnClick(R.id.tv_wechat_login)
    void onWechatLogin() {
        UMShareAPI.get(this).getPlatformInfo(this, SHARE_MEDIA.WEIXIN, umAuthListener);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
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
    public void weChatLoginSuccess() {
        //微信登录成功
        MainActivity.show(this);
    }

    @Override
    public void weChatLoginNoBind() {
        //微信登录成功，但未绑定手机
        if (!TextUtils.isEmpty(openId) && !TextUtils.isEmpty(name) && !TextUtils.isEmpty(photoUrl)) {
            BindPhoneActivity.show(this, photoUrl, name, openId);
            finish();
        } else {
            Application.showToast("参数有误");
        }
    }


    @Override
    protected LoginContract.Presenter initPresenter() {
        return new LoginPresenter(this);
    }


    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {
            //授权开始回调
            Log.d(TAG, "onStart: ");
        }

        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
            Log.d(TAG, "onComplete: ");
            if (map != null) {
                //openid
                openId = map.get("openid");
                //昵称
                name = map.get("name");
                //用户头像
                photoUrl = map.get("iconurl");
                mPresenter.weChatLogin(photoUrl, name, openId);
            } else {
                Application.showToast("未知错误");
            }

        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
            Log.d(TAG, "onError: " + throwable.getMessage());
            Application.showToast(throwable.getMessage());
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {
            Log.d(TAG, "onCancel: ");
        }
    };


}
