package bocai.com.yanghuaji.presenter.intelligentPlanting;

import bocai.com.yanghuaji.base.presenter.BaseContract;
import bocai.com.yanghuaji.model.EquipmentCard;

/**
 * 作者 yuanfei on 2017/12/1.
 * 邮箱 yuanfei221@126.com
 */

public interface ConnectContract {
    interface View extends BaseContract.View<Presenter>{
        void addEquipmentSuccess(EquipmentCard card);
        void addEquipmentFailed();
    }

    interface Presenter extends BaseContract.Presenter {
        void addEquipment(String token,String equipmentName,String macAddress,String serialNum,String version,String longToothId,String timeStamp);
    }
}
