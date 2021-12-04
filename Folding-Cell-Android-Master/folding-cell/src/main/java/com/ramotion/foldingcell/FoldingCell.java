package com.ramotion.foldingcell;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ramotion.foldingcell.animations.AnimationEndListener;
import com.ramotion.foldingcell.animations.FoldAnimation;
import com.ramotion.foldingcell.animations.HeightAnimation;
import com.ramotion.foldingcell.views.FoldingCellView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.core.view.ViewCompat;
/**
 * NEF: fixed all control statment braces issues (code style)
 * fixed all longvariable code style violations
 * fixed all short variables code style violations
 * fixed all ImmutableFields design violations
 * fixed all methodargumentcouldbefinal code style violations
 */
/**
 * Very first implementation of Folding Cell by Ramotion for Android platform
 */
public class FoldingCell extends RelativeLayout {

    // state variables
    private boolean mUnfolded;
    private boolean mAnimateInProgr; //NEF Longvariable fix

    // default values
    private final int DEF_ANIMATION_DUR = 1000;
    private final int DEF_BACK_SIDE_CLR = Color.GRAY;
    private final int DEF_ADD_FLIPS = 0;
    private final int DEF_CAMERA_HEIGHT = 30;

    // current settings
    private int mAnimationDur = DEF_ANIMATION_DUR;
    private int mBackSideColor = DEF_BACK_SIDE_CLR;
    private final int mAddFlipsCnt = DEF_ADD_FLIPS;//NEF ImmutableField fix
    private int mCameraHeight = DEF_CAMERA_HEIGHT;

    public FoldingCell(final Context context) {
        this(context, null);
    }

    public FoldingCell(final Context context,final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FoldingCell(final Context context, final AttributeSet attrs,final int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.FoldingCell);
        if (styledAttrs!=null){
            final int count = styledAttrs.getIndexCount();
            for (int i = 0; i < count; ++i) {
                int attr = styledAttrs.getIndex(i);
                if (attr == R.styleable.FoldingCell_animationDuration) {
                    this. mAnimationDur = styledAttrs.getInt(R.styleable.FoldingCell_animationDuration, DEF_ANIMATION_DURATION);
                } else if (attr == R.styleable.FoldingCell_backSideColor) {
                    this.mBackSideColor = styledAttrs.getColor(R.styleable.FoldingCell_backSideColor, DEF_BACK_SIDE_COLOR);
                } else if (attr == R.styleable.FoldingCell_additionalFlipsCount) {
                    this.mAdditionalFlipsCount = styledAttrs.getInt(R.styleable.FoldingCell_additionalFlipsCount, DEF_ADDITIONAL_FLIPS);
                } else if (attr == R.styleable.FoldingCell_cameraHeight) {
                    this.mCameraHeight = styledAttrs.getInt(R.styleable.FoldingCell_cameraHeight, DEF_CAMERA_HEIGHT);
                }
            }
            styledAttrs.recycle();
        }

        this.setClipChildren(false);
        this.setClipToPadding(false);
    }

    /**
     * Initializes folding cell programmatically with custom settings
     *
     * @param animationDuration    animation duration, default is 1000
     * @param backSideColor        color of back side, default is android.graphics.Color.GREY (0xFF888888)
     * @param additionalFlipsCount count of additional flips (after first one), set 0 for auto
     */
  //NEF fixed methodargumentcouldbefinal
    public void initialize(final int animateDur, final int backSideColor,final int addFlipsCnt) {
        this.mAnimationDuration = animateDur;
        this.mBackSideColor = backSideColor;
        this.mAdditionalFlipsCount = addFlipsCnt;
    }

    /**
     * Initializes folding cell programmatically with custom settings
     *
     * @param animationDuration    animation duration, default is 1000
     * @param backSideColor        color of back side, default is android.graphics.Color.GREY (0xFF888888)
     * @param additionalFlipsCount count of additional flips (after first one), set 0 for auto
     */
  //NEF fixed methodargumentcouldbefinal
    public void initialize(final int cameraHeight,final int animationDuration, final int backSideColor, final int addFlipsCnt) {
        this.mAnimationDuration = animationDuration;
        this.mBackSideColor = backSideColor;
        this.mAdditionalFlipsCount = addFlipsCnt;
        this.mCameraHeight = cameraHeight;
    }

    public boolean isUnfolded() {
        return mUnfolded;
    }

