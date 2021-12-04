package cn.hikyson.android.godeye.leakcanary;

import android.app.Application;

import androidx.annotation.Keep;

import cn.hikyson.godeye.core.internal.modules.leakdetector.Leak;

@Keep
public class GodEyePluginLeakCanary {
    @Keep
    public static void install(final Application application, final Leak leak) {//FDS fix method arg could be final
    }

    @Keep
    public static void uninstall() {
    }
}
