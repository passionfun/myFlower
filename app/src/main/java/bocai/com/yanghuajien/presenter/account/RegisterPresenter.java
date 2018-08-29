package bocai.com.yanghuajien.presenter.account;

import com.google.gson.Gson;

import bocai.com.yanghuajien.R;
import bocai.com.yanghuajien.base.Application;
import bocai.com.yanghuajien.base.presenter.BasePresenter;
import bocai.com.yanghuajien.model.AccountRspModel;
import bocai.com.yanghuajien.model.BaseRspModel;
import bocai.com.yanghuajien.model.GetSmsCodeModel;
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
 * 作者 yuanfei on 2017/11/15.
 * 邮箱 yuanfei221@126.com
 */

public class RegisterPresenter extends BasePresenter<RegisterContract.View>
        implements RegisterContract.Presenter {
    private static final String TAG = "RegisterPresenter";
    final RegisterContract.View view = getView();

    public RegisterPresenter(RegisterContract.View view) {
        super(view);
    }


    @Override
    public void getSmsCode(String email) {
        view.showLoading();
        GetSmsCodeModel model = new GetSmsCodeModel(email, "0");
        //请求类型， 0：注册， 1：找回密码， 2：修改手机， 3：验证码登陆， 4：绑定手机 开发阶段默认为1234
        Observable<BaseRspModel> observable = Network.remote().getSmsCode(email, "0");
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseRspModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseRspModel baseRspModel) {
                        view.hideLoading();
                        LogUtil.d(TAG,"注册时发送验证码的接口返回的数据："+new Gson().toJson(baseRspModel));
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
    public void register(String email, String smsCode, String password, String rePassword) {
        view.showLoading();
        Observable<BaseRspModel<AccountRspModel>> observable = Network.remote().
                register(email, smsCode, password, rePassword);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseRspModel<AccountRspModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseRspModel<AccountRspModel> accountRspModelBaseRspModel) {
                        LogUtil.d(TAG,"新用户注册接口返回的数据："+new Gson().toJson(accountRspModelBaseRspModel));
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
