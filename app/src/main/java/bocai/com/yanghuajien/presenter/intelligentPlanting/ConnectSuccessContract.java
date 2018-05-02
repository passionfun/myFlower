package bocai.com.yanghuajien.presenter.intelligentPlanting;

import bocai.com.yanghuajien.base.presenter.BaseContract;
import bocai.com.yanghuajien.model.EquipmentPhotoModel;

/**
 * 作者 yuanfei on 2017/12/8.
 * 邮箱 yuanfei221@126.com
 */

public interface ConnectSuccessContract {

    interface View extends BaseContract.View<Presenter>{

        void getEquipmentPhotoSuccess(EquipmentPhotoModel photoModel);
    }

    interface Presenter extends BaseContract.Presenter{

        // type:1添加设备产品演示区图      2连接设备成功-设备图
        void getEquipmentPhoto(String type,String equipmentType);
    }
}
