package bocai.com.yanghuaji.presenter.intelligentPlanting;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.Application;
import bocai.com.yanghuaji.base.presenter.BasePresenter;
import bocai.com.yanghuaji.model.BaseRspModel;
import bocai.com.yanghuaji.model.EquipmentRspModel;
import bocai.com.yanghuaji.net.Network;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 作者 yuanfei on 2017/11/28.
 * 邮箱 yuanfei221@126.com
 */

public class IntelligentPlantPresenter extends BasePresenter<IntelligentPlantContract.View>
        implements IntelligentPlantContract.Presenter {
    IntelligentPlantContract.View view = getView();

    public IntelligentPlantPresenter(IntelligentPlantContract.View view) {
        super(view);
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
                        if (equipmentRspModelBaseRspModel.getReturnCode().equals("200")) {
                            view.getAllEquipmentsSuccess(equipmentRspModelBaseRspModel.getData().getList());
                        } else {
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

}
