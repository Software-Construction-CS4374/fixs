package cn.hikyson.godeye.core.internal.modules.pageload;

import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import java.util.HashSet;
import java.util.Set;

import cn.hikyson.godeye.core.GodEye;
import cn.hikyson.godeye.core.internal.Engine;
import cn.hikyson.godeye.core.internal.Producer;
import cn.hikyson.godeye.core.utils.ViewUtil;
import cn.hikyson.methodcanary.lib.MethodCanary;
import cn.hikyson.methodcanary.lib.MethodEvent;
import cn.hikyson.methodcanary.lib.OnPageLifecycleEventCallback;

public class ActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks, Engine, OnPageLifecycleEventCallback {

    private final PageLifecycleRecords mPLR;
    private final PageInfoProvider mPageInfoProvider;
    private final Producer<PageLifecycleEventInfo> mProducer;
    private final Handler mHandler;
    private final Set<Activity> mStartedActivity;

    ActivityLifecycleCallbacks(final PageLifecycleRecords plcRecords,final PageInfoProvider pageInfoProvider,final Producer<PageLifecycleEventInfo> producer,final Handler handler) {
        mPLR = plcRecords;
        mPageInfoProvider = pageInfoProvider;
        mProducer = producer;
        mHandler = handler;
        mStartedActivity = new HashSet<>();
    }

    void onActivityLoad(final Activity activity) {
        onActivityLifecycleEvent(activity, ActivityLifecycleEvent.ON_LOAD, true);
    }

    void onFragmentV4Load(final Fragment frag) {
        onFragmentV4LifecycleEvent(frag, FragmentLifecycleEvent.ON_LOAD, true);
    }

    void onFragmentLoad(final android.app.Fragment frag) {
        onFragmentLifecycleEvent(frag, FragmentLifecycleEvent.ON_LOAD, true);
    }

    void onFragmentV4Show(final Fragment frag) {
        onFragmentV4LifecycleEvent(frag, FragmentLifecycleEvent.ON_SHOW, false);
    }

    void onFragmentV4Hide(final Fragment frag) {
        onFragmentV4LifecycleEvent(frag, FragmentLifecycleEvent.ON_HIDE, false);
    }

    void onFragmentShow(final android.app.Fragment frag) {
        onFragmentLifecycleEvent(f, FragmentLifecycleEvent.ON_SHOW, false);
    }

    void onFragmentHide(final android.app.Fragment frag) {
        onFragmentLifecycleEvent(frag, FragmentLifecycleEvent.ON_HIDE, false);
    }

