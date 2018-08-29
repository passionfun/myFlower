package bocai.com.yanghuajien.net;

import android.text.TextUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import bocai.com.yanghuajien.base.common.Common;
import bocai.com.yanghuajien.base.common.Factory;
import bocai.com.yanghuajien.util.LogUtil;
import bocai.com.yanghuajien.util.persistence.Account;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 作者 yuanfei on 2017/11/15.
 * 邮箱 yuanfei221@126.com
 */

public class Network {
    private static final String TAG = "Network";
    private static Network instance;
    private Retrofit retrofit;

    static {
        instance = new Network();
    }

    private Network(){}

    public static Retrofit getRetrofit(){
        if (instance.retrofit!=null){
            return instance.retrofit;
        }
        final HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
//                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        LogUtil.d(TAG,"Interceptor:"+chain.toString()+",request:"+chain.request()+",token:"+Account.getToken());
                        Request original = chain.request();
                        Request.Builder builder = original.newBuilder();
                        if (!TextUtils.isEmpty(Account.getToken())){
                            //注入token
                            builder.addHeader("token",Account.getToken());
                        }
                        builder.addHeader("Content-Type", "application/json");
                        Request newRequest = builder.build();
                        return chain.proceed(newRequest);
                    }
                })
                .addInterceptor(httpLoggingInterceptor)
                .build();

        Retrofit.Builder builder = new Retrofit.Builder();
        instance.retrofit = builder.baseUrl(Common.Constance.API_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(Factory.getGson()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return instance.retrofit;
    }

    //得到一个请求代理
    public static RemoteService remote(){
        return Network.getRetrofit().create(RemoteService.class);
    }
}
