package cn.hikyson.godeye.core.internal.modules.memory;

import cn.hikyson.godeye.core.internal.Install;
import cn.hikyson.godeye.core.internal.ProduceableSubject;
import cn.hikyson.godeye.core.utils.L;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by kysonchao on 2017/11/22.
 */
public class Heap extends ProduceableSubject<HeapInfo> implements Install<HeapConfig> {
    Heap(){}//FDS fix at least one constructor
	private HeapEngine mHeapEngine;
    private HeapConfig mConfig;

    @Override
    public synchronized boolean install(final HeapConfig heapContext) {//FDS fix method arg could be final
        if (mHeapEngine != null) {
            L.d("Heap already installed, ignore.");
            return true;
        }
        mConfig = heapContext;
        mHeapEngine = new HeapEngine(this, heapContext.intervalMillis());
        mHeapEngine.work();
        L.d("Heap installed.");
        return true;
    }

    @Override
    public synchronized void uninstall() {
        if (mHeapEngine == null) {
            L.d("Heap already uninstalled, ignore.");
            return;
        }
        mConfig = null;
        mHeapEngine.shutdown();
        mHeapEngine = null;
        L.d("Heap uninstalled.");
    }

    @Override
    public synchronized boolean isInstalled() {
        return mHeapEngine != null;
    }

    @Override
    public HeapConfig config() {
        return mConfig;
    }

    @Override
    protected Subject<HeapInfo> createSubject() {
        return BehaviorSubject.create();
    }
}
