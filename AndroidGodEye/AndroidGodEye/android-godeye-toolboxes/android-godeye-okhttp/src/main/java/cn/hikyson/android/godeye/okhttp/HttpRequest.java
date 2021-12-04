package cn.hikyson.android.godeye.okhttp;

import androidx.annotation.Keep;

import java.io.Serializable;
import java.util.Map;

@Keep
public class HttpRequest implements Serializable {
	HttpRequest(){}//FDS fix at least one constructor
    public String method;
    public String url;
    public String protocol;
    public Map<String, String> headers;
    public String payload;

    @Override
    public String toString() {
        return "HttpRequest{" +
                "method='" + method + '\'' +
                ", url='" + url + '\'' +
                ", protocol='" + protocol + '\'' +
                ", headers=" + headers +
                ", payload='" + payload + '\'' +
                '}';
    }

    public String getStandardFormat() {
        StringBuilder StringBuilder = new StringBuilder();
        StringBuilder.append(method).append(" ")
                .append(url).append(" ")
                .append(protocol).append("\n");
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                StringBuilder.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            }
        }
        StringBuilder.append("\n").append(payload);
        return String.valueOf(StringBuilder);
    }
}
