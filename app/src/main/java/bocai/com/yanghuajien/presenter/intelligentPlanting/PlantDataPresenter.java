package bocai.com.yanghuajien.presenter.intelligentPlanting;

import bocai.com.yanghuajien.R;
import bocai.com.yanghuajien.base.presenter.BasePresenter;
import bocai.com.yanghuajien.model.BaseRspModel;
import bocai.com.yanghuajien.model.EquipmentDataModel;
import bocai.com.yanghuajien.net.Network;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 *
 * Created by shc on 2017/12/20.
 */

public class PlantDataPresenter extends BasePresenter<PlantingDataContract.View>
        implements PlantingDataContract.Presenter {
    PlantingDataContract.View view = getView();
    public PlantDataPresenter(PlantingDataContract.View view) {
        super(view);
    }

    @Override
    public void setData(String token, String mac, String temperature, String waterLevel, String isLightOn, String Ec) {
        Observable<BaseRspModel<EquipmentDataModel>> observable = Network.remote().setData(token, mac, temperature, waterLevel, isLightOn, Ec);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseRspModel<EquipmentDataModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseRspModel<EquipmentDataModel> equipmentDataModelBaseRspModel) {
                        if (equipmentDataModelBaseRspModel.getReturnCode().equals("200")) {
                            view.setDataSuccess(equipmentDataModelBaseRspModel.getData());
                        }else if (equipmentDataModelBaseRspModel.getReturnCode().equals("9997")) {
                            view.onConnectionConflict();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void setUpdateStatus(String mac, String status) {
        Observable<BaseRspModel> observable = Network.remote().setUpdateStatus(mac, status);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseRspModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseRspModel baseRspModel) {
                        view.setUpdateStatusSuccess();
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
