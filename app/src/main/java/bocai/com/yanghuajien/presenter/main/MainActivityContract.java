package bocai.com.yanghuajien.presenter.main;

import java.util.List;

import bocai.com.yanghuajien.base.presenter.BaseContract;
import bocai.com.yanghuajien.model.EquipmentCard;
import bocai.com.yanghuajien.model.EquipmentConfigModel;
import bocai.com.yanghuajien.model.GroupRspModel;
import bocai.com.yanghuajien.model.VersionInfoModel;

/**
 * 作者 yuanfei on 2017/11/25.
 * 邮箱 yuanfei221@126.com
 */

public interface MainActivityContract {

    interface View extends BaseContract.View<Presenter>{
//        void getAllEquipmentsSuccess(List<EquipmentRspModel.ListBean> listBeans );

        void getAllGroupsSuccess(List<GroupRspModel.ListBean> listBeans);

        void getEquipmentConfigSuccess(EquipmentConfigModel equipmentConfigModel);

        void getEquipmentConfigFailed();

        void checkVersionSuccess(VersionInfoModel model);

        void getDefaultEquipmentsSuccess(List<EquipmentCard> equipmentCards);
    }

    interface Presenter extends BaseContract.Presenter{
//        void getAllEquipments(String token,String limit,String page);

        void getAllGroups(String token,String limit,String page);

        void getEquipmentConfig(String platform);

        //	平台   0 ios  1 android
        void checkVersion(String platform);

        void getDefaultEquipments(String token);
    }
}
