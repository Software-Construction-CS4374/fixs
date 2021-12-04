package cn.hikyson.godeye.core.internal.modules.pageload;

import java.util.List;

public class PageloadUtil {
    public static long parsePageDrawTimeMillis(final List<PageLifecycleEventWithTime> allEvents) {//FDS fix method arg could be final lines 6 and 25
        long startTime = 0;
        long drawTime = 0;
        for (PageLifecycleEventWithTime pageLifecycleEWT : allEvents) {
            if (pageLifecycleEWT.lifecycleEvent == ActivityLifecycleEvent.ON_CREATE
                    || pageLifecycleEWT.lifecycleEvent == FragmentLifecycleEvent.ON_ATTACH) {
                startTime = pageLifecycleEWT.startTimeMillis;
            }
            if (pageLifecycleEWT.lifecycleEvent == ActivityLifecycleEvent.ON_DRAW
                    || pageLifecycleEWT.lifecycleEvent == FragmentLifecycleEvent.ON_DRAW) {
                drawTime = pageLifecycleEWT.endTimeMillis;
            }
        }
        if (startTime > 0 && drawTime > 0 && drawTime > startTime) {
            return drawTime - startTime;
        }
        return 0;
    }

    public static long parsePageloadTimeMillis(final List<PageLifecycleEventWithTime> allEvents) {
        long startTime = 0;
        long loadTime = 0;
        for (PageLifecycleEventWithTime pageLifecycleEWT : allEvents) {//FDS fix long var
            if (pageLifecycleEWT.lifecycleEvent == ActivityLifecycleEvent.ON_CREATE
                    || pageLifecycleEWT.lifecycleEvent == FragmentLifecycleEvent.ON_ATTACH) {
                startTime = pageLifecycleEWT.startTimeMillis;
            }
            if (pageLifecycleEWT.lifecycleEvent == ActivityLifecycleEvent.ON_LOAD
                    || pageLifecycleEWT.lifecycleEvent == FragmentLifecycleEvent.ON_LOAD) {
                loadTime = pageLifecycleEWT.endTimeMillis;
            }
        }
        if (startTime > 0 && loadTime > 0 && loadTime > startTime) {
            return loadTime - startTime;
        }
        return 0;
    }

}
