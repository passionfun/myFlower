package bocai.com.yanghuaji.presenter.intelligentPlanting;

import java.util.List;

import bocai.com.yanghuaji.base.presenter.BaseContract;
import bocai.com.yanghuaji.model.EquipmentCard;

/**
 *
 * Created by shc on 2017/12/27.
 */

public interface AddEquipmentsContract {

    interface View extends BaseContract.View<Presenter>{

        void addEquipmentsSuccess(List<EquipmentCard> cards);
        void addEquipmentsFailed();
    }
    interface Presenter extends BaseContract.Presenter{

        void addEquipments(String token,String Equipments);
    }

}
