package bocai.com.yanghuaji.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;
import net.qiujuer.genius.ui.widget.Loading;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.ui.account.LoginActivity;
import bocai.com.yanghuaji.util.persistence.Account;

public class UiTool {
    private static int STATUS_BAR_HEIGHT = -1;

    /**
     * 得到我们的状态栏的高度
     * @param activity Activity
     * @return 状态栏的高度
     */
    public static int getStatusBarHeight(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && STATUS_BAR_HEIGHT == -1) {
            try {
                final Resources res = activity.getResources();
                // 尝试获取status_bar_height这个属性的Id对应的资源int值
                int resourceId = res.getIdentifier("status_bar_height", "dimen", "android");
                if (resourceId <= 0) {
                    Class<?> clazz = Class.forName("com.android.internal.R$dimen");
                    Object object = clazz.newInstance();
                    resourceId = Integer.parseInt(clazz.getField("status_bar_height")
                            .get(object).toString());
                }


                // 如果拿到了就直接调用获取值
                if (resourceId > 0)
                    STATUS_BAR_HEIGHT = res.getDimensionPixelSize(resourceId);

                // 如果还是未拿到
                if (STATUS_BAR_HEIGHT <= 0) {
                    // 通过Window拿取
                    Rect rectangle = new Rect();
                    Window window = activity.getWindow();
                    window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
                    STATUS_BAR_HEIGHT = rectangle.top;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return STATUS_BAR_HEIGHT;
    }


    //获取标题栏高度
    public static int getHeight(Activity activity){
//        DisplayMetrics dm = new DisplayMetrics();
//        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
//        Log.e("WangJ", "屏幕高:" + dm.heightPixels);
        //应用区域
        Rect outRect1 = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect1);
        int viewTop = activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
        int titleHeight1 = viewTop - outRect1.top;
        return titleHeight1;
    }

    public static int getScreenWidth(Activity activity) {
        DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();
        //int width = activity.getWindowManager().getDefaultDisplay().getWidth();
        return displayMetrics.widthPixels;
    }

    public static int getScreenHeight(Activity activity) {
        DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();
        //int width = activity.getWindowManager().getDefaultDisplay().getWidth();
        return displayMetrics.heightPixels;
    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    private static Dialog dialog;

    public static void showLoading(Context context) {

//        dialog  = new ProgressDialog(context);
        dialog  = UiTool.createLoadingDialog(context);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }




    public static void hideLoading() {
        if (dialog!=null&&dialog.isShowing()){
            dialog.dismiss();
        }
    }


    /**
     * 加粗字体
     */
    public static void setBlod(TextView textView){
        TextPaint tp = textView.getPaint();
        tp.setFakeBoldText(true);
    }




    /**
     * 得到自定义的progressDialog
     * @param context
     * @return
     */
    public static Dialog createLoadingDialog(Context context) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.layout_loading_dialog, null);// 得到加载view
        LinearLayout layout =  v.findViewById(R.id.dialog_view);// 加载布局
        // main.xml中的ImageView
        Loading spaceshipImage =  v.findViewById(R.id.loading);
        spaceshipImage.setForegroundColor(Color.parseColor("#87BC52"));
        TextView tipTextView = v.findViewById(R.id.tipTextView);// 提示文字
        // 加载动画
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                context, R.anim.load_animation);
        // 使用ImageView显示动画
        spaceshipImage.startAnimation(hyperspaceJumpAnimation);
        Dialog loadingDialog = new Dialog(context,R.style.loading_dialog);// 创建自定义样式dialog

        loadingDialog.setCancelable(true);// 可以用“返回键”取消
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
        return loadingDialog;

    }


    /**
     * 被踢下线
     */
    public static void onConnectionConflict(final Context context){
        if (context==null)
            return;
        AlertDialog.Builder deleteDialog = new AlertDialog.Builder(context);
        deleteDialog.setTitle("账号已在其他终端登录，请重新登录？");
        deleteDialog.setCancelable(false);
        deleteDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ActivityUtil.finishActivity();
                Account.logOff(context);
                LoginActivity.show(context);
            }
        });
        deleteDialog.show();
    }


    // 水平进度条的最大值
    private static final int MAX_PROGRESS = 100;
    // 默认的初始值
    private int progress = 0;

    public static ProgressDialog showProgressBarDialog(Context context){
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("升级中...");
//        progressDialog.setMessage("请稍后...");
        // 设置进度对话框的风格 ,默认是圆形的
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        // 设置是否可以通过点击Back键取消  默认true
        progressDialog.setCancelable(false);
        // 设置在点击Dialog外是否取消Dialog进度条  默认true
        progressDialog.setCanceledOnTouchOutside(false);

        // 设置最大值
        progressDialog.setMax(MAX_PROGRESS);
        // 设置暂停按钮
//        progressDialog.setButton(DialogInterface.BUTTON_POSITIVE, "暂停", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                // 通过删除消息代码的方式停止定时器
//                progressDialogHandler.removeMessages(PROGRESSDIALOG_FLAG);
//            }
//        });



        // 展示
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                progressDialog.show();
            }
        });
        return progressDialog;
    }


    /**
     * 检查网络是否可用
     */
    public static boolean isNetworkAvailable(Context context) {

        ConnectivityManager manager = (ConnectivityManager) context
                .getApplicationContext().getSystemService(
                        Context.CONNECTIVITY_SERVICE);

        if (manager == null) {
            return false;
        }

        NetworkInfo networkinfo = manager.getActiveNetworkInfo();

        if (networkinfo == null || !networkinfo.isAvailable()) {
            return false;
        }

        return true;
    }

}
