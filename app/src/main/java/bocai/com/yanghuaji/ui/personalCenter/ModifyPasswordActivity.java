package bocai.com.yanghuaji.ui.personalCenter;

import android.content.Context;
import android.content.Intent;
import android.widget.EditText;
import android.widget.TextView;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.presenter.PresenterActivity;
import bocai.com.yanghuaji.presenter.personalCenter.ModifyPasswordContract;
import bocai.com.yanghuaji.presenter.personalCenter.ModifyPasswordPresenter;
import bocai.com.yanghuaji.util.UiTool;
import bocai.com.yanghuaji.util.persistence.Account;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者 yuanfei on 2017/11/10.
 * 邮箱 yuanfei221@126.com
 */

public class ModifyPasswordActivity extends PresenterActivity<ModifyPasswordContract.Presenter>
        implements ModifyPasswordContract.View {
    @BindView(R.id.tv_title)
    TextView mTitle;

    @BindView(R.id.et_input_password)
    EditText mOriginalPassword;

    @BindView(R.id.et_input_new_password)
    EditText mNewPassword;

    @BindView(R.id.et_confirm_password)
    EditText mRePassword;


    //显示的入口
    public static void show(Context context) {
        context.startActivity(new Intent(context, ModifyPasswordActivity.class));
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_modify_password;
    }

    @Override
    protected void initData() {
        super.initData();
        UiTool.setBlod(mTitle);
        mTitle.setText("修改密码");
    }

    @OnClick(R.id.img_back)
    void onBackClick(){
        finish();
    }

    // 修改密码
    @OnClick(R.id.bt_save)
    void onSaveSubmit() {
        String token = Account.getToken();
        String originalPas = mOriginalPassword.getText().toString();
        String newPas = mNewPassword.getText().toString();
        String reNewPas = mRePassword.getText().toString();
        mPresenter.modifyPassword(token,originalPas,newPas,reNewPas);
    }

    @Override
    protected ModifyPasswordContract.Presenter initPresenter() {
        return new ModifyPasswordPresenter(this);
    }


    @Override
    public void modifySuccess() {
        finish();
    }
}
