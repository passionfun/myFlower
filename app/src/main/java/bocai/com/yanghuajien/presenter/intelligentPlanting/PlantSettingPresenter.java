package bocai.com.yanghuajien.presenter.intelligentPlanting;

import com.google.gson.Gson;

import java.util.List;
import java.util.Map;

import bocai.com.yanghuajien.R;
import bocai.com.yanghuajien.base.Application;
import bocai.com.yanghuajien.base.presenter.BasePresenter;
import bocai.com.yanghuajien.model.AutoModel;
import bocai.com.yanghuajien.model.BaseRspModel;
import bocai.com.yanghuajien.model.LifeCycleModel;
import bocai.com.yanghuajien.model.PlantSettingModel;
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

public class PlantSettingPresenter extends BasePresenter<PlantSettingContract.View>
        implements PlantSettingContract.Presenter {
    private static final String TAG = "PlantSettingPresenter";
    PlantSettingContract.View view = getView();

    public PlantSettingPresenter(PlantSettingContract.View view) {
        super(view);
    }

    @Override
    public void setupPlant(Map<String, String> map) {
        view.showLoading();
        Observable<BaseRspModel<PlantSettingModel>> observable = Network.remote().setupPlant(map);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseRspModel<PlantSettingModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseRspModel<PlantSettingModel> plantSettingModelBaseRspModel) {
                        if (plantSettingModelBaseRspModel.getReturnCode().equals("200")) {
                            view.setupPlantSuccess(plantSettingModelBaseRspModel.getData());
                        }else if (plantSettingModelBaseRspModel.getReturnCode().equals("9997")){
                            view.onConnectionConflict();
                        }else {
                            Application.showToast(plantSettingModelBaseRspModel.getMsg());
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
    public void plantMode() {
        view.showLoading();
        Observable<BaseRspModel<LifeCycleModel>> observable = Network.remote().plantMode();
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseRspModel<LifeCycleModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseRspModel<LifeCycleModel> lifeCycleModelBaseRspModel) {
                        if (lifeCycleModelBaseRspModel.getReturnCode().equals("200")) {
                            view.plantModeSuccess(lifeCycleModelBaseRspModel.getData());
                        } else {
                            Application.showToast(lifeCycleModelBaseRspModel.getMsg());
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
    public void lifeCycle() {
        view.showLoading();
        Observable<BaseRspModel<LifeCycleModel>> observable = Network.remote().lifeCycle();
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseRspModel<LifeCycleModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseRspModel<LifeCycleModel> lifeCycleModelBaseRspModel) {
                        if (lifeCycleModelBaseRspModel.getReturnCode().equals("200")) {
                            view.lifeCycleSuccess(lifeCycleModelBaseRspModel.getData());
                        } else {
                            Application.showToast(lifeCycleModelBaseRspModel.getMsg());
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
    public void getAutoPara(String equipmentId,String plantId, String lifeCircleId) {
        view.showLoading();
        Observable<BaseRspModel<List<AutoModel.ParaBean>>> observable = Network.remote().
                getAutoPara(equipmentId,plantId, lifeCircleId);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseRspModel<List<AutoModel.ParaBean>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseRspModel<List<AutoModel.ParaBean>> listBaseRspModel) {
                        LogUtil.d(TAG,"调用plant_info接口返回的数据："+new Gson().toJson(listBaseRspModel));
                        if (listBaseRspModel.getReturnCode().equals("200")) {
                            view.getAutoParaSuccess(listBaseRspModel.getData());
                        }else {
                            view.hideLoading();
                            view.getAutoParaFailed();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.getAutoParaFailed();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


}
