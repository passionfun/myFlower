package bocai.com.yanghuaji.base.presenter;

import android.content.Context;

import bocai.com.yanghuaji.base.Application;
import bocai.com.yanghuaji.base.Fragment;

/**
 * 作者 yuanfei on 2017/11/15.
 * 邮箱 yuanfei221@126.com
 */

public abstract class PrensterFragment<Presenter extends BaseContract.Presenter> extends Fragment implements BaseContract.View<Presenter> {
    protected Presenter mPresenter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        initPresenter();
    }

    protected abstract Presenter initPresenter();

    @Override
    public void setPresenter(Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showError(int str) {
        Application.showToast(str);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.destroy();
        }
    }
}
