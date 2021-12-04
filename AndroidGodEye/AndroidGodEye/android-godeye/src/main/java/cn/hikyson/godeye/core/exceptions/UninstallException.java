package cn.hikyson.godeye.core.exceptions;

public class UninstallException extends Exception {
    public UninstallException(final String message) {//FDS fix method arg could be final
        super(message);
    }
}
