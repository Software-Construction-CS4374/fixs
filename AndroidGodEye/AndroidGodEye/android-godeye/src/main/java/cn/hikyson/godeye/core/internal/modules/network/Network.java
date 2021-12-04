package cn.hikyson.godeye.core.internal.modules.network;

import cn.hikyson.godeye.core.internal.Install;
import cn.hikyson.godeye.core.internal.ProduceableSubject;
import cn.hikyson.godeye.core.utils.L;
import io.reactivex.subjects.ReplaySubject;
import io.reactivex.subjects.Subject;

/**
 * Created by kysonchao on 2017/11/22.
 */
public class Network extends ProduceableSubject<NetworkInfo> implements Install<NetworkConfig> {
	Network(){}//FDS fix at least one constructor
    private NetworkConfig mConfig;

    @Override
    public synchronized boolean install(final NetworkConfig config) {//FDS fix method arg could be final line 17 and 51
        if (config == null) {
            throw new IllegalArgumentException("Network module install fail because config is null.");
        }
        if (mConfig != null) {
            L.d("Network already installed, ignore.");
            return true;
        }
        mConfig = config;
        L.d("Network installed.");
        return true;
    }

    @Override
    public synchronized void uninstall() {
        if (mConfig == null) {
            L.d("Network already uninstalled, ignore.");
            return;
        }
        mConfig = null;
        L.d("Network uninstalled.");
    }

    @Override
    public synchronized boolean isInstalled() {
        return mConfig != null;
    }

    @Override
    public NetworkConfig config() {
        return mConfig;
    }

    @Override
    public void produce(final NetworkInfo data) {
        if (mConfig == null) {
            L.d("Network is not installed, produce data fail.");
            return;
        }
        super.produce(data);
    }

    @Override
    protected Subject<NetworkInfo> createSubject() {
        return ReplaySubject.create();
    }
}
