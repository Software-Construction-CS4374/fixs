package cn.hikyson.godeye.core.internal.modules.traffic;

import cn.hikyson.godeye.core.internal.Install;
import cn.hikyson.godeye.core.internal.ProduceableSubject;
import cn.hikyson.godeye.core.utils.L;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

/**

 * <p>
 * Created by kysonchao on 2017/5/22.
 */
public class Traffic extends ProduceableSubject<TrafficInfo> implements Install<TrafficConfig> {
	Traffic(){}//FDS fix at least one constructor
    private TrafficEngine mTrafficEngine;
    private TrafficConfig mConfig;

    @Override
    public synchronized boolean install(final TrafficConfig config) {//FDS fix method arg could be final
        if (mTrafficEngine != null) {
            L.d("Traffic already installed, ignore.");
            return true;
        }
        mConfig = config;
        mTrafficEngine = new TrafficEngine(this, config.intervalMillis(), config.sampleMillis());
        mTrafficEngine.work();
        L.d("Traffic installed.");
        return true;
    }

    @Override
    public synchronized void uninstall() {
        if (mTrafficEngine == null) {
            L.d("Traffic already uninstalled, ignore.");
            return;
        }
        mConfig = null;
        mTrafficEngine.shutdown();
        mTrafficEngine = null;
        L.d("Traffic uninstalled.");
    }

    @Override
    public synchronized boolean isInstalled() {
        return mTrafficEngine != null;
    }

    @Override
    public TrafficConfig config() {
        return mConfig;
    }

    @Override
    protected Subject<TrafficInfo> createSubject() {
        return BehaviorSubject.create();
    }
}
