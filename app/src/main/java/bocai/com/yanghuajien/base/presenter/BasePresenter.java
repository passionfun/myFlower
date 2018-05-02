package bocai.com.yanghuajien.base.presenter;


public class BasePresenter<T extends BaseContract.View> implements BaseContract.Presenter {
    private T mView;

    public BasePresenter(T view) {
        setView(view);
    }


    @SuppressWarnings("unchecked")
    protected void setView(T view) {
        this.mView = view;
        this.mView.setPresenter(this);
    }


    protected final T getView() {
        return mView;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void destroy() {
        T view = mView;
        mView = null;
        if (view != null) {
            // 把Presenter设置为NULL
            view.setPresenter(null);
        }
    }
}
