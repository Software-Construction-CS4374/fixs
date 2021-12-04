package cn.hikyson.godeye.core.internal.modules.memory;


import androidx.annotation.Keep;

import java.io.Serializable;

@Keep
public class HeapConfig implements Serializable {
    public long intervalMillis;

    public HeapConfig(final long intervalMillis) {//FDS fix method arg could be final
        this.intervalMillis = intervalMillis;
    }

    public HeapConfig() {
        this.intervalMillis = 2000;
    }

    public long intervalMillis() {
        return intervalMillis;
    }

    @Override
    public String toString() {
        return "HeapConfig{" +
                "intervalMillis=" + intervalMillis +
                '}';
    }
}