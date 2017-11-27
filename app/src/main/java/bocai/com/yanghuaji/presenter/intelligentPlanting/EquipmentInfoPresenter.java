package bocai.com.yanghuaji.presenter.intelligentPlanting;

import java.util.Map;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.Application;
import bocai.com.yanghuaji.base.presenter.BasePresenter;
import bocai.com.yanghuaji.model.BaseRspModel;
import bocai.com.yanghuaji.model.EquipmentInfoModel;
import bocai.com.yanghuaji.model.PlantSettingModel;
import bocai.com.yanghuaji.net.Network;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by apple on 17-11-27.
 */

public class EquipmentInfoPresenter extends BasePresenter<EquipmentInfoContract.View>
        implements EquipmentInfoContract.Presenter {
    EquipmentInfoContract.View view = getView();
    public EquipmentInfoPresenter(EquipmentInfoContract.View view) {
        super(view);
    }

    @Override
    public void equipmentInfo(Map<String, String> map) {
        Observable<BaseRspModel<EquipmentInfoModel>> observable = Network.remote().equipmentInfo(map);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseRspModel<EquipmentInfoModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseRspModel<EquipmentInfoModel> equipmentInfoModelBaseRspModel) {
                        if (equipmentInfoModelBaseRspModel.getReturnCode().equals("200")) {
                            view.equipmentInfoSuccess(equipmentInfoModelBaseRspModel.getData());
                        } else {
                            Application.showToast(equipmentInfoModelBaseRspModel.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showError(R.string.net_error);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
