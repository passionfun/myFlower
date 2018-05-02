package bocai.com.yanghuajien.presenter.plantingDiary;

import bocai.com.yanghuajien.R;
import bocai.com.yanghuajien.base.Application;
import bocai.com.yanghuajien.base.presenter.BasePresenter;
import bocai.com.yanghuajien.model.BaseRspModel;
import bocai.com.yanghuajien.model.DiaryCardModel;
import bocai.com.yanghuajien.net.Network;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 作者 yuanfei on 2017/11/30.
 * 邮箱 yuanfei221@126.com
 */

public class DiaryListPresenter extends BasePresenter<DiaryListContract.View>
        implements DiaryListContract.Presenter {
    DiaryListContract.View view = getView();

    public DiaryListPresenter(DiaryListContract.View view) {
        super(view);
    }

    @Override
    public void getDiaryData(String diaryId) {
        view.showLoading();
        Observable<BaseRspModel<DiaryCardModel>> observable = Network.remote().getDiaryData(diaryId);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseRspModel<DiaryCardModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseRspModel<DiaryCardModel> diaryCardModelBaseRspModel) {
                        if (diaryCardModelBaseRspModel.getReturnCode().equals("200")) {
                            view.getDiaryDataSuccess(diaryCardModelBaseRspModel.getData());
                        } else {
                            Application.showToast(diaryCardModelBaseRspModel.getMsg());
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
    public void deleteDiary(String diaryId) {
        view.showLoading();
        Observable<BaseRspModel> observable = Network.remote().deleteDiary(diaryId);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseRspModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseRspModel baseRspModel) {
                        if (baseRspModel.getReturnCode().equals("200")) {
                            view.deleteDiarySuccess();
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

                    }
                });
    }
}
