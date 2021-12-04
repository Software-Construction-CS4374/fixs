package cn.hikyson.godeye.core.exceptions;

public class UnexpectException extends RuntimeException {
    public UnexpectException(final String message) {//FDS fix method arg could be final
        super(message);
    }
}
