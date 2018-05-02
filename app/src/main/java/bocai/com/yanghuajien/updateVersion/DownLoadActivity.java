package bocai.com.yanghuajien.updateVersion;


import android.app.Activity;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.MimeTypeMap;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import bocai.com.yanghuajien.R;
import bocai.com.yanghuajien.base.Application;
import bocai.com.yanghuajien.updateVersion.manager.UpdateManager;
import bocai.com.yanghuajien.updateVersion.manager.fileload.FileCallback;
import bocai.com.yanghuajien.updateVersion.manager.fileload.FileResponseBody;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * 下载页面
 * Created by shc on 2018/4/19.
 */

public class DownLoadActivity extends Activity {

    /**
     * 目标文件存储的文件夹路径
     */
    private String  destFileDir = Environment.getExternalStorageDirectory().getAbsolutePath() + File
            .separator + "M_DEFAULT_DIR";
    /**
     * 目标文件存储的文件名
     */
    private String destFileName = "shan_yao.apk";

    private Context mContext;
    private int preProgress = 0;
    private int NOTIFY_ID = 1000;
    private NotificationCompat.Builder builder;
    private NotificationManager notificationManager;
    private Retrofit.Builder retrofit;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);
        mContext = this;
        String url = getIntent().getStringExtra(UpdateManager.APK_URL);
        if (!TextUtils.isEmpty(url)){
            loadFile(url);
        }
    }

    /**
     * 下载文件
     */
    private void loadFile(String url) {
        //"http://www.izis.cn/mygoedu/yztv_1.apk"
        String baseUrl = url.substring(0,url.lastIndexOf("/")+1);
        String content = url.substring(url.lastIndexOf("/")+1);
        Log.d("sunhengchao", "url:"+url+"loadFile: "+baseUrl+"..."+content);
        initNotification();
        if (retrofit == null) {
            retrofit = new Retrofit.Builder();
        }
        retrofit.baseUrl(baseUrl)
                .client(initOkHttpClient())
                .build()
                .create(DownLoadService.IFileLoad.class)
                .loadFile(content)
                .enqueue(new FileCallback(destFileDir, destFileName) {

                    @Override
                    public void onSuccess(File file) {
                        Log.e("zs", "请求成功");
                        // 安装软件
                        cancelNotification();
                        installApk(file);
                    }

                    @Override
                    public void onLoading(long progress, long total) {
                        Log.e("zs", progress + "----" + total);
                        updateNotification(progress * 100 / total);
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("zs", "请求失败");
                        cancelNotification();
                    }
                });
    }

    public interface IFileLoad {
        @GET("{url}")
        Call<ResponseBody> loadFile(@Path("url")String url);
    }

    /**
     * 安装软件
     *
     * @param file
     */
    private void installApk(File file) {
        Uri uri = Uri.fromFile(file);
        Intent install = new Intent(Intent.ACTION_VIEW);
        install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        install.setDataAndType(uri, "application/vnd.android.package-archive");
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
            Uri uriForFile = FileProvider.getUriForFile(mContext,
                    mContext.getApplicationContext().getPackageName() + ".provider", file);
            install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            install.setDataAndType(uri, "application/vnd.android.package-archive");
            install.setDataAndType(uriForFile, mContext.getContentResolver().getType(uriForFile));

        }else{
            install.setDataAndType(Uri.fromFile(file), getMIMEType(file));
        }
        // 执行意图进行安装
        mContext.startActivity(install);
        finish();
    }

    public String getMIMEType(File file) {
        String var1 = "";
        String var2 = file.getName();
        String var3 = var2.substring(var2.lastIndexOf(".") + 1, var2.length()).toLowerCase();
        var1 = MimeTypeMap.getSingleton().getMimeTypeFromExtension(var3);
        return var1;
    }

    /**
     * 初始化OkHttpClient
     *
     * @return
     */
    private OkHttpClient initOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(100000, TimeUnit.SECONDS);
        builder.networkInterceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response originalResponse = chain.proceed(chain.request());
                return originalResponse
                        .newBuilder()
                        .body(new FileResponseBody(originalResponse))
                        .build();
            }
        });
        return builder.build();
    }

    /**
     * 初始化Notification通知
     */
    public void initNotification() {



        progressDialog = new ProgressDialog(mContext);
        progressDialog.setTitle(Application.getStringText(R.string.app_update));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMax(100);
        progressDialog.setProgress(1);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M&&Build.VERSION.SDK_INT < Build.VERSION_CODES.O){//6.0
//            progressDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
//        }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//            progressDialog.getWindow().setType((WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY));
//        }else {
//            progressDialog.getWindow().setType((WindowManager.LayoutParams.TYPE_SYSTEM_ALERT));
//        }
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                progressDialog.show();
            }
        });



//        builder = new NotificationCompat.Builder(mContext)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentText("0%")
//                .setContentTitle("慕奈花舍更新")
//                .setProgress(100, 0, false);
//        notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify(NOTIFY_ID, builder.build());
    }

    /**
     * 更新通知
     */
    public void updateNotification(long progress) {
        int currProgress = (int) progress;
        if (preProgress < currProgress) {
            progressDialog.setProgress((int) progress);
        }
        preProgress = (int) progress;


//        int currProgress = (int) progress;
//        if (preProgress < currProgress) {
//            builder.setContentText(progress + "%");
//            builder.setProgress(100, (int) progress, false);
//            notificationManager.notify(NOTIFY_ID, builder.build());
//        }
//        preProgress = (int) progress;
    }

    /**
     * 取消通知
     */
    public void cancelNotification() {
//        progressDialog.cancel();
        progressDialog.dismiss();
//        notificationManager.cancel(NOTIFY_ID);
    }



}
