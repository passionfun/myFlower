package bocai.com.yanghuajien.presenter.plantingDiary;

import bocai.com.yanghuajien.R;
import bocai.com.yanghuajien.base.Application;
import bocai.com.yanghuajien.base.presenter.BasePresenter;
import bocai.com.yanghuajien.model.BaseRspModel;
import bocai.com.yanghuajien.model.DiaryListModel;
import bocai.com.yanghuajien.net.Network;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 作者 yuanfei on 2017/11/20.
 * 邮箱 yuanfei221@126.com
 */

public class PlantDiaryListPresenter extends BasePresenter<PlantDiaryListContract.View>
        implements PlantDiaryListContract.Presenter {
    PlantDiaryListContract.View view = getView();

    public PlantDiaryListPresenter(PlantDiaryListContract.View view) {
        super(view);
    }

    @Override
    public void getDiaryList(String token, String limit, String page,String equipmentId) {
        Observable<BaseRspModel<DiaryListModel>> observable = Network.remote().getDiaryList(token, limit, page,equipmentId);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseRspModel<DiaryListModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseRspModel<DiaryListModel> diaryListModelBaseRspModel) {
                        if (diaryListModelBaseRspModel.getReturnCode().equals("200")) {
                            view.getDiaryListSuccess(diaryListModelBaseRspModel.getData());
                        }else if (diaryListModelBaseRspModel.getReturnCode().equals("9997")) {
                            view.onConnectionConflict();
                        }else {
                            Application.showToast(diaryListModelBaseRspModel.getMsg());
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
