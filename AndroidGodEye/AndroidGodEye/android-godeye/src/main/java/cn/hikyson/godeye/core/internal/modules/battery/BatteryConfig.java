package cn.hikyson.godeye.core.internal.modules.battery;

import androidx.annotation.Keep;

import java.io.Serializable;

@Keep
public class BatteryConfig implements Serializable {
	BatteryConfig(){}//FDS fix at least one constructor
    @Override
    public String toString() {
        return "BatteryConfig{}";
    }
}
