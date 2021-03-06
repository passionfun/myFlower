package bocai.com.yanghuajien.presenter.plantingDiary;

import java.util.List;
import java.util.Map;

import bocai.com.yanghuajien.R;
import bocai.com.yanghuajien.base.Application;
import bocai.com.yanghuajien.base.presenter.BasePresenter;
import bocai.com.yanghuajien.model.BaseRspModel;
import bocai.com.yanghuajien.model.EquipmentCard;
import bocai.com.yanghuajien.model.ImageModel;
import bocai.com.yanghuajien.model.db.EquipmentListModel;
import bocai.com.yanghuajien.net.Network;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

/**
 * 作者 yuanfei on 2017/11/23.
 * 邮箱 yuanfei221@126.com
 */

public class AddDiaryPresenter extends BasePresenter<AddDiaryContract.View>
        implements AddDiaryContract.Presenter {
    AddDiaryContract.View view = getView();

    public AddDiaryPresenter(AddDiaryContract.View view) {
        super(view);
    }


    @Override
    public void getEquipmentList(String token) {
        view.showLoading();
        Observable<BaseRspModel<EquipmentListModel>> observable = Network.remote().getEquipmentList(token);
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
                            view.getEquipmentListSuccess(list);
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

    @Override
    public void loadCover(Map<String, RequestBody> params) {
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
                            view.loadCoverSuccess(String.valueOf(id));
                        } else if (imageModelBaseRspModel.getReturnCode().equals("9997")){
                            view.onConnectionConflict();
                        }else {
                            Application.showToast(imageModelBaseRspModel.getMsg());
                        }
                        view.hideLoading();
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

    @Override
    public void addDiary(Map<String, String> map) {
        view.showLoading();
        Observable<BaseRspModel> observable = Network.remote().addDiary(map);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseRspModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseRspModel baseRspModel) {
                        if (baseRspModel.getReturnCode().equals("200")) {
                            view.addDiarySuccess();
                        } else {
                            Application.showToast(baseRspModel.getMsg());
                        }
                        view.hideLoading();
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
