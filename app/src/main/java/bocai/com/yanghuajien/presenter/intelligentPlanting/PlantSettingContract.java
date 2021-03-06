package bocai.com.yanghuajien.presenter.intelligentPlanting;

import java.util.List;
import java.util.Map;

import bocai.com.yanghuajien.base.presenter.BaseContract;
import bocai.com.yanghuajien.model.AutoModel;
import bocai.com.yanghuajien.model.LifeCycleModel;
import bocai.com.yanghuajien.model.PlantSettingModel;

/**
 *
 * Created by apple on 17-11-27.
 */

public interface PlantSettingContract  {
    interface View extends BaseContract.View<Presenter>{
        void setupPlantSuccess(PlantSettingModel model);
        void plantModeSuccess(LifeCycleModel model);
        void lifeCycleSuccess(LifeCycleModel model);
        void getAutoParaSuccess(List<AutoModel.ParaBean> paraBeans);
        void getAutoParaFailed();

    }

    interface Presenter extends BaseContract.Presenter{
        void setupPlant(Map<String,String> map);
        void plantMode();
        void lifeCycle();
        void getAutoPara(String equipmentId,String plantId,String lifeCircleId);

    }
}
