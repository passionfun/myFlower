package bocai.com.yanghuaji.base.presenter;

import android.support.annotation.StringRes;


public interface BaseContract {
    // 基本的界面职责
    interface View<T extends Presenter> {
        // 公共的：显示一个字符串错误
        void showError(@StringRes int str);

        void showLoading();

        void hideLoading();

        // 支持设置一个Presenter
        void setPresenter(T presenter);
    }

    // 基本的Presenter职责
    interface Presenter {

        // 共用的销毁触发
        void destroy();
    }

}
