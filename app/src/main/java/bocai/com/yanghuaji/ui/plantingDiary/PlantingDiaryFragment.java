package bocai.com.yanghuaji.ui.plantingDiary;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.Fragment;
import butterknife.OnClick;

/**
 * 作者 yuanfei on 2017/11/9.
 * 邮箱 yuanfei221@126.com
 */

public class PlantingDiaryFragment extends Fragment {



    public static PlantingDiaryFragment newInstance() {
        return new PlantingDiaryFragment();
    }


    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_planting_diary;
    }

    @OnClick(R.id.bt_add_diary)
    void onGoToAddClick(){
        AddDiaryActivity.show(getContext());
    }

}
