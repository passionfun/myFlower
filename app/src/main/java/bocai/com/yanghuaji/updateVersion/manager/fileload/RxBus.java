package bocai.com.yanghuaji.updateVersion.manager.fileload;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 *
 * Created by shc on 2018/1/4.
 */
public class RxBus {

    private static volatile RxBus mInstance;
    private final Subject<Object> mBus;

    private RxBus() {
        mBus = PublishSubject.create().toSerialized();
    }

    /**
     * 单例RxBus
     *
     * @return
     */
    public static RxBus getDefault() {
        RxBus rxBus = mInstance;
        if (mInstance == null) {
            synchronized (RxBus.class) {
                rxBus = mInstance;
                if (mInstance == null) {
                    rxBus = new RxBus();
                    mInstance = rxBus;
                }
            }
        }
        return rxBus;
    }

    /**
     * 发送一个新事件
     *
     * @param o
     */
    public void post(Object o) {
        mBus.onNext(o);
    }

    /**
     * 返回特定类型的被观察者
     *
     * @param eventType
     * @param <T>
     * @return
     */
    public <T> Observable<T> toObservable(Class<T> eventType) {
        return mBus.ofType(eventType);
    }

}
