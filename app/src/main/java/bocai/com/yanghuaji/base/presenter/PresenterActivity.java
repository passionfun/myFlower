package bocai.com.yanghuaji.base.presenter;

import bocai.com.yanghuaji.base.Activity;
import bocai.com.yanghuaji.base.Application;

/**
 * 作者 yuanfei on 2017/11/15.
 * 邮箱 yuanfei221@126.com
 */

public abstract class PresenterActivity<Presenter extends BaseContract.Presenter> extends Activity
        implements BaseContract.View<Presenter> {
    protected Presenter mPresenter;

    @Override
    protected void initBefore() {
        super.initBefore();
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
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null){
            mPresenter.destroy();
        }
    }
}
