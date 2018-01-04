package bocai.com.yanghuaji.updateVersion.manager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Toast;

import bocai.com.yanghuaji.model.VersionInfoModel;
import bocai.com.yanghuaji.updateVersion.DownLoadService;
import bocai.com.yanghuaji.updateVersion.util.DeviceUtils;
import bocai.com.yanghuaji.util.ActivityUtil;


/**
 *
 * Created by shc on 2018/1/4.
 */
public class UpdateManager {

    private Context mContext;
    public static final String APK_URL = "APK_URL";

    public UpdateManager(Context context) {
        this.mContext = context;
    }

    /**
     * 检测软件更新
     */
    public void checkUpdate(VersionInfoModel model) {
        /**
         * 在这里请求后台接口，获取更新的内容和最新的版本号
         */
        // 版本的更新信息
        boolean isForceupdating = model.isForceupdating();
        String versionInfo = model.getTitle();
        int mVersion_code = DeviceUtils.getVersionCode(mContext);// 当前的版本号
        int nVersion_code = Integer.valueOf(model.getVersion());
        if (mVersion_code < nVersion_code) {
            // 显示提示对话
            showNoticeDialog(versionInfo,isForceupdating,model.getUrl());
        }
    }

    /**
     * 显示更新对话框
     *
     */
    private void showNoticeDialog(String versionInfo,boolean isForceupdating,final String url) {
        // 构造对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("版本更新：");
        builder.setMessage(versionInfo);
        // 更新
        builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent(mContext, DownLoadService.class);
                intent.putExtra(APK_URL,url);
                mContext.startService(intent);
            }
        });
        if (isForceupdating){
            //强制更新，不更新就退出
            builder.setNegativeButton("退出应用", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ActivityUtil.finishActivity();
                    dialog.dismiss();
                }
            });
        }else {
            // 稍后更新
            builder.setNegativeButton("以后更新", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        Dialog noticeDialog = builder.create();
        noticeDialog.show();
    }
}
