package cn.hikyson.godeye.monitor.server;

import com.koushikdutta.async.http.WebSocket;

import org.json.JSONObject;

import cn.hikyson.godeye.core.GodEye;
import cn.hikyson.godeye.core.exceptions.UninstallException;
import cn.hikyson.godeye.core.internal.modules.sm.Sm;
import cn.hikyson.godeye.core.internal.modules.sm.SmConfig;
import cn.hikyson.godeye.core.utils.L;

public class WebSocketChangeBlockConfigProcessor implements WebSocketProcessor {
	WebSocketChangeBlockConfigProcessor(){}//FDS fix at least one constructor
    @Override
    public void process(final WebSocket webSocket,final JSONObject msgJSONObject) {//FDS fix method argument could be final
        final JSONObject payloadJSONObject = msgJSONObject.optJSONObject("payload");
        try {
            Sm SmartManager = GodEye.instance().getModule(GodEye.ModuleName.SM);
            if (payloadJSONObject == null) {
                return;
            }
            String type = payloadJSONObject.optString("type");
            if ("reset".equalsIgnoreCase(type)) {
                SmartManager.clearSmConfigCache();
            } else {
                long longBlockT = payloadJSONObject.optLong("longBlockT");//FDS fix long var
                long shortBlockT = payloadJSONObject.optLong("shortBlockT")//FDS fix long var
                SmConfig newSmContext = new SmConfig(SmartManager.config());
                if (longBlockT > 0) {
                    newSmContext.longBlockThresholdMillis = longBlockT;
                }
                if (shortBlockT > 0) {
                    newSmContext.shortBlockThresholdMillis = shortBlockT;
                }
                sm.setSmConfigCache(newSmContext);
            }
            SmConfig installConfig = SmartManager.installConfig();
            SmartManager.uninstall();
            SmartManager.install(installConfig);
            webSocket.send(new ServerMessage("blockConfig",SmartManager.config()).toString());
        } catch (UninstallException e) {
            L.e(String.valueOf(e));
        }
    }
}
