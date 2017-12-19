package bocai.com.yanghuaji.ui.account;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;
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
    public static final String TAG = LoginActivity.class.getName();
    private boolean isPasswordLogin = false;
    private String openId;
    private String name;
    private String photoUrl;
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

    @Override
    protected void initWidget() {
        super.initWidget();
        mPresenter.getEquipmentConfig();
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
        if (!TextUtils.isEmpty(openId)&&!TextUtils.isEmpty(name)&&!TextUtils.isEmpty(photoUrl)){
            BindPhoneActivity.show(this,photoUrl,name,openId);
            finish();
        }else {
            Application.showToast("参数有误");
        }
    }

    @Override
    public void getEquipmentConfigSuccess(final EquipmentConfigModel equipmentConfigModel) {
        Factory.runOnAsync(new Runnable() {
            @Override
            public void run() {
                try {
//                    //启动长牙
//                    LongTooth.setRegisterHost("114.215.170.184", 53180);
//                    LongTooth.start(Application.getInstance(),
//                            2000110273,
//                            1,
//                            "30820126300D06092A864886F70D010101050003820113003082010E028201023030384645304233423539423931413943414435463341363735463632444645443333343739414132433337423543434333354239323733413330413241354244414539424344373142374334463944423237393430394139463235373245414534424133324141453334433133433036444645333937423531434636413743424143463638434446304432313945334644374442464341383032363645413730353039414239393230374246393735323435314133343943383530394135393232463038413531423344333037353035424646353139363234413835413842443742463634364230444438373944433542453131453230393443363132373944440206303130303031",
//                            new LongToothHandler());

                    //启动长牙
                    LongTooth.setRegisterHost(equipmentConfigModel.getRegisterHost(), Integer.valueOf(equipmentConfigModel.getPort()));
                    LongTooth.start(Application.getInstance(),
                            Integer.valueOf(equipmentConfigModel.getDeveloperID()),
                            Integer.valueOf(equipmentConfigModel.getAppID()),
                            equipmentConfigModel.getAppKey(),
                            new LongToothHandler());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        if (Account.isLogin()) {
            MainActivity.show(this);
            finish();
        }

    }

    @Override
    public void getEquipmentConfigFailed() {
        Factory.runOnAsync(new Runnable() {
            @Override
            public void run() {
                try {
                    //启动长牙
                    LongTooth.setRegisterHost(Account.getRegisterHost(), Integer.valueOf(Account.getPort()));
                    LongTooth.start(Application.getInstance(),
                            Integer.valueOf(Account.getDevelopId()),
                            Integer.valueOf(Account.getAppId()),
                            Account.getAppKey(),
                            new LongToothHandler());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        if (Account.isLogin()) {
            MainActivity.show(this);
            finish();
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






    private class LongToothHandler implements LongToothEventHandler {
        @Override
        public void handleEvent(int code, String ltid_str, String srv_str, byte[] msg, LongToothAttachment attachment) {
//            if (code == LongToothEvent.EVENT_LONGTOOTH_STARTED) {
//
//            }
            Log.d("shcbind", "handleEvent: "+code);

            if (code == LongToothEvent.EVENT_LONGTOOTH_STARTED) {
            } else if (code == LongToothEvent.EVENT_LONGTOOTH_ACTIVATED) {
                LongTooth.addService("n22s", new LongToothNSServer());
                LongTooth.addService("longtooth", new LongToothServer());
            } else if (code == LongToothEvent.EVENT_LONGTOOTH_OFFLINE) {

            } else if (code == LongToothEvent.EVENT_LONGTOOTH_TIMEOUT) {

            } else if (code == LongToothEvent.EVENT_LONGTOOTH_UNREACHABLE) {

            } else if (code == LongToothEvent.EVENT_SERVICE_NOT_EXIST) {

            }
        }
    }


    /**
     * Handler the n22s request
     * */
    private class LongToothNSServer implements LongToothServiceRequestHandler {

        @Override
        public void handleServiceRequest(LongToothTunnel arg0, String arg1,
                                         String arg2, int arg3, byte[] arg4) {
            try {

                if (arg4 != null) {
                    byte[] b = "n22s response---".getBytes();
                    SampleAttachment a = new SampleAttachment();
                    LongTooth.respond(arg0, LongToothTunnel.LT_ARGUMENTS, b, 0,
                            b.length, a);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private int  isResponse = 0;
    /**
     * Handler the longtooth service request
     * */
    private class LongToothServer implements LongToothServiceRequestHandler {

        @Override
        public void handleServiceRequest(LongToothTunnel arg0, String arg1,
                                         String arg2, int arg3, byte[] arg4) {
            try {
                if (arg4 != null) {

                    byte[] b = "longtooth response:".getBytes();
                    SampleAttachment a = new SampleAttachment();
                    LongTooth.respond(arg0, LongToothTunnel.LT_ARGUMENTS, b, 0,
                            b.length, a);
                    if(isResponse<307){
                        Log.d("shcbind", "handleServiceRequest: "+307);
//                        LongTooth.request(serverLongToothId, servername,
//                                LongToothTunnel.LT_ARGUMENTS, sb.toString().getBytes(), 0,
//                                sb.toString().getBytes().length, new SampleAttachment(),
//                                new LongToothResponse());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }













}
