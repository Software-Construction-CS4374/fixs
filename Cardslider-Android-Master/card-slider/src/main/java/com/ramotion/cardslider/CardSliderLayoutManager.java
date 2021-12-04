package com.ramotion.cardslider;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PointF;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;

/**
 * A {@link androidx.recyclerview.widget.RecyclerView.LayoutManager} implementation.
 */
public class CardSliderLayoutManager extends RecyclerView.LayoutManager
        implements RecyclerView.SmoothScroller.ScrollVectorProvider {

    private static final int DEFAULT_ACTIVE_CARD_LEFT_OFFSET = 50;
    private static final int DEFAULT_CARD_WIDTH = 148;
    private static final int DEFAULT_CARDS_GAP = 12;
    private static final int LEFT_CARD_COUNT = 2;

    private final SparseArray<View> viewCache = new SparseArray<>();
    private final SparseIntArray cardsXCoords = new SparseIntArray();

    private int cardWidth;
    private int activeCardLeft;
    private int activeCardRight;
    private int activeCardCenter;

    private float cardsGap;

    final int scrollPos= 0;

    private ViewUpdater viewUpdater;
    private RecyclerView recyclerView;

    /**
     * A ViewUpdater is invoked whenever a visible/attached card is scrolled.
     */
    public interface ViewUpdater {
        /**
         * Called when CardSliderLayoutManager initialized
         */
        void onLayoutManagerInitialized(@NonNull CardSliderLayoutManager layout);

        /**
         * Called on view update (scroll, layout).
         * @param view      Updating view
         * @param position  Position of card relative to the current active card position of the layout manager.
         *                  0 is active card. 1 is first right card, and -1 is first left (stacked) card.
         */
        void updateView(@NonNull View view, float position);
    }

    private static class SavedState implements Parcelable {

        int anchorPos;

        SavedState() {

        }

        SavedState(Parcel inPar) {
            anchorPos = inPar.readInt();
        }

        public SavedState(SavedState other) {
            anchorPos = other.anchorPos;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int index) {
            parcel.writeInt(anchorPos);
        }

        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };

    }

    /**
     * Creates CardSliderLayoutManager with default values
     *
     * @param context   Current context, will be used to access resources.
     */
    public CardSliderLayoutManager(@NonNull Context context) {
        this(context, null, 0, 0);
    }

    /**
     * Constructor used when layout manager is set in XML by RecyclerView attribute
     * "layoutManager".
     *
     * See {@link R.styleable#CardSlider_activeCardLeftOffset}
     * See {@link R.styleable#CardSlider_cardWidth}
     * See {@link R.styleable#CardSlider_cardsGap}
     */
    public CardSliderLayoutManager(final @NonNull Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        final float density = context.getResources().getDisplayMetrics().density;

        final int defaultCardWidth = (int) (DEFAULT_CARD_WIDTH * density);
        final int defaultACLeft = (int) (DEFAULT_ACTIVE_CARD_LEFT_OFFSET * density);
        final float defaultCardsGap = DEFAULT_CARDS_GAP * density;

        if (attrs == null) {
            initialize(defaultACLeft, defaultCardWidth, defaultCardsGap, null);
        } else {
            int attrCardWidth;
            int attrACLeft;
            float attrCardsGap;
            String updateClassName;

            final TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CardSlider, 0, 0);
            try {
                attrCardWidth = array.getDimensionPixelSize(R.styleable.CardSlider_cardWidth, defaultCardWidth);
                attrACLeft = array.getDimensionPixelSize(R.styleable.CardSlider_activeCardLeftOffset, defaultACLeft);
                attrCardsGap = array.getDimension(R.styleable.CardSlider_cardsGap, defaultCardsGap);
                updateClassName = array.getString(R.styleable.CardSlider_viewUpdater);
            } finally {
                a.recycle();
            }

            final ViewUpdater viewUpdater = loadViewUpdater(context, updateClassName, attrs);
            initialize(attrACLeft, attrCardWidth, attrCardsGap, viewUpdater);
        }
    }

    /**
     * Creates CardSliderLayoutManager with specified values in pixels.
     *
     * @param activeCardLeft    Active card offset from start of RecyclerView. Default value is 50dp.
     * @param cardWidth         Card width. Default value is 148dp.
     * @param cardsGap          Distance between cards. Default value is 12dp.
     */
    public CardSliderLayoutManager(final int activeCardLeft, final int cardWidth, final float cardsGap) {
        initialize(activeCardLeft, cardWidth, cardsGap, null);
    }

    private void initialize(final int left, final int width, final float gap, final @Nullable ViewUpdater updater) {
        this.cardWidth = width;
        this.activeCardLeft = left;
        this.activeCardRight = activeCardLeft + cardWidth;
        this.activeCardCenter = activeCardLeft + ((this.activeCardRight - activeCardLeft) / 2);
        this.cardsGap = gap;

        this.viewUpdater = updater;
        if (this.viewUpdater == null) {
            this.viewUpdater = new DefaultViewUpdater();
        }
        viewUpdater.onLayoutManagerInitialized(this);
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.WRAP_CONTENT,
                RecyclerView.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(final RecyclerView.Recycler recycler, final RecyclerView.State state) {
        if (getItemCount() == 0) {
            removeAndRecycleAllViews(recycler);
            return;
        }

        if (getChildCount() == 0 && state.isPreLayout()) {
            return;
        }

        int anchorPos = getActiveCardPosition();

        if (state.isPreLayout()) {
            final LinkedList<Integer> removed = new LinkedList<>();
            for (int i = 0, cnt = getChildCount(); i < cnt; i++) {
                final View child = getChildAt(i);
                
                final boolean isRemoved = ((RecyclerView.LayoutParams)child.getLayoutParams()).isItemRemoved();
                if (isRemoved) {
                    removed.add(getPosition(child));
                }
            }

            if (removed.contains(anchorPos)) {
                final int first = removed.getFirst();
                final int last = removed.getLast();

                final int left = first - 1;
                final int right = last == getItemCount() + removed.size() - 1 ? RecyclerView.NO_POSITION : last;

                anchorPos = Math.max(left, right);
            }

            scrollRequestedPosition = anchorPos;
        }

        detachAndScrapAttachedViews(recycler);
        fill(anchorPos, recycler, state);

        if (cardsXCoords.size() != 0) {
            layoutByCoords();
        }

        if (state.isPreLayout()) {
            recyclerView.postOnAnimationDelayed(new Runnable() {
                @Override
                public void run() {
                    updateViewScale();
                }
            }, 415);
        } else {
            updateViewScale();
        }
    }

    @Override
    public boolean supportsPredictiveItemAnimations() {
        return true;
    }

    @Override
    public void onAdapterChanged(final RecyclerView.Adapter oldAdapter,final RecyclerView.Adapter newAdapter) {
        removeAllViews();
    }

    @Override
    public boolean canScrollHorizontally() {
        return getChildCount() != 0;
    }

    @Override
    public void scrollToPosition(final int position) {
        if (position < 0 || position >= getItemCount()) {
            return;
        }

        scrollRequestedPosition = position;
        requestLayout();
    }

    @Override
    public int scrollHorizontallyBy(final int deltaX, final RecyclerView.Recycler recycler, final RecyclerView.State state) {
        scrollRequestedPosition = RecyclerView.NO_POSITION;

        int delta;
        if (deltaX < 0) {
            delta = scrollRight(Math.max(deltaX, -cardWidth));
        } else {
            delta = scrollLeft(deltaX);
        }

        fill(getActiveCardPosition(), recycler, state);
        updateViewScale();

        cardsXCoords.clear();
        for (int i = 0, cnt = getChildCount(); i < cnt; i++) {
            final View view = getChildAt(i);
            cardsXCoords.put(getPosition(view), getDecoratedLeft(view));
        }

        return delta;
    }

    @Override
    public PointF computeScrollVectorForPosition(final int targetPosition) {
        return new PointF(targetPosition - getActiveCardPosition(), 0);
    }

    @Override
    public void smoothScrollToPosition(final RecyclerView recyclerView,final RecyclerView.State state, final int position) {
        if (position < 0 || position >= getItemCount()) {
            return;
        }

        final LinearSmoothScroller scroller = getSmoothScroller(recyclerView);
        scroller.setTargetPosition(position);
        startSmoothScroll(scroller);
    }

    @Override
    public void onItemsRemoved(final RecyclerView recyclerView,final int positionStart,final int count) {
        final int anchorPos = getActiveCardPosition();
        if (positionStart + count <= anchorPos) {
            scrollRequestedPosition = anchorPos - 1;
        }
    }

    @Override
    public Parcelable onSaveInstanceState() {
        SavedState state = new SavedState();
        state.anchorPos = getActiveCardPosition();
        return state;
    }

    @Override
    public void onRestoreInstanceState(final Parcelable parcelable) {
        if (parcelable instanceof SavedState) {
            SavedState state = (SavedState) parcelable;
            scrollRequestedPosition = state.anchorPos;
            requestLayout();
        }
    }

    @Override
    public void onAttachedToWindow(final RecyclerView view) {
        super.onAttachedToWindow(view);
        recyclerView = view;
    }

    @Override
    public void onDetachedFromWindow(final RecyclerView view,final RecyclerView.Recycler recycler) {
        super.onDetachedFromWindow(view, recycler);
        recyclerView = null;
    }

    /**
     * @return active card position or RecyclerView.NO_POSITION
     */
    public int getActiveCardPosition() {
        if (scrollRequestedPosition != RecyclerView.NO_POSITION) {
            return scrollRequestedPosition;
        } else {
            int result = RecyclerView.NO_POSITION;

            View biggestView = null;
            float lastScaleX = 0f;

            for (int i = 0, cnt = getChildCount(); i < cnt; i++) {
                final View child = getChildAt(i);
                final int viewLeft = getDecoratedLeft(child);
                if (viewLeft >= activeCardRight) {
                    continue;
                }

                final float scaleX = ViewCompat.getScaleX(child);
                if (lastScaleX < scaleX && viewLeft < activeCardCenter) {
                    lastScaleX = scaleX;
                    biggestView = child;
                }
            }

            if (biggestView != null) {
                result = getPosition(biggestView);
            }

            return result;
        }
    }

    @Nullable
    public View getTopView() {
        if (getChildCount() == 0) {
            return null;
        }

        View result = null;
        float lastValue = cardWidth;

        for (int i = 0, cnt = getChildCount(); i < cnt; i++) {
            final View child = getChildAt(i);
            if (getDecoratedLeft(child) >= activeCardRight) {
                continue;
            }

            final int viewLeft = getDecoratedLeft(child);
            final int diff = activeCardRight - viewLeft;
            if (diff < lastValue) {
                lastValue = diff;
                result = child;
            }
        }

        return result;
    }

    public int getActiveCardLeft() {
        return activeCardLeft;
    }

    public int getActiveCardRight() {
        return activeCardRight;
    }

    public int getActiveCardCenter() {
        return activeCardCenter;
    }

    public int getCardWidth() {
        return cardWidth;
    }

    public float getCardsGap() {
        return cardsGap;
    }

    public LinearSmoothScroller getSmoothScroller(final RecyclerView recyclerView) {
        return new LinearSmoothScroller(recyclerView.getContext()) {
            @Override
            public int calculateDxToMakeVisible(View view, int snapPreference) {
                final int viewStart = getDecoratedLeft(view);
                if (viewStart > activeCardLeft) {
                    return activeCardLeft - viewStart;
                } else {
                    int delta = 0;
                    int topViewPos = 0;

                    final View topView = getTopView();
                    if (topView != null) {
                        topViewPos = getPosition(topView);
                        if (topViewPos != getTargetPosition()) {
                            final int topViewLeft = getDecoratedLeft(topView);
                            if (topViewLeft >= activeCardLeft && topViewLeft < activeCardRight) {
                                delta = activeCardRight - topViewLeft;
                            }
                        }
                    }

                    return delta + (cardWidth) * Math.max(0, topViewPos - getTargetPosition() - 1);
                }
            }

            @Override
            protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                return 0.5f;
            }

        };
    }

    private ViewUpdater loadViewUpdater(final Context context, final String className, final AttributeSet attrs) {
        if (className == null || className.trim().length() == 0) {
            return null;
        }

        final String fullClassName;
        if (className.charAt(0) == '.') {
            fullClassName = context.getPackageName() + className;
        } else if (className.contains(".")) {
            fullClassName = className;
        } else {
            fullClassName = CardSliderLayoutManager.class.getPackage().getName() + '.' + className;
        }

        ViewUpdater updater;
        try {
            final ClassLoader classLoader = context.getClassLoader();

            final Class<? extends ViewUpdater> viewUpdaterClass =
                    classLoader.loadClass(fullClassName).asSubclass(ViewUpdater.class);
            final Constructor<? extends ViewUpdater> constructor =
                    viewUpdaterClass.getConstructor();

            constructor.setAccessible(true);
            updater = constructor.newInstance();
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException(attrs.getPositionDescription() +
                    ": Error creating LayoutManager " + className, e);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(attrs.getPositionDescription()
                    + ": Unable to find ViewUpdater" + className, e);
        } catch (InvocationTargetException e) {
            throw new IllegalStateException(attrs.getPositionDescription()
                    + ": Could not instantiate the ViewUpdater: " + className, e);
        } catch (InstantiationException e) {
            throw new IllegalStateException(attrs.getPositionDescription()
                    + ": Could not instantiate the ViewUpdater: " + className, e);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(attrs.getPositionDescription()
                    + ": Cannot access non-public constructor " + className, e);
        } catch (ClassCastException e) {
            throw new IllegalStateException(attrs.getPositionDescription()
                    + ": Class is not a ViewUpdater " + className, e);
        }

        return updater;
    }

    private int scrollRight(final int deltaX) {
        final int childCount = getChildCount();

        if (childCount == 0) {
            return 0;
        }

        final View rightestView = getChildAt(childCount - 1);
        final int deltaBorder = activeCardLeft + getPosition(rightestView) * cardWidth;
        final int delta = getAllowedRightDelta(rightestView, deltaX, deltaBorder);

        final LinkedList<View> rightViews = new LinkedList<>();
        final LinkedList<View> leftViews = new LinkedList<>();

        for (int i = childCount - 1; i >= 0; i--) {
            final View view = getChildAt(i);
            final int viewLeft = getDecoratedLeft(view);

            if (viewLeft >= activeCardRight) {
                rightViews.add(view);
            } else {
                leftViews.add(view);
            }
        }

        for (View view: rightViews) {
            final int border = activeCardLeft + getPosition(view) * cardWidth;
            final int allowedDelta = getAllowedRightDelta(view, dx, border);
            view.offsetLeftAndRight(-allowedDelta);
        }

        final int step = activeCardLeft / LEFT_CARD_COUNT;
        final int jDelta = (int) Math.floor(1f * delta * step / cardWidth);

        View prevView = null;
        int offSet = 0;

        for (int i = 0, cnt = leftViews.size(); i < cnt; i++) {
            final View view = leftViews.get(i);
            if (prevView == null || getDecoratedLeft(prevView) >= activeCardRight) {
                final int border = activeCardLeft + getPosition(view) * cardWidth;
                final int allowedDelta = getAllowedRightDelta(view, deltaX, border);
                view.offsetLeftAndRight(-allowedDelta);
            } else {
                final int border = activeCardLeft - step * offSet;
                view.offsetLeftAndRight(-getAllowedRightDelta(view, jDelta, border));
                j++;
            }

            prevView = view;
        }

        return delta;
    }

    private int scrollLeft(final int deltaX) {
        final int childCount = getChildCount();
        if (childCount == 0) {
            return 0;
        }

        final View lastView = getChildAt(childCount - 1);
        final boolean isLastItem = getPosition(lastView) == getItemCount() - 1;

        final int delta;
        if (isLastItem) {
            delta = Math.min(dx, getDecoratedRight(lastView) - activeCardRight);
        } else {
            delta = deltaX;
        }

        final int step = activeCardLeft / LEFT_CARD_COUNT;
        final int jDelta = (int) Math.ceil(1f * delta * step / cardWidth);

        for (int i = childCount - 1; i >= 0; i--) {
            final View view = getChildAt(i);
            final int viewLeft = getDecoratedLeft(view);

            if (viewLeft > activeCardLeft) {
                view.offsetLeftAndRight(getAllowedLeftDelta(view, delta, activeCardLeft));
            } else {
                int border = activeCardLeft - step;
                for (int j = i; j >= 0; j--) {
                    final View jView = getChildAt(j);
                    jView.offsetLeftAndRight(getAllowedLeftDelta(jView, jDelta, border));
                    border -= step;
                }

                break;
            }
        }

        return delta;
    }

    private int getAllowedLeftDelta(final @NonNull View view,final int deltaX,final int border) {
        final int viewLeft = getDecoratedLeft(view);
        if (viewLeft - deltaX > border) {
            return -deltaX;
        } else {
            return border - viewLeft;
        }
    }

    private int getAllowedRightDelta(final @NonNull View view,final int deltaX,final int border) {
        final int viewLeft = getDecoratedLeft(view);
        if (viewLeft + Math.abs(deltaX) < border) {
            return deltaX;
        } else {
            return viewLeft - border;
        }
    }

    private void layoutByCoords() {
        final int count = Math.min(getChildCount(), cardsXCoords.size());
        for (int i = 0; i < count; i++) {
            final View view = getChildAt(i);
            final int viewLeft = cardsXCoords.get(getPosition(view));
            layoutDecorated(view, viewLeft, 0, viewLeft + cardWidth, getDecoratedBottom(view));
        }
        cardsXCoords.clear();
    }

    private void fill(final int anchorPos, RecyclerView.Recycler recycler,final RecyclerView.State state) {
        viewCache.clear();

        for (int i = 0, cnt = getChildCount(); i < cnt; i++) {
            View view = getChildAt(i);
            int pos = getPosition(view);
            viewCache.put(pos, view);
        }

        for (int i = 0, cnt = viewCache.size(); i < cnt; i++) {
            detachView(viewCache.valueAt(i));
        }

        if (!state.isPreLayout()) {
            fillLeft(anchorPos, recycler);
            fillRight(anchorPos, recycler);
        }

        for (int i = 0, cnt = viewCache.size(); i < cnt; i++) {
            recycler.recycleView(viewCache.valueAt(i));
        }
    }

    private void fillLeft(final int anchorPos, final RecyclerView.Recycler recycler) {
        if (anchorPos == RecyclerView.NO_POSITION) {
            return;
        }

        final int layoutStep = activeCardLeft / LEFT_CARD_COUNT;
        int pos = Math.max(0, anchorPos - LEFT_CARD_COUNT - 1);
        int viewLeft = Math.max(-1, LEFT_CARD_COUNT - (anchorPos - pos)) * layoutStep;

        while (pos < anchorPos) {
            View view = viewCache.get(pos);
            if (view != null) {
                attachView(view);
                viewCache.remove(pos);
            } else {
                view = recycler.getViewForPosition(pos);
                addView(view);
                measureChildWithMargins(view, 0, 0);
                final int viewHeight = getDecoratedMeasuredHeight(view);
                layoutDecorated(view, viewLeft, 0, viewLeft + cardWidth, viewHeight);
            }

            viewLeft += layoutStep;
            pos++;
        }

    }

    private void fillRight(final int anchorPos,final RecyclerView.Recycler recycler) {
        if (anchorPos == RecyclerView.NO_POSITION) {
            return;
        }

        final int width = getWidth();
        final int itemCount = getItemCount();

        int pos = anchorPos;
        int viewLeft = activeCardLeft;
        boolean fillRight = true;

        while (fillRight && pos < itemCount) {
            View view = viewCache.get(pos);
            if (view != null) {
                attachView(view);
                viewCache.remove(pos);
            } else {
                view = recycler.getViewForPosition(pos);
                addView(view);
                measureChildWithMargins(view, 0, 0);
                final int viewHeight = getDecoratedMeasuredHeight(view);
                layoutDecorated(view, viewLeft, 0, viewLeft + cardWidth, viewHeight);
            }

            viewLeft = getDecoratedRight(view);
            fillRight = viewLeft < width + cardWidth;
            pos++;
        }
    }

    private void updateViewScale() {
        for (int i = 0, cnt = getChildCount(); i < cnt; i++) {
            final View view = getChildAt(i);
            final int viewLeft = getDecoratedLeft(view);

            final float position = ((float) (viewLeft - activeCardLeft) / cardWidth);
            viewUpdater.updateView(view, position);
        }
    }

}