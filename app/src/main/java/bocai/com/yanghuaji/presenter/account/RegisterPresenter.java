package bocai.com.yanghuaji.presenter.account;

import bocai.com.yanghuaji.base.Application;
import bocai.com.yanghuaji.base.presenter.BasePresenter;
import bocai.com.yanghuaji.presenter.RegisterContract;

/**
 * 作者 yuanfei on 2017/11/15.
 * 邮箱 yuanfei221@126.com
 */

public class RegisterPresenter extends BasePresenter<RegisterContract.View>
implements RegisterContract.Presenter{

    public RegisterPresenter(RegisterContract.View view) {
        super(view);
    }

    @Override
    public void getSmsCode(String phone) {
        Application.showToast("shc");
    }

    @Override
    public void register(String phone, String smsCode, String password, String rePassword) {

    }
}
