package bocai.com.yanghuajien.presenter.account;

import bocai.com.yanghuajien.base.presenter.BaseContract;

/**
 * 作者 yuanfei on 2017/11/15.
 * 邮箱 yuanfei221@126.com
 */

public interface RegisterContract {

    interface View extends BaseContract.View<Presenter>{

        void getVerifiCationcodeSuccess(String msg);

        void registerSuccess();
    }

    interface Presenter extends BaseContract.Presenter{

        void getSmsCode(String phone);

        void register(String phone,String smsCode,String password,String rePassword);
    }
}
