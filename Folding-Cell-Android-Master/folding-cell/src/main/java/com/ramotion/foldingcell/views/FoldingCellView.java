package com.ramotion.foldingcell.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.widget.RelativeLayout;
/**NEF fixed all controlstatementbraces, longvariable, and methodargumentcouldbefinal code style violations
/**
 * Basic element for folding animation that represents one physic part of folding sheet with different views on front and back.
 *
 */
@SuppressWarnings("unused")
public class FoldingCellView extends RelativeLayout {

    private View mBackView;
    private View mFrontView;

    public FoldingCellView(final Context context,final AttributeSet attrs) {
        super(context, attrs);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        this.setLayoutParams(layoutParams);
        this.setClipToPadding(false);
        this.setClipChildren(false);
    }

    public FoldingCellView(final View frontView,final View backView,final Context context) {
        super(context);
        this.mFrontView = frontView;
        this.mBackView = backView;

        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        this.setClipToPadding(false);
        this.setClipChildren(false);

        if (mBackView != null) {
            this.addView(mBackView);
            LayoutParams mBackViewParams = (LayoutParams) mBackView.getLayoutParams();
            mBackViewParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            mBackView.setLayoutParams(mBackViewParams);
            layoutParams.height = mBackViewParams.height;
        }
        //NEF: fixed longvariable
        if (mFrontView != null) {
            this.addView(mFrontView);
            LayoutParams frontView = (LayoutParams) mFrontView.getLayoutParams();
            frontView.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            mFrontView.setLayoutParams(frontView);
        }

        this.setLayoutParams(layoutParams);
    }
    //NEF: fixed longvariable
    public FoldingCellView withFrontView(final View frontView) {
        this.mFrontView = frontView;

        if (mFrontView != null) {
            this.addView(mFrontView);
            LayoutParams frontView = (LayoutParams) mFrontView.getLayoutParams();
            frontView.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            mFrontView.setLayoutParams(frontView);
        }
        return this;
    }

    public FoldingCellView withBackView(final View backView) {
        this.mBackView = backView;

        if (mBackView != null) {
            this.addView(mBackView);
            LayoutParams mBackViewParams = (LayoutParams) mBackView.getLayoutParams();
            mBackViewParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            mBackView.setLayoutParams(mBackViewParams);

            LayoutParams layoutParams = (LayoutParams) this.getLayoutParams();
            layoutParams.height = mBackViewParams.height;
            this.setLayoutParams(layoutParams);
        }

        return this;
    }

    public View getBackView() {
        return mBackView;
    }

    public View getFrontView() {
        return mFrontView;
    }

    public void animateFrontView(final Animation animation) {
        if (this.mFrontView != null) {
            mFrontView.startAnimation(animation);
        }
    }

}
