package bocai.com.yanghuaji.base.presenter;

import android.app.Dialog;

import bocai.com.yanghuaji.base.Activity;
import bocai.com.yanghuaji.base.Application;
import bocai.com.yanghuaji.util.UiTool;

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
        hideLoading();
        Application.showToast(str);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null){
            mPresenter.destroy();
        }

        UiTool.closeConflictDialog();
    }
    Dialog dialog;
    @Override
    public void showLoading() {
        dialog  = UiTool.createLoadingDialog(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    @Override
    public void onConnectionConflict() {
        UiTool.onConnectionConflict(this);
    }

    @Override
    public void hideLoading() {
        if (dialog!=null&&dialog.isShowing()){
            dialog.dismiss();
        }
    }
}
