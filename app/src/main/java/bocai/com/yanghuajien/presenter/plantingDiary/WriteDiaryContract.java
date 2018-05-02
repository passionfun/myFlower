package bocai.com.yanghuajien.presenter.plantingDiary;

import java.util.List;
import java.util.Map;

import bocai.com.yanghuajien.base.presenter.BaseContract;
import bocai.com.yanghuajien.model.ImageModel;
import okhttp3.RequestBody;

/**
 * 作者 yuanfei on 2017/11/24.
 * 邮箱 yuanfei221@126.com
 */

public interface WriteDiaryContract {

    interface View extends BaseContract.View<Presenter>{
        void addPhotosSuccess(List<ImageModel.AvatarBean> Avatar);

        void writeDiarySuccess();
    }

    interface Presenter extends BaseContract.Presenter{

        void addPhotos(Map<String, RequestBody> params);

        void writeDiary(String token,String content,String location,String photosId,String diaryId);
    }

}
