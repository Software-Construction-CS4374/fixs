package cn.hikyson.godeye.core.internal.notification;

import androidx.annotation.Keep;

import java.io.Serializable;
import java.util.Map;

@Keep
public class NotificationContent implements Serializable {
    public String message;
    public Map<String, Object> extras;

    public NotificationContent(final String message,final Map<String, Object> extras) {//FDS fix method arg could be final
        this.message = message;
        this.extras = extras;
    }

    @Override
    public String toString() {
        return "NotificationContent{" +
                "message='" + message + '\'' +
                ", extras=" + extras +
                '}';
    }
}
