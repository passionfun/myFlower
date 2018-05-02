package bocai.com.yanghuajien.presenter.intelligentPlanting;

import bocai.com.yanghuajien.base.presenter.BaseContract;
import bocai.com.yanghuajien.model.EquipmentDataModel;

/**
 * Created by shc on 2017/12/20.
 */

public interface PlantingDataContract {


    interface View extends BaseContract.View<Presenter> {
        void setDataSuccess(EquipmentDataModel model);
        void setUpdateStatusSuccess();
    }

    interface Presenter extends BaseContract.Presenter {

        void setData(String token, String mac, String temperature, String waterLevel, String isLightOn, String Ec);

        //升级状态  0否  1是
        void setUpdateStatus(String mac,String status);
    }
}
