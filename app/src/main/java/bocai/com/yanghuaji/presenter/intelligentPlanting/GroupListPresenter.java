package bocai.com.yanghuaji.presenter.intelligentPlanting;

import bocai.com.yanghuaji.base.presenter.BasePresenter;
import bocai.com.yanghuaji.model.BaseRspModel;
import bocai.com.yanghuaji.model.GroupRspModel;
import bocai.com.yanghuaji.net.Network;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 作者 yuanfei on 2017/11/27.
 * 邮箱 yuanfei221@126.com
 */

public class GroupListPresenter extends BasePresenter<GroupListContract.View>
        implements GroupListContract.Presenter {
    GroupListContract.View view = getView();

    public GroupListPresenter(GroupListContract.View view) {
        super(view);
    }

    @Override
    public void addGroup(String token, String groupName) {
        view.showLoading();
        Observable<BaseRspModel<GroupRspModel.ListBean>> observable = Network.remote().addGroup(token, groupName);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseRspModel<GroupRspModel.ListBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseRspModel<GroupRspModel.ListBean> listBeanBaseRspModel) {
                        if (listBeanBaseRspModel.getReturnCode().equals("200")){

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
