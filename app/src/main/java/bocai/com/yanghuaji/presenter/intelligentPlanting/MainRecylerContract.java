package bocai.com.yanghuaji.presenter.intelligentPlanting;

import bocai.com.yanghuaji.base.presenter.BaseContract;
import bocai.com.yanghuaji.model.CheckboxStatusModel;
import bocai.com.yanghuaji.model.EquipmentDataModel;

/**
 * 作者 yuanfei on 2017/12/7.
 * 邮箱 yuanfei221@126.com
 */

public interface MainRecylerContract {

    interface View extends BaseContract.View<Presenter>{
        void setDataSuccess(EquipmentDataModel model);

        void setCheckBoxSuccess(CheckboxStatusModel model);

        void deleteEquipmentSuccess();
    }


    interface Presenter extends BaseContract.Presenter{
        void setData(String token,String mac,String temperature,String waterLevel,String isLightOn,String Ec);

        //type:类型区分   1光照状态   2消息推送状态
        void setCheckBox(String token,String type,String status,String equipmentId);


        void deleteEquipment(String equipmentId);

    }


}
