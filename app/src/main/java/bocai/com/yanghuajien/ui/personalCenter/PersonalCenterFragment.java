package bocai.com.yanghuajien.ui.personalCenter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import bocai.com.yanghuajien.R;
import boc.com.imgselector.GlideApp;
import bocai.com.yanghuajien.base.Application;
import bocai.com.yanghuajien.base.presenter.PrensterFragment;
import bocai.com.yanghuajien.model.MessageEvent;
import bocai.com.yanghuajien.model.NoticeStatusRspModel;
import bocai.com.yanghuajien.model.PersonalCenterPresenter;
import bocai.com.yanghuajien.model.db.User;
import bocai.com.yanghuajien.presenter.personalCenter.PersonalCenterContract;
import bocai.com.yanghuajien.ui.account.LoginActivity;
import bocai.com.yanghuajien.util.persistence.Account;
import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 个人中心
 * 作者 yuanfei on 2017/11/9.
 * 邮箱 yuanfei221@126.com
 */

public class PersonalCenterFragment extends PrensterFragment<PersonalCenterContract.Presenter>
        implements PersonalCenterContract.View {
    @BindView(R.id.ll_root)
    LinearLayout mRootLayout;

    @BindView(R.id.tv_name)
    TextView mName;

    @BindView(R.id.img_portrait)
    CircleImageView mPortrait;

    @BindView(R.id.tv_system_notification)
    TextView mNoticeStatus;

    public static final String REFRESH_NOTICE_STATUS="REFRESH_NOTICE_STATUS";


    public static PersonalCenterFragment newInstance() {
        return new PersonalCenterFragment();
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_personal_center;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fresh(MessageEvent messageEvent) {
        if (messageEvent.getMessage().equals(REFRESH_NOTICE_STATUS)) {
            mPresenter.getNoticeStatus(Account.getToken());
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden){
            mPresenter.getNoticeStatus(Account.getToken());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        User user = Account.getUser();
        String emile = Account.getEmile()==null?"":Account.getEmile();
        //String account = emile.substring(0,3)+"****"+emile.substring(7);
        if (user != null) {
            if (!TextUtils.isEmpty(user.getNickName())){
                mName.setText(user.getNickName());
            }else {
                mName.setText(emile);
            }
            GlideApp.with(getActivity())
                    .load(user.getRelativePath())
                    .placeholder(R.mipmap.img_portrait_empty)
                    .centerCrop()
                    .into(mPortrait);
        }else {
            mName.setText(emile);
        }
    }

    @OnClick(R.id.tv_modify_password)
    void onModifyPasswordClick() {
        ModifyPasswordActivity.show(getContext());
    }

    @OnClick(R.id.tv_my_planting_record)
    void onPlantingRecordClick() {
        PlantingRecordActivity.show(getContext());
    }

    @OnClick(R.id.tv_edit_personal_data)
    void onEditPersonalClick() {
        EditPersonalDataActivity.show(getActivity());
    }

    @OnClick(R.id.tv_system_notification)
    void onSystemNotificationClick() {
        SystemNotificationActivity.show(getActivity());
    }

    @OnClick(R.id.tv_setting)
    void onSettingClick() {
        SettingActivity.show(getActivity());
    }

    //退出应用
    @OnClick(R.id.tv_exit)
    void onExitClick() {
        AlertDialog.Builder deleteDialog = new AlertDialog.Builder(getContext());
        deleteDialog.setTitle(Application.getStringText(R.string.ensure_logout));
        deleteDialog.setPositiveButton(getResources().getString(R.string.ensure), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //确定
                Account.logOff(getContext());
                LoginActivity.show(getContext());
                getActivity().finish();
            }
        });
        deleteDialog.setNegativeButton(getResources().getString(R.string.cancel),null);
        deleteDialog.show();
    }


    @Override
    public void getNoticeStatusSuccess(NoticeStatusRspModel noticeStatusRspModel) {
        //0无新消息   1有新消息
        if (noticeStatusRspModel.getStatus().equals("0")){
            mNoticeStatus.setCompoundDrawablesRelativeWithIntrinsicBounds(R.mipmap.img_no_message,0,R.mipmap.img_personal_center_arrow,0);
        }else {
            mNoticeStatus.setCompoundDrawablesRelativeWithIntrinsicBounds(R.mipmap.img_system_notification_new,0,R.mipmap.img_personal_center_arrow,0);
        }
    }

    @Override
    protected PersonalCenterContract.Presenter initPresenter() {
        return new PersonalCenterPresenter(this);
    }
}
