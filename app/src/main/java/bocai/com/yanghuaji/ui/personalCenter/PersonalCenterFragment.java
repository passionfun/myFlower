package bocai.com.yanghuaji.ui.personalCenter;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.Fragment;
import bocai.com.yanghuaji.base.GlideApp;
import bocai.com.yanghuaji.model.db.User;
import bocai.com.yanghuaji.util.ActivityUtil;
import bocai.com.yanghuaji.util.persistence.Account;
import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 个人中心
 * 作者 yuanfei on 2017/11/9.
 * 邮箱 yuanfei221@126.com
 */

public class PersonalCenterFragment extends Fragment {
    @BindView(R.id.ll_root)
    LinearLayout mRootLayout;

    @BindView(R.id.tv_name)
    TextView mName;

    @BindView(R.id.img_portrait)
    CircleImageView mPortrait;


    public static PersonalCenterFragment newInstance() {
        return new PersonalCenterFragment();
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_personal_center;
    }


    @Override
    public void onResume() {
        super.onResume();
        User user = Account.getUser();
        if (user != null){
            if (!TextUtils.isEmpty(user.getNickName()))
            mName.setText(user.getNickName());
            GlideApp.with(getActivity())
                    .load(user.getRelativePath())
                    .placeholder(R.mipmap.img_portrait_empty)
                    .centerCrop()
                    .into(mPortrait);
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
        final SexSelectPopupWindow popupWindow = new SexSelectPopupWindow(getActivity());
        popupWindow.setTextTop("确定");
        popupWindow.setTextBottom("取消");
        popupWindow.setOnTtemClickListener(new SexSelectPopupWindow.ItemClickListener() {
            @Override
            public void onItemClick(View view) {
                switch (view.getId()) {
                    case R.id.tv_take_photo:
                        //确定
                        popupWindow.dismiss();
                        ActivityUtil.finishActivity();
                        break;
                    case R.id.tv_from_gallery:
                        // 取消
                        popupWindow.dismiss();
                        break;
                }
            }
        });
        ActivityUtil.setBackgroundAlpha(getActivity(), 0.19f);
        popupWindow.showAtLocation(mRootLayout, Gravity.CENTER, 0, 0);


    }


}
