package bocai.com.yanghuajien.presenter.plantingDiary;

import bocai.com.yanghuajien.base.presenter.BaseContract;
import bocai.com.yanghuajien.model.DiaryDetailModel;

/**
 * 作者 yuanfei on 2017/11/30.
 * 邮箱 yuanfei221@126.com
 */

public interface DiaryDetailContract {

    interface View extends BaseContract.View<Presenter>{
        void getDiaryDetailSuccess(DiaryDetailModel diaryDetailModel);

        void deleteDiaryItemSuccess();
    }

    interface Presenter extends BaseContract.Presenter{
        void getDiaryDetail(String diaryItemId);

        void deleteDiaryItem(String diaryItemId);
    }

}
