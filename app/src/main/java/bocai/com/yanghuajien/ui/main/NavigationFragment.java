package bocai.com.yanghuajien.ui.main;


import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import bocai.com.yanghuajien.R;
import bocai.com.yanghuajien.base.Fragment;
import bocai.com.yanghuajien.ui.intelligentPlanting.IntelligentPlantingFragment;
import bocai.com.yanghuajien.ui.personalCenter.PersonalCenterFragment;
import bocai.com.yanghuajien.ui.plantingDiary.PlantingDiaryFragment;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者 yuanfei on 2017/11/6.
 * 邮箱 yuanfei221@126.com
 */

public class NavigationFragment extends Fragment {
    private Fragment currentFragment;
    private IntelligentPlantingFragment mIntelligentPlantingFragment;
    private PlantingDiaryFragment mPlantingDiaryFragment;
    private PersonalCenterFragment mPersonalCenterFragment;

    @BindView(R.id.tv_intelligent_planting)
    TextView mTvIntelligentPlanting;

    @BindView(R.id.tv_planting_diary)
    TextView mTvPlantingDiary;

    @BindView(R.id.tv_personal_center)
    TextView mTvPersonalCenter;

    @BindView(R.id.tv_shop)
    TextView mTvShop;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_navigation;
    }


    public static NavigationFragment newInstance() {
        return new NavigationFragment();
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        setDefaultFragment();
    }

    @OnClick({R.id.tv_intelligent_planting, R.id.tv_planting_diary, R.id.tv_personal_center, R.id.tv_shop})
    void onTabClick(View view) {
        switch (view.getId()) {
            case R.id.tv_intelligent_planting:
                //智能种植
                resetTabState();
                setTabState(mTvIntelligentPlanting, R.mipmap.img_intelligent_planting_selected, getColor(R.color.selected));
                switchFrgment(0);
                break;
            case R.id.tv_planting_diary:
                //种植日记
                resetTabState();
                setTabState(mTvPlantingDiary, R.mipmap.img_planting_diary_selected, getColor(R.color.selected));
                switchFrgment(1);
                break;
            case R.id.tv_personal_center:
                //个人中心
                resetTabState();
                setTabState(mTvPersonalCenter, R.mipmap.img_persnal_center_selected, getColor(R.color.selected));
                switchFrgment(2);
                break;
            case R.id.tv_shop:
                //商城
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse("https://h5.youzan.com/v2/showcase/homepage?alias=1dnmgamr");
                intent.setData(content_url);
                startActivity(intent);
                break;
        }
    }

    private void setTabState(TextView textView, int image, int color) {
        textView.setCompoundDrawablesRelativeWithIntrinsicBounds(0, image, 0, 0);//Call requires API level 17
        textView.setTextColor(color);
    }

    private void resetTabState() {
        setTabState(mTvIntelligentPlanting, R.mipmap.img_intelligent_planting_nomal, getColor(R.color.normal));
        setTabState(mTvPlantingDiary, R.mipmap.img_planting_diary_nomal, getColor(R.color.normal));
        setTabState(mTvPersonalCenter, R.mipmap.img_persnal_center_nomal, getColor(R.color.normal));
    }

    private int getColor(int i) {
        return ContextCompat.getColor(getActivity(), i);
    }

    private void switchFrgment(int position) {
        FragmentTransaction beginTransaction = getFragmentManager().beginTransaction();
        if (currentFragment != null) {
//            beginTransaction.detach(currentFragment);
            beginTransaction.hide(currentFragment);
        }
        switch (position) {
            case 0:
                if (mIntelligentPlantingFragment == null) {
                    mIntelligentPlantingFragment = IntelligentPlantingFragment.newInstance();
                    beginTransaction.add(R.id.sub_content, mIntelligentPlantingFragment);
                } else {
//                    beginTransaction.attach(mIntelligentPlantingFragment);
                    beginTransaction.show(mIntelligentPlantingFragment);
                }
                currentFragment = mIntelligentPlantingFragment;
                break;
            case 1:
                if (mPlantingDiaryFragment == null) {
                    mPlantingDiaryFragment = PlantingDiaryFragment.newInstance();
                    beginTransaction.add(R.id.sub_content, mPlantingDiaryFragment);
                } else {
//                    beginTransaction.attach(mPlantingDiaryFragment);
                    beginTransaction.show(mPlantingDiaryFragment);
                }
                currentFragment = mPlantingDiaryFragment;
                break;
            case 2:
                if (mPersonalCenterFragment == null) {
                    mPersonalCenterFragment = PersonalCenterFragment.newInstance();
                    beginTransaction.add(R.id.sub_content, mPersonalCenterFragment);
                } else {
//                    beginTransaction.attach(mPersonalCenterFragment);
                    beginTransaction.show(mPersonalCenterFragment);
                }
                currentFragment = mPersonalCenterFragment;
                break;
        }
        beginTransaction.commit();
    }


    private void setDefaultFragment() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        currentFragment = mIntelligentPlantingFragment = IntelligentPlantingFragment.newInstance();
        transaction.add(R.id.sub_content, mIntelligentPlantingFragment).commit();
    }

    public void switchType() {
        mIntelligentPlantingFragment.switchType();
    }

}
