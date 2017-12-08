package bocai.com.yanghuaji.util.persistence;

import android.content.Context;
import android.content.SharedPreferences;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

import bocai.com.yanghuaji.base.Application;
import bocai.com.yanghuaji.model.AccountRspModel;
import bocai.com.yanghuaji.model.EquipmentRspModel;
import bocai.com.yanghuaji.model.db.User;
import bocai.com.yanghuaji.model.db.User_Table;

/**
 * 数据持久化
 * 作者 yuanfei on 2017/11/15.
 * 邮箱 yuanfei221@126.com
 */

public class Account {
    private static final String KEY_PUSH_ID = "KEY_PUSH_ID";
    private static final String KEY_TOKEN = "KEY_TOKEN";
    private static final String KEY_PHONE = "KEY_PHONE";
    private static final String KEY_USER_ID = "KEY_USER_ID";
    // 设备的推送Id
    private static String pushId;
    private static String token;
    private static String phone;
    private static String userId;
    //所有已经添加过的设备
    private static List<EquipmentRspModel.ListBean> listBeans;
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
     * 进行数据加载
     */
    public static void load(Context context) {
        SharedPreferences sp = context.getSharedPreferences(Account.class.getName(),
                Context.MODE_PRIVATE);
        pushId = sp.getString(KEY_PUSH_ID, "");
        token = sp.getString(KEY_TOKEN, "");
        userId = sp.getString(KEY_USER_ID, "");
    }


    public static void login(AccountRspModel model) {
        // 存储当前登录的账户, token, 用户Id
        Account.token = model.getToken();
        Account.phone = model.getPhone();
        Account.userId = model.getId();
        save(Application.getInstance());
    }


    /**
     * 设置并存储设备的Id
     */
    public static void setPushId(String pushId) {
        Account.pushId = pushId;
        Account.save(Application.getInstance());
    }

    /**
     * 获取推送Id
     */
    public static String getPushId() {
        return pushId;
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
        return  SQLite.select()
                .from(User.class)
                .where(User_Table.Id.eq(userId))
                .querySingle();
    }

}
