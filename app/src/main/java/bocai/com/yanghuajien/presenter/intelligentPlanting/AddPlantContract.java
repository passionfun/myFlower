package bocai.com.yanghuajien.presenter.intelligentPlanting;

import java.util.List;

import bocai.com.yanghuajien.base.presenter.BaseContract;
import bocai.com.yanghuajien.model.PlantRspModel;

/**
 * 作者 yuanfei on 2017/11/24.
 * 邮箱 yuanfei221@126.com
 */

public interface AddPlantContract {

    interface View extends BaseContract.View<Presenter>{
        void searchSuccess(List<PlantRspModel.PlantCard> cards);

        void searchCommonPlantSuccess(List<PlantRspModel.PlantCard> cards);
    }

    interface Presenter extends BaseContract.Presenter{
        void search(String keyword,String limit,String page);
        void searchCommonPlant();
    }
}
