package bocai.com.yanghuaji.ui.intelligentPlanting;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.List;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.presenter.PresenterActivity;
import bocai.com.yanghuaji.model.GroupRspModel;
import bocai.com.yanghuaji.presenter.intelligentPlanting.EquipmentSettingContract;
import bocai.com.yanghuaji.presenter.intelligentPlanting.EquipmentSettingPresenter;
import bocai.com.yanghuaji.util.ActivityUtil;
import bocai.com.yanghuaji.util.persistence.Account;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者 yuanfei on 2017/11/15.
 * 邮箱 yuanfei221@126.com
 */

public class EquipmentSettingActivity extends PresenterActivity<EquipmentSettingContract.Presenter>
        implements EquipmentSettingContract.View {
    @BindView(R.id.scroll_root)
    ScrollView mRoot;

    @BindView(R.id.tv_title)
    TextView mTitle;

    @BindView(R.id.img_back)
    ImageView mImgBack;

    @BindView(R.id.tv_group)
    TextView mGroupName;

    private String mName;

    //显示的入口
    public static void show(Context context) {
        context.startActivity(new Intent(context, EquipmentSettingActivity.class));
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_equipment_setting;
    }

    @OnClick(R.id.img_back)
    void onBackClick() {
        finish();
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        mTitle.setText("种植机设置");
    }


    @OnClick(R.id.tv_group)
    void onGroupClick() {
        mPresenter.getGroupList(Account.getToken());
    }

    @Override
    public void getGroupListSuccess(List<GroupRspModel.ListBean> groupCards) {
            GroupListPopupWindow popupWindow = new GroupListPopupWindow(this);
            ActivityUtil.setBackgroundAlpha(this, 0.19f);
            popupWindow.addData(groupCards);
            popupWindow.showAtLocation(mRoot, Gravity.CENTER, 0, 0);
            popupWindow.setOnSelectListener(new GroupListPopupWindow.SelectListener() {
                @Override
                public void selected(GroupRspModel.ListBean card) {
                    mName = card.getGroupName();
                    mGroupName.setText(mName);
                }
            });
    }

    @Override
    protected EquipmentSettingContract.Presenter initPresenter() {
        return new EquipmentSettingPresenter(this);
    }
}
