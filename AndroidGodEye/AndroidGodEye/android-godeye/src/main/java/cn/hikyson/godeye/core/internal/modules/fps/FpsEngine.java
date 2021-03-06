package cn.hikyson.godeye.core.internal.modules.fps;

import android.content.Context;
import android.view.Display;
import android.view.WindowManager;

import java.util.concurrent.TimeUnit;

import cn.hikyson.godeye.core.helper.AndroidDebug;
import cn.hikyson.godeye.core.internal.Engine;
import cn.hikyson.godeye.core.internal.Producer;

import cn.hikyson.godeye.core.utils.ThreadUtil;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by kysonchao on 2017/11/23.
 */
public class FpsEngine implements Engine {
    private final Producer<FpsInfo> mProducer;//FDS fix immutable field lines 21-24
    private final long mIntervalMillis;
    private final CompositeDisposable mCompositeDis;//FDS fix long var
    private final FpsMonitor mFpsMonitor;
    private final int mSystemRate;

    public FpsEngine(final Context context,final Producer<FpsInfo> producer,final long intervalMillis) {//FDS fix method argument could be final
        mProducer = producer;
        mIntervalMillis = intervalMillis;
        mSystemRate = getRefreshRate(context);
        mFpsMonitor = new FpsMonitor();
        mCompositeDis = new CompositeDisposable();
    }

    @Override
    public void work() {
        ThreadUtil.ensureMainThread("FpsEngine work");
        mFpsMonitor.start();
        mCompositeDis.add(Observable.interval(mIntervalMillis, TimeUnit.MILLISECONDS)
                .observeOn(ThreadUtil.computationScheduler())
                .subscribeOn(ThreadUtil.computationScheduler())
                .subscribe(aLong -> {
                    ThreadUtil.ensureWorkThread("FpsEngine accept");
                    if (!AndroidDebug.isDebugging()) {// if debugging, then ignore
                        int fps = mFpsMonitor.exportThenReset();
                        mProducer.produce(new FpsInfo(fps, mSystemRate));
                    }
                }));
    }

    @Override
    public void shutdown() {
        ThreadUtil.ensureMainThread("FpsEngine work");
        mCompositeDis.dispose();
        mFpsMonitor.stop();
    }

    /**
     * 
     *
     * @param context
     * @return
     */
    private static int getRefreshRate(final Context context) {//FDS fix method argument could be final
        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        return Math.round(display.getRefreshRate());
    }
}
