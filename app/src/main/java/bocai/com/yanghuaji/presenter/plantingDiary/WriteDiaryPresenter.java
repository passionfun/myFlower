package bocai.com.yanghuaji.presenter.plantingDiary;

import java.util.Map;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.Application;
import bocai.com.yanghuaji.base.presenter.BasePresenter;
import bocai.com.yanghuaji.model.BaseRspModel;
import bocai.com.yanghuaji.model.ImageModel;
import bocai.com.yanghuaji.net.Network;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

/**
 * 作者 yuanfei on 2017/11/24.
 * 邮箱 yuanfei221@126.com
 */

public class WriteDiaryPresenter extends BasePresenter<WriteDiaryContract.View>
        implements WriteDiaryContract.Presenter {
    WriteDiaryContract.View view = getView();

    public WriteDiaryPresenter(WriteDiaryContract.View view) {
        super(view);
    }

    @Override
    public void addPhotos(Map<String, RequestBody> params) {
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
                            view.addPhotosSuccess(model.getAvatar());
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
    public void writeDiary(String token, String content, String location, String photosId, String diaryId) {
        view.showLoading();
        Observable<BaseRspModel> observable = Network.remote().writeDiary(token,content,location,photosId,diaryId);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseRspModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseRspModel baseRspModel) {
                        if (baseRspModel.getReturnCode().equals("200")){
                            view.writeDiarySuccess();
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
