package bocai.com.yanghuajien.presenter.account;

import bocai.com.yanghuajien.base.presenter.BaseContract;

/**
 * 作者 yuanfei on 2017/11/16.
 * 邮箱 yuanfei221@126.com
 */

public interface LoginContract {
    interface View extends BaseContract.View<Presenter>{
        void loginSuccess();

        void weChatLoginSuccess();

        void weChatLoginNoBind();

    }
    interface Presenter extends BaseContract.Presenter{

        void passwordLogin(String phone,String password);

        void smsCodeLogin(String phone,String smsCode);

        void getMsmCode(String phone);

        void weChatLogin(String photoUrl,String name,String openId);



    }
}
