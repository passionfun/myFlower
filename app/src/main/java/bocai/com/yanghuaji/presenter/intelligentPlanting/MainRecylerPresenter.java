package bocai.com.yanghuaji.presenter.intelligentPlanting;

import android.util.Log;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.Application;
import bocai.com.yanghuaji.base.presenter.BasePresenter;
import bocai.com.yanghuaji.model.BaseRspModel;
import bocai.com.yanghuaji.model.CheckboxStatusModel;
import bocai.com.yanghuaji.model.EquipmentDataModel;
import bocai.com.yanghuaji.net.Network;
import bocai.com.yanghuaji.util.UiTool;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 作者 yuanfei on 2017/12/7.
 * 邮箱 yuanfei221@126.com
 */

public class MainRecylerPresenter extends BasePresenter<MainRecylerContract.View>
        implements MainRecylerContract.Presenter {
    MainRecylerContract.View view = getView();

    public MainRecylerPresenter(MainRecylerContract.View view) {
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
                        Log.d("sunhengchao","equipmentDataModelBaseRspModel"+equipmentDataModelBaseRspModel.getReturnCode());
                        if (equipmentDataModelBaseRspModel.getReturnCode().equals("200")) {
                            view.setDataSuccess(equipmentDataModelBaseRspModel.getData());
                        } else if (equipmentDataModelBaseRspModel.getReturnCode().equals("9997")) {
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
    public void setCheckBox(String token, String type, String status, String equipmentId) {
        if (!type.equals("3")){
            view.showLoading();
        }
        Observable<BaseRspModel<CheckboxStatusModel>> observable = Network.remote().setCheckboxStatus(token, type, status, equipmentId);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseRspModel<CheckboxStatusModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseRspModel<CheckboxStatusModel> checkboxStatusModelBaseRspModel) {
                        if (checkboxStatusModelBaseRspModel.getReturnCode().equals("200")) {
//                            view.setCheckBoxSuccess(checkboxStatusModelBaseRspModel.getData());
                        } else if (checkboxStatusModelBaseRspModel.getReturnCode().equals("9997")) {
                            view.onConnectionConflict();
                        } else {
                            Application.showToast(checkboxStatusModelBaseRspModel.getMsg());
                        }
                        view.hideLoading();
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
    public void deleteEquipment(String equipmentId) {
        view.showLoading();
        Observable<BaseRspModel> observable = Network.remote().deleteEquipment(equipmentId);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseRspModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseRspModel baseRspModel) {
                        if (baseRspModel.getReturnCode().equals("200")) {
                            view.deleteEquipmentSuccess();
                        }
                        Application.showToast(baseRspModel.getMsg());
                        view.hideLoading();
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
