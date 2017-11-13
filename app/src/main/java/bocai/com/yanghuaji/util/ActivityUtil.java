package bocai.com.yanghuaji.util;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者 yuanfei on 2016/12/21.
 * 邮箱 yuanfei221@126.com
 */

public class ActivityUtil {

    public static List<Activity> acticitys=new ArrayList<Activity>();

    public static void addActivity(Activity activity){
        acticitys.add(activity);
    }
    public static void deleteActivity(Activity activity){
        acticitys.remove(activity);
    }
    public static void finishActivity(){
        for (Activity activity : acticitys) {
            if(activity != null){
                activity.finish();
            }
        }
    }

    public static void removeActivity(Activity activity){
        for (int i = 0;i<acticitys.size();i++){
            if (activity.getComponentName().equals(acticitys.get(i).getComponentName())){
                acticitys.remove(i);
                break;
            }
        }
    }

    public static boolean isExsitMianActivity(Context context, Class<?> cls){
        Intent intent = new Intent(context, cls);
        ComponentName cmpName = intent.resolveActivity(context.getPackageManager());
        boolean flag = false;
        for (Activity activity : acticitys) {
            if(activity.getComponentName() .equals(cmpName)){
                flag = true;
                break;
            }
        }
        return flag;
    }

    public static void finishOneActivity(Context context, Class<?> cls){
        Intent intent = new Intent(context, cls);
        ComponentName cmpName = intent.resolveActivity(context.getPackageManager());
        boolean flag = false;
        for (Activity activity : acticitys) {
            if(activity.getComponentName() .equals(cmpName)){
                activity.finish();
                break;
            }
        }
    }

//    /**
//     * 解除绑定
//     * @param context
//     */
//    public static void removeAlias(Context context){
//        JPushInterface.setAliasAndTags(context.getApplicationContext(), "", null, new TagAliasCallback() {
//            @Override
//            public void gotResult(int i, String s, Set<String> set) {
//                if (i == 0){
//                    Log.e("String","解除绑定"+" "+s);
//                }
//            }
//        });
//    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha 屏幕透明度0.0-1.0 1表示完全不透明
     *
     */

//    public static void setBackgroundAlpha(float bgAlpha,Context context) {
//        WindowManager.LayoutParams lp = ((Activity) context).getWindow()
//                .getAttributes();
//        lp.alpha = bgAlpha;
//        ((Activity) context).getWindow().setAttributes(lp);
//    }

    /**
     * 设置页面的透明度
     * @param bgAlpha 1表示不透明
     */
    public static void setBackgroundAlpha(Activity activity, float bgAlpha) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        if (bgAlpha == 1) {
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
        }
        activity.getWindow().setAttributes(lp);
    }
}
