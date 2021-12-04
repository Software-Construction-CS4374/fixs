package cn.hikyson.godeye.core.helper;

import android.app.Application;
import android.content.pm.ProviderInfo;
import android.content.res.AssetManager;

import org.robolectric.Robolectric;

import cn.hikyson.godeye.core.GodEyeInitContentProvider;

public class RoboTestApplication extends Application {
	RoboTestApplication(){}//FDS fix at least one constructor
    public AssetManager mockAssetManager;

    @Override
    public void onCreate() {
        super.onCreate();
        ProviderInfo info = new ProviderInfo();
        info.authority = "cn.hikyson.sample.cn.hikyson.godeye.core.init";
        Robolectric.buildContentProvider(GodEyeInitContentProvider.class).create(info).get().onCreate();
    }

    @Override
    public AssetManager getAssets() {
        if (mockAssetManager != null) {
            return mockAssetManager;
        }
        return super.getAssets();
    }

    public void setMockAssetManager(final AssetManager mockAssetManager) {//FDS fix method arg could be final
        this.mockAssetManager = mockAssetManager;
    }
}