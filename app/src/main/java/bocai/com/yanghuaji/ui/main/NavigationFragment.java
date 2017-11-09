package bocai.com.yanghuaji.ui.main;


import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.Fragment;
import bocai.com.yanghuaji.ui.intelligentPlanting.IntelligentPlantingFragment;
import bocai.com.yanghuaji.ui.personalCenter.PersonalCenterFragment;
import bocai.com.yanghuaji.ui.plantingDiary.PlantingDiaryFragment;
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

    @OnClick(R.id.tv_intelligent_planting)
    void onIntellingentPlantClick() {
        resetTabState();
        setTabState(mTvIntelligentPlanting, R.mipmap.img_intelligent_planting_selected, getColor(R.color.selected));
        switchFrgment(0);
    }

    @OnClick(R.id.tv_planting_diary)
    void onPlantingDiaryClick() {
        resetTabState();
        setTabState(mTvPlantingDiary, R.mipmap.img_planting_diary_selected, getColor(R.color.selected));
        switchFrgment(1);
    }

    @OnClick(R.id.tv_personal_center)
    void onPersonalCenterClick() {
        resetTabState();
        setTabState(mTvPersonalCenter, R.mipmap.img_persnal_center_selected, getColor(R.color.selected));
        switchFrgment(2);
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
            beginTransaction.detach(currentFragment);
        }
        switch (position) {
            case 0:
                if (mIntelligentPlantingFragment == null) {
                    mIntelligentPlantingFragment = IntelligentPlantingFragment.newInstance();
                    beginTransaction.add(R.id.sub_content, mIntelligentPlantingFragment);
                } else {
                    beginTransaction.attach(mIntelligentPlantingFragment);
                }
                currentFragment = mIntelligentPlantingFragment;
                break;
            case 1:
                if (mPlantingDiaryFragment == null) {
                    mPlantingDiaryFragment = PlantingDiaryFragment.newInstance();
                    beginTransaction.add(R.id.sub_content, mPlantingDiaryFragment);
                } else {
                    beginTransaction.attach(mPlantingDiaryFragment);
                }
                currentFragment = mPlantingDiaryFragment;
                break;
            case 2:
                if (mPersonalCenterFragment == null) {
                    mPersonalCenterFragment = PersonalCenterFragment.newInstance();
                    beginTransaction.add(R.id.sub_content, mPersonalCenterFragment);
                } else {
                    beginTransaction.attach(mPersonalCenterFragment);
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

}
