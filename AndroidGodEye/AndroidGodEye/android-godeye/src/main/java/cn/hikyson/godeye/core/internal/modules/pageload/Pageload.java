package cn.hikyson.godeye.core.internal.modules.pageload;

import android.app.Activity;
import android.os.Handler;

import androidx.fragment.app.Fragment;

import cn.hikyson.godeye.core.internal.Install;
import cn.hikyson.godeye.core.internal.ProduceableSubject;
import cn.hikyson.godeye.core.utils.L;
import cn.hikyson.godeye.core.utils.ThreadUtil;
import io.reactivex.subjects.ReplaySubject;
import io.reactivex.subjects.Subject;

/**
 */
public class Pageload extends ProduceableSubject<PageLifecycleEventInfo> implements Install<PageloadConfig> {
    private static final String PAGELOAD_HANDLER = "godeye-pageload";
    private ActivityLifecycleCallbacks mActivityLifecycleCallbacks;
    private PageloadConfig mConfig;
    private boolean mInstalled = false;

    @Override
    public synchronized boolean install(final PageloadConfig config) {
        if (mInstalled) {
            L.d("Pageload already installed, ignore.");
            return true;
        }
        this.mConfig = config;
        PageInfoProvider pageInfoProvider = new DefaultPageInfoProvider();
        try {
            pageInfoProvider = (PageInfoProvider) Class.forName(this.mConfig.pageInfoProvider()).newInstance();
        } catch (Throwable e) {
            L.e("Pageload install warning, can not find pageload provider class. use DefaultPageInfoProvider:" + e);
        }
        Handler handler = ThreadUtil.createIfNotExistHandler(PAGELOAD_HANDLER);
        this.mActivityLifecycleCallbacks = new ActivityLifecycleCallbacks(new PageLifecycleRecords(), pageInfoProvider, this, handler);
        this.mActivityLifecycleCallbacks.work();
        this.mInstalled = true;
        L.d("Pageload installed.");
        return true;
    }

    @Override
    protected Subject<PageLifecycleEventInfo> createSubject() {
        return ReplaySubject.create();
    }

    @Override
    public synchronized void uninstall() {
        if (!mInstalled) {
            L.d("Pageload already uninstalled, ignore.");
            return;
        }
        mActivityLifecycleCallbacks.shutdown();
        mActivityLifecycleCallbacks = null;
        ThreadUtil.destoryHandler(PAGELOAD_HANDLER);
        this.mInstalled = false;
        L.d("Pageload uninstalled.");
    }

    @Override
    public synchronized boolean isInstalled() {
        return mInstalled;
    }

    @Override
    public PageloadConfig config() {
        return mConfig;
    }

    public synchronized void onActivityLoad(final Activity activity) {
        if (mInstalled && mActivityLifecycleCallbacks != null) {
            mActivityLifecycleCallbacks.onActivityLoad(activity);
        }
    }

    public synchronized void onFragmentLoad(final android.app.Fragment f) {
        if (mInstalled && mActivityLifecycleCallbacks != null) {
            mActivityLifecycleCallbacks.onFragmentLoad(f);
        }
    }

    public synchronized void onFragmentV4Load(final Fragment f) {
        if (mInstalled && mActivityLifecycleCallbacks != null) {
            mActivityLifecycleCallbacks.onFragmentV4Load(f);
        }
    }

    public synchronized void onFragmentV4Show(final Fragment f) {
        if (mInstalled && mActivityLifecycleCallbacks != null) {
            mActivityLifecycleCallbacks.onFragmentV4Show(f);
        }
    }

    public synchronized void onFragmentV4Hide(final Fragment f) {
        if (mInstalled && mActivityLifecycleCallbacks != null) {
            mActivityLifecycleCallbacks.onFragmentV4Hide(f);
        }
    }

    public synchronized void onFragmentShow(final android.app.Fragment f) {
        if (mInstalled && mActivityLifecycleCallbacks != null) {
            mActivityLifecycleCallbacks.onFragmentShow(f);
        }
    }

    public synchronized void onFragmentHide(final android.app.Fragment f) {
        if (mInstalled && mActivityLifecycleCallbacks != null) {
            mActivityLifecycleCallbacks.onFragmentHide(f);
        }
    }

}
