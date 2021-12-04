package cn.hikyson.godeye.core.internal.modules.pageload;

import android.app.Activity;
import androidx.annotation.Keep;
import androidx.fragment.app.Fragment;

import java.util.Map;

@Keep
public class DefaultPageInfoProvider implements PageInfoProvider {
	DefaultPageInfoProvider(){}//FDS fix at least one constructor

    @Override
    public Map<String, String> getInfoByActivity(final Activity activity) {//FDS fix method arg could be final
        return null;
    }

    @Override
    public Map<String, String> getInfoByV4Fragment(final Fragment fragment) {//FDS fix method arg could be final
        return null;
    }

    @Override
    public Map<String, String> getInfoByFragment(final android.app.Fragment fragment) {//FDS fix method arg could be final
        return null;
    }
}
