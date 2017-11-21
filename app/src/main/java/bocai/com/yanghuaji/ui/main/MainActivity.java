package bocai.com.yanghuaji.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.Activity;
import bocai.com.yanghuaji.base.Application;
import bocai.com.yanghuaji.base.GlideApp;
import bocai.com.yanghuaji.model.db.User;
import bocai.com.yanghuaji.ui.personalCenter.EditPersonalDataActivity;
import bocai.com.yanghuaji.util.ActivityUtil;
import bocai.com.yanghuaji.util.UiTool;
import bocai.com.yanghuaji.util.persistence.Account;
import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends Activity {
    private long firstTime=0;
    private NavigationFragment mNavigationFragment;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.frame_left)
    FrameLayout mFrameLeft;

    @BindView(R.id.frame_container)
    FrameLayout mFrameLayout;

    @BindView(R.id.img_portrait)
    CircleImageView mPortrait;

    @BindView(R.id.tv_name)
    TextView mName;




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
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            //将侧边栏顶部延伸至status bar
            mDrawerLayout.setFitsSystemWindows(true);
            //将主页面顶部延伸至status bar
            mDrawerLayout.setClipToPadding(false);
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
        User user = Account.getUser();
        if (user != null){
            mName.setText(user.getNickName());
            GlideApp.with(this)
                    .load(user.getRelativePath())
                    .centerCrop()
                    .into(mPortrait);
        }
    }




    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){     //KEYCODE_BACK：回退键
            long secondTime= System.currentTimeMillis();
            if (secondTime-firstTime>2000){
                Application.showToast("再按一次退出程序");
                firstTime=System.currentTimeMillis();
                return true;
            }else{
                ActivityUtil.finishActivity();
            }
        }
        return super.onKeyUp(keyCode, event);
    }
}
