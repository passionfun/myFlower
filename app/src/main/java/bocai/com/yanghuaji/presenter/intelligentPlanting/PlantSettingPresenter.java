package bocai.com.yanghuaji.presenter.intelligentPlanting;

import java.util.List;
import java.util.Map;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.Application;
import bocai.com.yanghuaji.base.presenter.BasePresenter;
import bocai.com.yanghuaji.model.AutoModel;
import bocai.com.yanghuaji.model.BaseRspModel;
import bocai.com.yanghuaji.model.LifeCycleModel;
import bocai.com.yanghuaji.model.PlantSettingModel;
import bocai.com.yanghuaji.net.Network;
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
                        }
                        view.hideLoading();
                        Application.showToast(plantSettingModelBaseRspModel.getMsg());
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
    public void getAutoPara(String plantId, String lifeCircleId) {
        Observable<BaseRspModel<List<AutoModel.ParaBean>>> observable = Network.remote().getAutoPara(plantId, lifeCircleId);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseRspModel<List<AutoModel.ParaBean>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseRspModel<List<AutoModel.ParaBean>> listBaseRspModel) {
                        if (listBaseRspModel.getReturnCode().equals("200")) {
                            view.getAutoParaSuccess(listBaseRspModel.getData());
                        }else {
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
