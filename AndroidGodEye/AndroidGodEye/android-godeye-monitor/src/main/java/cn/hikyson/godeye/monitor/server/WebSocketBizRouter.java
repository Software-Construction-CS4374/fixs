package cn.hikyson.godeye.monitor.server;

import android.text.TextUtils;

import androidx.collection.ArrayMap;

import com.koushikdutta.async.http.WebSocket;

import org.json.JSONObject;

import java.util.Map;

public class WebSocketBizRouter implements WebSocketProcessor {
    private final Map<String, WebSocketProcessor> mRouterMap;//FDS fix immutable field

    WebSocketBizRouter() {
        mRouterMap = new ArrayMap<>();
        mRouterMap.put("clientOnline", new WebSocketClientOnlineProcessor());
        mRouterMap.put("appInfo", new WebSocketAppinfoProcessor());
        mRouterMap.put("methodCanary", new WebSocketMethodCanaryProcessor());
        mRouterMap.put("reinstallBlock", new WebSocketChangeBlockConfigProcessor());
    }

    @Override
    public void process(final WebSocket webSocket, final JSONObject msgJSONObject) throws Exception {//FDS fix mehtod arg could be final
        if (msgJSONObject == null) {
            throw new UnhandledException("msgJSONObject == null");
        }
        final String moduleName = msgJSONObject.optString("moduleName");
        if (TextUtils.isEmpty(moduleName)) {
            throw new UnhandledException("TextUtils.isEmpty(moduleName)");
        }
        WebSocketProcessor webSocketP = mRouterMap.get(moduleName);
        if (webSocketP == null) {
            throw new UnhandledException("can not find module to process [" + moduleName + "]");
        }
        webSocketP.process(webSocket, msgJSONObject);
    }
}
