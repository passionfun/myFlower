package bocai.com.yanghuajien.presenter.intelligentPlanting;

import android.util.Log;

import com.google.gson.Gson;

import bocai.com.yanghuajien.R;
import bocai.com.yanghuajien.base.Application;
import bocai.com.yanghuajien.base.presenter.BasePresenter;
import bocai.com.yanghuajien.model.BaseRspModel;
import bocai.com.yanghuajien.model.CheckboxStatusModel;
import bocai.com.yanghuajien.model.EquipmentDataModel;
import bocai.com.yanghuajien.net.Network;
import bocai.com.yanghuajien.util.LogUtil;
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
    private static final String TAG = "MainRecylerPresenter";
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
                        LogUtil.d(TAG,"调用equipment_data接口返回的数据(设备数据输入)："+new Gson().toJson(equipmentDataModelBaseRspModel));
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
                        LogUtil.d(TAG,"setCheckBox设备状态设置（equipment_status）接口返回的数据："+new Gson().toJson(checkboxStatusModelBaseRspModel));
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
                        LogUtil.d(TAG,"删除设备的返回数据（del_equipment）:"+new Gson().toJson(baseRspModel));
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
