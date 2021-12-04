package com.ramotion.foldingcell.animations;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.Transformation;
/**
 * NEF: fixed all methodargumentcouldbefinal code style violations
 */
/**
 * Main piece of fold animation. Rotates view in 3d space around one of view borders.
 */
@SuppressWarnings("unused")
public class FoldAnimation extends Animation {

    public enum FoldAnimationMode {
        FOLD_UP, UNFOLD_DOWN, FOLD_DOWN, UNFOLD_UP
    }

    private final FoldAnimationMode mFoldMode;
    private final int mCameraHeight;
    private float mFromDegrees;
    private float mToDegrees;
    private float mCenterX;
    private float mCenterY;
    private Camera mCamera;

    public FoldAnimation(final FoldAnimationMode foldMode,final int cameraHeight,final long duration) {
        this.mFoldMode = foldMode;
        this.setFillAfter(true);
        this.setDuration(duration);
        this.mCameraHeight = cameraHeight;
    }

    public FoldAnimation withAnimationListener(final AnimationListener animationListener) {
        this.setAnimationListener(animationListener);
        return this;
    }
    //NEF: fixed methodargumentcouldbefinal
    public FoldAnimation withStartOffset(final int offset) {
        this.setStartOffset(offset);
        return this;
    }
  //NEF: fixed methodargumentcouldbefinal
    public FoldAnimation withInterpolator(final Interpolator interpolator) {
        if (interpolator != null) {
            this.setInterpolator(interpolator);
        }
        return this;
    }

    @Override
    public void initialize(final int width,final int height, final int parentWidth,final int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        this.mCamera = new Camera();
        mCamera.setLocation(0, 0, -mCameraHeight);

        this.mCenterX = width / 2;
        switch (mFoldMode) {
            case FOLD_UP:
                this.mCenterY = 0;
                this.mFromDegrees = 0;
                this.mToDegrees = 90;
                break;
            case FOLD_DOWN:
                this.mCenterY = height;
                this.mFromDegrees = 0;
                this.mToDegrees = -90;
                break;
            case UNFOLD_UP:
                this.mCenterY = height;
                this.mFromDegrees = -90;
                this.mToDegrees = 0;
                break;
            case UNFOLD_DOWN:
                this.mCenterY = 0;
                this.mFromDegrees = 90;
                this.mToDegrees = 0;
                break;
            default:
                throw new IllegalStateException("Unknown animation mode.");
        }
    }

    @Override
    //NEF fixed short variable
    protected void applyTransformation(final float interpolatedTime, final Transformation transform) {
        final Camera camera = mCamera;
        final Matrix matrix = transform.getMatrix();
        final float fromDegrees = mFromDegrees;
        final float degrees = fromDegrees + ((mToDegrees - fromDegrees) * interpolatedTime);

        camera.save();
        camera.rotateX(degrees);
        camera.getMatrix(matrix);
        camera.restore();

        matrix.preTranslate(-mCenterX, -mCenterY);
        matrix.postTranslate(mCenterX, mCenterY);
    }

    @Override
    public String toString() {
        return "FoldAnimation{" +
                "mFoldMode=" + mFoldMode +
                ", mFromDegrees=" + mFromDegrees +
                ", mToDegrees=" + mToDegrees +
                '}';
    }

}
