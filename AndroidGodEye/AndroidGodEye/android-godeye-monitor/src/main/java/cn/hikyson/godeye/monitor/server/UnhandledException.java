package cn.hikyson.godeye.monitor.server;

public class UnhandledException extends Exception {
    public UnhandledException(final String message) {//FDS fix method arg could be final
        super(message);
    }
}
