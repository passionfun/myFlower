package bocai.com.yanghuaji.presenter.main;

import java.util.List;

import bocai.com.yanghuaji.base.presenter.BaseContract;
import bocai.com.yanghuaji.model.EquipmentRspModel;
import bocai.com.yanghuaji.model.GroupRspModel;

/**
 * 作者 yuanfei on 2017/11/25.
 * 邮箱 yuanfei221@126.com
 */

public interface MainActivityContract {

    interface View extends BaseContract.View<Presenter>{
        void getAllEquipmentsSuccess(List<EquipmentRspModel.ListBean> listBeans );

        void getAllGroupsSuccess(List<GroupRspModel.ListBean> listBeans);
    }

    interface Presenter extends BaseContract.Presenter{
        void getAllEquipments(String token,String limit,String page);

        void getAllGroups(String token,String limit,String page);
    }
}
