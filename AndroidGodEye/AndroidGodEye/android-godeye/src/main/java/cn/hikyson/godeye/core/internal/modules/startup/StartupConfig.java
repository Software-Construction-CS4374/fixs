package cn.hikyson.godeye.core.internal.modules.startup;

import androidx.annotation.Keep;

import java.io.Serializable;

@Keep
public class StartupConfig implements Serializable {
	StartupConfig(){}//FDS fix method
    @Override
    public String toString() {
        return "StartupConfig{}";
    }
}