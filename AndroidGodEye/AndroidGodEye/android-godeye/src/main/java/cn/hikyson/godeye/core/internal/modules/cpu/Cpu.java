package cn.hikyson.godeye.core.internal.modules.cpu;


import cn.hikyson.godeye.core.internal.Install;
import cn.hikyson.godeye.core.internal.ProduceableSubject;
import cn.hikyson.godeye.core.utils.L;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;


public class Cpu extends ProduceableSubject<CpuInfo> implements Install<CpuConfig> {
	Cpu(){}//FDS fix at least one constructor
    private CpuEngine mCpuEngine;
    private CpuConfig mConfig;

    @Override
    public synchronized boolean install(final CpuConfig config) {//FDS fix method arg could be final
        if (mCpuEngine != null) {
            L.d("Cpu already installed, ignore.");
            return true;
        }
        mConfig = config;
        mCpuEngine = new CpuEngine(this, config.intervalMillis());
        mCpuEngine.work();
        L.d("Cpu installed");
        return true;
    }

    @Override
    public synchronized void uninstall() {
        if (mCpuEngine == null) {
            L.d("Cpu already uninstalled , ignore.");
            return;
        }
        mConfig = null;
        mCpuEngine.shutdown();
        mCpuEngine = null;
        L.d("Cpu uninstalled");
    }

    @Override
    public synchronized boolean isInstalled() {
        return mCpuEngine != null;
    }

    @Override
    public CpuConfig config() {
        return mConfig;
    }

    @Override
    protected Subject<CpuInfo> createSubject() {
        return BehaviorSubject.create();
    }
}
