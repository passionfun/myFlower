package bocai.com.yanghuaji.presenter.intelligentPlanting;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.Application;
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

public class EquipmentSettingPresenter extends BasePresenter<EquipmentSettingContract.View>
        implements EquipmentSettingContract.Presenter {
    EquipmentSettingContract.View view = getView();

    public EquipmentSettingPresenter(EquipmentSettingContract.View view) {
        super(view);
    }

    @Override
    public void getGroupList(String token) {
        view.showLoading();
        Observable<BaseRspModel<GroupRspModel>> observable = Network.remote().getAllGroupList(token);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseRspModel<GroupRspModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseRspModel<GroupRspModel> groupRspModelBaseRspModel) {
                        if (groupRspModelBaseRspModel.getReturnCode().equals("200")) {
                            view.getGroupListSuccess(groupRspModelBaseRspModel.getData().getList());
                        } else {
                            Application.showToast(groupRspModelBaseRspModel.getMsg());
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
