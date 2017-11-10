package bocai.com.yanghuaji.ui.personalCenter;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.Fragment;
import bocai.com.yanghuaji.util.ActivityUtil;
import butterknife.OnClick;

/**
 * 个人中心
 * 作者 yuanfei on 2017/11/9.
 * 邮箱 yuanfei221@126.com
 */

public class PersonalCenterFragment extends Fragment {



    public static PersonalCenterFragment newInstance() {
        return new PersonalCenterFragment();
    }
    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_personal_center;
    }


    @OnClick(R.id.tv_modify_password)
    void onModifyPasswordClick(){
        ModifyPasswordActivity.show(getContext());
    }

    @OnClick(R.id.tv_my_planting_record)
    void onPlantingRecordClick(){
        PlantingRecordActivity.show(getContext());
    }

    @OnClick(R.id.tv_edit_personal_data)
    void onEditPersonalClick(){
        EditPersonalDataActivity.show(getActivity());
    }

    @OnClick(R.id.tv_system_notification)
    void onSystemNotificationClick(){
        SystemNotificationActivity.show(getActivity());
    }

    @OnClick(R.id.tv_setting)
    void onSettingClick(){
        SettingActivity.show(getActivity());
    }

    //退出应用
    @OnClick(R.id.tv_exit)
    void onExitClick(){
        ActivityUtil.finishActivity();
    }



}
