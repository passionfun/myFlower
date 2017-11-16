package bocai.com.yanghuaji.base.presenter;

import android.app.ProgressDialog;

import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import bocai.com.yanghuaji.base.Activity;
import bocai.com.yanghuaji.base.Application;

/**
 * 作者 yuanfei on 2017/11/15.
 * 邮箱 yuanfei221@126.com
 */

public abstract class PresenterActivity<Presenter extends BaseContract.Presenter> extends Activity
        implements BaseContract.View<Presenter> {
    private QMUITipDialog mProgressDialog;
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

        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }

        if (mPresenter != null){
            mPresenter.destroy();
        }
    }
    ProgressDialog dialog;
    @Override
    public void showLoading() {
//        if (mProgressDialog == null) {
//            mProgressDialog = new QMUITipDialog.Builder(this)
//                    .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
//                    .setTipWord("正在加载")
//                    .create();
//            mProgressDialog.setCancelable(true);
//            mProgressDialog.show();
//        } else if (!mProgressDialog.isShowing()) {
//            mProgressDialog.show();
//        }
        dialog  = new ProgressDialog(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }



    @Override
    public void hideLoading() {
        if (dialog!=null&&dialog.isShowing()){
            dialog.dismiss();
        }
//        if (mProgressDialog != null && mProgressDialog.isShowing()) {
//            mProgressDialog.dismiss();
//        }
    }
}
