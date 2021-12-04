package cn.hikyson.android.godeye.okhttp;

import okhttp3.Call;
import okhttp3.EventListener;

public class GodEyePluginOkNetwork extends OkHttpNetworkContentInterceptor implements EventListener.Factory {

    public GodEyePluginOkNetwork() {
        super(new HttpContentTimeMapping());
    }

    @Override
    public EventListener create(final Call call) {//FDS fix method argument could be final
        return new OkNetworkEventListener(this.mHttpContentTimeMapping);
    }
}
