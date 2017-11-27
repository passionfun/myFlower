package bocai.com.yanghuaji.presenter.intelligentPlanting;

import java.util.Map;

import bocai.com.yanghuaji.base.presenter.BaseContract;
import bocai.com.yanghuaji.model.EquipmentInfoModel;
import bocai.com.yanghuaji.model.LifeCycleModel;
import bocai.com.yanghuaji.model.PlantSettingModel;

/**
 * Created by apple on 17-11-27.
 */

public interface EquipmentInfoContract  {
    interface View extends BaseContract.View<EquipmentInfoContract.Presenter>{
        void equipmentInfoSuccess(EquipmentInfoModel model);
    }

    interface Presenter extends BaseContract.Presenter{
        void equipmentInfo(Map<String,String> map);
    }
}
