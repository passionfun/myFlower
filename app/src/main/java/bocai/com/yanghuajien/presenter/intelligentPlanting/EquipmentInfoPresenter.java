package bocai.com.yanghuajien.presenter.intelligentPlanting;

import com.google.gson.Gson;

import java.util.Map;

import bocai.com.yanghuajien.R;
import bocai.com.yanghuajien.base.Application;
import bocai.com.yanghuajien.base.presenter.BasePresenter;
import bocai.com.yanghuajien.model.BaseRspModel;
import bocai.com.yanghuajien.model.EquipmentInfoModel;
import bocai.com.yanghuajien.model.UpdateVersionRspModel;
import bocai.com.yanghuajien.net.Network;
import bocai.com.yanghuajien.util.LogUtil;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 *
 * Created by apple on 17-11-27.
 */

public class EquipmentInfoPresenter extends BasePresenter<EquipmentInfoContract.View>
        implements EquipmentInfoContract.Presenter {
    private static final String TAG = "EquipmentInfoPresenter";
    EquipmentInfoContract.View view = getView();

    public EquipmentInfoPresenter(EquipmentInfoContract.View view) {
        super(view);
    }

    @Override
    public void updateVersion(String token, String version, String equipmentId) {
        Observable<BaseRspModel<UpdateVersionRspModel>> observable = Network.remote().updateVersion(token, version, equipmentId);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseRspModel<UpdateVersionRspModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseRspModel<UpdateVersionRspModel> updateVersionRspModelBaseRspModel) {
                        LogUtil.d(TAG,"调用edit_version接口的返回数据："+new Gson().toJson(updateVersionRspModelBaseRspModel));
                        if (updateVersionRspModelBaseRspModel.getReturnCode().equals("200")) {
                            view.updateVersionSuccess(updateVersionRspModelBaseRspModel.getData());
                        }else if (updateVersionRspModelBaseRspModel.getReturnCode().equals("9997")) {
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
                        LogUtil.d(TAG,"调用equipment_info接口的返回数据："+new Gson().toJson(equipmentInfoModelBaseRspModel));
                        if (equipmentInfoModelBaseRspModel.getReturnCode().equals("200")) {
                            view.equipmentInfoSuccess(equipmentInfoModelBaseRspModel.getData());
                        } else if (equipmentInfoModelBaseRspModel.getReturnCode().equals("9997")){
                            view.onConnectionConflict();
                        }else {
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
