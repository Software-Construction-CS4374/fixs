package cn.hikyson.godeye.monitor.modules.leak;

import androidx.annotation.Keep;

import java.io.Serializable;

import cn.hikyson.godeye.core.internal.modules.leakdetector.LeakInfo;

@Keep
public class LeakSimpleInfo implements Serializable {
    public long createdTimeMillis;
    public long durationTimeMs;//FDS fix long var
    public String leakSignature;
    public String leakObject;
    public int leakSizeByte;
    public String leakTrace;
    public String detail;

    public LeakSimpleInfo(final LeakInfo leakInfo) {//FDS fix method arg could be final
        createdTimeMillis = leakInfo.createdTimeMillis;
        durationTimeMs = leakInfo.durationTimeMs;
        leakSignature = leakInfo.info.getSignature();
        leakObject = leakInfo.info.getLeakTraces().get(0).getLeakingObject().getClassSimpleName();
        leakSizeByte = leakInfo.info.getTotalRetainedHeapByteSize() == null ? 0 : leakInfo.info.getTotalRetainedHeapByteSize();
        leakTrace = leakInfo.info.getLeakTraces().get(0).toSimplePathString();
        detail = leakInfo.info.toString();
    }

    @Override
    public String toString() {
        return "LeakSimpleInfo{" +
                "createdTimeMillis=" + createdTimeMillis +
                ", durationTimeMs=" + durationTimeMs +
                ", leakSignature='" + leakSignature + '\'' +
                ", leakObject='" + leakObject + '\'' +
                ", leakSizeByte=" + leakSizeByte +
                ", leakTrace='" + leakTrace + '\'' +
                ", detail='" + detail + '\'' +
                '}';
    }
}
