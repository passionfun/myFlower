package bocai.com.yanghuaji.presenter.intelligentPlanting;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.Application;
import bocai.com.yanghuaji.base.presenter.BasePresenter;
import bocai.com.yanghuaji.model.BaseRspModel;
import bocai.com.yanghuaji.model.EquipmentCard;
import bocai.com.yanghuaji.net.Network;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 作者 yuanfei on 2017/12/1.
 * 邮箱 yuanfei221@126.com
 */

public class ConnectPresenter extends BasePresenter<ConnectContract.View> implements ConnectContract.Presenter {
    ConnectContract.View view = getView();
    public ConnectPresenter(ConnectContract.View view) {
        super(view);
    }

    @Override
    public void addEquipment(String token, String equipmentName, String macAddress, String serialNum, String version, String longToothId, String timeStamp,String series) {
        Observable<BaseRspModel<EquipmentCard>> observable = Network.remote().addEquipment(token, equipmentName, macAddress, serialNum, version,longToothId,timeStamp,series);
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
                        }else if (equipmentCardBaseRspModel.getReturnCode().equals("9997")) {
                            view.onConnectionConflict();
                        }else {
                            Application.showToast(equipmentCardBaseRspModel.getMsg());
                        }
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
