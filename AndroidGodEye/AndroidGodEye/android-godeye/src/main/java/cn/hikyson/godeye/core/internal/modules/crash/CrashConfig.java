package cn.hikyson.godeye.core.internal.modules.crash;


import androidx.annotation.Keep;

import java.io.Serializable;

@Keep
public class CrashConfig implements Serializable {
    public boolean immediate = false;

    public CrashConfig() {
    }

    public CrashConfig(final boolean immediate) {//FDS fix method arg could be final
        this.immediate = immediate;
    }

    public boolean immediate() {
        return immediate;
    }

    @Override
    public String toString() {
        return "CrashConfig{" +
                "immediate=" + immediate +
                '}';
    }
}
