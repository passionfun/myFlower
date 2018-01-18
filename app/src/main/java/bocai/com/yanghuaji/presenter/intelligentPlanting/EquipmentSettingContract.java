package bocai.com.yanghuaji.presenter.intelligentPlanting;

import java.util.List;
import java.util.Map;

import bocai.com.yanghuaji.base.presenter.BaseContract;
import bocai.com.yanghuaji.model.AutoModel;
import bocai.com.yanghuaji.model.EquipmentInfoModel;
import bocai.com.yanghuaji.model.EquipmentSetupModel;
import bocai.com.yanghuaji.model.GroupRspModel;

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
