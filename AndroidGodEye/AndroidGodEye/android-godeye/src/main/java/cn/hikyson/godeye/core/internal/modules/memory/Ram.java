package cn.hikyson.godeye.core.internal.modules.memory;


import cn.hikyson.godeye.core.GodEye;
import cn.hikyson.godeye.core.internal.Install;
import cn.hikyson.godeye.core.internal.ProduceableSubject;
import cn.hikyson.godeye.core.utils.L;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

/**

 * Created by kysonchao on 2017/11/22.
 */
public class Ram extends ProduceableSubject<RamInfo> implements Install<RamConfig> {
	Ram(){}//FDS fix at least one constructor
    private RamEngine mRamEngine;
    private RamConfig mConfig;

    @Override
    public synchronized boolean install(final RamConfig config) {//FDS fix method arg could be final
        if (mRamEngine != null) {
            L.d("Ram already installed, ignore.");
            return true;
        }
        mConfig = config;
        mRamEngine = new RamEngine(GodEye.instance().getApplication(), this, config.intervalMillis());
        mRamEngine.work();
        L.d("Ram installed.");
        return true;
    }

    @Override
    public synchronized void uninstall() {
        if (mRamEngine == null) {
            L.d("Ram already uninstalled, ignore.");
            return;
        }
        mConfig = null;
        mRamEngine.shutdown();
        mRamEngine = null;
        L.d("Ram uninstalled.");
    }


    @Override
    public synchronized boolean isInstalled() {
        return mRamEngine != null;
    }

    @Override
    public RamConfig config() {
        return mConfig;
    }

    @Override
    protected Subject<RamInfo> createSubject() {
        return BehaviorSubject.create();
    }
}
