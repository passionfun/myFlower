package bocai.com.yanghuaji.base.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

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

    private Factory(){
        // 新建一个4个线程的线程池
        executor = Executors.newFixedThreadPool(4);
        gson = new GsonBuilder()
                // 设置时间格式
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
                .create();
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

}
