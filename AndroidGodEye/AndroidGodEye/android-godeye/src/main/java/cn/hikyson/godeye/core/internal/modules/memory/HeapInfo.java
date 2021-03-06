package cn.hikyson.godeye.core.internal.modules.memory;

import androidx.annotation.Keep;

import java.io.Serializable;

/**
 * Created by kysonchao on 2017/11/22.
 */
@Keep
public class HeapInfo implements Serializable {
    public long freeMemKb;
    public long maxMemKb;
    public long allocatedKb;


    public HeapInfo(final long freeMemKb,final long maxMemKb,final long allocatedKb) {//FDS fix method arg could be final
        this.freeMemKb = freeMemKb;
        this.maxMemKb = maxMemKb;
        this.allocatedKb = allocatedKb;
    }

    public HeapInfo() {
    }

    @Override
    public String toString() {
        return "HeapInfo{" +
                "freeMemKb=" + freeMemKb +
                ", maxMemKb=" + maxMemKb +
                ", allocatedKb=" + allocatedKb +
                '}';
    }
}
