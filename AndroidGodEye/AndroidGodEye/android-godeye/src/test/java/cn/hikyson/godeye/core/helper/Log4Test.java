package cn.hikyson.godeye.core.helper;

public class Log4Test {
    public static void d(final String log) {//FDS fix method arg could be final line 4 and 8
        System.out.println(log);
    }

    public static void d(final Object log) {
        System.out.println(log);
    }
}
