package bocai.com.yanghuaji.base.presenter;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;

import bocai.com.yanghuaji.base.Application;
import bocai.com.yanghuaji.base.Fragment;
import bocai.com.yanghuaji.util.UiTool;

/**
 * 作者 yuanfei on 2017/11/15.
 * 邮箱 yuanfei221@126.com
 */

public abstract class PrensterFragment<Presenter extends BaseContract.Presenter> extends Fragment implements BaseContract.View<Presenter> {
//    private QMUITipDialog mProgressDialog;
//    protected ProgressDialog dialog;
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
        hideLoading();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.destroy();
        }
    }

    @Override
    public void onConnectionConflict() {
        UiTool.onConnectionConflict(getContext());
    }

    Dialog dialog;
    @Override
    public void showLoading() {
        dialog  = UiTool.createLoadingDialog(getContext());
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }



    @Override
    public void hideLoading() {
        if (dialog!=null&&dialog.isShowing()){
            dialog.dismiss();
        }
    }
}
