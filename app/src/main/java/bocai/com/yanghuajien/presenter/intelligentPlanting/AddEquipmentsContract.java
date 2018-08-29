package bocai.com.yanghuajien.presenter.intelligentPlanting;

import java.util.List;
import java.util.Map;

import bocai.com.yanghuajien.base.presenter.BaseContract;
import bocai.com.yanghuajien.model.AutoModel;
import bocai.com.yanghuajien.model.EquipmentCard;
import bocai.com.yanghuajien.model.EquipmentInfoModel;
import bocai.com.yanghuajien.model.EquipmentRspModel;

/**
 *
 * Created by shc on 2017/12/27.
 */

public interface AddEquipmentsContract {

    interface View extends BaseContract.View<Presenter>{

        void addEquipmentsSuccess(List<EquipmentCard> cards);
        void addEquipmentsFailed();
        //fun add 获取设备信息成功
        void equipmentInfoSuccess(EquipmentInfoModel model);
        //fun add 获取智能控制植物数据成功
        void getAutoParaSuccess(List<AutoModel.ParaBean> paraBeans);
        //fun add 获取智能控制植物数据失败
        void getAutoParaFailed();
        //fun add 获取所有的设备列表成功
        void getAllEquipmentsSuccess(List<EquipmentRspModel.ListBean> listBeans);
    }
    interface Presenter extends BaseContract.Presenter{
        //添加新设备到服务器
        void addEquipments(String token,String Equipments);
        //获取设备信息
        void equipmentInfo(Map<String,String> map);
        //获取智能控制植物数据
        void getAutoPara(String equipmentId,String plantId,String lifeCircleId);
        //fun add 获取所有的设备列表
        void getAllEquipments(String token,String limit,String page);
    }

}
