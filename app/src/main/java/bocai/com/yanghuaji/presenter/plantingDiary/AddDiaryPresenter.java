package bocai.com.yanghuaji.presenter.plantingDiary;

import java.util.List;
import java.util.Map;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.Application;
import bocai.com.yanghuaji.base.presenter.BasePresenter;
import bocai.com.yanghuaji.model.BaseRspModel;
import bocai.com.yanghuaji.model.EquipmentCard;
import bocai.com.yanghuaji.model.ImageModel;
import bocai.com.yanghuaji.model.db.EquipmentListModel;
import bocai.com.yanghuaji.net.Network;
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
                        } else {
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
    public void addDiary(String token, String photoId, String diaryName, String equipName, String equipId, String plantTime) {

    }
}
