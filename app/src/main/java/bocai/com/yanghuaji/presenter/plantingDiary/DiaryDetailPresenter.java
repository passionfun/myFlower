package bocai.com.yanghuaji.presenter.plantingDiary;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.Application;
import bocai.com.yanghuaji.base.presenter.BasePresenter;
import bocai.com.yanghuaji.model.BaseRspModel;
import bocai.com.yanghuaji.model.DiaryDetailModel;
import bocai.com.yanghuaji.net.Network;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 作者 yuanfei on 2017/11/30.
 * 邮箱 yuanfei221@126.com
 */

public class DiaryDetailPresenter extends BasePresenter<DiaryDetailContract.View>
        implements DiaryDetailContract.Presenter {
    DiaryDetailContract.View view = getView();

    public DiaryDetailPresenter(DiaryDetailContract.View view) {
        super(view);
    }

    @Override
    public void getDiaryDetail(String diaryItemId) {
        view.showLoading();
        Observable<BaseRspModel<DiaryDetailModel>> observable = Network.remote().getDiaryDetail(diaryItemId);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseRspModel<DiaryDetailModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseRspModel<DiaryDetailModel> diaryDetailModelBaseRspModel) {
                        if (diaryDetailModelBaseRspModel.getReturnCode().equals("200")) {
                            view.getDiaryDetailSuccess(diaryDetailModelBaseRspModel.getData());
                        } else {
                            Application.showToast(diaryDetailModelBaseRspModel.getMsg());
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
    public void deleteDiaryItem(String diaryItemId) {
        view.showLoading();
        Observable<BaseRspModel> observable = Network.remote().deleteDiaryItem(diaryItemId);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseRspModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseRspModel baseRspModel) {
                        if (baseRspModel.getReturnCode().equals("200")) {
                            view.deleteDiaryItemSuccess();
                        }
                        Application.showToast(baseRspModel.getMsg());
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
