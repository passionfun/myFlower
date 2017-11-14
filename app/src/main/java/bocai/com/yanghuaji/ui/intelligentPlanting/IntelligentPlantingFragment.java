package bocai.com.yanghuaji.ui.intelligentPlanting;

import android.content.Context;
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

    public static IntelligentPlantingFragment newInstance() {
        return new IntelligentPlantingFragment();
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_intelligent_planting;
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
