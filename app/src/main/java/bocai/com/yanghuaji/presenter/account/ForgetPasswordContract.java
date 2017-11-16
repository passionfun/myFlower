package bocai.com.yanghuaji.presenter.account;

import bocai.com.yanghuaji.base.presenter.BaseContract;

/**
 * 作者 yuanfei on 2017/11/16.
 * 邮箱 yuanfei221@126.com
 */

public interface ForgetPasswordContract {
    interface View extends BaseContract.View<Presenter>{
        void modifyPasswordSuccess();
    }

    interface Presenter extends BaseContract.Presenter{
        void modifyPassword(String phone ,String smsCode,String newPassword,String rePassword);
        void getMsmCode(String phone);
    }

}
