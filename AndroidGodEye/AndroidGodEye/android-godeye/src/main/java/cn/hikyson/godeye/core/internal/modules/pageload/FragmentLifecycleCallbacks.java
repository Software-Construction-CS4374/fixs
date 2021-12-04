package cn.hikyson.godeye.core.internal.modules.pageload;


import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.final FragmentManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import cn.hikyson.godeye.core.utils.ViewUtil;

@TargetApi(Build.VERSION_CODES.O)
public class FragmentLifecycleCallbacks extends final FragmentManager.FragmentLifecycleCallbacks {
    private final ActivityLcCbs mActivityLc;//FDS fix immutable field and long var

    FragmentLifecycleCallbacks(final ActivityLcCbs ActivityLcCbs) {
        mActivityLc = ActivityLcCbs;
    }

    @Override
    public void onFragmentAttached(final final FragmentManager fragm, final Fragment frag,final Context context) {//FDS fix method arg could be final lines 23,28,33
        mActivityLc.onFragmentLifecycleEvent(frag, FragmentLifecycleEvent.ON_ATTACH, false);
    }

    @Override
    public void onFragmentCreated(final final FragmentManager fragm, final Fragment frag,final Bundle savedIS) {//FDS fix short var and method argument could be final for lines 23,28,33,41,46,51,56,61,66,71
        mActivityLc.onFragmentLifecycleEvent(frag, FragmentLifecycleEvent.ON_CREATE, false);
    }

    @Override
    public void onFragmentViewCreated(final final FragmentManager fragm, final final Fragment frag, View view,final Bundle savedIS) {
        mActivityLc.onFragmentLifecycleEvent(frag, FragmentLifecycleEvent.ON_VIEW_CREATE, false);
        ViewUtil.measureFragmentDidDraw(frag, () -> {
            mActivityLc.onFragmentLifecycleEvent(frag, FragmentLifecycleEvent.ON_DRAW, false);
        });
    }

    @Override
    public void onFragmentStarted(final FragmentManager fragm, final Fragment frag) {
        mActivityLc.onFragmentLifecycleEvent(frag, FragmentLifecycleEvent.ON_START, false);
    }

    @Override
    public void onFragmentResumed(final FragmentManager fragm, final Fragment frag) {
        mActivityLc.onFragmentLifecycleEvent(frag, FragmentLifecycleEvent.ON_RESUME, false);
    }

    @Override
    public void onFragmentPaused(final FragmentManager fragm, final Fragment frag) {
        mActivityLc.onFragmentLifecycleEvent(frag, FragmentLifecycleEvent.ON_PAUSE, false);
    }

    @Override
    public void onFragmentStopped(final FragmentManager fragm, final Fragment frag) {
        mActivityLc.onFragmentLifecycleEvent(frag, FragmentLifecycleEvent.ON_STOP, false);
    }

    @Override
    public void onFragmentViewDestroyed(final FragmentManager fragm, final Fragment frag) {
        mActivityLc.onFragmentLifecycleEvent(frag, FragmentLifecycleEvent.ON_VIEW_DESTROY, false);
    }

    @Override
    public void onFragmentDestroyed(final FragmentManager fragm, final Fragment frag) {
        mActivityLc.onFragmentLifecycleEvent(frag, FragmentLifecycleEvent.ON_DESTROY, false);
    }

    @Override
    public void onFragmentDetached(final FragmentManager fragm, final Fragment frag) {
        mActivityLc.onFragmentLifecycleEvent(frag, FragmentLifecycleEvent.ON_DETACH, false);
    }

}
