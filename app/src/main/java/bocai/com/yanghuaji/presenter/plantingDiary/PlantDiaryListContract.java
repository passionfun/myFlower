package bocai.com.yanghuaji.presenter.plantingDiary;

import bocai.com.yanghuaji.base.presenter.BaseContract;
import bocai.com.yanghuaji.model.DiaryListModel;

/**
 * 作者 yuanfei on 2017/11/20.
 * 邮箱 yuanfei221@126.com
 */

public interface PlantDiaryListContract {
    interface View extends BaseContract.View<Presenter> {
        void getDiaryListSuccess(DiaryListModel model);
    }

    interface Presenter extends BaseContract.Presenter {
        void getDiaryList(String token,String limit,String page);
    }
}
