package cn.hikyson.godeye.core.internal.modules.startup;

import cn.hikyson.godeye.core.internal.Install;
import cn.hikyson.godeye.core.internal.ProduceableSubject;
import cn.hikyson.godeye.core.utils.L;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by kysonchao on 2017/11/23.
 */
public class Startup extends ProduceableSubject<StartupInfo> implements Install<StartupConfig> {
	Startup(){}//FDS fix at least one constructor
    private StartupConfig mConfig;

    @Override
    public synchronized boolean install(final StartupConfig config) {//FDS fix method arg could be final line 19 and 53
        if (config == null) {
            throw new IllegalArgumentException("Startup module install fail because config is null.");
        }
        if (mConfig != null) {
            L.d("Startup already installed, ignore.");
            return true;
        }
        mConfig = config;
        L.d("Startup installed.");
        return true;
    }

    @Override
    public synchronized void uninstall() {
        if (mConfig == null) {
            L.d("Startup already uninstalled, ignore.");
            return;
        }
        mConfig = null;
        L.d("Startup uninstall.");
    }

    @Override
    public synchronized boolean isInstalled() {
        return mConfig != null;
    }

    @Override
    public StartupConfig config() {
        return mConfig;
    }

    @Override
    public void produce(final StartupInfo data) {
        if (mConfig == null) {
            L.d("Startup is not installed, produce data fail.");
            return;
        }
        super.produce(data);
    }

    @Override
    protected Subject<StartupInfo> createSubject() {
        return BehaviorSubject.create();
    }
}
