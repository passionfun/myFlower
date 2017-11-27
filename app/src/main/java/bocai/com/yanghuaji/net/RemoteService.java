package bocai.com.yanghuaji.net;

import java.util.Map;

import bocai.com.yanghuaji.model.AccountRspModel;
import bocai.com.yanghuaji.model.BaseRspModel;
import bocai.com.yanghuaji.model.DiaryListModel;
import bocai.com.yanghuaji.model.EquipmentCard;
import bocai.com.yanghuaji.model.EquipmentInfoModel;
import bocai.com.yanghuaji.model.EquipmentRspModel;
import bocai.com.yanghuaji.model.GroupRspModel;
import bocai.com.yanghuaji.model.ImageModel;
import bocai.com.yanghuaji.model.LifeCycleModel;
import bocai.com.yanghuaji.model.PlantRspModel;
import bocai.com.yanghuaji.model.PlantSettingModel;
import bocai.com.yanghuaji.model.db.EquipmentListModel;
import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
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


    //获取设备
    @POST("diary/get_equipment")
    @FormUrlEncoded
    Observable<BaseRspModel<EquipmentListModel>> getEquipmentList(@Field("Token") String token);


    //写日记
    @POST("diary/create_diary")
    @FormUrlEncoded
    Observable<BaseRspModel> writeDiary(@Field("Token") String token,@Field("Content") String content,
                                        @Field("Location") String location,@Field("Photos") String photosId,@Field("Bid") String diaryId);


    //添加设备
    @POST("equipment/create_equipment")
    @FormUrlEncoded
    Observable<BaseRspModel<EquipmentCard>> addEquipment(@Field("Token") String token, @Field("EquipName") String equipmentName,
                                                         @Field("Mac") String macAddress, @Field("SerialNum") String serialNum, @Field("Version") String version);


    //搜索植物
    @POST("plant/plant_search")
    @FormUrlEncoded
    Observable<BaseRspModel<PlantRspModel>> searchPlant(@Field("Keyword") String keyword, @Field("Limit") String limit,
                                                         @Field("Page") String page);


    //第一次设置
    @POST("equipment/first_setup")
    @FormUrlEncoded
    Observable<BaseRspModel> firstSetting(@Field("Token") String token, @Field("EquipName") String equipmentName,
                                                        @Field("PlantName") String plantName,@Field("Pid") String plantId,@Field("Id") String equipmentId);


    //通用植物
    @GET("plant/plant_habit")
    Observable<BaseRspModel<PlantRspModel>> searchCommonPlant();


    //首页设备列表
    @POST("equipment/equipment_list")
    @FormUrlEncoded
    Observable<BaseRspModel<EquipmentRspModel>> getAllEquipments(@Field("Token") String token, @Field("Limit") String limit,
                                                                 @Field("Page") String page);



    //获取所有设备列表
    @POST("group/all_equipment")
    @FormUrlEncoded
    Observable<BaseRspModel<GroupRspModel>> getAllGroups(@Field("Token") String token, @Field("Limit") String limit,
                                                             @Field("Page") String page);



    //分组列表（获取所有分组）
    @POST("group/group_list")
    @FormUrlEncoded
    Observable<BaseRspModel<GroupRspModel>> getAllGroupList(@Field("Token") String token);



    //新增分组
    @POST("group/create_group")
    @FormUrlEncoded
    Observable<BaseRspModel<GroupRspModel.ListBean>> addGroup(@Field("Token") String token,String groupName);

    //设备设置
    @POST("equipment/setup_plant")
    @FormUrlEncoded
    Observable<BaseRspModel<PlantSettingModel>> setupPlant(@FieldMap Map<String,String> map);

    //植物设置-种植模式获取
    @POST("equipment/plant_mode")
    Observable<BaseRspModel<LifeCycleModel>> plantMode();


    //植物设置-获取生长周期
    @POST("equipment/life_cycle")
    Observable<BaseRspModel<LifeCycleModel>> lifeCycle();

    //设备信息
    @POST("equipment/equipment_info")
    @FormUrlEncoded
    Observable<BaseRspModel<EquipmentInfoModel>> equipmentInfo(@FieldMap Map<String,String> map);


}
