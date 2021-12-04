package cn.hikyson.godeye.sample.utils;

public class ThreadUtil {
    public static void sleep(final long time) {//FDS fix method arg could be final
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
