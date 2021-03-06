package cn.hikyson.godeye.monitor.server;

import com.koushikdutta.async.http.WebSocket;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import cn.hikyson.godeye.core.GodEye;
import cn.hikyson.godeye.core.exceptions.UninstallException;
import cn.hikyson.godeye.core.internal.Install;
import cn.hikyson.godeye.core.utils.L;

public class WebSocketClientOnlineProcessor implements WebSocketProcessor {
	WebSocketClientOnlineProcessor(){}//FDS fix at least one constructor

    @Override
    public void process(final WebSocket webSocket,final JSONObject msgJSONObject) throws Exception {//FDS fix method argument could be final
        try {
            Set<String> installedModules = GodEye.instance().getInstalledModuleNames();
            Map<String, Object> moduleConfigs = new HashMap<>();
            for (String module : installedModules) {
                moduleConfigs.put(module, GodEye.instance().<Install>getModule(module).config());
            }
            webSocket.send(new ServerMessage("installedModuleConfigs", moduleConfigs).toString());
        } catch (UninstallException e) {
            L.e(String.valueOf(e));
        }
    }
}
