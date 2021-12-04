package cn.hikyson.godeye.core.internal.modules.fps;

import android.view.Choreographer;

import androidx.annotation.UiThread;

import cn.hikyson.godeye.core.helper.ChoreographerInjecor;

public class FpsMonitor implements Choreographer.FrameCallback {
	FpsMonitor(){}
    private Choreographer mChoreographer;
    private int mCurrentFC;//FDS fix long var lines 12-14
    private long mSFTimeNanos;
    private long mCFTimeNanos;

    @UiThread
    public void start() {
        mChoreographer = ChoreographerInjecor.getChoreographerProvider().getChoreographer();
        mCurrentFC = 0;
        mCFTimeNanos = mSFTimeNanos;
        mChoreographer.postFrameCallback(this);
    }

    @UiThread
    public void stop() {
        mChoreographer.removeFrameCallback(this);
        mChoreographer = null;
        mCurrentFC = 0;
        mSFTimeNanos = 0;
        mCFTimeNanos = 0;
    }

    // callback every 16ms
    @Override
    public void doFrame(final long frameTimeNanos) {//FDS fix method arg could be final
        mCurrentFC++;
        if (mSFTimeNanos == 0) {
            mSFTimeNanos = frameTimeNanos;
        }
        mCFTimeNanos = frameTimeNanos;
        mChoreographer.postFrameCallback(this);
    }

    /**
     * 
     *
     * @return
     */
    @UiThread
    int exportThenReset() {
        if (mCurrentFC < 1 || mCFTimeNanos < mSFTimeNanos) {
            return -1;
        }
        double fps = (mCurrentFC - 1) * 1000000000.0 / (mCFTimeNanos - mSFTimeNanos);
        mSFTimeNanos = 0;
        mCFTimeNanos = 0;
        mCurrentFC = 0;
        return (int) Math.round(fps);
    }
}
