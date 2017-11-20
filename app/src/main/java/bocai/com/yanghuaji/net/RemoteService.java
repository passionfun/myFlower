package bocai.com.yanghuaji.net;

import java.util.Map;

import bocai.com.yanghuaji.model.AccountRspModel;
import bocai.com.yanghuaji.model.BaseRspModel;
import bocai.com.yanghuaji.model.DiaryListModel;
import bocai.com.yanghuaji.model.ImageModel;
import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;

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
    Observable<BaseRspModel<AccountRspModel>> register(@Field("Phone") String phone, @Field("SmsCode")String smsCode,
                                                       @Field("Password") String password, @Field("RePassword")String rePassword);


    //密码登录
    @POST("member/pwd_login")
    @FormUrlEncoded
    Observable<BaseRspModel<AccountRspModel>> passwordLogin(@Field("Phone") String phone,
                                                            @Field("Password")String password,@Field("MobileDevice")String pushId);

    //验证码登录
    @POST("member/phone_login")
    @FormUrlEncoded
    Observable<BaseRspModel<AccountRspModel>> smsCodeLogin(@Field("Phone") String phone,
                                                           @Field("SmsCode")String smsCode,@Field("MobileDevice")String pushId);

    //找回密码
    @POST("member/find")
    @FormUrlEncoded
    Observable<BaseRspModel> modifyPassword(@Field("Phone") String phone,@Field("SmsCode")String smsCode,
                                            @Field("Password")String password,@Field("RePassword")String rePassword);

    //修改个人信息
    @POST("member/edit_info")
    @FormUrlEncoded
    Observable<BaseRspModel<AccountRspModel>> modifyData(@Field("Token") String token,@Field("Avatar") String portraitPath,
                                        @Field("NickName")String name,@Field("Sex")int set,@Field("Birthday")String birthday);

    //上传文件
    @POST("uploads/photolist")
    @Multipart
    Observable<BaseRspModel<ImageModel>> modifyPortrait(@PartMap Map<String, RequestBody> params);


    //修改密码
    @POST("member/reset_password")
    @FormUrlEncoded
    Observable<BaseRspModel> fixPassword(@Field("Token") String token,@Field("Password")String originalPas,
                                            @Field("NewPassword")String newPas,@Field("RePassword")String rePassword);


    //获取日记首页列表
    @POST("diary/index")
    @FormUrlEncoded
    Observable<BaseRspModel<DiaryListModel>> getDiaryList(@Field("Token") String token, @Field("Limit")String limit,
                                                          @Field("Page")String page);

}
