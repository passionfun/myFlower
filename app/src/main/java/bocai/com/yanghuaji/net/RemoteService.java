package bocai.com.yanghuaji.net;

import bocai.com.yanghuaji.model.AccountRspModel;
import bocai.com.yanghuaji.model.BaseRspModel;
import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * 网络请求接口
 * 作者 yuanfei on 2017/11/15.
 * 邮箱 yuanfei221@126.com
 */

public interface RemoteService {
    //获取验证码接口
    @POST("member/send_sms")
    @FormUrlEncoded
    Observable<BaseRspModel> getSmsCode(@Field("Phone") String phone,@Field("Type")String type);


    //注册接口
    @POST("member/signup")
    @FormUrlEncoded
    Observable<BaseRspModel<AccountRspModel>> register(@Field("Phone") String phone, @Field("SmsCode")String smsCode, @Field("Password") String password, @Field("RePassword")String rePassword);


    //密码登录
    @POST("member/pwd_login")
    @FormUrlEncoded
    Observable<BaseRspModel<AccountRspModel>> passwordLogin(@Field("Phone") String phone,@Field("Password")String password,@Field("MobileDevice")String pushId);

    //验证码登录
    @POST("member/phone_login")
    @FormUrlEncoded
    Observable<BaseRspModel<AccountRspModel>> smsCodeLogin(@Field("Phone") String phone,@Field("SmsCode")String smsCode,@Field("MobileDevice")String pushId);

    //修改密码
    @POST("member/find")
    @FormUrlEncoded
    Observable<BaseRspModel> modifyPassword(@Field("Phone") String phone,@Field("SmsCode")String smsCode,@Field("Password")String password,@Field("RePassword")String rePassword);
}