    /**
     * Unfold cell with (or without) animation
     *
     * @param skipAnimation if true - change state of cell instantly without animation
     */
    public void unfold(final boolean skipAnimation) {
        if (mUnfolded || mAnimationInProgress) {
        	return;
        }

        // get main content parts
        final View contentView = getChildAt(0);
        if (contentView == null) { //NEF fixed control statement braces
        	return;
        }
        final View titleView = getChildAt(1);
        if (titleView == null) {
        	return;
        }

        // hide title and content views
        titleView.setVisibility(GONE);
        contentView.setVisibility(GONE);

        // Measure views and take a bitmaps to replace real views with images
        Bitmap fromTitleView = measureViewAndGetBitmap(titleView, this.getMeasuredWidth());
        Bitmap fromContentView = measureViewAndGetBitmap(contentView, this.getMeasuredWidth());

        if (skipAnimation) {
            contentView.setVisibility(VISIBLE);
            FoldingCell.this.mUnfolded = true;
            FoldingCell.this.mAnimationInProgress = false;
            this.getLayoutParams().height = contentView.getHeight();
        } else {
            ViewCompat.setHasTransientState(this, true);
            // create layout container for animation elements
            final LinearLayout foldingLayout = createAndPrepareFoldingContainer();
            this.addView(foldingLayout);
            // calculate heights of animation parts
            ArrayList<Integer> heights = calculateHeightsForAnimationParts(titleView.getHeight(), contentView.getHeight(), mAdditionalFlipsCount);
            // create list with animation parts for animation
            ArrayList<FoldingCellView> foldingCellEle = prepareViewsForAnimation(heights, fromTitleView, fromContentVie);
            // start unfold animation with end listener
            int childCount = foldingCellEle.size();
            int pt90degAnimateDur = mAnimationDuration / (childCount * 2);
            startUnfoldAnimation(foldingCellEle, foldingLayout, part90degreeAnimationDuration, new AnimationEndListener() {
                public void onAnimationEnd(final Animation animation) {
                    contentView.setVisibility(VISIBLE);
                    foldingLayout.setVisibility(GONE);
                    FoldingCell.this.removeView(foldingLayout);
                    FoldingCell.this.mUnfolded = true;
                    FoldingCell.this.mAnimationInProgress = false;
                    ViewCompat.setHasTransientState(FoldingCell.this, true);
                }
            });

            startExpandHeightAnimation(heights, part90degreeAnimationDuration * 2);
            this.mAnimationInProgress = true;
        }
    }

    /**
     * Fold cell with (or without) animation
     *
     * @param skipAnimation if true - change state of cell instantly without animation
     */
    public void fold(final boolean skipAnimation) {
        if (!mUnfolded || mAnimationInProgress) {
        	return;
        }

        // get basic views
        final View contentView = getChildAt(0);
        if (contentView == null) {
        	return;
        }
        final View titleView = getChildAt(1);
        if (titleView == null) {
        	return;
        }

        // hide title and content views
        titleView.setVisibility(GONE);
        contentView.setVisibility(GONE);

        // make bitmaps from title and content views
        Bitmap fromTitleView = measureViewAndGetBitmap(titleView, this.getMeasuredWidth());
        Bitmap fromContentView = measureViewAndGetBitmap(contentView, this.getMeasuredWidth());

        if (skipAnimation) {
            contentView.setVisibility(GONE);
            titleView.setVisibility(VISIBLE);
            FoldingCell.this.mAnimationInProgress = false;
            FoldingCell.this.mUnfolded = false;
            this.getLayoutParams().height = titleView.getHeight();
        } else {
            ViewCompat.setHasTransientState(this, true);
            // create empty layout for folding animation
            final LinearLayout foldingLayout = createAndPrepareFoldingContainer();
            // add that layout to structure
            this.addView(foldingLayout);

            // calculate heights of animation parts
            ArrayList<Integer> heights = calculateHeightsForAnimationParts(titleView.getHeight(), contentView.getHeight(), mAdditionalFlipsCount);
            // create list with animation parts for animation
            ArrayList<FoldingCellView> foldingCellEle = prepareViewsForAnimation(heights, fromTitleView, fromContentView);
            int childCount = foldingCellEle.size();
            int pt90degAnimateDur = mAnimateDur / (childCount * 2);
            // start fold animation with end listener
            startFoldAnimation(foldingCellEle, foldingLayout, pt90degAnimateDur, new AnimationEndListener() {
                @Override
                public void onAnimationEnd(final Animation animation) {
                    contentView.setVisibility(GONE);
                    titleView.setVisibility(VISIBLE);
                    foldingLayout.setVisibility(GONE);
                    FoldingCell.this.removeView(foldingLayout);
                    FoldingCell.this.mAnimationInProgress = false;
                    FoldingCell.this.mUnfolded = false;
                    ViewCompat.setHasTransientState(FoldingCell.this, true);
                }
            });
            startCollapseHeightAnimation(heights, pt90degAnimateDur * 2);
            this.mAnimationInProgress = true;
        }
    }


