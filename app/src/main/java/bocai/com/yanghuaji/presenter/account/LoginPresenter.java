package bocai.com.yanghuaji.presenter.account;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.Application;
import bocai.com.yanghuaji.base.presenter.BasePresenter;
import bocai.com.yanghuaji.model.AccountRspModel;
import bocai.com.yanghuaji.model.BaseRspModel;
import bocai.com.yanghuaji.net.Network;
import bocai.com.yanghuaji.util.persistence.Account;
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
    LoginContract.View view = getView();

    public LoginPresenter(LoginContract.View view) {
        super(view);
    }

    //密码登录
    @Override
    public void passwordLogin(String phone, String password) {
        view.showLoading();
        Observable<BaseRspModel<AccountRspModel>> observable = Network.remote().passwordLogin(phone, password, Account.getPushId());
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseRspModel<AccountRspModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseRspModel<AccountRspModel> accountRspModelBaseRspModel) {
                        if (accountRspModelBaseRspModel.getReturnCode().equals("200")) {
                            AccountRspModel model = accountRspModelBaseRspModel.getData();
                            Account.login(model);
                            view.loginSuccess();
                        } else {
                            Application.showToast(accountRspModelBaseRspModel.getMsg());
                            view.hideLoading();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showError(R.string.net_error);
                    }

                    @Override
                    public void onComplete() {
                        view.hideLoading();
                    }
                });

    }

    @Override
    public void smsCodeLogin(String phone, String smsCode) {
        view.showLoading();
        Observable<BaseRspModel<AccountRspModel>> observable = Network.remote().smsCodeLogin(phone, smsCode, Account.getPushId());
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseRspModel<AccountRspModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseRspModel<AccountRspModel> accountRspModelBaseRspModel) {
                        if (accountRspModelBaseRspModel.getReturnCode().equals("200")) {
                            AccountRspModel model = accountRspModelBaseRspModel.getData();
                            Account.login(model);
                            view.loginSuccess();
                        } else {
                            Application.showToast(accountRspModelBaseRspModel.getMsg());
                            view.hideLoading();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showError(R.string.net_error);
                    }

                    @Override
                    public void onComplete() {
                        view.hideLoading();
                    }
                });
    }

    @Override
    public void getMsmCode(String phone) {
            //请求类型， 0：注册， 1：找回密码， 2：修改手机， 3：验证码登陆， 4：绑定手机 开发阶段默认为1234
            Observable<BaseRspModel> observable = Network.remote().getSmsCode(phone, "3");
            observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<BaseRspModel>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(BaseRspModel baseRspModel) {
                            Application.showToast(baseRspModel.getMsg());
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
    public void weChatLogin() {

    }
}
