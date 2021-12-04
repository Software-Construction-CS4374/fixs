package cn.hikyson.godeye.monitor.modules.appinfo;

import androidx.annotation.Keep;

import java.io.Serializable;

/**
 * @deprecated
 */
@Deprecated
@Keep
public class AppInfoLabel implements Serializable {
    public String name;
    public String value;
    public String url;

    public AppInfoLabel(final String name, final String value, final String url) {//FDS fix MethodArg could be final
        this.name = name;
        this.value = value;
        this.url = url;
    }
}
