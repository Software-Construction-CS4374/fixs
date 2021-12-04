package cn.hikyson.godeye.core.internal.modules.thread;

import androidx.annotation.Keep;

@Keep
public class DefaultThreadTagger implements ThreadTagger {
	DefaultThreadTagger(){}//FDS fix at least one constructor
    @Override
    public String tag(final Thread thread,final ThreadInfo threadInfo) {//FDS fix method arg could be final
        return null;
    }
}
