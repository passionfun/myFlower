package bocai.com.yanghuajien.presenter.intelligentPlanting;

import bocai.com.yanghuajien.base.presenter.BaseContract;
import bocai.com.yanghuajien.model.CheckboxStatusModel;

/**
 * 作者 yuanfei on 2017/12/7.
 * 邮箱 yuanfei221@126.com
 */

public interface SecondSettingContract {

    interface View extends BaseContract.View<Presenter>{
       void clearDataSuccess();

       void setCheckBoxSuccess(CheckboxStatusModel model);
    }



    interface Presenter extends BaseContract.Presenter{
        void clearData(String token,String equipmentId);

        //type:类型区分   1光照状态   2消息推送状态
        void setCheckBox(String token,String type,String status,String equipmentId);

    }

}
