package bocai.com.yanghuaji.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Window;

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


}
