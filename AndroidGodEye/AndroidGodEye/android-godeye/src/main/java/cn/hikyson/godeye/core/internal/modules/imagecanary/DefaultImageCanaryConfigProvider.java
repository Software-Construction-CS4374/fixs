package cn.hikyson.godeye.core.internal.modules.imagecanary;

import androidx.annotation.Keep;

@Keep
public class DefaultImageCanaryConfigProvider implements ImageCanaryConfigProvider {
	DefaultImageCanaryConfigProvider(){}//FDS fix at least one constructor

    @Override
    public boolean isBitmapQualityTooHigh(final int bitmapWidth,final int bitmapHeight,final int imageViewWidth,final int imageViewHeight) {//FDS fix method arg could be fina;
        return bitmapWidth * bitmapHeight > imageViewWidth * imageViewHeight * 1.5;
    }

    @Override
    public boolean isBitmapQualityTooLow(final int bitmapWidth,final int bitmapHeight,final int imageViewWidth,final int imageViewHeight) {//FDS fix method arg could be final
        return bitmapWidth * bitmapHeight * 2 < imageViewWidth * imageViewHeight;
    }
}
