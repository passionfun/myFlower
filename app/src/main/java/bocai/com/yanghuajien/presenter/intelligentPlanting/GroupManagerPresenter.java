package bocai.com.yanghuajien.presenter.intelligentPlanting;

import bocai.com.yanghuajien.R;
import bocai.com.yanghuajien.base.Application;
import bocai.com.yanghuajien.base.presenter.BasePresenter;
import bocai.com.yanghuajien.model.BaseRspModel;
import bocai.com.yanghuajien.model.GroupRspModel;
import bocai.com.yanghuajien.net.Network;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 作者 yuanfei on 2017/11/27.
 * 邮箱 yuanfei221@126.com
 */

public class GroupManagerPresenter extends BasePresenter<GroupManagerContract.View>
        implements GroupManagerContract.Presenter {
    GroupManagerContract.View view = getView();

    public GroupManagerPresenter(GroupManagerContract.View view) {
        super(view);
    }

    @Override
    public void getAllGroups(String token) {
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
                            view.getAllGroupsSuccess(groupRspModelBaseRspModel.getData().getList());
                        } else if (groupRspModelBaseRspModel.getReturnCode().equals("9997")) {
                            view.onConnectionConflict();
                        }else {
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

    @Override
    public void deleteGroup(String groupId) {
        Observable<BaseRspModel> observable = Network.remote().deleteGroup(groupId);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseRspModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseRspModel baseRspModel) {
                        if (baseRspModel.getReturnCode().equals("200")) {
                            view.deleteGroupSuccess();
                        }
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
                        view.hideLoading();
                        if (listBeanBaseRspModel.getReturnCode().equals("200")) {
                            view.addGroupSuccess(listBeanBaseRspModel.getData());
                        }else if (listBeanBaseRspModel.getReturnCode().equals("9997")) {
                            view.onConnectionConflict();
                        }
//                        Application.showToast(listBeanBaseRspModel.getMsg());
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
