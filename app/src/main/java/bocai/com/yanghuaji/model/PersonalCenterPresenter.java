package bocai.com.yanghuaji.model;

import bocai.com.yanghuaji.base.Application;
import bocai.com.yanghuaji.base.presenter.BasePresenter;
import bocai.com.yanghuaji.net.Network;
import bocai.com.yanghuaji.presenter.personalCenter.PersonalCenterContract;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 作者 yuanfei on 2017/12/12.
 * 邮箱 yuanfei221@126.com
 */

public class PersonalCenterPresenter extends BasePresenter<PersonalCenterContract.View>
        implements PersonalCenterContract.Presenter {
    PersonalCenterContract.View view = getView();

    public PersonalCenterPresenter(PersonalCenterContract.View view) {
        super(view);
    }

    @Override
    public void getNoticeStatus(String token) {
        Observable<BaseRspModel<NoticeStatusRspModel>> observable = Network.remote().getNoticeStatus(token);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseRspModel<NoticeStatusRspModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseRspModel<NoticeStatusRspModel> noticeStatusRspModelBaseRspModel) {
                        if (noticeStatusRspModelBaseRspModel.getReturnCode().equals("200")) {
                            view.getNoticeStatusSuccess(noticeStatusRspModelBaseRspModel.getData());
                        }else {
                            Application.showToast(noticeStatusRspModelBaseRspModel.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
