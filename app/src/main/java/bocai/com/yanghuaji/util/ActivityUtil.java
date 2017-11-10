package bocai.com.yanghuaji.util;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

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

}
