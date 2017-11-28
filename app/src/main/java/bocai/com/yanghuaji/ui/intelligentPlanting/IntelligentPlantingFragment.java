package bocai.com.yanghuaji.ui.intelligentPlanting;

import android.content.Context;
import android.support.v4.app.FragmentTransaction;
import android.widget.Button;
import android.widget.ImageView;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.Fragment;
import bocai.com.yanghuaji.ui.main.MainActivity;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者 yuanfei on 2017/11/9.
 * 邮箱 yuanfei221@126.com
 */

public class IntelligentPlantingFragment extends Fragment {
    private MainActivity mMainActivity;
    @BindView(R.id.img_show_left)
    ImageView mImgShowLeft;

    @BindView(R.id.bt_go_to_add)
    Button mBtGoToAdd;

    private HorizontalRecyclerFragment mHorizontalFragment;
    private VeticalRecyclerFragment mVerticalFragment;
    private boolean isHorizontal = true;


    public static IntelligentPlantingFragment newInstance() {
        return new IntelligentPlantingFragment();
    }

    public void switchType(){
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        isHorizontal = !isHorizontal;
        if (isHorizontal){
            transaction.replace(R.id.container,mHorizontalFragment).commit();
        }else {
            if (mVerticalFragment ==null){
                mVerticalFragment = new VeticalRecyclerFragment();
            }
            transaction.replace(R.id.container,mVerticalFragment).commit();
        }
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_intelligent_planting;
    }

    @Override
    protected void initData() {
        super.initData();
        mHorizontalFragment = new HorizontalRecyclerFragment();
        FragmentTransaction mTransaction = getChildFragmentManager().beginTransaction();
        mTransaction.replace(R.id.container,mHorizontalFragment).commit();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMainActivity = (MainActivity) getActivity();
    }

    @OnClick(R.id.img_show_left)
    void onShowLeftClick(){
        mMainActivity.showLeft();
    }

    @OnClick(R.id.bt_go_to_add)
    void onGoToAddClick(){
        AddEquipmentActivity.show(getContext());
    }



}
