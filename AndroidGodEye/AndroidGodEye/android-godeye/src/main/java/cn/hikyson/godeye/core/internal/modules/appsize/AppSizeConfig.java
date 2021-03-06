package cn.hikyson.godeye.core.internal.modules.appsize;


import androidx.annotation.Keep;

import java.io.Serializable;

@Keep
public class AppSizeConfig implements Serializable {
    public long delayMillis;

    public AppSizeConfig(final long delayMillis) {//FDS Fix method arg could be final
        this.delayMillis = delayMillis;
    }

    public AppSizeConfig() {
        this.delayMillis = 0;
    }

    public long delayMillis() {
        return delayMillis;
    }

    @Override
    public String toString() {
        return "AppSizeConfig{" +
                "delayMillis=" + delayMillis +
                '}';
    }
}
