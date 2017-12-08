package bocai.com.yanghuaji.presenter.intelligentPlanting;

import java.util.Map;

import bocai.com.yanghuaji.base.presenter.BaseContract;
import bocai.com.yanghuaji.model.EquipmentInfoModel;
import bocai.com.yanghuaji.model.UpdateVersionRspModel;

/**
 * Created by apple on 17-11-27.
 */

public interface EquipmentInfoContract  {
    interface View extends BaseContract.View<EquipmentInfoContract.Presenter>{
        void equipmentInfoSuccess(EquipmentInfoModel model);
        void updateVersionSuccess(UpdateVersionRspModel model);
    }

    interface Presenter extends BaseContract.Presenter{

        void updateVersion(String token,String version,String equipmentId);

        void equipmentInfo(Map<String,String> map);
    }
}
