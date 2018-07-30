package bocai.com.yanghuajien.ui.account;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import bocai.com.yanghuajien.R;
import bocai.com.yanghuajien.base.Application;
import bocai.com.yanghuajien.base.presenter.PresenterActivity;
import bocai.com.yanghuajien.presenter.account.LoginContract;
import bocai.com.yanghuajien.presenter.account.LoginPresenter;
import bocai.com.yanghuajien.ui.main.MainActivity;
import bocai.com.yanghuajien.util.PermissionUtils;
import bocai.com.yanghuajien.util.persistence.Account;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者 yuanfei on 2017/11/8.
 * 邮箱 yuanfei221@126.com
 */

public class LoginActivity extends PresenterActivity<LoginContract.Presenter>
        implements LoginContract.View {
    public static final String TAG = LoginActivity.class.getName();
    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.bt_login)
    Button btLogin;
    @BindView(R.id.tv_forget_password)
    TextView tvForgetPassword;
    @BindView(R.id.tv_create_new_account)
    TextView tvCreateNewAccount;
    private String[] phoneState = new String[]{Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.GET_ACCOUNTS,
            Manifest.permission.ACCESS_COARSE_LOCATION};
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
    protected void initWidget() {
        super.initWidget();
        boolean isHavePhoneStatePermission = PermissionUtils.checkPermissionAllGranted(this, phoneState);
        if (!isHavePhoneStatePermission) {
            ActivityCompat.requestPermissions(this, phoneState, MY_PERMISSION_REQUEST_CODE);
        } else {
            if (Account.isLogin()) {
                MainActivity.show(this);
                finish();
            }
            tvForgetPassword.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        }
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
                }, 1000);

            } else {
                if (Account.isLogin()) {
                    MainActivity.show(this);
                    finish();
                }
                tvForgetPassword.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
            }
        }
    }


    //  账户登录
    @OnClick({R.id.bt_login, R.id.tv_create_new_account, R.id.tv_forget_password})
    void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.bt_login:
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                mPresenter.login(email, password);
                break;
            case R.id.tv_create_new_account:
                //创建新账户
                RegisterActivity.show(this);
                break;
            case R.id.tv_forget_password:
                //忘记密码
                ForgetPasswordActivity.show(this);
                break;


        }
    }

    @Override
    public void showLoading() {
        super.showLoading();
        btLogin.setEnabled(false);
        tvCreateNewAccount.setEnabled(false);
        tvForgetPassword.setEnabled(false);
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
        btLogin.setEnabled(true);
        tvCreateNewAccount.setEnabled(true);
        tvForgetPassword.setEnabled(true);
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
