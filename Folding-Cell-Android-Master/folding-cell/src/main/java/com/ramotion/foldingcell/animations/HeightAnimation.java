package com.ramotion.foldingcell.animations;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.Transformation;
/**
 * NEF: fixed all methodargumentcouldbefinal code style issues and short variables
 */
/**
 * Height animation for cell container to change cell size according to flip animation.
 */
public class HeightAnimation extends Animation {

    private final View mView;
    private final int mHeightFrom;
    private final int mHeightTo;

    public HeightAnimation(final View mView, final int heightFrom, final int heightTo,final int duration) {
        this.mView = mView;
        this.mHeightFrom = heightFrom;
        this.mHeightTo = heightTo;
        this.setDuration(duration);
    }

    public HeightAnimation withInterpolator(final Interpolator interpolator) {
        if (interpolator != null) {
            this.setInterpolator(interpolator);
        }
        return this;
    }

    @Override
    public void initialize(final int width, final int height,final int parentWidth, final int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
    }

    @Override
    protected void applyTransformation(final float interpolatedTime, final Transformation transform) {
        float newHeight = mHeightFrom + (mHeightTo - mHeightFrom) * interpolatedTime;

        if (interpolatedTime == 1) {
            mView.getLayoutParams().height = mHeightTo;
        } else {
            mView.getLayoutParams().height = (int) newHeight;
        }
        mView.requestLayout();
    }

    @Override
    public boolean willChangeBounds() {
        return true;
    }

    @Override
    public boolean isFillEnabled() {
        return false;
    }

    @Override
    public String toString() {
        return "HeightAnimation{" +
                "mHeightFrom=" + mHeightFrom +
                ", mHeightTo=" + mHeightTo +
                ", offset =" + getStartOffset() +
                ", duration =" + getDuration() +
                '}';
    }
}
