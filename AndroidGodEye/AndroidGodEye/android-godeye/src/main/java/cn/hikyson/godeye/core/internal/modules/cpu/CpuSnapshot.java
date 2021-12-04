package cn.hikyson.godeye.core.internal.modules.cpu;

import androidx.annotation.Keep;

import java.io.Serializable;

/**
 * 1. adb shell dumpsys cpuinfo |grep packagename
 * 2. adb shell top -m 10 -s cpu
 * <p>
 * Created by kysonchao on 2017/5/22.
 */
@Keep
public class CpuSnapshot implements Serializable {
    public long user = 0;
    public long system = 0;
    public long idle = 0;
    public long ioWait = 0;
    public long total = 0;
    public long app = 0;

    public CpuSnapshot(final long user,final long system,final long idle,final long ioWait,final long total,final long app) {//FDS fix method arg could be final
        this.user = user;
        this.system = system;
        this.idle = idle;
        this.ioWait = ioWait;
        this.total = total;
        this.app = app;
    }

    public CpuSnapshot() {
    }
}
