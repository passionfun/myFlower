package bocai.com.yanghuaji.ui.personalCenter;

import bocai.com.yanghuaji.R;
import bocai.com.yanghuaji.base.Fragment;

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
}
