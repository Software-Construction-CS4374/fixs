package cn.hikyson.godeye.core.internal.modules.imagecanary;


import androidx.annotation.Keep;

import java.io.Serializable;

@Keep
public class ImageCanaryConfig implements Serializable {

    // ImageCanaryConfigProvider
    public String imageCanaryCP;//FDS fix long var

    public ImageCanaryConfig() {
        this.imageCanaryCP = DefaultImageCanaryConfigProvider.class.getName();
    }

    public ImageCanaryConfig(final String imageCanaryCP) {//FDS fix method arg could be final
        this.imageCanaryCP = imageCanaryCP;
    }

    // ImageCanaryConfigProvider
    public String getImageCanaryConfigProvider() {
        return imageCanaryCP;
    }

    @Override
    public String toString() {
        return "ImageCanaryConfig{" +
                "imageCanaryCP='" + imageCanaryCP + '\'' +
                '}';
    }
}