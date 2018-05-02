package bocai.com.yanghuajien.presenter.personalCenter;

import java.util.Map;

import bocai.com.yanghuajien.base.presenter.BaseContract;
import okhttp3.RequestBody;

/**
 * 作者 yuanfei on 2017/11/17.
 * 邮箱 yuanfei221@126.com
 */

public interface EditPersonalDataContract {
    interface View extends BaseContract.View<Presenter>{
        void modifyDataSuccess();

        void modifyPortraitSuccess(String portraitId);
    }
    interface Presenter extends BaseContract.Presenter{
        void modifyData(String token,String portraitId,String name,String sex,String birthday);

        void modifyPortrait(Map<String, RequestBody> params);
    }
}
