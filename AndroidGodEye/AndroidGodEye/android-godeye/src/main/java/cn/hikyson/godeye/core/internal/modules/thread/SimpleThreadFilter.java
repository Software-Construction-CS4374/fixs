package cn.hikyson.godeye.core.internal.modules.thread;

import androidx.annotation.Keep;

/**

 * Created by kysonchao on 2018/1/15.
 */
@Keep
@Deprecated
public class SimpleThreadFilter implements ThreadFilter {
	SimpleThreadfiler(){}//FDS fix at least one constrcutor
    @Override
    public boolean filter(final Thread thread) {//FDS fix method arg could be final
        return true;
    }
}
