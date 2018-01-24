package bocai.com.yanghuaji.util.persistence;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

import bocai.com.yanghuaji.base.Application;
import bocai.com.yanghuaji.model.AccountRspModel;
import bocai.com.yanghuaji.model.EquipmentConfigModel;
import bocai.com.yanghuaji.model.EquipmentRspModel;
import bocai.com.yanghuaji.model.db.User;
import bocai.com.yanghuaji.model.db.User_Table;
import bocai.com.yanghuaji.receiver.TagAliasOperatorHelper;
import cn.jpush.android.api.JPushInterface;

/**
 * 数据持久化
 * 作者 yuanfei on 2017/11/15.
 * 邮箱 yuanfei221@126.com
 */

public class Account {
    private static final String KEY_TOKEN = "KEY_TOKEN";
    private static final String KEY_PHONE = "KEY_PHONE";
    private static final String KEY_USER_ID = "KEY_USER_ID";
    private static final String KEY_PUSH_ID = "KEY_USER_ID";
    // 设备配置数据
    private static final String KEY_DEVELEOP_ID = "KEY_DEVELEOP_ID";
    private static final String KEY_APP_ID = "KEY_APP_ID";
    private static final String KEY_APP_KEY = "KEY_APP_KEY";
    private static final String KEY_PORT = "KEY_PORT";
    private static final String KEY_REGISTER_HOST = "KEY_REGISTER_HOST";
    private static final String KEY_SERVICE_NAME = "KEY_SERVICE_NAME";


    private static String token;
    private static String phone;
    private static String userId;
    private static String pushId;


    private static String developId;
    private static String appId;
    private static String appKey;
    private static String port;
    private static String registerHost;
    private static String serviceName;


    //所有已经添加过的设备
    private static List<EquipmentRspModel.ListBean> listBeans;
    private static int horizonVisiablePosition = 0;
    private static int verticalVisiablePosition = 1;

    /**
     * 存储数据到XML文件，持久化
     */
    private static void save(Context context) {
        // 获取数据持久化的SP
        SharedPreferences sp = context.getSharedPreferences(Account.class.getName(),
                Context.MODE_PRIVATE);
        // 存储数据
        sp.edit()
                .putString(KEY_PUSH_ID, pushId)
                .putString(KEY_TOKEN, token)
                .putString(KEY_PHONE, phone)
                .putString(KEY_USER_ID, userId)
                .apply();
    }


    /**
     * 存储数据到XML文件，持久化
     */
    private static void saveEquipmentConfig(Context context) {
        // 获取数据持久化的SP
        SharedPreferences sp = context.getSharedPreferences("equipmentConfig",
                Context.MODE_PRIVATE);
        // 存储数据
        sp.edit()
                .putString(KEY_DEVELEOP_ID, developId)
                .putString(KEY_APP_ID, appId)
                .putString(KEY_APP_KEY, appKey)
                .putString(KEY_PORT, port)
                .putString(KEY_REGISTER_HOST, registerHost)
                .putString(KEY_SERVICE_NAME, serviceName)
                .apply();
    }

    /**
     * 持久化设备信息
     */
    public static void saveConfig(EquipmentConfigModel model) {
        developId = model.getDeveloperID();
        appId = model.getAppID();
        appKey = model.getAppKey();
        port = model.getPort();
        registerHost = model.getRegisterHost();
        serviceName = model.getServiceName();
        saveEquipmentConfig(Application.getInstance());
    }

    /**
     * 进行数据加载
     */
    public static void loadEquipmentConfig(Context context) {
        SharedPreferences sp = context.getSharedPreferences("equipmentConfig",
                Context.MODE_PRIVATE);
        developId = sp.getString(KEY_DEVELEOP_ID, "");
        appId = sp.getString(KEY_APP_ID, "");
        appKey = sp.getString(KEY_APP_KEY, "");
        port = sp.getString(KEY_PORT, "");
        registerHost = sp.getString(KEY_REGISTER_HOST, "");
        serviceName = sp.getString(KEY_SERVICE_NAME, "");
    }

    /**
     * 进行数据加载
     */
    public static void load(Context context) {
        SharedPreferences sp = context.getSharedPreferences(Account.class.getName(),
                Context.MODE_PRIVATE);
        token = sp.getString(KEY_TOKEN, "");
        userId = sp.getString(KEY_USER_ID, "");
        phone = sp.getString(KEY_PHONE, "");
    }


