package bocai.com.yanghuajien.presenter.account;

import bocai.com.yanghuajien.base.presenter.BaseContract;

/**
 * 作者 yuanfei on 2017/12/18.
 * 邮箱 yuanfei221@126.com
 */

public interface BindPhoneContract {

    interface View extends BaseContract.View<Presenter>{
        void getSmsCodeSuccess(String msg);

        void bindPhoneSuccess();
    }

    interface Presenter extends BaseContract.Presenter{
        void getSmsCode(String phone);

        void bindPhone(String phone,String smsCode,String openId,String photoUrl,String name);
    }
}
