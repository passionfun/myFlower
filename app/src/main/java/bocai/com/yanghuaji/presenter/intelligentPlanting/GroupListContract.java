package bocai.com.yanghuaji.presenter.intelligentPlanting;

import bocai.com.yanghuaji.base.presenter.BaseContract;
import bocai.com.yanghuaji.model.GroupRspModel;

/**
 * 作者 yuanfei on 2017/11/27.
 * 邮箱 yuanfei221@126.com
 */

public interface GroupListContract {

    interface View extends BaseContract.View<Presenter>{
        void addGroupSuccess(GroupRspModel.ListBean groupCard);
    }

    interface Presenter extends BaseContract.Presenter{
        void addGroup(String token,String groupName);
    }
}
