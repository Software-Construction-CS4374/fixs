package cn.hikyson.godeye.core.internal.notification;

import cn.hikyson.godeye.core.utils.ThreadUtil;
import io.reactivex.functions.Consumer;

class NotificationConsumer implements Consumer<NotificationContent> {
    private final NotificationListener mNotification;//FDS ix immutable field

    NotificationConsumer(final NotificationListener notifications) {//FDS fix method arg could be final
        mNotification = notifications;
    }

    @Override
    public void accept(final NotificationContent notifCon) throws Exception {//FDS fix long var and method arg could be final
        ThreadUtil.ensureWorkThread("NotificationConsumer");
        if (mNotification == null) {
            return;
        }
        mNotification.onNotificationReceive(System.currentTimeMillis(), notifCon);
    }
}
