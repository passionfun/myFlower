package bocai.com.yanghuaji.ui.intelligentPlanting;

import android.content.Context;
import android.support.v4.app.FragmentTransaction;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.Fragment;
import bocai.com.yanghuaji.model.MessageEvent;
import bocai.com.yanghuaji.ui.main.MainActivity;
import bocai.com.yanghuaji.util.UiTool;
import butterknife.BindView;
import butterknife.OnClick;

import static bocai.com.yanghuaji.ui.intelligentPlanting.HorizontalRecyclerFragment.HORIZONTALRECYLER_VISIABLE;
import static bocai.com.yanghuaji.ui.intelligentPlanting.VeticalRecyclerFragment.VERTICALRECYCLER_VISIABLE;

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

    @BindView(R.id.tv_Equipment)
    TextView mEquipment;

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
            EventBus.getDefault().postSticky(new MessageEvent(HORIZONTALRECYLER_VISIABLE));
            if (mVerticalFragment!=null){
                transaction.hide(mVerticalFragment);
            }
            if (mHorizontalFragment==null){
                mHorizontalFragment = new HorizontalRecyclerFragment();
                transaction.add(R.id.container,mHorizontalFragment);
            }else {
                transaction.show(mHorizontalFragment);
            }
        }else {
            EventBus.getDefault().postSticky(new MessageEvent(VERTICALRECYCLER_VISIABLE));
            if (mHorizontalFragment!=null){
                transaction.hide(mHorizontalFragment);
            }
            if (mVerticalFragment ==null){
                mVerticalFragment = new VeticalRecyclerFragment();
                transaction.add(R.id.container,mVerticalFragment);
            }else {
                transaction.show(mVerticalFragment);
            }
        }
//        if (isHorizontal){
//            if (mHorizontalFragment==null) {
//                mHorizontalFragment = new HorizontalRecyclerFragment();
//            }
//            transaction.replace(R.id.container,mHorizontalFragment);
//        }else {
//
//        }
        transaction.commit();
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_intelligent_planting;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (mHorizontalFragment!=null){
            mHorizontalFragment.onHiddenChanged(hidden);
        }
        if (mVerticalFragment!=null){
            mVerticalFragment.onHiddenChanged(hidden);
        }
    }

    @Override
    protected void initData() {
        super.initData();
        UiTool.setBlod(mEquipment);
        mHorizontalFragment = new HorizontalRecyclerFragment();
        FragmentTransaction mTransaction = getChildFragmentManager().beginTransaction();
        mTransaction.add(R.id.container,mHorizontalFragment).commit();
        EventBus.getDefault().postSticky(new MessageEvent(HORIZONTALRECYLER_VISIABLE));
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
