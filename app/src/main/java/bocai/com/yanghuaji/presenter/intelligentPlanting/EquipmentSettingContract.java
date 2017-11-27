package bocai.com.yanghuaji.presenter.intelligentPlanting;

import java.util.List;

import bocai.com.yanghuaji.base.presenter.BaseContract;
import bocai.com.yanghuaji.model.GroupRspModel;

/**
 * 作者 yuanfei on 2017/11/27.
 * 邮箱 yuanfei221@126.com
 */

public interface EquipmentSettingContract {

    interface View extends BaseContract.View<Presenter>{
        void getGroupListSuccess(List<GroupRspModel.ListBean> groupCards);
    }

    interface Presenter extends BaseContract.Presenter{
        void getGroupList(String token);
    }

}
