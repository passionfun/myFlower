package bocai.com.yanghuaji.presenter.intelligentPlanting;

import bocai.com.yanghuaji.base.presenter.BaseContract;
import bocai.com.yanghuaji.model.EquipmentsByGroupModel;

/**
 * 作者 yuanfei on 2017/11/27.
 * 邮箱 yuanfei221@126.com
 */

public interface EditGroupContract {

    interface View extends BaseContract.View<Presenter>{
        void getEquipmentsByGroupSuccess(EquipmentsByGroupModel model);

        void editGroupSuccess(EquipmentsByGroupModel model);
    }

    interface Presenter extends BaseContract.Presenter{
        void getEquipmentsByGroup(String token,String id);

        void editGroup(String groupId,String token,String groupName,String deleteIds,String addIds);
    }
}
