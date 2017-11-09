package bocai.com.yanghuaji.ui.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.Activity;
import butterknife.BindView;

public class MainActivity extends Activity {
    private NavigationFragment mNavigationFragment;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.navigation_view)
    NavigationView mNavigationView;

    @BindView(R.id.frame_container)
    FrameLayout mFrameLayout;


    //显示的入口
    public static void show(Context context) {
        context.startActivity(new Intent(context, MainActivity.class));
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                //将侧边栏顶部延伸至status bar
                mDrawerLayout.setFitsSystemWindows(true);
                //将主页面顶部延伸至status bar;虽默认为false,但经测试,DrawerLayout需显示设置
                mDrawerLayout.setClipToPadding(false);
            }
        }
    }


    private int getHeight(){
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        Log.e("WangJ", "屏幕高:" + dm.heightPixels);

        //应用区域
        Rect outRect1 = new Rect();
       getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect1);
        int viewTop = getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
        int titleHeight1 = viewTop - outRect1.top;
        return titleHeight1;
    }

    @Override
    protected void initData() {
        super.initData();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        mNavigationFragment = NavigationFragment.newInstance();
        transaction.replace(R.id.frame_container,mNavigationFragment).commit();
        mDrawerLayout.addDrawerListener(new MyDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
                Display display = manager.getDefaultDisplay();
                int height = getHeight();
                mFrameLayout.layout(mNavigationView.getRight(), -getHeight(), display.getHeight() + mNavigationView.getRight(), display.getHeight());
            }
        });

    }
}
