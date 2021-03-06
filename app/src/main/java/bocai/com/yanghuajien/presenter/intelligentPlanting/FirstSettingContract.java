package bocai.com.yanghuajien.presenter.intelligentPlanting;

import java.util.List;

import bocai.com.yanghuajien.base.presenter.BaseContract;
import bocai.com.yanghuajien.model.AutoModel;
import bocai.com.yanghuajien.model.LifeCycleModel;

/**
 * 作者 yuanfei on 2017/11/24.
 * 邮箱 yuanfei221@126.com
 */

public interface FirstSettingContract {

    interface View extends BaseContract.View<Presenter>{
        void lifeCycleSuccess(LifeCycleModel model);
        void setupSuccess();
        void getAutoParaSuccess(List<AutoModel.ParaBean> paraBeans);
        void getAutoParaFailed();
    }

    interface Presenter extends BaseContract.Presenter{
        void lifeCycle();
        void setup(String token,String equipmentName,String plantName,String plantId,String equipmentId,
                   String lifeCycle,String lifeCycleId);
        void getAutoPara(String equipmentId,String plantId,String lifeCircleId);
    }
}
