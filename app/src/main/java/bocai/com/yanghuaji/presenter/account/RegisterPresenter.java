package bocai.com.yanghuaji.presenter.account;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.Application;
import bocai.com.yanghuaji.base.presenter.BasePresenter;
import bocai.com.yanghuaji.model.AccountRspModel;
import bocai.com.yanghuaji.model.BaseRspModel;
import bocai.com.yanghuaji.model.GetSmsCodeModel;
import bocai.com.yanghuaji.model.db.User;
import bocai.com.yanghuaji.net.Network;
import bocai.com.yanghuaji.util.persistence.Account;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 作者 yuanfei on 2017/11/15.
 * 邮箱 yuanfei221@126.com
 */

public class RegisterPresenter extends BasePresenter<RegisterContract.View>
        implements RegisterContract.Presenter {
    final RegisterContract.View view = getView();

    public RegisterPresenter(RegisterContract.View view) {
        super(view);
    }


    @Override
    public void getSmsCode(String phone) {
        GetSmsCodeModel model = new GetSmsCodeModel(phone, "0");
        //请求类型， 0：注册， 1：找回密码， 2：修改手机， 3：验证码登陆， 4：绑定手机 开发阶段默认为1234
        Observable<BaseRspModel> observable = Network.remote().getSmsCode(phone, "0");
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseRspModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseRspModel baseRspModel) {
                        if (baseRspModel.getReturnCode().equals("200")){
                            view.getVerifiCationcodeSuccess(baseRspModel.getMsg());
                        }else {
                            Application.showToast(baseRspModel.getMsg());
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

    @Override
    public void register(String phone, String smsCode, String password, String rePassword) {
        view.showLoading();
        Observable<BaseRspModel<AccountRspModel>> observable = Network.remote().register(phone, smsCode, password, rePassword);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseRspModel<AccountRspModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseRspModel<AccountRspModel> accountRspModelBaseRspModel) {
                        if (accountRspModelBaseRspModel.getReturnCode().equals("200") ) {
                            AccountRspModel model = accountRspModelBaseRspModel.getData();
                            User user = model.build();
                            user.save();
                            //做持久化保存
                            Account.login(model);
                            view.registerSuccess();
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
}
