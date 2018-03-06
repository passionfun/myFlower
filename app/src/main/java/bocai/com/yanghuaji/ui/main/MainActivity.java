package bocai.com.yanghuaji.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.Application;
import boc.com.imgselector.GlideApp;
import bocai.com.yanghuaji.base.RecyclerAdapter;
import bocai.com.yanghuaji.base.common.Common;
import bocai.com.yanghuaji.base.presenter.PresenterActivity;
import bocai.com.yanghuaji.model.EquipmentCard;
import bocai.com.yanghuaji.model.EquipmentConfigModel;
import bocai.com.yanghuaji.model.EquipmentRspModel;
import bocai.com.yanghuaji.model.GroupRspModel;
import bocai.com.yanghuaji.model.MessageEvent;
import bocai.com.yanghuaji.model.VersionInfoModel;
import bocai.com.yanghuaji.model.db.User;
import bocai.com.yanghuaji.presenter.main.MainActivityContract;
import bocai.com.yanghuaji.presenter.main.MainActivityPresenter;
import bocai.com.yanghuaji.receiver.TagAliasOperatorHelper;
import bocai.com.yanghuaji.ui.intelligentPlanting.AddWifiActivity;
import bocai.com.yanghuaji.ui.intelligentPlanting.GroupManagerActivity;
import bocai.com.yanghuaji.ui.intelligentPlanting.PlantingDateAct;
import bocai.com.yanghuaji.ui.intelligentPlanting.ShopActivity;
import bocai.com.yanghuaji.ui.personalCenter.EditPersonalDataActivity;
import bocai.com.yanghuaji.updateVersion.manager.UpdateManager;
import bocai.com.yanghuaji.util.ActivityUtil;
import bocai.com.yanghuaji.util.LongToothUtil;
import bocai.com.yanghuaji.util.UiTool;
import bocai.com.yanghuaji.util.persistence.Account;
import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends PresenterActivity<MainActivityContract.Presenter>
        implements MainActivityContract.View {
    public static final String TAG = MainActivity.class.getName();
    public static final String MAINACTIVITY_DESTROY = "MAINACTIVITY_DESTROY";
    private long firstTime = 0;
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

    @BindView(R.id.recycler_default_equipment)
    RecyclerView mRecyclerDefault;

    @BindView(R.id.view_all_equipments)
    View mDivideAllEquipments;

    @BindView(R.id.cb_default_equipments)
    CheckBox mCbDefaultEquipments;


    @BindView(R.id.recycler_group)
    RecyclerView mRecyclerGroup;

    //    private int page = 1;
//    private int pageGroup = 1;
    private RecyclerAdapter<EquipmentCard> mAdapter;
    private RecyclerAdapter<GroupRspModel.ListBean> mGroupAdapter;
    public static final String MAIN_ACTIVITY_REFRESH = "MAIN_ACTIVITY_REFRESH";
    public static final String GROUP_REFRESH = "GROUP_REFRESH";


    //显示的入口
    public static void show(Context context) {
        context.startActivity(new Intent(context, MainActivity.class));
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @OnClick(R.id.frame_switch_type)
    void onSwitchClick() {
        //切换横竖列表
        mNavigationFragment.switchType();
        hideLeft();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEidtPersonDataSuccess(MessageEvent messageEvent) {
        if (messageEvent.getMessage().equals(EditPersonalDataActivity.MODIFY_PERSONAL_DATA_SUCCESS)) {
            User user = Account.getUser();
            if (user != null) {
                if (TextUtils.isEmpty(user.getNickName())) {
                    mName.setText(user.getNickName());
                }
                GlideApp.with(this)
                        .load(user.getRelativePath())
                        .centerCrop()
                        .into(mPortrait);
            }
        } else if (messageEvent.getMessage().equals(MAIN_ACTIVITY_REFRESH)) {
            initAllEquipments();
            initAllGroups();
        }else if (messageEvent.getMessage().equals(GROUP_REFRESH)) {
            initAllGroups();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        User user = Account.getUser();
        String phone = Account.getPhone();
        Log.d(TAG, "onResume: " + phone);
        String account = phone.substring(0, 3) + "****" + phone.substring(7);
        if (user != null) {
            if (!TextUtils.isEmpty(user.getNickName())) {
                mName.setText(user.getNickName());
            } else {
                mName.setText(account);
            }
            GlideApp.with(this)
                    .load(user.getRelativePath())
                    .placeholder(R.mipmap.img_portrait_empty)
                    .centerCrop()
                    .into(mPortrait);
        } else {
            mName.setText(account);
        }
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        //	2 (Android)  3 (iOS)  4 (Java)
        mPresenter.getEquipmentConfig("2");
        EventBus.getDefault().register(this);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            //将侧边栏顶部延伸至status bar
            mDrawerLayout.setFitsSystemWindows(true);
            //将主页面顶部延伸至status bar
            mDrawerLayout.setClipToPadding(false);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().post(new MessageEvent("MAINACTIVITY_DESTROY"));
        EventBus.getDefault().unregister(this);
        UiTool.closeConflictDialog();
//        Account.clearJPushAlias(this);
    }

    @Override
    protected void initData() {
        super.initData();
        //	平台   0 ios  1 android
        mPresenter.checkVersion("1");
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        mNavigationFragment = NavigationFragment.newInstance();
        transaction.replace(R.id.frame_container, mNavigationFragment).commit();
//        mDrawerLayout.addDrawerListener(new MyDrawerListener() {
//            @Override
//            public void onDrawerSlide(View drawerView, float slideOffset) {
//                mFrameLayout.layout(mFrameLeft.getRight(), -UiTool.getHeight(MainActivity.this),
//                        UiTool.getScreenWidth(MainActivity.this) + mFrameLeft.getRight(),
//                        UiTool.getScreenHeight(MainActivity.this));
//            }
//        });
        initAllEquipments();
        initAllGroups();

        JPushInterface.resumePush(this);
        TagAliasOperatorHelper.TagAliasBean tagAliasBean = new TagAliasOperatorHelper.TagAliasBean();
        tagAliasBean.setAction(TagAliasOperatorHelper.ACTION_SET);
        tagAliasBean.setAlias(Account.getPhone());
        tagAliasBean.setAliasAction(true);
        TagAliasOperatorHelper.getInstance().handleAction(getApplicationContext(), 0, tagAliasBean);
    }

    private void initAllGroups() {
        mRecyclerGroup.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerGroup.setAdapter(mGroupAdapter = new RecyclerAdapter<GroupRspModel.ListBean>() {
            @Override
            protected int getItemViewType(int position, GroupRspModel.ListBean listBean) {
                return R.layout.item_groups;
            }

            @Override
            protected ViewHolder<GroupRspModel.ListBean> onCreateViewHolder(View root, int viewType) {
                return new GroupViewHolder(root);
            }
        });
        mPresenter.getAllGroups(Account.getToken(), "0", "0");
    }

    private void initAllEquipments() {
        mRecyclerDefault.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerDefault.setAdapter(mAdapter = new RecyclerAdapter<EquipmentCard>() {
            @Override
            protected int getItemViewType(int position, EquipmentCard equipmentCard) {
                return R.layout.item_equipment;
            }

            @Override
            protected ViewHolder<EquipmentCard> onCreateViewHolder(View root, int viewType) {
                return new MainActivity.ViewHolder(root);
            }
        });

        mAdapter.setListener(new RecyclerAdapter.AdapterListenerImpl<EquipmentCard>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder holder, EquipmentCard equipmentCard) {
                super.onItemClick(holder, equipmentCard);
                List<EquipmentRspModel.ListBean> listBeans = Account.getListBeans();
                for (EquipmentRspModel.ListBean bean : listBeans) {
                    if (bean.getEquipName().equals(equipmentCard.getEquipName())){
                        String url = Common.Constance.H5_BASE + "product.html?id=" + bean.getId();
                        PlantingDateAct.show(mName.getContext(), url, bean);
                    }
                }
            }
        });


        mCbDefaultEquipments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCbDefaultEquipments.isChecked()) {
                    mRecyclerDefault.setVisibility(View.GONE);
                    mDivideAllEquipments.setVisibility(View.VISIBLE);
                } else {
                    mRecyclerDefault.setVisibility(View.VISIBLE);
                    if (mAdapter.getItemCount() > 0) {
                        mDivideAllEquipments.setVisibility(View.GONE);
                    }
                }
            }
        });


