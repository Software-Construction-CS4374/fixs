package cn.hikyson.godeye.core.internal.modules.traffic;

import android.net.TrafficStats;
import androidx.annotation.Keep;
import androidx.annotation.WorkerThread;

import java.io.Serializable;

/**
 * 
 * Created by kysonchao on 2017/5/22.
 */
@Keep
public class TrafficSnapshot implements Serializable {
	TrafficSnapshot(){}//FDS fix at least oen constructor
	
    public float rxTotalKB;
    
    public float txTotalKB;
   
    public float rxUidKB;
   
    public float txUidKB;

    @WorkerThread
    public static TrafficSnapshot snapshot() {
        TrafficSnapshot snapshot = new TrafficSnapshot();
        snapshot.rxTotalKB = TrafficStats.getTotalRxBytes() / 1024f;
        snapshot.txTotalKB = TrafficStats.getTotalTxBytes() / 1024f;
        snapshot.rxUidKB = TrafficStats.getUidRxBytes(android.os.Process.myUid()) / 1024f;
        snapshot.txUidKB = TrafficStats.getUidTxBytes(android.os.Process.myUid()) / 1024f;
        return snapshot;
    }


}
