package bocai.com.yanghuajien.presenter.personalCenter;

import bocai.com.yanghuajien.base.presenter.BaseContract;
import bocai.com.yanghuajien.model.NoticeStatusRspModel;

/**
 * 作者 yuanfei on 2017/12/12.
 * 邮箱 yuanfei221@126.com
 */

public interface PersonalCenterContract {

    interface View extends BaseContract.View<Presenter>{
        void getNoticeStatusSuccess(NoticeStatusRspModel noticeStatusRspModel);
    }


    interface Presenter extends BaseContract.Presenter {
        void getNoticeStatus(String token);
    }
}
