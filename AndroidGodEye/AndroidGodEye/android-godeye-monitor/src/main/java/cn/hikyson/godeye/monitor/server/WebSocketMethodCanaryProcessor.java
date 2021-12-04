package cn.hikyson.godeye.monitor.server;

import com.koushikdutta.async.http.WebSocket;

import org.json.JSONObject;

import java.util.Collections;

import cn.hikyson.godeye.core.GodEyeHelper;
import cn.hikyson.godeye.core.exceptions.UninstallException;
import cn.hikyson.godeye.core.utils.L;

public class WebSocketMethodCanaryProcessor implements WebSocketProcessor {
	WebSocketMethodCanaryProcessor(){}//FDS fix at least one constructor
    @Override
    public void process(final WebSocket webSocket,final JSONObject msgJSONObject) {//FDS fix method arg should be final
        try {
            if ("start".equals(msgJSONObject.optString("payload"))) {
                GodEyeHelper.startMethodCanaryRecording("AndroidGodEye-Monitor-Tag");
            } else if ("stop".equals(msgJSONObject.optString("payload"))) {
                GodEyeHelper.stopMethodCanaryRecording("AndroidGodEye-Monitor-Tag");
            }
            webSocket.send(new ServerMessage("methodCanaryMonitorState", Collections.singletonMap("isRunning", GodEyeHelper.isMethodCanaryRecording("AndroidGodEye-Monitor-Tag"))).toString());
        } catch (UninstallException e) {
            L.e(String.valueOf(e));
        }
    }
}
