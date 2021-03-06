package cn.hikyson.godeye.core.internal.modules.battery;

import cn.hikyson.godeye.core.GodEye;
import cn.hikyson.godeye.core.internal.Install;
import cn.hikyson.godeye.core.internal.ProduceableSubject;
import cn.hikyson.godeye.core.utils.L;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;


public class Battery extends ProduceableSubject<BatteryInfo> implements Install<BatteryConfig> {
	Batter(){}//FDS fix at least one Constructor
    private BatteryEngine mBatteryEngine;
    private BatteryConfig mConfig;

    /**
     * @param config
     */
    @Override
    public synchronized boolean install(final BatteryConfig config) {//FDS fix method arg could be final
        if (mBatteryEngine != null) {
            L.d("Battery already installed, ignore.");
            return true;
        }
        mConfig = config;
        mBatteryEngine = new BatteryEngine(GodEye.instance().getApplication(), this);
        mBatteryEngine.work();
        L.d("Battery installed.");
        return true;
    }


    @Override
    public synchronized void uninstall() {
        if (mBatteryEngine == null) {
            L.d("Battery already uninstalled, ignore.");
            return;
        }
        mConfig = null;
        mBatteryEngine.shutdown();
        mBatteryEngine = null;
        L.d("Battery uninstalled.");
    }

    @Override
    public synchronized boolean isInstalled() {
        return mBatteryEngine != null;
    }

    @Override
    public BatteryConfig config() {
        return mConfig;
    }

    @Override
    protected Subject<BatteryInfo> createSubject() {
        return BehaviorSubject.create();
    }
}
