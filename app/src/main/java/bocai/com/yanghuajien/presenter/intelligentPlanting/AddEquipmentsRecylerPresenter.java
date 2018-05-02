package bocai.com.yanghuajien.presenter.intelligentPlanting;

import bocai.com.yanghuajien.R;
import bocai.com.yanghuajien.base.Application;
import bocai.com.yanghuajien.base.presenter.BasePresenter;
import bocai.com.yanghuajien.model.BaseRspModel;
import bocai.com.yanghuajien.model.EquipmentPhotoModel;
import bocai.com.yanghuajien.net.Network;
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

}
