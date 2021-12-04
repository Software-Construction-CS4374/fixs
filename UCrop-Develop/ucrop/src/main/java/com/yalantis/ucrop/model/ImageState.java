package com.yalantis.ucrop.model;

import android.graphics.RectF;
/**
 * NEF: fixed methodargumentcouldbefinal and immutable field
 */

/**
 * Created by Oleksii Shliama [https://github.com/shliama] on 6/21/16.
 */
public class ImageState {

    private final RectF mCropRect;
    private final RectF mCurrentImageRect;

    private final float mCurrentScale, mCurrentAngle;

    public ImageState(final RectF cropRect, final RectF currentImageRect,final  float currentScale, final float currentAngle) {
        mCropRect = cropRect;
        mCurrentImageRect = currentImageRect;
        mCurrentScale = currentScale;
        mCurrentAngle = currentAngle;
    }

    public RectF getCropRect() {
        return mCropRect;
    }

    public RectF getCurrentImageRect() {
        return mCurrentImageRect;
    }

    public float getCurrentScale() {
        return mCurrentScale;
    }

    public float getCurrentAngle() {
        return mCurrentAngle;
    }
}
