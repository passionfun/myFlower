package bocai.com.yanghuajien.presenter.plantingDiary;

import bocai.com.yanghuajien.base.presenter.BaseContract;
import bocai.com.yanghuajien.model.DiaryCardModel;

/**
 * 作者 yuanfei on 2017/11/30.
 * 邮箱 yuanfei221@126.com
 */

public interface DiaryListContract {

    interface View extends BaseContract.View<Presenter>{
        void getDiaryDataSuccess(DiaryCardModel diaryCardModel);

        void deleteDiarySuccess();
    }

    interface Presenter extends BaseContract.Presenter{
        void getDiaryData(String diaryId);

        void deleteDiary(String diaryId);
    }
}
