package bocai.com.yanghuaji.presenter.intelligentPlanting;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.Application;
import bocai.com.yanghuaji.base.presenter.BasePresenter;
import bocai.com.yanghuaji.model.BaseRspModel;
import bocai.com.yanghuaji.model.EquipmentCard;
import bocai.com.yanghuaji.model.EquipmentPhotoModel;
import bocai.com.yanghuaji.net.Network;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 作者 yuanfei on 2017/12/8.
 * 邮箱 yuanfei221@126.com
 */

public class AddEquipmentsRecylerPresenter extends BasePresenter<AddEquipmentsRecylerContract.View>
        implements AddEquipmentsRecylerContract.Presenter {
    AddEquipmentsRecylerContract.View view = getView();

    public AddEquipmentsRecylerPresenter(AddEquipmentsRecylerContract.View view) {
        super(view);
    }

    @Override
    public void getEquipmentPhoto(String type, String equipmentType) {
        Observable<BaseRspModel<EquipmentPhotoModel>> observable = Network.remote().getEquipmentPhoto(type, equipmentType);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseRspModel<EquipmentPhotoModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseRspModel<EquipmentPhotoModel> equipmentPhotoModelBaseRspModel) {
                        if (equipmentPhotoModelBaseRspModel.getReturnCode().equals("200")) {
                            view.getEquipmentPhotoSuccess(equipmentPhotoModelBaseRspModel.getData());
                        } else {
                            Application.showToast(equipmentPhotoModelBaseRspModel.getMsg());
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

    @Override
    public void addEquipment(String token, String equipmentName, String macAddress, String serialNum, String version, String longToothId, String timeStamp) {
        Observable<BaseRspModel<EquipmentCard>> observable = Network.remote().addEquipment(token, equipmentName, macAddress, serialNum, version,longToothId,timeStamp);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseRspModel<EquipmentCard>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseRspModel<EquipmentCard> equipmentCardBaseRspModel) {
                        if (equipmentCardBaseRspModel.getReturnCode().equals("200")) {
                            view.addEquipmentSuccess(equipmentCardBaseRspModel.getData());
                        }
                        Application.showToast(equipmentCardBaseRspModel.getMsg());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Application.showToast(R.string.net_error);
                        view.addEquipmentFailed();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


}