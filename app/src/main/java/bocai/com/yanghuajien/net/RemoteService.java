package bocai.com.yanghuajien.net;

import java.util.List;
import java.util.Map;

import bocai.com.yanghuajien.model.AccountRspModel;
import bocai.com.yanghuajien.model.AutoModel;
import bocai.com.yanghuajien.model.BaseRspModel;
import bocai.com.yanghuajien.model.CheckboxStatusModel;
import bocai.com.yanghuajien.model.DiaryCardModel;
import bocai.com.yanghuajien.model.DiaryDetailModel;
import bocai.com.yanghuajien.model.DiaryListModel;
import bocai.com.yanghuajien.model.EquipmentCard;
import bocai.com.yanghuajien.model.EquipmentConfigModel;
import bocai.com.yanghuajien.model.EquipmentDataModel;
import bocai.com.yanghuajien.model.EquipmentInfoModel;
import bocai.com.yanghuajien.model.EquipmentPhotoModel;
import bocai.com.yanghuajien.model.EquipmentRspModel;
import bocai.com.yanghuajien.model.EquipmentSetupModel;
import bocai.com.yanghuajien.model.EquipmentsByGroupModel;
import bocai.com.yanghuajien.model.GroupRspModel;
import bocai.com.yanghuajien.model.ImageModel;
import bocai.com.yanghuajien.model.LifeCycleModel;
import bocai.com.yanghuajien.model.NoticeStatusRspModel;
import bocai.com.yanghuajien.model.PlantRspModel;
import bocai.com.yanghuajien.model.PlantSeriesModel;
import bocai.com.yanghuajien.model.PlantSettingModel;
import bocai.com.yanghuajien.model.UpdateVersionRspModel;
import bocai.com.yanghuajien.model.VersionInfoModel;
import bocai.com.yanghuajien.model.db.EquipmentListModel;
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
    Observable<BaseRspModel> getSmsCode(@Field("Email") String phone,@Field("Type")String type);


    //注册接口
    @POST("member/signup")
    @FormUrlEncoded
    Observable<BaseRspModel<AccountRspModel>> register(@Field("Email") String email, @Field("SmsCode")String smsCode,
                                                       @Field("Password") String password, @Field("RePassword")String rePassword);


//    //密码登录
//    @POST("member/pwd_login")
//    @FormUrlEncoded
//    Observable<BaseRspModel<AccountRspModel>> passwordLogin(@Field("Phone") String phone,
//                                                            @Field("Password")String password,@Field("MobileDevice")String pushId);

    //密码登录(英文版)
    @POST("member/pwd_login")
    @FormUrlEncoded
    Observable<BaseRspModel<AccountRspModel>> passwordLogin(@Field("Email") String phone,
                                                            @Field("Password")String password);

    //验证码登录
    @POST("member/phone_login")
    @FormUrlEncoded
    Observable<BaseRspModel<AccountRspModel>> smsCodeLogin(@Field("Phone") String phone,
                                                           @Field("SmsCode")String smsCode,@Field("MobileDevice")String pushId);

    //找回密码
    @POST("member/find")
    @FormUrlEncoded
    Observable<BaseRspModel> modifyPassword(@Field("Email") String phone,@Field("SmsCode")String smsCode,
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
                                                          @Field("Page")String page,@Field("Eid")String equipmentId);


    //获取设备
    @POST("diary/get_equipment")
    @FormUrlEncoded
    Observable<BaseRspModel<EquipmentListModel>> getEquipmentList(@Field("Token") String token);



    //获取默认设备列表
    @POST("group/nogroup_equip")
    @FormUrlEncoded
    Observable<BaseRspModel<EquipmentListModel>> getDefaultEquipmentList(@Field("Token") String token);


    //写日记
    @POST("diary/create_diary")
    @FormUrlEncoded
    Observable<BaseRspModel> writeDiary(@Field("Token") String token,@Field("Content") String content,
                                        @Field("Location") String location,@Field("Photos") String photosId,@Field("Bid") String diaryId);


    //添加设备
    @POST("equipment/create_equipment")
    @FormUrlEncoded
    Observable<BaseRspModel<EquipmentCard>> addEquipment(@Field("Token") String token, @Field("EquipName") String equipmentName,
                                                         @Field("Mac") String macAddress, @Field("SerialNum") String serialNum,
                                                         @Field("Version") String version,@Field("LTID") String longToothId,@Field("PSIGN") String timeStamp,@Field("Series") String series);


    //搜索植物
    @POST("plant/plant_search")
    @FormUrlEncoded
    Observable<BaseRspModel<PlantRspModel>> searchPlant(@Field("Keyword") String keyword, @Field("Limit") String limit,
                                                         @Field("Page") String page);


    //第一次设置
    @POST("equipment/first_setup")
    @FormUrlEncoded
    Observable<BaseRspModel> firstSetting(@Field("Token") String token, @Field("EquipName") String equipmentName,
                                                        @Field("PlantName") String plantName,@Field("Pid") String plantId,
                                          @Field("Id") String equipmentId,@Field("LifeCycle") String lifeCycle,
                                          @Field("Lid") String lifeCycleId);


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
    Observable<BaseRspModel<GroupRspModel.ListBean>> addGroup(@Field("Token") String token,@Field("GroupName") String groupName);



    //分组下设备列表
    @POST("group/group_data")
    @FormUrlEncoded
    Observable<BaseRspModel<EquipmentsByGroupModel>> getEquipmentsByGroup(@Field("Token") String token,@Field("Id") String id);



    //编辑分组
    @POST("group/edit_group")
    @FormUrlEncoded
    Observable<BaseRspModel<EquipmentsByGroupModel>> editGroup(@Field("Id") String groupId,@Field("Token") String token,
                                                               @Field("GroupName") String groupName,@Field("Eids") String deleteIds,
                                                               @Field("AddEids") String addIds);


