package bocai.com.yanghuajien.presenter.intelligentPlanting;

import java.util.List;
import java.util.Map;

import bocai.com.yanghuajien.base.presenter.BaseContract;
import bocai.com.yanghuajien.model.AutoModel;
import bocai.com.yanghuajien.model.EquipmentInfoModel;
import bocai.com.yanghuajien.model.EquipmentSetupModel;
import bocai.com.yanghuajien.model.GroupRspModel;

/**
 * 作者 yuanfei on 2017/11/27.
 * 邮箱 yuanfei221@126.com
 */

public interface EquipmentSettingContract {

    interface View extends BaseContract.View<Presenter>{
        void getGroupListSuccess(List<GroupRspModel.ListBean> groupCards);
        void setupEquipmentSuccess(EquipmentSetupModel model);
        void equipmentInfoSuccess(EquipmentInfoModel model);
        void getAutoParaSuccess(List<AutoModel.ParaBean> paraBeans);
        void getAutoParaFailed();
    }

    interface Presenter extends BaseContract.Presenter{
        void getGroupList(String token);
        void setupEquipment(Map<String,String> map);
        void equipmentInfo(Map<String,String> map);
        void getAutoPara(String equipmentId,String plantId,String lifeCircleId);
    }

}
