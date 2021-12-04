package cn.hikyson.godeye.core.internal.modules.battery;

import androidx.annotation.Keep;

import java.io.Serializable;

/**
 * Created by kysonchao on 2017/11/22.
 */
@Keep
public class BatteryInfo implements Serializable {
    
    public int status;
   
    public int health;
  
    public boolean present;
    
    public int level;
    
    public int scale;
    
    public int plugged;
    
    public int voltage;
    
    public int temperature;
    
    public String technology;

    public BatteryInfo() {
    }

    public BatteryInfo(final int status,final int health,final boolean present,final int level,final int scale,final int plugged,final int voltage,final int temperature,final String technology) {
        this.status = status;
        this.health = health;
        this.present = present;
        this.level = level;
        this.scale = scale;
        this.plugged = plugged;
        this.voltage = voltage;
        this.temperature = temperature;
        this.technology = technology;
    }

    @Override
    public String toString() {
        return "BatteryInfo{" +
                "status=" + status +
                ", health=" + health +
                ", present=" + present +
                ", level=" + level +
                ", scale=" + scale +
                ", plugged=" + plugged +
                ", voltage=" + voltage +
                ", temperature=" + temperature +
                ", technology='" + technology + '\'' +
                '}';
    }
}
