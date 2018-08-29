package bocai.com.yanghuajien.presenter.intelligentPlanting;

import com.google.gson.Gson;

import java.util.List;
import java.util.Map;

import bocai.com.yanghuajien.R;
import bocai.com.yanghuajien.base.Application;
import bocai.com.yanghuajien.base.presenter.BasePresenter;
import bocai.com.yanghuajien.model.AutoModel;
import bocai.com.yanghuajien.model.BaseRspModel;
import bocai.com.yanghuajien.model.EquipmentCard;
import bocai.com.yanghuajien.model.EquipmentInfoModel;
import bocai.com.yanghuajien.model.EquipmentRspModel;
import bocai.com.yanghuajien.net.Network;
import bocai.com.yanghuajien.util.LogUtil;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 *
 * Created by shc on 2017/12/27.
 */

public class AddEquipmentsPresenter extends BasePresenter<AddEquipmentsContract.View>
        implements AddEquipmentsContract.Presenter {
    private static final String TAG = "AddEquipmentsPresenter";
    AddEquipmentsContract.View view = getView();

    public AddEquipmentsPresenter(AddEquipmentsContract.View view) {
        super(view);
    }

    @Override
    public void addEquipments(String token, String equipments) {
        Observable<BaseRspModel<List<EquipmentCard>>> observable = Network.remote().addEquipments(token, equipments);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseRspModel<List<EquipmentCard>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseRspModel<List<EquipmentCard>> listBaseRspModel) {
                        LogUtil.d(TAG,"addEquipments onNext(add device)："+new Gson().toJson(listBaseRspModel));
                        if (listBaseRspModel.getReturnCode().equals("200")) {
                            view.addEquipmentsSuccess(listBaseRspModel.getData());
                        }else if (listBaseRspModel.getReturnCode().equals("9997")) {
                            view.onConnectionConflict();
                        }else {
                            Application.showToast(listBaseRspModel.getMsg());
                        }
                        view.hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showError(R.string.net_error);
                        view.addEquipmentsFailed();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void getAllEquipments(String token, String limit, String page) {
        Observable<BaseRspModel<EquipmentRspModel>> observable = Network.remote().getAllEquipments(token, limit, page);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseRspModel<EquipmentRspModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseRspModel<EquipmentRspModel> equipmentRspModelBaseRspModel) {
                        LogUtil.d(TAG,"getAllEquipments equipment_list(add device):"+new Gson().toJson(equipmentRspModelBaseRspModel));
                        if (equipmentRspModelBaseRspModel.getReturnCode().equals("200")) {
                            view.getAllEquipmentsSuccess(equipmentRspModelBaseRspModel.getData().getList());
                        } else if (equipmentRspModelBaseRspModel.getReturnCode().equals("9997")) {
                            view.onConnectionConflict();
                        }else {
                            Application.showToast(equipmentRspModelBaseRspModel.getMsg());
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
                        LogUtil.d(TAG,"调用equipment_info接口的返回数据："+ new Gson().toJson(equipmentInfoModelBaseRspModel));
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
                        LogUtil.d(TAG,"EquipmentInfo onError:"+e.getMessage());
                        view.hideLoading();
                        view.showError(R.string.net_error);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
    @Override
    public void getAutoPara(String equipmentId,String plantId, String lifeCircleId) {
        view.showLoading();
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
                        LogUtil.d(TAG,"调用plant_info接口的返回数据："+new Gson().toJson(listBaseRspModel));
                        if (listBaseRspModel.getReturnCode().equals("200")) {
                            view.getAutoParaSuccess(listBaseRspModel.getData());
                        }else {
                            view.hideLoading();
                            view.getAutoParaFailed();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.hideLoading();
                        view.getAutoParaFailed();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
//    new Observer<BaseRspModel<EquipmentCard>>() {
//        @Override
//        public void onSubscribe(Disposable d) {
//
//        }
//
//        @Override
//        public void onNext(BaseRspModel<EquipmentCard> equipmentCardBaseRspModel) {
//            if (equipmentCardBaseRspModel.getReturnCode().equals("200")) {
//                view.addEquipmentsSuccess(equipmentCardBaseRspModel.getData());
//            }
//            Application.showToast(equipmentCardBaseRspModel.getMsg());
//        }
//
//        @Override
//        public void onError(Throwable e) {
//            Application.showToast(R.string.net_error);
//            view.addEquipmentsFailed();
//        }
//
//        @Override
//        public void onComplete() {
//
//        }
//    }


}
