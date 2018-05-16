package bocai.com.yanghuajien.presenter.account;

import bocai.com.yanghuajien.base.presenter.BaseContract;

/**
 * 作者 yuanfei on 2017/11/16.
 * 邮箱 yuanfei221@126.com
 */

public interface ForgetPasswordContract {
    interface View extends BaseContract.View<Presenter>{
        void modifyPasswordSuccess();
    }

    interface Presenter extends BaseContract.Presenter{
        void modifyPassword(String email ,String smsCode,String newPassword,String rePassword);
        void getMsmCode(String phone);
    }

}
