package cn.hikyson.godeye.monitor.server;

import cn.hikyson.godeye.core.utils.ThreadUtil;
import io.reactivex.functions.Consumer;

public class SendMessageConsumer implements Consumer<ServerMessage> {
    private final Messager mMessager;//FDS fix immutable field

    public SendMessageConsumer(final Messager messager) {//FDS fix method arg could be final
        mMessager = messager;
    }

    @Override
    public void accept(final ServerMessage serverMessage) throws Exception {//FDS fix method arg could be final
        ThreadUtil.ensureWorkThread("SendMessageConsumer accept");
        if (mMessager != null) {
            mMessager.sendMessage(serverMessage.toString());
        }
    }
}
