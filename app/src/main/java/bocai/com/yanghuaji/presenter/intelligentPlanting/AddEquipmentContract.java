package bocai.com.yanghuaji.presenter.intelligentPlanting;

import java.util.List;

import bocai.com.yanghuaji.base.presenter.BaseContract;
import bocai.com.yanghuaji.model.EquipmentPhotoModel;
import bocai.com.yanghuaji.model.PlantSeriesModel;

/**
 * 作者 yuanfei on 2017/11/29.
 * 邮箱 yuanfei221@126.com
 */

public interface AddEquipmentContract {

    interface View extends BaseContract.View<Presenter>{
        void getEquipmentSeriesSuccess(List<PlantSeriesModel.PlantSeriesCard> cards);

        void getEquipmentPhotoSuccess(EquipmentPhotoModel photoModel);
    }

    interface Presenter extends BaseContract.Presenter{
        void getEquipmentSeries(String limit,String page);

        // type:1添加设备产品演示区图      2连接设备成功-设备图
        void getEquipmentPhoto(String type,String equipmentType);
    }
}
