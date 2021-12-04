package cn.hikyson.godeye.core.helper;

import cn.hikyson.godeye.core.GodEye;
import cn.hikyson.godeye.core.exceptions.UnexpectException;
import cn.hikyson.godeye.core.exceptions.UninstallException;
import cn.hikyson.godeye.core.internal.SubjectSupport;
import cn.hikyson.godeye.core.utils.L;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class RxModule {

    public static <T> Observable<T> wrapThreadComputationObservable(final Observable<T> observable) {//FDS fix method arg could be final line 13 and 17
        return observable.subscribeOn(Schedulers.computation()).observeOn(Schedulers.computation());
    }

    public static <T> Observable<T> wrapThreadComputationObservable(final @GodEye.ModuleName String moduleName) {
        try {
            T module = GodEye.instance().getModule(moduleName);
            if (!(module instanceof SubjectSupport)) {
                throw new UnexpectException(moduleName + " is not instance of SubjectSupport.");
            }
            // noinspection unchecked
            return wrapThreadComputationObservable(((SubjectSupport<T>) module).subject());
        } catch (UninstallException e) {
            L.d(moduleName + " is not installed.");
            return Observable.empty();
        }
    }
}
