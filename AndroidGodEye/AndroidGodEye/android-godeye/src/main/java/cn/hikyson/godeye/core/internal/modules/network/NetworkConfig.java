package cn.hikyson.godeye.core.internal.modules.network;


import androidx.annotation.Keep;

import java.io.Serializable;

@Keep
public class NetworkConfig implements Serializable {
	NetworkConfig(){}//FDS fix at least one constructor
    @Override
    public String toString() {
        return "NetworkConfig{}";
    }
}
