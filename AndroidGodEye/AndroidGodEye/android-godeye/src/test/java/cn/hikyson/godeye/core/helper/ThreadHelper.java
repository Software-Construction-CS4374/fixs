package cn.hikyson.godeye.core.helper;

import io.reactivex.Scheduler;
import io.reactivex.functions.Function;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.TestScheduler;

public class ThreadHelper {
    public static void sleep(final long time) {//FDS fix method arg could be final line 9,21
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void setupRxjava() {
        TestScheduler testScheduler = new TestScheduler();
        RxJavaPlugins.setComputationSchedulerHandler(new Function<Scheduler, Scheduler>() {
            @Override
            public Scheduler apply(final Scheduler scheduler) throws Exception {
                return testScheduler;
            }
        });
    }

    public static void teardownRxjava() {
        RxJavaPlugins.setComputationSchedulerHandler(null);
    }
}
