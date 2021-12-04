package com.ramotion.cardslider;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import android.view.View;

/**
 * Default implementation of {@link CardSliderLayoutManager.ViewUpdater}
 */
public class DefaultViewUpdater implements CardSliderLayoutManager.ViewUpdater {

    public static final float SCALE_LEFT = 0.65f;
    public static final float SCALE_CENTER = 0.95f;
    public static final float SCALE_RIGHT = 0.8f;
    public static final float SCALE_CENTER_TO_LEFT = SCALE_CENTER - SCALE_LEFT;
    public static final float SCALE_CENTER_TO_RIGHT = SCALE_CENTER - SCALE_RIGHT;

    public static final int Z_CENTER_1 = 12;
    public static final int Z_CENTER_2 = 16;
    public static final int Z_RIGHT = 8;

    private int cardWidth;
    private int activeCardLeft;
    private int activeCardRight;
    private int activeCardCenter;
    private float cardsGap;

    private int transitionEnd;
    private int transDist;
    private float transRight2Cen;

    private CardSliderLayoutManager layOut;

    private View previewView;

    @Override
    public void onLayoutManagerInitialized(@NonNull CardSliderLayoutManager layOut) {
        this.layOut = layOut;

        this.cardWidth = layOut.getCardWidth();
        this.activeCardLeft = layOut.getActiveCardLeft();
        this.activeCardRight = layOut.getActiveCardRight();
        this.activeCardCenter = layOut.getActiveCardCenter();
        this.cardsGap = layOut.getCardsGap();

        this.transitionEnd = activeCardCenter;
        this.transDist = activeCardRight - transitionEnd;

        final float centerBorder = (cardWidth - cardWidth * SCALE_CENTER) / 2f;
        final float rightBorder = (cardWidth - cardWidth * SCALE_RIGHT) / 2f;
        final float right2CenDist = (activeCardRight + centerBorder) - (activeCardRight - rightBorder);
        this.transRight2Cen = right2CenDist - cardsGap;
    }

    @Override
    public void updateView(@NonNull View view, float position) {
        final float scale;
        final float alpha;
        final float zNum;
        final float xNum;

        if (position < 0) {
            final float ratio = (float) layOut.getDecoratedLeft(view) / activeCardLeft;
            scale = SCALE_LEFT + SCALE_CENTER_TO_LEFT * ratio;
            alpha = 0.1f + ratio;
            zNum = Z_CENTER_1 * ratio;
            xNum = 0;
        } else if (position < 0.5f) {
            scale = SCALE_CENTER;
            alpha = 1;
            zNum = Z_CENTER_1;
            xNum = 0;
        } else if (position < 1f) {
            final int viewLeft = layOut.getDecoratedLeft(view);
            final float ratio = (float) (viewLeft - activeCardCenter) / (activeCardRight - activeCardCenter);
            scale = SCALE_CENTER - SCALE_CENTER_TO_RIGHT * ratio;
            alpha = 1;
            zNum = Z_CENTER_2;
            if (Math.abs(transRight2Cen) < Math.abs(transRight2Cen * (viewLeft - transitionEnd) / transDist)) {
                xNum = -transRight2Cen;
            } else {
                xNum = -transRight2Cen * (viewLeft - transitionEnd) / transDist;
            }
        } else {
            scale = SCALE_RIGHT;
            alpha = 1;
            zNum = Z_RIGHT;

            if (previewView != null) {
                final float prevViewScale;
                final float prevTransition;
                final int prevRight;

                final boolean isFirstRight = layOut.getDecoratedRight(previewView) <= activeCardRight;
                if (isFirstRight) {
                    prevViewScale = SCALE_CENTER;
                    prevRight = activeCardRight;
                    prevTransition = 0;
                } else {
                    prevViewScale = ViewCompat.getScaleX(previewView);
                    prevRight = layOut.getDecoratedRight(previewView);
                    prevTransition = ViewCompat.getTranslationX(previewView);
                }

                final float prevBorder = (cardWidth - cardWidth * prevViewScale) / 2;
                final float currentBorder = (cardWidth - cardWidth * SCALE_RIGHT) / 2;
                final float distance = (layOut.getDecoratedLeft(view) + currentBorder) - (prevRight - prevBorder + prevTransition);

                final float transition = distance - cardsGap;
                xNum = -transition;
            } else {
                xNum = 0;
            }
        }

        ViewCompat.setScaleX(view, scale);
        ViewCompat.setScaleY(view, scale);
        ViewCompat.setZ(view, zNum);
        ViewCompat.setTranslationX(view, xNum);
        ViewCompat.setAlpha(view, alpha);

        previewView = view;
    }

    protected CardSliderLayoutManager getLayoutManager() {
        return layOut;
    }

}
