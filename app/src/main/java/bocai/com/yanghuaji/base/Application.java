package bocai.com.yanghuaji.base;

import android.content.Context;
import android.os.SystemClock;
import android.support.annotation.StringRes;
import android.support.multidex.MultiDex;
import android.widget.Toast;

import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.io.File;

import bocai.com.yanghuaji.base.common.Factory;
import bocai.com.yanghuaji.receiver.TagAliasOperatorHelper;
import bocai.com.yanghuaji.util.persistence.Account;
import cn.jpush.android.api.JPushInterface;

/**
 * 作者 yuanfei on 2017/11/8.
 * 邮箱 yuanfei221@126.com
 */
public class Application extends android.app.Application {
    private static Application instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Account.load(this);
        //加载长牙配置信息
        Account.loadEquipmentConfig(this);
        Factory.setup();
        //该接口需在init接口之前调用，避免出现部分日志没打印的情况
        TagAliasOperatorHelper.TagAliasBean tagAliasBean = new TagAliasOperatorHelper.TagAliasBean();
        tagAliasBean.setAlias("");
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        UMShareAPI.get(this);
        //bugly初始化
        CrashReport.initCrashReport(getApplicationContext(), "6d18085e76", true);
//        //扫一扫初始化
        ZXingLibrary.initDisplayOpinion(this);
    }

    {
        // 设置微信参数
        PlatformConfig.setWeixin("wx8bb8dc2f4ebd73a5", "2c81167a27f12783d2cd014c4f0c60b8");
        //设置qq参数
        PlatformConfig.setQQZone("1106603998", "9GaOkyLJr7r5ql4f");
    }

    public static Application getInstance() {
        return instance;
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    /**
     * 获取缓存文件夹地址
     *
     * @return 当前APP的缓存文件夹地址
     */
    public static File getCacheDirFile() {
        return instance.getCacheDir();
    }

    /**
     * 获取头像的临时存储文件地址
     *
     * @return 临时文件
     */
    public static File getPortraitTmpFile() {
        // 得到头像目录的缓存地址
        File dir = new File(getCacheDirFile(), "portrait");
        // 创建所有的对应的文件夹
        dir.mkdirs();

        // 删除旧的一些缓存为文件
        File[] files = dir.listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                //noinspection ResultOfMethodCallIgnored
                file.delete();
            }
        }

        // 返回一个当前时间戳的目录文件地址
        File path = new File(dir, SystemClock.uptimeMillis() + ".jpg");
        return path.getAbsoluteFile();
    }


    /**
     * 显示一个Toast
     *
     * @param msg 字符串
     */
    public static void showToast(final String msg) {
        // Toast 只能在主线程中显示，所有需要进行线程转换，
        // 保证一定是在主线程进行的show操作
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                Toast.makeText(instance, msg, Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * 显示一个Toast
     *
     * @param msgId 传递的是字符串的资源
     */
    public static void showToast(@StringRes int msgId) {
        showToast(instance.getString(msgId));
    }

    public static String getStringText(@StringRes int resourceId) {
        return instance.getString(resourceId);
    }

}
