package bocai.com.yanghuajien.presenter.intelligentPlanting;

import java.util.List;
import java.util.Map;

import bocai.com.yanghuajien.R;
import bocai.com.yanghuajien.base.Application;
import bocai.com.yanghuajien.base.presenter.BasePresenter;
import bocai.com.yanghuajien.model.AutoModel;
import bocai.com.yanghuajien.model.BaseRspModel;
import bocai.com.yanghuajien.model.EquipmentInfoModel;
import bocai.com.yanghuajien.model.EquipmentSetupModel;
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
    public void setupEquipment(Map<String, String> map) {
        view.showLoading();
        Observable<BaseRspModel<EquipmentSetupModel>> observable = Network.remote().setupEquipment(map);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseRspModel<EquipmentSetupModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseRspModel<EquipmentSetupModel> equipmentSetupModelBaseRspModel) {
                        if (equipmentSetupModelBaseRspModel.getReturnCode().equals("200")) {
                            view.setupEquipmentSuccess(equipmentSetupModelBaseRspModel.getData());
                        }else if (equipmentSetupModelBaseRspModel.getReturnCode().equals("9997")){
                            view.hideLoading();
                            view.onConnectionConflict();
                        }else {
                            view.hideLoading();
                            Application.showToast(equipmentSetupModelBaseRspModel.getMsg());
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
    public void equipmentInfo(Map<String, String> map) {
        Observable<BaseRspModel<EquipmentInfoModel>> observable = Network.remote().equipmentInfo(map);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseRspModel<EquipmentInfoModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseRspModel<EquipmentInfoModel> equipmentInfoModelBaseRspModel) {
                        if (equipmentInfoModelBaseRspModel.getReturnCode().equals("200")) {
                            view.equipmentInfoSuccess(equipmentInfoModelBaseRspModel.getData());
                        } else if (equipmentInfoModelBaseRspModel.getReturnCode().equals("9997")){
                            view.onConnectionConflict();
                        }else {
                            Application.showToast(equipmentInfoModelBaseRspModel.getMsg());
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
    public void getAutoPara(String equipmentId,String plantId, String lifeCircleId) {
//        view.showLoading();
        Observable<BaseRspModel<List<AutoModel.ParaBean>>> observable = Network.remote().
                getAutoPara(equipmentId,plantId, lifeCircleId);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseRspModel<List<AutoModel.ParaBean>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseRspModel<List<AutoModel.ParaBean>> listBaseRspModel) {
                        if (listBaseRspModel.getReturnCode().equals("200")) {
                            view.getAutoParaSuccess(listBaseRspModel.getData());
                        }else {
                            view.hideLoading();
                            view.getAutoParaFailed();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.getAutoParaFailed();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}