    /**
     * Toggle current state of FoldingCellLayout
     */
  //NEF fixed methodargumentcouldbefinal
    public void toggle(final boolean skipAnimation) {
        if (this.mUnfolded) {
            this.fold(skipAnimation);
        } else {
            this.unfold(skipAnimation);
            this.requestLayout();
        }
    }

    /**
     * Create and prepare list of FoldingCellViews with different bitmap parts for fold animation
     *
     * @param titleViewBitmap   bitmap from title view
     * @param contentViewBitmap bitmap from content view
     * @return list of FoldingCellViews with bitmap parts
     */
  //NEF fixed methodargumentcouldbefinal
    protected ArrayList<FoldingCellView> prepareViewsForAnimation(final ArrayList<Integer> viewHeights,final Bitmap titleViewBitmap, final Bitmap contentViewBitmap) {
        if (viewHeights == null || viewHeights.isEmpty()) {
            throw new IllegalStateException("ViewHeights array must be not null and not empty");
        }
        ArrayList<FoldingCellView> partsList = new ArrayList<>();

        int partWidth = titleViewBitmap.getWidth();
        int yOffset = 0;
        for (int i = 0; i < viewHeights.size(); i++) {
            int partHeight = viewHeights.get(i);
            Bitmap partBitmap = Bitmap.createBitmap(partWidth, partHeight, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(partBitmap);
            Rect srcRect = new Rect(0, yOffset, partWidth, yOffset + partHeight);
            Rect destRect = new Rect(0, 0, partWidth, partHeight);
            canvas.drawBitmap(contentViewBitmap, srcRect, destRect, null);
            ImageView backView = createImageViewFromBitmap(partBitmap);
            ImageView frontView = null;
            if (i < viewHeights.size() - 1) {
                frontView = (i == 0) ? createImageViewFromBitmap(titleViewBitmap) : createBackSideView(viewHeights.get(i + 1));
            }
            partsList.add(new FoldingCellView(frontView, backView, getContext()));
            yOffset = yOffset + partHeight;
        }

        return partsList;
    }

    /**
     * Calculate heights for animation parts with some logic
     *
     * @param titleViewHeight      height of title view
     * @param contentViewHeight    height of content view
     * @param additionalFlipsCount count of additional flips (after first one), set 0 for auto
     * @return list of calculated heights
     */
  //NEF fixed methodargumentcouldbefinal
    protected ArrayList<Integer> calculateHeightsForAnimationParts(final int titleViewHeight, final int contentViewHeight, final int addFlipsCnt) {
        ArrayList<Integer> partHeights = new ArrayList<>();
        int addPartsTotHeight = contentViewHeight - titleViewHeight * 2;
        if (addPartsTotHeight < 0) {
            throw new IllegalStateException("Content View height is too small");
        }
        // add two main parts - guarantee first flip
        partHeights.add(titleViewHeight);
        partHeights.add(titleViewHeight);

        // if no space left - return
        if (additionalPartsTotalHeight == 0) {
        	return partHeights;
        }
            

        // if some space remained - use two different logic
        if (additionalFlipsCount != 0) {
            // 1 - additional parts count is specified and it is not 0 - divide remained space
            int addPtHeight = additionalPartsTotalHeight / additionalFlipsCount;
            int remainingHeight = additionalPartsTotalHeight % additionalFlipsCount;

            if (addPtHeight + remainingHeight > titleViewHeight) {
                throw new IllegalStateException("Additional flips count is too small");
            }
            for (int i = 0; i < additionalFlipsCount; i++) {
                partHeights.add(addPtHeight + (i == 0 ? remainingHeight : 0));
            }
        } else {
            // 2 - additional parts count isn't specified or 0 - divide remained space to parts with title view size
            int partsCount = additionalPartsTotalHeight / titleViewHeight;
            int restPartHeight = additionalPartsTotalHeight % titleViewHeight;
            for (int i = 0; i < partsCount; i++) {
                partHeights.add(titleViewHeight);
            }
            if (restPartHeight > 0) {
                partHeights.add(restPartHeight);
            }
        }

        return partHeights;
    }

    /**
     * Create image view for display back side of flip view
     *
     * @param height height for view
     * @return ImageView with selected height and default background color
     */
    protected ImageView createBackSideView(final int height) {
        ImageView imageView = new ImageView(getContext());
        imageView.setBackgroundColor(mBackSideColor);
        imageView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
        return imageView;
    }

    /**
     * Create image view for display selected bitmap
     *
     * @param bitmap bitmap to display in image view
     * @return ImageView with selected bitmap
     */
    protected ImageView createImageViewFromBitmap(final Bitmap bitmap) {
        ImageView imageView = new ImageView(getContext());
        imageView.setImageBitmap(bitmap);
        imageView.setLayoutParams(new LayoutParams(bitmap.getWidth(), bitmap.getHeight()));
        return imageView;
    }

    /**
     * Create bitmap from specified View with specified with
     *
     * @param view        source for bitmap
     * @param parentWidth result bitmap width
     * @return bitmap from specified view
     */
  //NEF fixed methodargumentcouldbefinal
    protected Bitmap measureViewAndGetBitmap(final View view, final int parentWidth) {
        int specW = View.MeasureSpec.makeMeasureSpec(parentWidth, View.MeasureSpec.EXACTLY);
        int specH = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(specW, specH);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        Bitmap bit = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bit);
        canvas.translate(-view.getScrollX(), -view.getScrollY());
        view.draw(canvas);
        return bit;
    }

