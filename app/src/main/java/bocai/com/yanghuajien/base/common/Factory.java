package bocai.com.yanghuajien.base.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import bocai.com.yanghuajien.base.Application;
import bocai.com.yanghuajien.model.LongToothRspModel;
import bocai.com.yanghuajien.util.DBFlowExclusionStrategy;

/**
 * 作者 yuanfei on 2017/11/15.
 * 邮箱 yuanfei221@126.com
 */

public class Factory {
    private static final Factory instance;
    // 全局的Gson
    private final Gson gson;
    // 全局的线程池
    private final Executor executor;

    static {
        instance = new Factory();
    }

    private Factory() {
        // 新建一个4个线程的线程池
        executor = Executors.newFixedThreadPool(4);
        gson = new GsonBuilder()
                // 设置时间格式
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
                //设置一个数据库过滤器
                .setExclusionStrategies(new DBFlowExclusionStrategy())
                .create();
    }


    /**
     * Factory 中的初始化
     */
    public static void setup() {
        // 初始化数据库
        FlowManager.init(new FlowConfig.Builder(Application.getInstance())
                .openDatabasesOnInit(true) // 数据库初始化的时候就开始打开
                .build());


    }


    //异步运行的方法
    public static void runOnAsync(Runnable runnable) {
        // 拿到单例，拿到线程池，然后异步执行
        instance.executor.execute(runnable);
    }

    // 返回一个全局的Gson，在这可以进行Gson的一些全局的初始化
    public static Gson getGson() {
        return instance.gson;
    }


    public static void decodeRspCode(LongToothRspModel longToothRspModel) {
        if (longToothRspModel == null) {
            return;
        }
        switch (longToothRspModel.getCODE()) {
            case LongToothRspModel.SUCCEED://0
                //绑定成功
                break;
            case LongToothRspModel.BIND_FAILED://1
                Application.showToast("绑定失败");
                break;
            case LongToothRspModel.EQUIPMENT_HAVE_BINDED://2
                Application.showToast("设备已被绑定");
                break;
        }
    }

}
