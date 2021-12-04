package cn.hikyson.godeye.monitor.modules.battery;

import androidx.annotation.Keep;

import java.io.Serializable;

@Keep
public class BatterySummaryInfo implements Serializable {
	BatterySummaryInfo(){}//FDS fix at least one constructor
    public String status;
    public String health;
  
    public boolean present;
   
    public int level;
 
    public int scale;
  
    public String plugged;
   
    public int voltage;
   
    public int temperature;
    
    public String technology;

    @Override
    public String toString() {
        return "BatterySummaryInfo{" +
                "status='" + status + '\'' +
                ", health='" + health + '\'' +
                ", present=" + present +
                ", level=" + level +
                ", scale=" + scale +
                ", plugged='" + plugged + '\'' +
                ", voltage=" + voltage +
                ", temperature=" + temperature +
                ", technology='" + technology + '\'' +
                '}';
    }
}