    /**
     * Create layout that will be a container for animation elements
     *
     * @return Configured container for animation elements (LinearLayout)
     */
    protected LinearLayout createAndPrepareFoldingContainer() {
        LinearLayout foldingContainer = new LinearLayout(getContext());
        foldingContainer.setClipToPadding(false);
        foldingContainer.setClipChildren(false);
        foldingContainer.setOrientation(LinearLayout.VERTICAL);
        foldingContainer.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        return foldingContainer;
    }

    /**
     * Prepare and start height expand animation for FoldingCellLayout
     *
     * @param partAnimationDuration one part animate duration
     * @param viewHeights           heights of animation parts
     */
    protected void startExpandHeightAnimation(final ArrayList<Integer> viewHeights, final int ptAnimationDur) {
        if (viewHeights == null || viewHeights.isEmpty()) {
            throw new IllegalArgumentException("ViewHeights array must have at least 2 elements");
        }
        ArrayList<Animation> heightAnimations = new ArrayList<>();
        int fromHeight = viewHeights.get(0);
        int delay = 0;
        int animationDuration = ptAnimationDu - delay;
        for (int i = 1; i < viewHeights.size(); i++) {
            int toHeight = fromHeight + viewHeights.get(i);
            HeightAnimation heightAnimation = new HeightAnimation(this, fromHeight, toHeight, animationDuration)
                    .withInterpolator(new DecelerateInterpolator());
            heightAnimation.setStartOffset(delay);
            heightAnimations.add(heightAnimation);
            fromHeight = toHeight;
        }
        createAnimationChain(heightAnimations, this);
        this.startAnimation(heightAnimations.get(0));
    }

    /**
     * Prepare and start height collapse animation for FoldingCellLayout
     *
     * @param partAnimationDuration one part animate duration
     * @param viewHeights           heights of animation parts
     */
    protected void startCollapseHeightAnimation(final ArrayList<Integer> viewHeights,final int ptAnimateTime) {
        if (viewHeights == null || viewHeights.isEmpty()) {
            throw new IllegalArgumentException("ViewHeights array must have at least 2 elements");
        }
        ArrayList<Animation> heightAnimations = new ArrayList<>();
        int fromHeight = viewHeights.get(0);
        for (int i = 1; i < viewHeights.size(); i++) {
            int toHeight = fromHeight + viewHeights.get(i);
            heightAnimations.add(new HeightAnimation(this, toHeight, fromHeight, ptAnimateTime)
                    .withInterpolator(new DecelerateInterpolator()));
            fromHeight = toHeight;
        }

        Collections.reverse(heightAnimations);
        createAnimationChain(heightAnimations, this);
        this.startAnimation(heightAnimations.get(0));
    }

