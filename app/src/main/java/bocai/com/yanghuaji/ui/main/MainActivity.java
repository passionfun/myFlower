package bocai.com.yanghuaji.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.Activity;
import bocai.com.yanghuaji.ui.personalCenter.EditPersonalDataActivity;
import bocai.com.yanghuaji.util.UiTool;
import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends Activity {
    private NavigationFragment mNavigationFragment;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.frame_left)
    FrameLayout mFrameLeft;

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

    @Override
    protected void initData() {
        super.initData();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        mNavigationFragment = NavigationFragment.newInstance();
        transaction.replace(R.id.frame_container, mNavigationFragment).commit();
        mDrawerLayout.addDrawerListener(new MyDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                mFrameLayout.layout(mFrameLeft.getRight(), -UiTool.getHeight(MainActivity.this),
                        UiTool.getScreenWidth(MainActivity.this) + mFrameLeft.getRight(), UiTool.getScreenHeight(MainActivity.this));
            }
        });
    }


    @OnClick(R.id.tv_edit_personal_data)
    void onEditPersonalClick(){
        EditPersonalDataActivity.show(this);
    }

    public void showLeft() {
        mDrawerLayout.openDrawer(GravityCompat.START);
    }
}
