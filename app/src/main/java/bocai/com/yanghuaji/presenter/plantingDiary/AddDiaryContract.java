package bocai.com.yanghuaji.presenter.plantingDiary;

import java.util.List;
import java.util.Map;

import bocai.com.yanghuaji.base.presenter.BaseContract;
import bocai.com.yanghuaji.model.EquipmentCard;
import okhttp3.RequestBody;

/**
 * 作者 yuanfei on 2017/11/23.
 * 邮箱 yuanfei221@126.com
 */

public interface AddDiaryContract {

    interface View extends BaseContract.View<Presenter> {
        void addDiarySuccess();

        void loadCoverSuccess(String portraitId);

        void getEquipmentListSuccess(List<EquipmentCard> cards);
    }

    interface Presenter extends BaseContract.Presenter {
        void getEquipmentList(String token);

        void loadCover(Map<String, RequestBody> params);

        void addDiary(String token,String photoId,String diaryName,String equipName,String equipId,String plantTime);
    }

}
