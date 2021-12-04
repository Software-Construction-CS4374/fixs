package cn.hikyson.godeye.core.internal.modules.thread;

import androidx.annotation.Keep;

/**
 *
 * Created by kysonchao on 2018/1/15.
 */
@Keep
public class ExcludeSystemThreadFilter implements ThreadFilter {//FDS fix at least one constructor
	ExludeSystemThreadFilter(){}
	@Override
    public boolean filter(final Thread thread) {//FDS fix method argument could be final
        if (thread == null) {
            return false;
        }
        if (thread.getThreadGroup() == null) {
            return true;
        }
        return !"system".equals(thread.getThreadGroup().getName());
    }
}
