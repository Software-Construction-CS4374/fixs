package cn.hikyson.godeye.monitor.modules;

import androidx.annotation.Keep;

import java.io.Serializable;
import java.util.Map;

import cn.hikyson.godeye.core.internal.modules.pageload.LifecycleEvent;
import cn.hikyson.godeye.core.internal.modules.pageload.PageType;

@Keep
public class PageLifecycleProcessedEvent implements Serializable {
	PageLifecycleProcessedEvent(){}//FDS fix at least one constructor
    public PageType pageType;
    public String pageClassName;
    public int pageHashCode;
    public LifecycleEvent lifecycleEvent;
    public long startTimeMillis;
    public long endTimeMillis;
    public Map<String, Object> processedInfo;
}
