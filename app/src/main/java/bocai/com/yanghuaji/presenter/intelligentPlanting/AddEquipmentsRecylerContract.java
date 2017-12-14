package bocai.com.yanghuaji.presenter.intelligentPlanting;

import bocai.com.yanghuaji.base.presenter.BaseContract;
import bocai.com.yanghuaji.model.EquipmentCard;
import bocai.com.yanghuaji.model.EquipmentPhotoModel;

/**
 * 作者 yuanfei on 2017/12/8.
 * 邮箱 yuanfei221@126.com
 */

public interface AddEquipmentsRecylerContract {

    interface View extends BaseContract.View<Presenter>{

        void getEquipmentPhotoSuccess(EquipmentPhotoModel photoModel);

        void addEquipmentSuccess(EquipmentCard card);
        void addEquipmentFailed();
    }
    interface Presenter extends BaseContract.Presenter{

        // type:1添加设备产品演示区图      2连接设备成功-设备图
        void getEquipmentPhoto(String type,String equipmentType);

        void addEquipment(String token,String equipmentName,String macAddress,String serialNum,String version,String longToothId,String timeStamp,String series);
    }
}