    public static void login(AccountRspModel model) {
        // 存储当前登录的账户, token, 用户Id
        Account.token = model.getToken();
        Account.phone = model.getPhone();
        Account.userId = model.getId();
        save(Application.getInstance());
    }

    public static void logOff(Context context) {
        // 存储当前登录的账户, token, 用户Id
        SharedPreferences sp = context.getSharedPreferences(Account.class.getName(),
                Context.MODE_PRIVATE);
        sp.edit().clear().apply();
        clearJPushAlias(context);
    }

    public static void clearJPushAlias(Context context) {
        JPushInterface.clearAllNotifications(context);
        TagAliasOperatorHelper.TagAliasBean tagAliasBean = new TagAliasOperatorHelper.TagAliasBean();
        tagAliasBean.setAction(TagAliasOperatorHelper.ACTION_CLEAN);
        tagAliasBean.setAlias("");
        tagAliasBean.setAliasAction(true);
        TagAliasOperatorHelper.getInstance().handleAction(context,
                0, tagAliasBean);
        JPushInterface.stopPush(context);
    }


    public static void saveWifiPassword(Context context, String wifiName, String wifiPassword) {
        SharedPreferences sp = context.getSharedPreferences("wifiConfig",
                Context.MODE_PRIVATE);
        sp.edit().putString(wifiName, wifiPassword).apply();
    }

    public static String getWifiPassword(Context context, String wifiName) {
        SharedPreferences sp = context.getSharedPreferences("wifiConfig",
                Context.MODE_PRIVATE);
        return sp.getString(wifiName, "");
    }

    /**
     * 判断是否已经登录
     */
    public static boolean isLogin() {
        SharedPreferences sp = Application.getInstance().getSharedPreferences(Account.class.getName(),
                Context.MODE_PRIVATE);
        return !TextUtils.isEmpty(sp.getString(KEY_TOKEN, "")) && !TextUtils.isEmpty(sp.getString(KEY_PHONE, ""));
    }


    public static String getToken() {
        return token;
    }

    public static List<EquipmentRspModel.ListBean> getListBeans() {
        return listBeans;
    }

    public static void setListBeans(List<EquipmentRspModel.ListBean> listBeans) {
        Account.listBeans = listBeans;
    }

    /**
     * 获取当前登录的用户信息
     */
    public static User getUser() {
        // 如果为null返回一个new的User，其次从数据库查询
//        return TextUtils.isEmpty(userId) ? new User() : SQLite.select()
//                .from(User.class)
//                .where(User_Table.Id.eq(userId))
//                .querySingle();
        return SQLite.select()
                .from(User.class)
                .where(User_Table.Id.eq(userId))
                .querySingle();
    }

    public static String getPushId() {
        return pushId;
    }

    public static String getPhone() {
        return phone;
    }

    public static void setPhone(String phone) {
        Account.phone = phone;
    }


    public static String getDevelopId() {
        return developId;
    }

    public static void setDevelopId(String developId) {
        Account.developId = developId;
    }

    public static String getAppId() {
        return appId;
    }

    public static void setAppId(String appId) {
        Account.appId = appId;
    }

    public static String getAppKey() {
        return appKey;
    }

    public static void setAppKey(String appKey) {
        Account.appKey = appKey;
    }

    public static String getPort() {
        return port;
    }

    public static void setPort(String port) {
        Account.port = port;
    }

    public static String getRegisterHost() {
        return registerHost;
    }

    public static void setRegisterHost(String registerHost) {
        Account.registerHost = registerHost;
    }

    public static String getServiceName() {
        return serviceName;
    }

    public static void setServiceName(String serviceName) {
        Account.serviceName = serviceName;
    }

    public static int getHorizonVisiablePosition() {
        return horizonVisiablePosition;
    }

    public static void setHorizonVisiablePosition(int horizonVisiablePosition) {
        Account.horizonVisiablePosition = horizonVisiablePosition;
    }

    public static int getVerticalVisiablePosition() {
        return verticalVisiablePosition;
    }

    public static void setVerticalVisiablePosition(int verticalVisiablePosition) {
        Account.verticalVisiablePosition = verticalVisiablePosition;
    }
}
