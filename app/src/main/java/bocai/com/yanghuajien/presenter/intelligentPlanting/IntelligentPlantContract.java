package bocai.com.yanghuajien.presenter.intelligentPlanting;

import java.util.List;

import bocai.com.yanghuajien.base.presenter.BaseContract;
import bocai.com.yanghuajien.model.EquipmentRspModel;

/**
 * 作者 yuanfei on 2017/11/28.
 * 邮箱 yuanfei221@126.com
 */

public interface IntelligentPlantContract {

    interface View extends BaseContract.View<Presenter>{
        void getAllEquipmentsSuccess(List<EquipmentRspModel.ListBean> listBeans);


    }


    interface Presenter extends BaseContract.Presenter{

        void getAllEquipments(String token,String limit,String page);


    }
}
