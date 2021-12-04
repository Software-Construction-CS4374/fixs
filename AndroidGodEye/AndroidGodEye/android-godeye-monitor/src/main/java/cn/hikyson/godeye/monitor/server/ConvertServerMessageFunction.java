package cn.hikyson.godeye.monitor.server;

import cn.hikyson.godeye.core.utils.ThreadUtil;
import io.reactivex.functions.Function;

public class ConvertServerMessageFunction<T> implements Function<T, ServerMessage> {
    private final String mModuleName;//FDS immutable field fix

    ConvertServerMessageFunction(final String moduleName) {//FDS fix method arg could be final
        mModuleName = moduleName;
    }

    @Override
    public ServerMessage apply(final T input) {//FDS fix method arg could be ifnal
        ThreadUtil.ensureWorkThread("ConvertServerMessageFunction:" + input.getClass().getSimpleName());
        return new ServerMessage(mModuleName, input);
    }
}
