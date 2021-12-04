package cn.hikyson.godeye.core.internal.modules.pageload;

import androidx.annotation.Keep;

import java.io.Serializable;

@Keep
public class PageLifecycleEventWithTime<T> implements Serializable {
    public PageInfo<T> pageInfo;
    public LifecycleEvent lifecycleEvent;
    public long startTimeMillis;
    public long endTimeMillis;

    public PageLifecycleEventWithTime(final PageInfo<T> pageInfo,final LifecycleEvent lifecycleEvent,final long startTimeMillis,final long endTimeMillis) {//FDS fix method arg could be final
        this.pageInfo = pageInfo;
        this.lifecycleEvent = lifecycleEvent;
        this.startTimeMillis = startTimeMillis;
        this.endTimeMillis = endTimeMillis;
    }

    @Override
    public String toString() {
        return "PageLifecycleEventWithTime{" +
                "pageInfo=" + pageInfo +
                ", lifecycleEvent=" + lifecycleEvent +
                ", startTimeMillis=" + startTimeMillis +
                ", endTimeMillis=" + endTimeMillis +
                '}';
    }
}
