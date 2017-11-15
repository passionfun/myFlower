package bocai.com.yanghuaji.net;

import bocai.com.yanghuaji.model.RegisterModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * 网络请求接口
 * 作者 yuanfei on 2017/11/15.
 * 邮箱 yuanfei221@126.com
 */

public interface RemoteService {
    //注册接口
    @POST("member/signup")
    Call<> register(@Body RegisterModel model);
}
