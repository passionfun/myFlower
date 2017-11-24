package bocai.com.yanghuaji.presenter.intelligentPlanting;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.Application;
import bocai.com.yanghuaji.base.presenter.BasePresenter;
import bocai.com.yanghuaji.model.BaseRspModel;
import bocai.com.yanghuaji.model.PlantRspModel;
import bocai.com.yanghuaji.net.Network;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 作者 yuanfei on 2017/11/24.
 * 邮箱 yuanfei221@126.com
 */

public class AddPlantPresenter extends BasePresenter<AddPlantContract.View>
        implements AddPlantContract.Presenter {
    AddPlantContract.View view = getView();

    public AddPlantPresenter(AddPlantContract.View view) {
        super(view);
    }

    @Override
    public void search(String keyword, String limit, String page) {
        Observable<BaseRspModel<PlantRspModel>> observable = Network.remote().searchPlant(keyword, limit, page);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseRspModel<PlantRspModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseRspModel<PlantRspModel> plantRspModelBaseRspModel) {
                        if (plantRspModelBaseRspModel.getReturnCode().equals("200")) {
                            view.searchSuccess(plantRspModelBaseRspModel.getData().getList());
                        }else {
                            Application.showToast(plantRspModelBaseRspModel.getMsg());
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
