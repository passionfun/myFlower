package bocai.com.yanghuaji.updateVersion.manager.fileload;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *
 * Created by shc on 2018/1/4.
 */
public abstract class FileCallback implements Callback<ResponseBody>{
    /**
     * 订阅下载进度
     */
    private CompositeDisposable rxSubscriptions = new CompositeDisposable();
//    private CompositeSubscription rxSubscriptions = new CompositeSubscription(0);

    /**
     * 目标文件存储的文件夹路径
     */
    private String destFileDir;
    /**
     * 目标文件存储的文件名
     */
    private String destFileName;

    public FileCallback(String destFileDir, String destFileName) {
        this.destFileDir = destFileDir;
        this.destFileName = destFileName;
        subscribeLoadProgress();// 订阅下载进度
    }
    /**
     * 成功后回调
     */
    public abstract void onSuccess(File file);

    /**
     * 下载过程回调
     */
    public abstract void onLoading(long progress, long total);

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        try {
            saveFile(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public File saveFile(Response<ResponseBody> response) throws Exception {
        InputStream in = null;
        FileOutputStream out = null;
        byte[] buf = new byte[2048*10];
        int len;
        try {
            File dir = new File(destFileDir);
            if (!dir.exists()) {// 如果文件不存在新建一个
                dir.mkdirs();
            }
            in = response.body().byteStream();
            File file = new File(dir,destFileName);
            out = new FileOutputStream(file);
            while ((len = in.read(buf)) != -1){
                out.write(buf,0,len);
            }
            // 回调成功的接口
            onSuccess(file);
            unSubscribe();// 取消订阅
            return file;
        }finally {
            in.close();
            out.close();
        }
    }
    /**
     * 订阅文件下载进度
     */
    private void subscribeLoadProgress() {
        rxSubscriptions.add(RxBus.getDefault()
                .toObservable(FileLoadingBean.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<FileLoadingBean>() {
                    @Override
                    public void accept(FileLoadingBean fileLoadEvent) throws Exception {
                        onLoading(fileLoadEvent.getProgress(), fileLoadEvent.getTotal());
                    }
                }));
//                .subscribe(new Action<FileLoadingBean>() {
//                    @Override
//                    public void call(FileLoadingBean fileLoadEvent) {
//                        onLoading(fileLoadEvent.getProgress(), fileLoadEvent.getTotal());
//                    }
//                }));
    }
    /**
     * 取消订阅，防止内存泄漏
     */
    private void unSubscribe() {
        if (!rxSubscriptions.isDisposed()) {
            rxSubscriptions.dispose();
        }
    }
}
