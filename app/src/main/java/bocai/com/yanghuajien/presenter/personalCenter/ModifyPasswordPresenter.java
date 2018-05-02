package bocai.com.yanghuajien.presenter.personalCenter;

import bocai.com.yanghuajien.R;
import bocai.com.yanghuajien.base.Application;
import bocai.com.yanghuajien.base.presenter.BasePresenter;
import bocai.com.yanghuajien.model.BaseRspModel;
import bocai.com.yanghuajien.net.Network;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 作者 yuanfei on 2017/11/20.
 * 邮箱 yuanfei221@126.com
 */

public class ModifyPasswordPresenter extends BasePresenter<ModifyPasswordContract.View>
        implements ModifyPasswordContract.Presenter {
    ModifyPasswordContract.View view = getView();

    public ModifyPasswordPresenter(ModifyPasswordContract.View view) {
        super(view);
    }

    @Override
    public void modifyPassword(String token, String password, String newPassword, String rePassword) {
        view.showLoading();
        Observable<BaseRspModel> observable = Network.remote().fixPassword(token, password, newPassword, rePassword);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseRspModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseRspModel baseRspModel) {
                        Application.showToast(baseRspModel.getMsg());
                        view.hideLoading();
                        if (baseRspModel.getReturnCode().equals("200")){
                            view.modifySuccess();
                        }else if (baseRspModel.getReturnCode().equals("9997")) {
                            view.onConnectionConflict();
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
