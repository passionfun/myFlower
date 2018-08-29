package bocai.com.yanghuajien.presenter.intelligentPlanting;

import com.google.gson.Gson;

import bocai.com.yanghuajien.R;
import bocai.com.yanghuajien.base.Application;
import bocai.com.yanghuajien.base.presenter.BasePresenter;
import bocai.com.yanghuajien.model.BaseRspModel;
import bocai.com.yanghuajien.model.EquipmentRspModel;
import bocai.com.yanghuajien.net.Network;
import bocai.com.yanghuajien.util.LogUtil;
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
    private static final String TAG = "IntelligentPlantPresenter";

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
                        LogUtil.d(TAG,"首页设备列表equipment_list(threadName):"+Thread.currentThread().getName()+",返回的数据："+new Gson().toJson(equipmentRspModelBaseRspModel));
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

}
