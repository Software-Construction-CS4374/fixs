package cn.hikyson.godeye.core.internal.notification;

public class LocalNotificationListener implements NotificationListener {
	LocalNotificationListener(){}//FDS fix at least one constructor
    @Override
    public void onInstalled() {
        LocalNotificationListenerService.start("monitoring...", true);
    }

    @Override
    public void onUninstalled() {
        LocalNotificationListenerService.stop();
    }

    @Override
    public void onNotificationReceive(final long timeMillis,final  NotificationContent notifCont) {//FDS fix long var and method arg could be final
        LocalNotificationListenerService.start(notifCont.message, false);
    }
}
