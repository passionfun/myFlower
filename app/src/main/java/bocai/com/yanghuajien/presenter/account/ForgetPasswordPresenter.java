package bocai.com.yanghuajien.presenter.account;

import com.google.gson.Gson;

import bocai.com.yanghuajien.R;
import bocai.com.yanghuajien.base.Application;
import bocai.com.yanghuajien.base.presenter.BasePresenter;
import bocai.com.yanghuajien.model.BaseRspModel;
import bocai.com.yanghuajien.net.Network;
import bocai.com.yanghuajien.util.LogUtil;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 作者 yuanfei on 2017/11/16.
 * 邮箱 yuanfei221@126.com
 */

public class ForgetPasswordPresenter extends BasePresenter<ForgetPasswordContract.View>
        implements ForgetPasswordContract.Presenter {
    private static final String TAG = "ForgetPasswordPresenter";
    ForgetPasswordContract.View view = getView();

    public ForgetPasswordPresenter(ForgetPasswordContract.View view) {
        super(view);
    }

    @Override
    public void modifyPassword(String email, String smsCode, String newPassword, String rePassword) {
        view.showLoading();
        Observable<BaseRspModel> observable = Network.remote().modifyPassword(email, smsCode, newPassword, rePassword);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseRspModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseRspModel baseRspModel) {
                        LogUtil.d(TAG,"忘记密码修改密码的接口返回的数据："+new Gson().toJson(baseRspModel));
                        if (baseRspModel.getReturnCode().equals("200")){
                            view.modifyPasswordSuccess();
                        }
                        Application.showToast(baseRspModel.getMsg());
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
    public void getMsmCode(String phone) {
        view.showLoading();
        //请求类型， 0：注册， 1：找回密码， 2：修改手机， 3：验证码登陆， 4：绑定手机 开发阶段默认为1234
        Observable<BaseRspModel> observable = Network.remote().getSmsCode(phone, "1");
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseRspModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseRspModel baseRspModel) {
                        LogUtil.d(TAG,"忘记密码发送邮箱验证码接口的返回数据："+new Gson().toJson(baseRspModel));
                        view.hideLoading();
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
}
