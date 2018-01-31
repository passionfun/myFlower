package bocai.com.yanghuaji.presenter.personalCenter;

import java.util.Map;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.Application;
import bocai.com.yanghuaji.base.presenter.BasePresenter;
import bocai.com.yanghuaji.model.AccountRspModel;
import bocai.com.yanghuaji.model.BaseRspModel;
import bocai.com.yanghuaji.model.ImageModel;
import bocai.com.yanghuaji.model.db.User;
import bocai.com.yanghuaji.net.Network;
import bocai.com.yanghuaji.util.persistence.Account;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

/**
 * 作者 yuanfei on 2017/11/17.
 * 邮箱 yuanfei221@126.com
 */

public class EditPersonalDataPresenter extends BasePresenter<EditPersonalDataContract.View>
        implements EditPersonalDataContract.Presenter {
    EditPersonalDataContract.View view = getView();

    public EditPersonalDataPresenter(EditPersonalDataContract.View view) {
        super(view);
    }


    @Override
    public void modifyData(String token, String portraitId, String name, String sex, String birthday) {
        view.showLoading();
        int sexPara;
        switch (sex) {
            case "保密":
                sexPara = 0;
                break;
            case "男":
                sexPara = 1;
                break;
            default:
                sexPara = 2;
                break;
        }
        Observable<BaseRspModel<AccountRspModel>> observable = Network.remote().modifyData(token, portraitId, name, sexPara, birthday);
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
                            User user = model.build();
                            user.save();
                            view.modifyDataSuccess();
                        }else if (accountRspModelBaseRspModel.getReturnCode().equals("9997")) {
                            view.onConnectionConflict();
                        }else {
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

    @Override
    public void modifyPortrait(Map<String, RequestBody> params) {
        view.showLoading();
        Observable<BaseRspModel<ImageModel>> observable = Network.remote().modifyPortrait(params);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseRspModel<ImageModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseRspModel<ImageModel> imageModelBaseRspModel) {
                        if (imageModelBaseRspModel.getReturnCode().equals("200")) {
                            ImageModel model = imageModelBaseRspModel.getData();
                            int id = model.getAvatar().get(0).getId();
                            view.modifyPortraitSuccess(String.valueOf(id));
                        } else if (imageModelBaseRspModel.getReturnCode().equals("9997")){
                            view.onConnectionConflict();
                        }else {
                            Application.showToast(imageModelBaseRspModel.getMsg());
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
