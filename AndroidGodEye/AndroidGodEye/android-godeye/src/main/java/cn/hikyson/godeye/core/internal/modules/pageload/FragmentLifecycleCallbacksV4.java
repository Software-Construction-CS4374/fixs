package cn.hikyson.godeye.core.internal.modules.pageload;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import cn.hikyson.godeye.core.utils.ViewUtil;

public class FragmentLifecycleCallbacksV4 extends FragmentManager.FragmentLifecycleCallbacks {
    private final ActivityLifecycleCallbacks mActivityLcCbs;//FDS fix long var for all with mActivityLifecycleCallbacks and immutable field

    FragmentLifecycleCallbacksV4(final ActivityLifecycleCallbacks activityLifecycleCallbacks) {//FDS fix method arg could be final
        mActivityLcCbs = activityLifecycleCallbacks;
    }

    @Override
    public void onFragmentAttached(final FragmentManagerfm, final Fragment f, Context context) {
        mActivityLcCbs.onFragmentV4LifecycleEvent(f, FragmentLifecycleEvent.ON_ATTACH, false);
    }

    @Override
    public void onFragmentCreated(final FragmentManagerfm, final Fragment f, Bundle savedInstanceState) {
        mActivityLcCbs.onFragmentV4LifecycleEvent(f, FragmentLifecycleEvent.ON_CREATE, false);
    }

    @Override
    public void onFragmentViewCreated(final FragmentManagerfm, final Fragment f, View v, Bundle savedInstanceState) {
        mActivityLcCbs.onFragmentV4LifecycleEvent(f, FragmentLifecycleEvent.ON_VIEW_CREATE, false);
        ViewUtil.measureFragmentV4DidDraw(f, () -> {
            mActivityLcCbs.onFragmentV4LifecycleEvent(f, FragmentLifecycleEvent.ON_DRAW, false);
        });
    }

    @Override
    public void onFragmentStarted(final FragmentManagerfm, final Fragment f) {
        mActivityLcCbs.onFragmentV4LifecycleEvent(f, FragmentLifecycleEvent.ON_START, false);
    }

    @Override
    public void onFragmentResumed(final FragmentManagerfm, final Fragment f) {
        mActivityLcCbs.onFragmentV4LifecycleEvent(f, FragmentLifecycleEvent.ON_RESUME, false);
    }

    @Override
    public void onFragmentPaused(final FragmentManagerfm, final Fragment f) {
        mActivityLcCbs.onFragmentV4LifecycleEvent(f, FragmentLifecycleEvent.ON_PAUSE, false);
    }

    @Override
    public void onFragmentStopped(final FragmentManagerfm, final Fragment f) {
        mActivityLcCbs.onFragmentV4LifecycleEvent(f, FragmentLifecycleEvent.ON_STOP, false);
    }

    @Override
    public void onFragmentViewDestroyed(final FragmentManagerfm, final Fragment f) {
        mActivityLcCbs.onFragmentV4LifecycleEvent(f, FragmentLifecycleEvent.ON_VIEW_DESTROY, false);
    }

    @Override
    public void onFragmentDestroyed(final FragmentManagerfm, final Fragment f) {
        mActivityLcCbs.onFragmentV4LifecycleEvent(f, FragmentLifecycleEvent.ON_DESTROY, false);
    }

    @Override
    public void onFragmentDetached(final FragmentManagerfm, final Fragment f) {
        mActivityLcCbs.onFragmentV4LifecycleEvent(f, FragmentLifecycleEvent.ON_DETACH, false);
    }
}
