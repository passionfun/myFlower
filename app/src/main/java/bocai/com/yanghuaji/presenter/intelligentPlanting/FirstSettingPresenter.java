package bocai.com.yanghuaji.presenter.intelligentPlanting;

import java.util.List;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.Application;
import bocai.com.yanghuaji.base.presenter.BasePresenter;
import bocai.com.yanghuaji.model.AutoModel;
import bocai.com.yanghuaji.model.BaseRspModel;
import bocai.com.yanghuaji.model.LifeCycleModel;
import bocai.com.yanghuaji.net.Network;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 作者 yuanfei on 2017/11/24.
 * 邮箱 yuanfei221@126.com
 */

public class FirstSettingPresenter extends BasePresenter<FirstSettingContract.View>
        implements FirstSettingContract.Presenter {

    FirstSettingContract.View view = getView();

    public FirstSettingPresenter(FirstSettingContract.View view) {
        super(view);
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
    public void setup(String token, String equipmentName, String plantName, String plantId, String equipmentId,
                      String lifeCycle,String lifeCycleId) {
        view.showLoading();
        Observable<BaseRspModel> observable = Network.remote().firstSetting(token, equipmentName, plantName,
                plantId, equipmentId,lifeCycle,lifeCycleId);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseRspModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseRspModel baseRspModel) {
                        if (baseRspModel.getReturnCode().equals("200")) {
                            view.setupSuccess();
                        } else if (baseRspModel.getReturnCode().equals("9997")) {
                            view.onConnectionConflict();
                        } else {
                            Application.showToast(baseRspModel.getMsg());
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
    public void getAutoPara(String equipmentId, String plantId, String lifeCircleId) {
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
