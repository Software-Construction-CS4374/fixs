package cn.hikyson.godeye.core.internal.modules.network;

import androidx.annotation.Keep;

import java.io.Serializable;
import java.util.LinkedHashMap;

@Keep
public class NetworkTime implements Serializable {
    public long totalTimeMillis;
    public LinkedHashMap<String, Long> networkTimeMISM;//FDS fix long var

    public NetworkTime() {
        networkTimeMISM = new LinkedHashMap<>();
    }

    @Override
    public String toString() {
        return "NetworkTime{" +
                "totalTimeMillis=" + totalTimeMillis +
                ", networkTimeMISM=" + networkTimeMISM +
                '}';
    }
}
