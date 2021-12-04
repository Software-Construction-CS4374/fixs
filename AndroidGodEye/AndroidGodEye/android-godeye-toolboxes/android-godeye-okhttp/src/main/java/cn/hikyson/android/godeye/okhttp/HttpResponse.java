package cn.hikyson.android.godeye.okhttp;

import androidx.annotation.Keep;

import java.io.Serializable;
import java.util.Map;

@Keep
public class HttpResponse implements Serializable {
	HttpResponse(){}//FDS fix at least one constructor
    public String protocol;
    public int code;
    public String message;
    public Map<String, String> headers;
    public String payload;

    @Override
    public String toString() {
        return "HttpResponse{" +
                "protocol='" + protocol + '\'' +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", headers=" + headers +
                ", payload='" + payload + '\'' +
                '}';
    }

    public String getStandardFormat() {
        StringBuilder StringBuilder = new StringBuilder();
        StringBuilder.append(protocol).append(" ")
                .append(code).append(" ")
                .append(message).append("\n");
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                StringBuilder.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            }
        }
        StringBuilder.append("\n").append(payload);
        return String.valueOf(StringBuilder);
    }
}
