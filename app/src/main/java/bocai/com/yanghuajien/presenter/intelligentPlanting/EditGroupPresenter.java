package bocai.com.yanghuajien.presenter.intelligentPlanting;

import bocai.com.yanghuajien.R;
import bocai.com.yanghuajien.base.Application;
import bocai.com.yanghuajien.base.presenter.BasePresenter;
import bocai.com.yanghuajien.model.BaseRspModel;
import bocai.com.yanghuajien.model.EquipmentsByGroupModel;
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

public class EditGroupPresenter extends BasePresenter<EditGroupContract.View>
        implements EditGroupContract.Presenter {
    EditGroupContract.View view = getView();

    public EditGroupPresenter(EditGroupContract.View view) {
        super(view);
    }

    @Override
    public void getEquipmentsByGroup(String token, String id) {
        Observable<BaseRspModel<EquipmentsByGroupModel>> observable = Network.remote().getEquipmentsByGroup(token, id);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseRspModel<EquipmentsByGroupModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseRspModel<EquipmentsByGroupModel> equipmentsByGroupModelBaseRspModel) {
                        if (equipmentsByGroupModelBaseRspModel.getReturnCode().equals("200")) {
                            view.getEquipmentsByGroupSuccess(equipmentsByGroupModelBaseRspModel.getData());
                        } else if (equipmentsByGroupModelBaseRspModel.getReturnCode().equals("9997")) {
                            view.onConnectionConflict();
                        }else {
                            Application.showToast(equipmentsByGroupModelBaseRspModel.getMsg());
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
    public void editGroup(String groupId, String token, String groupName, String deleteIds,String addIds) {
        view.showLoading();
        Observable<BaseRspModel<EquipmentsByGroupModel>> observable = Network.remote().editGroup(groupId, token, groupName, deleteIds,addIds);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseRspModel<EquipmentsByGroupModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseRspModel<EquipmentsByGroupModel> equipmentsByGroupModelBaseRspModel) {
                        if (equipmentsByGroupModelBaseRspModel.getReturnCode().equals("200")) {
                            view.editGroupSuccess(equipmentsByGroupModelBaseRspModel.getData());
                        }else if (equipmentsByGroupModelBaseRspModel.getReturnCode().equals("9997")) {
                            view.onConnectionConflict();
                        }
                        Application.showToast(equipmentsByGroupModelBaseRspModel.getMsg());
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