    /**
     * Create "animation chain" for selected view from list of animation objects
     *
     * @param animationList   collection with animations
     * @param animationObject view for animations
     */
    protected void createAnimationChain(final List<Animation> animationList, final View animationObject) {
        for (int i = 0; i < animationList.size(); i++) {
            Animation animation = animationList.get(i);
            if (i + 1 < animationList.size()) {
                final int finalI = i;
                animation.setAnimationListener(new AnimationEndListener() {
                    public void onAnimationEnd(final Animation animation) {
                        animationObject.startAnimation(animationList.get(finalI + 1));
                    }
                });
            }
        }
    }

    /**
     * Start fold animation
     *
     * @param foldingCellElements           ordered list with animation parts from top to bottom
     * @param foldingLayout                 prepared layout for animation parts
     * @param part90degreeAnimationDuration animation duration for 90 degree rotation
     * @param animationEndListener          animation end callback
     */
    protected void startFoldAnimation(final ArrayList<FoldingCellView> foldingCellEle, final ViewGroup foldingLayout,
                                     final int pt90degAniTime, final AnimationEndListener animateEndLstnr) {
        for (FoldingCellView fCellEle : foldingCellEle) {
            foldingLayout.addView(fCellEle);
        }
        Collections.reverse(foldingCellEle);

        int nextDelay = 0;
        for (int i = 0; i < foldingCellEle.size(); i++) {
            FoldingCellView cell = foldingCellEle.get(i);
            cell.setVisibility(VISIBLE);
            // not FIRST(BOTTOM) element - animate front view
            if (i != 0) {
                FoldAnimation foldAnimation = new FoldAnimation(FoldAnimation.FoldAnimationMode.UNFOLD_UP, mCameraHeight, pt90degAniTime)
                        .withStartOffset(nextDelay)
                        .withInterpolator(new DecelerateInterpolator());
                // if last(top) element - add end listener
                if (i == foldingCellEle.size() - 1) {
                    foldAnimation.setAnimationListener(animationEndListener);
                }
                cell.animateFrontView(foldAnimation);
                nextDelay = nextDelay + pt90degAnimationTime;
            }
            // if not last(top) element - animate whole view
            if (i != foldingCellEle.size() - 1) {
                cell.startAnimation(new FoldAnimation(FoldAnimation.FoldAnimationMode.FOLD_UP, mCameraHeight, pt90degAniTime)
                        .withStartOffset(nextDelay)
                        .withInterpolator(new DecelerateInterpolator()));
                nextDelay = nextDelay + pt90degAniTime;
            }
        }
    }

    /**
     * Start unfold animation
     *
     * @param foldingCellElements           ordered list with animation parts from top to bottom
     * @param foldingLayout                 prepared layout for animation parts
     * @param part90degreeAnimationDuration animation duration for 90 degree rotation
     * @param animationEndListener          animation end callback
     */
    //NEF fixed methodargumentcouldbefinal
    protected void startUnfoldAnimation(final ArrayList<FoldingCellView> foldingCellEle, final ViewGroup foldingLayout,
                                      final int pt90degAnimateDur, final AnimationEndListener animateEndLstnr) {
        int nextDelay = 0;
        for (int i = 0; i < foldingCellEle.size(); i++) {
            FoldingCellView cell = foldingCellEle.get(i);
            cell.setVisibility(VISIBLE);
            foldingLayout.addView(cell);
            // if not first(top) element - animate whole view
            if (i != 0) {
                FoldAnimation foldAnimation = new FoldAnimation(FoldAnimation.FoldAnimationMode.UNFOLD_DOWN, mCameraHeight, pt90degAnimationDur)
                        .withStartOffset(nextDelay)
                        .withInterpolator(new DecelerateInterpolator());

                // if last(bottom) element - add end listener
                if (i == foldingCellEle.size() - 1) {
                    foldAnimation.setAnimationListener(animateEndLstn);
                }

                nextDelay = nextDelay + pt90degAnimationDur;
                cell.startAnimation(foldAnimation);

            }
            // not last(bottom) element - animate front view
            if (i != foldingCellElements.size() - 1) {
                cell.animateFrontView(new FoldAnimation(FoldAnimation.FoldAnimationMode.FOLD_DOWN, mCameraHeight, pt90degAnimationDur)
                        .withStartOffset(nextDelay)
                        .withInterpolator(new DecelerateInterpolator()));
                nextDelay = nextDelay + pt90degAnimationDur;
            }
        }
    }
}
