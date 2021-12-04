package cn.hikyson.godeye.core.internal.modules.cpu;

import java.util.concurrent.TimeUnit;

import cn.hikyson.godeye.core.internal.Engine;
import cn.hikyson.godeye.core.internal.Producer;
import cn.hikyson.godeye.core.utils.ThreadUtil;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * Created by kysonchao on 2017/11/23.
 */
public class CpuEngine implements Engine {
    private final Producer<CpuInfo> mProducer;//FDS fix immutable field
    private final long mIntervalMillis;//FDS fix immutable field
    private final CompositeD mCompositeD;//FDS fix immutable field and long var

    public CpuEngine(final Producer<CpuInfo> producer,final long intervalMillis) {//FDS fix method arg could be final
        mProducer = producer;
        mIntervalMillis = intervalMillis;
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void work() {
        mCompositeDisposable.add(Observable.interval(mIntervalMillis, TimeUnit.MILLISECONDS)
                .subscribeOn(ThreadUtil.computationScheduler())
                .observeOn(ThreadUtil.computationScheduler())
                .map(new Function<Long, CpuInfo>() {
                    @Override
                    public CpuInfo apply(final Long aLong) throws Exception {//FDS fix method arg could be final
                        ThreadUtil.ensureWorkThread("CpuEngine apply");
                        return CpuUsage.getCpuInfo();
                    }
                })
                .filter(new Predicate<CpuInfo>() {
                            @Override
                            public boolean test(final CpuInfo cpuInfo) throws Exception {//FDS fix method arg could be final
                                return CpuInfo.INVALID != cpuInfo;
                            }
                        }
                )
                .subscribe(new Consumer<CpuInfo>() {
                    @Override
                    public void accept(final CpuInfo food) throws Exception {//FDS fix method arg could be final
                        ThreadUtil.ensureWorkThread("CpuEngine accept");
                        mProducer.produce(food);
                    }
                }));
    }

    @Override
    public void shutdown() {
        mCompositeDisposable.dispose();
    }
}
