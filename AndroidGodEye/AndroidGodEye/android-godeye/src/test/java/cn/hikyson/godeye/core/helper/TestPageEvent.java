package cn.hikyson.godeye.core.helper;

import cn.hikyson.godeye.core.internal.modules.pageload.LifecycleEvent;

public class TestPageEvent {
    public int pageHashCode;
    public LifecycleEvent lifecycleEvent;
    public int allEventSize;

    public TestPageEvent(final int pageHashCode,final LifecycleEvent lifecycleEvent,final int allEventSize) {//FDS fix method arg could be final
        this.pageHashCode = pageHashCode;
        this.lifecycleEvent = lifecycleEvent;
        this.allEventSize = allEventSize;
    }
}