//    //分组列表（获取所有分组）
//    @POST("group/group_list")
//    @FormUrlEncoded
//    Observable<BaseRspModel<GroupRspModel>> getAllGroupList(@Field("Token") String token);

    //植物设置
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

    //删除分组
    @POST("group/del_group")
    @FormUrlEncoded
    Observable<BaseRspModel> deleteGroup(@Field("Id") String groupId);


    //种植机设置
    @POST("equipment/setup_equipment")
    @FormUrlEncoded
    Observable<BaseRspModel<EquipmentSetupModel>> setupEquipment(@FieldMap Map<String,String> map);


    //添加日记本
    @POST("diary/create_diary_book")
    @FormUrlEncoded
    Observable<BaseRspModel> addDiary(@FieldMap Map<String,String> map);


    //获取设备系列
    @POST("equipment/equipment_series")
    @FormUrlEncoded
    Observable<BaseRspModel<PlantSeriesModel>> getPlantSeries( @Field("Limit") String limit,
                                                            @Field("Page") String page);


    //获取日记资料（卡片数据）
    @POST("diary/diary_book_data")
    @FormUrlEncoded
    Observable<BaseRspModel<DiaryCardModel>> getDiaryData(@Field("Id") String diaryId);


    //获取日记详情
    @POST("diary/diary_info")
    @FormUrlEncoded
    Observable<BaseRspModel<DiaryDetailModel>> getDiaryDetail(@Field("Id") String diaryItemId);


    //删除某条日记
    @POST("diary/diary_del")
    @FormUrlEncoded
    Observable<BaseRspModel> deleteDiaryItem(@Field("Id") String diaryItemId);


    //连接设备时页面配置图片
    @POST("equipment/link_banner")
    @FormUrlEncoded
    Observable<BaseRspModel<EquipmentPhotoModel>> getEquipmentPhoto(@Field("Type") String type,@Field("Series") String equipmentType);


    //智能控制植物数据
    @POST("plant/plant_info")
    @FormUrlEncoded
    Observable<BaseRspModel<List<AutoModel.ParaBean>>> getAutoPara(@Field("Eid") String equipmentId,
                                                                   @Field("Id") String plantId,
                                                                   @Field("Lid") String lifeCircleId);


    //设备数据输入
    @POST("equipment/equipment_data")
    @FormUrlEncoded
    Observable<BaseRspModel<EquipmentDataModel>> setData(@Field("Token") String token, @Field("Mac") String mac, @Field("Degree") String temperature,
                                                         @Field("Water") String waterLevel, @Field("Light") String isLightOn, @Field("Ec") String Ec);



    //设备版本升级后修改版本
    @POST("equipment/edit_version")
    @FormUrlEncoded
    Observable<BaseRspModel<UpdateVersionRspModel>> updateVersion(@Field("Token") String token, @Field("Version") String version, @Field("Id") String id);



    //清除设备当前数据
    @POST("equipment/clean_data")
    @FormUrlEncoded
    Observable<BaseRspModel> clearData(@Field("Token") String token, @Field("Id") String equipmentId);



    //设备状态设置
    @POST("equipment/equipment_status")
    @FormUrlEncoded
    Observable<BaseRspModel<CheckboxStatusModel>> setCheckboxStatus(@Field("Token") String token, @Field("Type") String type, @Field("Status") String status, @Field("Id") String equipmentId);



    //多设备添加
    @POST("equipment/create_equipment_more")
    @FormUrlEncoded
    Observable<BaseRspModel<List<EquipmentCard>>> addEquipments(@Field("Token") String token, @Field("Equipments") String equipments);



    //新消息通知状态
    @POST("member/notice_status")
    @FormUrlEncoded
    Observable<BaseRspModel<NoticeStatusRspModel>> getNoticeStatus(@Field("Token") String token);



    //设备升级状态设置
    @POST("equipment/up_status")
    @FormUrlEncoded
    Observable<BaseRspModel> setUpdateStatus(@Field("Mac") String mac,@Field("Status") String status);



    //删除设备
    @POST("equipment/del_equipment")
    @FormUrlEncoded
    Observable<BaseRspModel> deleteEquipment(@Field("Id") String equipmentId);


    //微信登录
    @POST("member/wechat_login")
    @FormUrlEncoded
    Observable<BaseRspModel<AccountRspModel>> weChatLogin(@Field("Avatar") String photoUrl,
                                                            @Field("NickName")String name,@Field("Openid")String openId);


    //微信登录 - 绑定手机
    @POST("member/phone_number")
    @FormUrlEncoded
    Observable<BaseRspModel<AccountRspModel>> bindPhone(@Field("Phone") String phone,@Field("SmsCode") String smsCode,@Field("Openid")String openId,@Field("Avatar") String photoUrl,
                                                          @Field("NickName")String name);



    //删除日记本
    @POST("diary/diarybook_del")
    @FormUrlEncoded
    Observable<BaseRspModel> deleteDiary(@Field("Id") String diaryId);



    //设备配置数据获取
    @POST("equipment/config_data")
    @FormUrlEncoded
    Observable<BaseRspModel<EquipmentConfigModel>> getEquipmentConfig(@Field("AppID") String appId);


    //app版本更新
    @POST("member/version")
    @FormUrlEncoded
    Observable<BaseRspModel<VersionInfoModel>> checkVersion(@Field("Platform") String platform);


}
