package bocai.com.yanghuaji.presenter.intelligentPlanting;

import bocai.com.yanghuaji.base.presenter.BaseContract;

/**
 * 作者 yuanfei on 2017/11/24.
 * 邮箱 yuanfei221@126.com
 */

public interface FirstSettingContract {

    interface View extends BaseContract.View<Presenter>{
        void setupSuccess();
    }

    interface Presenter extends BaseContract.Presenter{
        void setup(String token,String equipmentName,String plantName,String plantId,String equipmentId);
    }
}
