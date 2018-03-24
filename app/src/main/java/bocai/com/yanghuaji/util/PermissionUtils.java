package bocai.com.yanghuaji.util;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import bocai.com.yanghuaji.R;

/**
 * 作者 yuanfei on 2017/12/12.
 * 邮箱 yuanfei221@126.com
 */

public class PermissionUtils {


    /**
     * 检查是否拥有指定的所有权限
     */
    public static boolean checkPermissionAllGranted(Context context,String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                // 只要有一个权限没有被授予, 则直接返回 false
                return false;
            }
        }
        return true;
    }


    /**
     * 打开 APP 的详情设置
     */
    public static void openAppDetails(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("需要权限，请到 “应用信息 -> 权限” 中授予！");
        builder.setPositiveButton("去手动授权", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setData(Uri.parse("package:" + context.getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                context.startActivity(intent);
            }
        });
        builder.setNegativeButton(context.getString(R.string.cancel), null);
        builder.show();
    }

}
