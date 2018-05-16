package bocai.com.yanghuajien.presenter.account;

import bocai.com.yanghuajien.base.presenter.BaseContract;

/**
 * 作者 yuanfei on 2017/11/16.
 * 邮箱 yuanfei221@126.com
 */

public interface LoginContract {
    interface View extends BaseContract.View<Presenter>{
        void loginSuccess();

    }
    interface Presenter extends BaseContract.Presenter{

        void login(String phone,String password);


    }
}