    @Override
    public void onActivityCreated(final Activity activity,final Bundle savedIState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            activity.getFragmentManager().registerFragmentLifecycleCallbacks(new FragmentLifecycleCallbacks(ActivityLifecycleCallbacks.this), true);
        }
        if (activity instanceof FragmentActivity) {
            ((FragmentActivity) activity).getSupportFragmentManager().registerFragmentLifecycleCallbacks(new FragmentLifecycleCallbacksV4(ActivityLifecycleCallbacks.this), true);
        }
        onActivityLifecycleEvent(activity, ActivityLifecycleEvent.ON_CREATE, false);
    }

    @Override
    public void onActivityStarted(final Activity activity) {
        onActivityLifecycleEvent(activity, ActivityLifecycleEvent.ON_START, false);
        if (!mStartedActivity.contains(activity)) {
            mStartedActivity.add(activity);
            ViewUtil.measureActivityDidDraw(activity, () -> {
                onActivityLifecycleEvent(activity, ActivityLifecycleEvent.ON_DRAW, false);
            });
        }
    }

    @Override
    public void onActivityResumed(final Activity activity) {
        onActivityLifecycleEvent(activity, ActivityLifecycleEvent.ON_RESUME, false);
    }

    @Override
    public void onActivityPaused(final Activity activity) {
        onActivityLifecycleEvent(activity, ActivityLifecycleEvent.ON_PAUSE, false);
    }

    @Override
    public void onActivityStopped(final Activity activity) {
        onActivityLifecycleEvent(activity, ActivityLifecycleEvent.ON_STOP, false);
    }

    @Override
    public void onActivitySaveInstanceState(final Activity activity, final Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(final Activity activity) {
        onActivityLifecycleEvent(activity, ActivityLifecycleEvent.ON_DESTROY, false);
        mStartedActivity.remove(activity);
    }

    // method canary callback for lifecycle method cost
    @Override
    public void onLifecycleEvent(final MethodEvent lcMethodEvent, final Object page) {
        mHandler.post(() -> {
            LifecycleEvent lifecycleEvent = null;
            PageInfo<?> pageInfo = null;
            if (page instanceof Activity) {
                pageInfo = new PageInfo<>(page, mPageInfoProvider.getInfoByActivity((Activity) page));
                lifecycleEvent = PageLifecycleMethodEventTypes.convert(PageType.ACTIVITY, lcMethodEvent);
            } else if (page instanceof Fragment) {
                pageInfo = new PageInfo<>(page, mPageInfoProvider.getInfoByV4Fragment((Fragment) page));
                lifecycleEvent = PageLifecycleMethodEventTypes.convert(PageType.FRAGMENT, lcMethodEvent);
            } else if (page instanceof android.app.Fragment) {
                pageInfo = new PageInfo<>(page, mPageInfoProvider.getInfoByFragment((android.app.Fragment) page));
                lifecycleEvent = PageLifecycleMethodEventTypes.convert(PageType.FRAGMENT, lcMethodEvent);
            }
            if (pageInfo == null || lifecycleEvent == null) {
                return;
            }
            if (lcMethodEvent.isEnter) {
                mPageLifecycleRecords.addMethodStartEvent(pageInfo, lifecycleEvent, lcMethodEvent.eventTimeMillis);
            } else {
                PageLifecycleEventWithTime<?> PLCEventWithTime = mPageLifecycleRecords.addMethodEndEvent(pageInfo, lifecycleEvent, lifecycleMethodEvent.eventTimeMillis);
                if (pageLifecycleEventWithTime != null) {
                    mProducer.produce(new PageLifecycleEventInfo(pageInfo, pageLifecycleEventWithTime, mPageLifecycleRecords.getLifecycleEventsByPageInfo(pageInfo)));
                }
            }
        });
    }

    private void onActivityLifecycleEvent(final Activity activity,final LifecycleEvent event,final boolean canNotRepeat) {
        final long time = System.currentTimeMillis();
        mHandler.post(() -> {
            PageInfo<Activity> pageInfo = new PageInfo<>(activity, mPageInfoProvider.getInfoByActivity(activity));
            if (canNotRepeat && mPageLifecycleRecords.isExistEvent(pageInfo, event)) {
                return;
            }
            PageLifecycleEventWithTime<Activity> lifecycleEvent = mPageLifecycleRecords.addLifecycleEvent(pageInfo, event, time);
            if (lifecycleEvent != null) {
                mProducer.produce(new PageLifecycleEventInfo<>(pageInfo, lifecycleEvent, mPageLifecycleRecords.getLifecycleEventsByPageInfo(pageInfo)));
            }
        });
    }

    void onFragmentLifecycleEvent(final android.app.Fragment fragment,final LifecycleEvent event,final boolean canNotRepeat) {
        final long time = System.currentTimeMillis();
        mHandler.post(() -> {
            PageInfo<android.app.Fragment> pageInfo = new PageInfo<>(fragment, mPageInfoProvider.getInfoByFragment(fragment));
            if (canNotRepeat && mPageLifecycleRecords.isExistEvent(pageInfo, event)) {
                return;
            }
            PageLifecycleEventWithTime<android.app.Fragment> lifecycleEvent = mPageLifecycleRecords.addLifecycleEvent(pageInfo, event, time);
            if (lifecycleEvent != null) {
                mProducer.produce(new PageLifecycleEventInfo<>(pageInfo, lifecycleEvent, mPageLifecycleRecords.getLifecycleEventsByPageInfo(pageInfo)));
            }
        });
    }

    void onFragmentV4LifecycleEvent(final Fragment fragment,final LifecycleEvent event,final boolean canNotRepeat) {
        final long time = System.currentTimeMillis();
        mHandler.post(() -> {
            PageInfo<Fragment> pageInfo = new PageInfo<>(fragment, mPageInfoProvider.getInfoByV4Fragment(fragment));
            if (canNotRepeat && mPageLifecycleRecords.isExistEvent(pageInfo, event)) {
                return;
            }
            PageLifecycleEventWithTime<Fragment> lifecycleEvent = mPageLifecycleRecords.addLifecycleEvent(pageInfo, event, time);
            if (lifecycleEvent != null) {
                mProducer.produce(new PageLifecycleEventInfo<>(pageInfo, lifecycleEvent, mPageLifecycleRecords.getLifecycleEventsByPageInfo(pageInfo)));
            }
        });
    }

    @Override
    public void work() {
        GodEye.instance().getApplication().registerActivityLifecycleCallbacks(this);
        MethodCanary.get().addOnPageLifecycleEventCallback(this);
    }

    @Override
    public void shutdown() {
        GodEye.instance().getApplication().unregisterActivityLifecycleCallbacks(this);
        MethodCanary.get().removeOnPageLifecycleEventCallback(this);
    }
}