//        page = 1;
        mPresenter.getDefaultEquipments(Account.getToken());
    }


    @OnClick(R.id.tv_edit_personal_data)
    void onEditPersonalClick() {
        EditPersonalDataActivity.show(this);
    }


    @OnClick(R.id.frame_group_manager)
    void onGroupManagerClick() {
        GroupManagerActivity.show(this);
    }


//    @OnClick(R.id.frame_shopping)
//    void onShoppingClick() {
//        ShopActivity.show(this);
//        hideLeft();
//    }

    public void showLeft() {
        mDrawerLayout.openDrawer(GravityCompat.START);
    }

    public void hideLeft() {
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {     //KEYCODE_BACK：回退键
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 2000) {
                Application.showToast("再按一次退出程序");
                firstTime = System.currentTimeMillis();
                return true;
            } else {
                ActivityUtil.finishActivity();
            }
        }
        return super.onKeyUp(keyCode, event);
    }

//    @Override
//    public void getAllEquipmentsSuccess(List<EquipmentRspModel.ListBean> listBeans) {
//        mAdapter.replace(listBeans);
//        if (mAdapter.getItems().size() == 0) {
//            mRecyclerDefault.setVisibility(View.GONE);
//            mDivideAllEquipments.setVisibility(View.VISIBLE);
//        }
//    }

    @Override
    public void getAllGroupsSuccess(List<GroupRspModel.ListBean> listBeans) {
        mGroupAdapter.replace(listBeans);
        if (listBeans != null && listBeans.size() > 0) {
            mRecyclerGroup.setVisibility(View.VISIBLE);
        } else {
            mRecyclerGroup.setVisibility(View.GONE);
        }


    }

    @Override
    public void getEquipmentConfigSuccess(final EquipmentConfigModel equipmentConfigModel) {
        if (!LongToothUtil.isInitSuccess) {
            LongToothUtil.longToothInit();
        }

    }

    @Override
    public void getEquipmentConfigFailed() {

        if (!LongToothUtil.isInitSuccess) {
            LongToothUtil.longToothInit();
        }
    }

    @Override
    public void checkVersionSuccess(VersionInfoModel model) {
        if (model != null) {
            String version = model.getVersion();
            if (!TextUtils.isEmpty(version)) {
                new UpdateManager(this).checkUpdate(model);
            }
        }
    }

    @Override
    public void getDefaultEquipmentsSuccess(List<EquipmentCard> equipmentCards) {
        mAdapter.replace(equipmentCards);
        if (mAdapter.getItems().size() == 0) {
            mRecyclerDefault.setVisibility(View.GONE);
            mDivideAllEquipments.setVisibility(View.VISIBLE);
        }
    }


    @Override
    protected MainActivityContract.Presenter initPresenter() {
        return new MainActivityPresenter(this);
    }

    class ViewHolder extends RecyclerAdapter.ViewHolder<EquipmentCard> {
        @BindView(R.id.tv_equipment_name)
        TextView mName;

        public ViewHolder(View itemView) {
            super(itemView);
        }


        @Override
            protected void onBind(EquipmentCard equipmentCard) {
            mName.setText(equipmentCard.getEquipName());
        }
    }

}
