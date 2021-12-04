package cn.hikyson.godeye.core.internal.modules.thread;

import java.util.List;

import cn.hikyson.godeye.core.internal.Install;
import cn.hikyson.godeye.core.internal.ProduceableSubject;
import cn.hikyson.godeye.core.utils.L;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by kysonchao on 2018/1/14.
 */
public class ThreadDump extends ProduceableSubject<List<ThreadInfo>> implements Install<ThreadConfig> {
	ThreadDump(){}//FDS fix at least one constructor
	private ThreadEngine mThreadEngine;
    private ThreadConfig mConfig;

    @Override
    public synchronized boolean install(final ThreadConfig config) {//FDS fix method arg could be final lines 19,28
        if (mThreadEngine != null) {
            L.d("ThreadDump already installed, ignore.");
            return true;
        }
        mConfig = config;
        mThreadEngine = new ThreadEngine(this, config());
        mThreadEngine.work();
        L.d("ThreadDump installed.");
        return true;
    }

    @Override
    public synchronized void uninstall() {
        if (mThreadEngine == null) {
            L.d("ThreadDump already uninstalled, ignore.");
            return;
        }
        mConfig = null;
        mThreadEngine.shutdown();
        mThreadEngine = null;
        L.d("ThreadDump uninstalled.");
    }

    @Override
    public synchronized boolean isInstalled() {
        return mThreadEngine != null;
    }

    @Override
    public ThreadConfig config() {
        return mConfig;
    }

    @Override
    protected Subject<List<ThreadInfo>> createSubject() {
        return BehaviorSubject.create();
    }
}
