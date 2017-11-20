package bocai.com.yanghuaji.presenter.personalCenter;

import bocai.com.yanghuaji.base.presenter.BaseContract;

/**
 * 作者 yuanfei on 2017/11/20.
 * 邮箱 yuanfei221@126.com
 */

public interface ModifyPasswordContract {
    interface View extends BaseContract.View<Presenter> {
        void modifySuccess();
    }

    interface Presenter extends BaseContract.Presenter {
        void modifyPassword(String token, String password, String newPassword, String rePassword);
    }
}
