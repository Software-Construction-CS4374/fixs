package cn.hikyson.godeye.core.internal;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by kysonchao on 2017/11/23.
 */
public class ProduceableSubject<T> implements SubjectSupport<T>, Producer<T> {
	private final Subject<T> mSubject;//FDS fix immutable fields

    public ProduceableSubject() {
        mSubject = createSubject();
    }

    protected Subject<T> createSubject() {
        return PublishSubject.create();
    }

    @Override
    public void produce(final T data) {//FDS fix method arg could be final
        mSubject.onNext(data);
    }

    @Override
    public Observable<T> subject() {
        return mSubject;
    }
}
