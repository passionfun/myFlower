package bocai.com.yanghuajien.presenter.account;

import android.util.Log;

import com.google.gson.Gson;

import bocai.com.yanghuajien.R;
import bocai.com.yanghuajien.base.Application;
import bocai.com.yanghuajien.base.presenter.BasePresenter;
import bocai.com.yanghuajien.model.AccountRspModel;
import bocai.com.yanghuajien.model.BaseRspModel;
import bocai.com.yanghuajien.model.db.User;
import bocai.com.yanghuajien.net.Network;
import bocai.com.yanghuajien.util.LogUtil;
import bocai.com.yanghuajien.util.persistence.Account;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 作者 yuanfei on 2017/11/16.
 * 邮箱 yuanfei221@126.com
 */

public class LoginPresenter extends BasePresenter<LoginContract.View> implements LoginContract.Presenter {
    private static final String TAG = "LoginPresenter";
    LoginContract.View view = getView();

    public LoginPresenter(LoginContract.View view) {
        super(view);
    }

    //密码登录
    @Override
    public void login(String phone, String password) {
        view.showLoading();
        Observable<BaseRspModel<AccountRspModel>> observable = Network.remote().passwordLogin(phone, password);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseRspModel<AccountRspModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseRspModel<AccountRspModel> accountRspModelBaseRspModel) {
                        LogUtil.d(TAG,"登录(邮箱)接口返回的数据："+new Gson().toJson(accountRspModelBaseRspModel));
                        if (accountRspModelBaseRspModel.getReturnCode().equals("200")) {
                            AccountRspModel model = accountRspModelBaseRspModel.getData();
                            User user = model.build();
                            user.save();
                            Account.login(model);
                            view.loginSuccess();
                        } else {
                            LogUtil.d(TAG,"CODE != 200:"+accountRspModelBaseRspModel.getMsg());
                            Application.showToast(accountRspModelBaseRspModel.getMsg());
                            view.hideLoading();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.d(TAG,"onError:"+e.getMessage());
                        view.showError(R.string.net_error);
                    }

                    @Override
                    public void onComplete() {
                        view.hideLoading();
                    }
                });

    }

}
