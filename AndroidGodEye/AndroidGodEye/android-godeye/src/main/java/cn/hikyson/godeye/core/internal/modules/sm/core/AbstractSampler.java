package cn.hikyson.godeye.core.internal.modules.sm.core;

import android.os.Handler;

import java.util.concurrent.atomic.AtomicBoolean;

import cn.hikyson.godeye.core.utils.ThreadUtil;


public abstract class AbstractSampler {

    private final AtomicBoolean mShouldSample = new AtomicBoolean(false);
    public static final String SM_DO_DUMP = "godeye-sm-do-dump";

    long mSampleInterval;

    long mSampleDelay;
    Handler handler;
    private final Runnable mRunnable = new Runnable() {//immutableField fixed FDS
        @Override
        public void run() {
            doSample();

            if (mShouldSample.get()) {
                Handler handler = ThreadUtil.obtainHandler(SM_DO_DUMP);
                if (handler != null) {
                    handler.postDelayed(mRunnable, mSampleInterval);
                }
            }
        }
    };

    AbstractSampler(final long sampleInterval, final long sampleDelay) {//FDS Fix method Argument Could be final
        mSampleInterval = sampleInterval;
        mSampleDelay = sampleDelay;
    }


    public void start() {
        if (mShouldSample.getAndSet(true)) {
            return;
        }
        Handler handler = ThreadUtil.obtainHandler(SM_DO_DUMP);
        if (handler != null) {
            handler.removeCallbacks(mRunnable);
            handler.postDelayed(mRunnable, mSampleDelay);
        }
    }

    public void stop() {
        if (!mShouldSample.getAndSet(false)) {
            return;
        }
        Handler handler = ThreadUtil.obtainHandler(SM_DO_DUMP);
        if (handler != null) {
            handler.removeCallbacks(mRunnable);
        }
    }

    abstract void doSample();
}
