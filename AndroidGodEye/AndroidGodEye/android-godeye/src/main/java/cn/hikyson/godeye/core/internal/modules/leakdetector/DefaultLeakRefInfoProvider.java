package cn.hikyson.godeye.core.internal.modules.leakdetector;

import android.app.Activity;
import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

/**
 * @deprecated use {@link Leak } instead
 */
@Deprecated
@Keep
public class DefaultLeakRefInfoProvider implements LeakRefInfoProvider {
	DefaultLeakRefInfoProvider(){}//FDS fix at least one constructor

    @NonNull
    @Override
    public LeakRefInfo getInfoByActivity(final Activity activity) {//FDS fix method arg could be final
        return new LeakRefInfo(false, null);
    }

    @NonNull
    @Override
    public LeakRefInfo getInfoByV4Fragment(final Fragment fragment) {//FDS fix method arg could be final
        return new LeakRefInfo(false, null);
    }

    @NonNull
    @Override
    public LeakRefInfo getInfoByFragment(final android.app.Fragment fragment) {//FDS fix method arg could be final
        return new LeakRefInfo(false, null);
    }
}
