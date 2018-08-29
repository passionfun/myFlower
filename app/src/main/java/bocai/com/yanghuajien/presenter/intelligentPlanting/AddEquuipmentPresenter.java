package bocai.com.yanghuajien.presenter.intelligentPlanting;

import com.google.gson.Gson;

import bocai.com.yanghuajien.R;
import bocai.com.yanghuajien.base.Application;
import bocai.com.yanghuajien.base.presenter.BasePresenter;
import bocai.com.yanghuajien.model.BaseRspModel;
import bocai.com.yanghuajien.model.EquipmentPhotoModel;
import bocai.com.yanghuajien.model.PlantSeriesModel;
import bocai.com.yanghuajien.net.Network;
import bocai.com.yanghuajien.util.LogUtil;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 作者 yuanfei on 2017/11/29.
 * 邮箱 yuanfei221@126.com
 */

public class AddEquuipmentPresenter extends BasePresenter<AddEquipmentContract.View>
        implements AddEquipmentContract.Presenter {
    AddEquipmentContract.View view = getView();
    private static final String TAG = "AddEquuipmentPresenter";

    public AddEquuipmentPresenter(AddEquipmentContract.View view) {
        super(view);
    }

    @Override
    public void getEquipmentSeries(String limit, String page) {
        Observable<BaseRspModel<PlantSeriesModel>> observable = Network.remote().getPlantSeries(limit, page);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseRspModel<PlantSeriesModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseRspModel<PlantSeriesModel> plantSeriesModelBaseRspModel) {
                        LogUtil.d(TAG,"获取设备系列（equipment_series）的返回数据："+ new Gson().toJson(plantSeriesModelBaseRspModel));
                        if (plantSeriesModelBaseRspModel.getReturnCode().equals("200")) {
                            view.getEquipmentSeriesSuccess(plantSeriesModelBaseRspModel.getData().getList());
                        } else {
                            Application.showToast(plantSeriesModelBaseRspModel.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.d(TAG,"设备系列onError："+e.getMessage());
                        view.showError(R.string.net_error);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void getEquipmentPhoto(String type,String equipmentType) {
        view.showLoading();
        Observable<BaseRspModel<EquipmentPhotoModel>> observable = Network.remote().getEquipmentPhoto(type,equipmentType);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseRspModel<EquipmentPhotoModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseRspModel<EquipmentPhotoModel> equipmentPhotoModelBaseRspModel) {
//                        LogUtil.d(TAG,"获取连接设备时页面配置图片（link_banner）："+ new Gson().toJson(equipmentPhotoModelBaseRspModel));
                        view.hideLoading();
                        if (equipmentPhotoModelBaseRspModel.getReturnCode().equals("200")) {
                            view.getEquipmentPhotoSuccess(equipmentPhotoModelBaseRspModel.getData());
                        } else {
                            Application.showToast(equipmentPhotoModelBaseRspModel.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.d(TAG,"获取连接设备时页面配置图片（link_banner）(onError):"+e.getMessage());
                        view.showError(R.string.net_error);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
