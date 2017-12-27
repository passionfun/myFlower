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
 * 作者 yuanfei on 2017/12/18.
 * 邮箱 yuanfei221@126.com
 */

public class BindPhonePresenter extends BasePresenter<BindPhoneContract.View>
        implements BindPhoneContract.Presenter {

    BindPhoneContract.View view = getView();

    public BindPhonePresenter(BindPhoneContract.View view) {
        super(view);
    }

    @Override
    public void getSmsCode(String phone) {
        GetSmsCodeModel model = new GetSmsCodeModel(phone, "4");
        //请求类型， 0：注册， 1：找回密码， 2：修改手机， 3：验证码登陆， 4：绑定手机 开发阶段默认为1234
        Observable<BaseRspModel> observable = Network.remote().getSmsCode(phone, "4");
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseRspModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseRspModel baseRspModel) {
                        if (baseRspModel.getReturnCode().equals("200")) {
                            view.getSmsCodeSuccess(baseRspModel.getMsg());
                        } else {
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
    public void bindPhone(String phone, String smsCode, String openId, String photoUrl, String name) {
        view.showLoading();
        Observable<BaseRspModel<AccountRspModel>> observable = Network.remote().bindPhone(phone, smsCode, openId, photoUrl, name);
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
                            User user = model.build();
                            user.save();
                            //做持久化保存
                            Account.login(model);
                            view.bindPhoneSuccess();
                        } else {
                            Application.showToast(accountRspModelBaseRspModel.getMsg());
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
}
