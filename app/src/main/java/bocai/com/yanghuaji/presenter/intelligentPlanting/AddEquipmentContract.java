package bocai.com.yanghuaji.presenter.intelligentPlanting;

import java.util.List;

import bocai.com.yanghuaji.base.presenter.BaseContract;
import bocai.com.yanghuaji.model.PlantSeriesModel;

/**
 * 作者 yuanfei on 2017/11/29.
 * 邮箱 yuanfei221@126.com
 */

public interface AddEquipmentContract {

    interface View extends BaseContract.View<Presenter>{
        void getEquipmentSeriesSuccess(List<PlantSeriesModel.PlantSeriesCard> cards);
    }

    interface Presenter extends BaseContract.Presenter{
        void getEquipmentSeries(String limit,String page);
    }
}
