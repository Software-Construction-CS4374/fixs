package cn.hikyson.godeye.core.internal.modules.fps;

import androidx.annotation.Keep;

import java.io.Serializable;

@Keep
public class FpsConfig implements Serializable {
    public long intervalMillis;

    public FpsConfig(final long intervalMillis) {//FDS fix method argument could be final
        this.intervalMillis = intervalMillis;
    }

    public FpsConfig() {
        this.intervalMillis = 2000;
    }

    public long intervalMillis() {
        return intervalMillis;
    }

    @Override
    public String toString() {
        return "FpsConfig{" +
                "intervalMillis=" + intervalMillis +
                '}';
    }
}