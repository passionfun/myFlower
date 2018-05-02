package bocai.com.yanghuajien.presenter.intelligentPlanting;

import java.util.List;

import bocai.com.yanghuajien.R;
import bocai.com.yanghuajien.base.Application;
import bocai.com.yanghuajien.base.presenter.BasePresenter;
import bocai.com.yanghuajien.model.BaseRspModel;
import bocai.com.yanghuajien.model.EquipmentCard;
import bocai.com.yanghuajien.net.Network;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 *
 * Created by shc on 2017/12/27.
 */

public class AddEquipmentsPresenter extends BasePresenter<AddEquipmentsContract.View>
        implements AddEquipmentsContract.Presenter {

    AddEquipmentsContract.View view = getView();

    public AddEquipmentsPresenter(AddEquipmentsContract.View view) {
        super(view);
    }


    @Override
    public void addEquipments(String token, String equipments) {
        Observable<BaseRspModel<List<EquipmentCard>>> observable = Network.remote().addEquipments(token, equipments);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseRspModel<List<EquipmentCard>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseRspModel<List<EquipmentCard>> listBaseRspModel) {
                        if (listBaseRspModel.getReturnCode().equals("200")) {
                            view.addEquipmentsSuccess(listBaseRspModel.getData());
                        }else if (listBaseRspModel.getReturnCode().equals("9997")) {
                            view.onConnectionConflict();
                        }else {
                            Application.showToast(listBaseRspModel.getMsg());
                        }
                        view.hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showError(R.string.net_error);
                        view.addEquipmentsFailed();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


//    new Observer<BaseRspModel<EquipmentCard>>() {
//        @Override
//        public void onSubscribe(Disposable d) {
//
//        }
//
//        @Override
//        public void onNext(BaseRspModel<EquipmentCard> equipmentCardBaseRspModel) {
//            if (equipmentCardBaseRspModel.getReturnCode().equals("200")) {
//                view.addEquipmentsSuccess(equipmentCardBaseRspModel.getData());
//            }
//            Application.showToast(equipmentCardBaseRspModel.getMsg());
//        }
//
//        @Override
//        public void onError(Throwable e) {
//            Application.showToast(R.string.net_error);
//            view.addEquipmentsFailed();
//        }
//
//        @Override
//        public void onComplete() {
//
//        }
//    }


}
