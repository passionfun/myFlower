package bocai.com.yanghuaji.presenter.intelligentPlanting;

import bocai.com.yanghuaji.base.presenter.BasePresenter;

/**
 * 作者 yuanfei on 2017/11/24.
 * 邮箱 yuanfei221@126.com
 */

public class ConnectSuccessPresenter extends BasePresenter<ConnectSuccessContract.View>
        implements ConnectSuccessContract.Presenter {
    ConnectSuccessContract.View view = getView();

    public ConnectSuccessPresenter(ConnectSuccessContract.View view) {
        super(view);
    }

    @Override
    public void addEquipment(String token, String equipmentName, String macAddress, String serialNum, String version) {
//        Observable<BaseRspModel<EquipmentCard>> observable = Network.remote().addEquipment(token, equipmentName, macAddress, serialNum, version);
//        observable.subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<BaseRspModel<EquipmentCard>>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(BaseRspModel<EquipmentCard> equipmentCardBaseRspModel) {
//                        if (equipmentCardBaseRspModel.getReturnCode().equals("200")) {
//                            view.addEquipmentSuccess(equipmentCardBaseRspModel.getData());
//                        }
//                        Application.showToast(equipmentCardBaseRspModel.getMsg());
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Application.showToast(R.string.net_error);
//                        view.addEquipmentFailed();
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });


    }
}
