package cn.hikyson.godeye.core.internal.modules.cpu;

import androidx.annotation.Keep;

import java.io.Serializable;
import java.util.Locale;

/**
 * Description:
 * 
 * Author: hui.zhao
 * Date: 2017/2/8
 * Copyright: Ctrip
 */
@Keep
public class CpuInfo implements Serializable {
    public static final CpuInfo INVALID = new CpuInfo();
   
    public double totalUseRatio;
    
    public double appCpuRatio;
    
    public double userCpuRatio;
    
    public double sysCpuRatio;
    
    public double ioWaitRatio;

    public CpuInfo(final double totalUseRatio,final double appCpuRatio,final double userCpuRatio,final double sysCpuRatio,final double//FDS fix method arg could be final
            ioWaitRatio) {
        this.totalUseRatio = totalUseRatio;
        this.appCpuRatio = appCpuRatio;
        this.userCpuRatio = userCpuRatio;
        this.sysCpuRatio = sysCpuRatio;
        this.ioWaitRatio = ioWaitRatio;
    }

    public CpuInfo() {
    }

    @Override
    public String toString() {
        return "app:" +
                String.format(Locale.US, "%.1f", appCpuRatio * 100f) +
                "% , total:" +
                String.format(Locale.US, "%.1f", totalUseRatio * 100f) +
                "% , user:" +
                String.format(Locale.US, "%.1f", userCpuRatio * 100f) +
                "% , system:" +
                String.format(Locale.US, "%.1f", sysCpuRatio * 100f) +
                "% , iowait:" +
                String.format(Locale.US, "%.1f", ioWaitRatio * 100f) + "%";
    }
}
