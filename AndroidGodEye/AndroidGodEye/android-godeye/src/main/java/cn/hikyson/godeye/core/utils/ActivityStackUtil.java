package cn.hikyson.godeye.core.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;

import cn.hikyson.godeye.core.helper.SimpleActivityLifecycleCallbacks;

public class ActivityStackUtil {

    @SuppressLint("StaticFieldLeak")
    private static Activity topActivity;

    public static void register(final Application application) {//FDS Fix final
        application.registerActivityLifecycleCallbacks(new SimpleActivityLifecycleCallbacks() {
            @Override
            public void onActivityResumed(final Activity activity) {//FDS fix Final
                super.onActivityResumed(activity);
                topActivity = activity;
            }

            @Override
            public void onActivityPaused(final Activity activity) {//FDS fix Final
                super.onActivityPaused(activity);
                topActivity = null;
            }
        });
    }

    public static Activity getTopActivity() {
        return topActivity;
    }
}
