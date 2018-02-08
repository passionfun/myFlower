package bocai.com.yanghuaji.presenter.main;

import java.util.List;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.Application;
import bocai.com.yanghuaji.base.presenter.BasePresenter;
import bocai.com.yanghuaji.model.BaseRspModel;
import bocai.com.yanghuaji.model.EquipmentCard;
import bocai.com.yanghuaji.model.EquipmentConfigModel;
import bocai.com.yanghuaji.model.EquipmentRspModel;
import bocai.com.yanghuaji.model.GroupRspModel;
import bocai.com.yanghuaji.model.VersionInfoModel;
import bocai.com.yanghuaji.model.db.EquipmentListModel;
import bocai.com.yanghuaji.net.Network;
import bocai.com.yanghuaji.util.persistence.Account;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 作者 yuanfei on 2017/11/25.
 * 邮箱 yuanfei221@126.com
 */

public class MainActivityPresenter extends BasePresenter<MainActivityContract.View>
        implements MainActivityContract.Presenter {
    MainActivityContract.View view = getView();

    public MainActivityPresenter(MainActivityContract.View view) {
        super(view);
    }

//    @Override
//    public void getAllEquipments(String token, String limit, String page) {
//        Observable<BaseRspModel<EquipmentRspModel>> observable = Network.remote().getAllEquipments(token, limit, page);
//        observable.subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<BaseRspModel<EquipmentRspModel>>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(BaseRspModel<EquipmentRspModel> equipmentRspModelBaseRspModel) {
//                        if (equipmentRspModelBaseRspModel.getReturnCode().equals("200")) {
//                            view.getAllEquipmentsSuccess(equipmentRspModelBaseRspModel.getData().getList());
//                        } else if (equipmentRspModelBaseRspModel.getReturnCode().equals("9997")) {
//                            view.onConnectionConflict();
//                        }else {
//                            Application.showToast(equipmentRspModelBaseRspModel.getMsg());
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        view.showError(R.string.net_error);
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
//    }

    @Override
    public void getAllGroups(String token, String limit, String page) {
        Observable<BaseRspModel<GroupRspModel>> observable = Network.remote().getAllGroups(token, limit, page);
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
                        }else if (groupRspModelBaseRspModel.getReturnCode().equals("9997")) {
                            view.onConnectionConflict();
                        } else {
                            Application.showToast(groupRspModelBaseRspModel.getMsg());
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
    public void getEquipmentConfig(String platform) {
        Observable<BaseRspModel<EquipmentConfigModel>> observable = Network.remote().getEquipmentConfig("2");
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseRspModel<EquipmentConfigModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseRspModel<EquipmentConfigModel> equipmentConfigModelBaseRspModel) {
                        if (equipmentConfigModelBaseRspModel.getReturnCode().equals("200")) {
                            EquipmentConfigModel model = equipmentConfigModelBaseRspModel.getData();
                            if (model != null) {
                                Account.saveConfig(model);
                            }
                            view.getEquipmentConfigSuccess(model);
                        } else {
                            view.getEquipmentConfigFailed();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.getEquipmentConfigFailed();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void checkVersion(String platform) {
        Observable<BaseRspModel<VersionInfoModel>> observable = Network.remote().checkVersion("1");
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseRspModel<VersionInfoModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseRspModel<VersionInfoModel> versionInfoModelBaseRspModel) {
                        if (versionInfoModelBaseRspModel.getReturnCode().equals("200")){
                            view.checkVersionSuccess(versionInfoModelBaseRspModel.getData());
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

    @Override
    public void getDefaultEquipments(String token) {
        view.showLoading();
        Observable<BaseRspModel<EquipmentListModel>> observable = Network.remote().getDefaultEquipmentList(token);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseRspModel<EquipmentListModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseRspModel<EquipmentListModel> equipmentListModelBaseRspModel) {
                        if (equipmentListModelBaseRspModel.getReturnCode().equals("200")) {
                            EquipmentListModel model = equipmentListModelBaseRspModel.getData();
                            List<EquipmentCard> list = model.getList();
                            view.getDefaultEquipmentsSuccess(list);
                        }else if (equipmentListModelBaseRspModel.getReturnCode().equals("9997")) {
                            view.onConnectionConflict();
                        }else {
                            Application.showToast(equipmentListModelBaseRspModel.getMsg());
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
