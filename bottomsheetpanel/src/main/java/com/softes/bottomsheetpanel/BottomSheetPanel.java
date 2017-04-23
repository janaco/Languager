package com.softes.bottomsheetpanel;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;

/**
 * Created by yana on 26.02.17.
 */

public class BottomSheetPanel extends FrameLayout {

    private static final int TAG_BACKGROUND = 1;
    private static final int TAG_PANEL = 2;

    private static final int DEFAULT_BACKGROUND_ID = -1;
    private static final int DEFAULT_TITLE_HEIGHT_NO_DISPLAY = 60;
    private static final int DEFAULT_PANEL_HEIGHT = 380;
    private static final int DEFAULT_MOVE_DISTANCE_TO_TRIGGER = 30;
    private static final int DEFAULT_ANIMATION_DURATION = 250;
    private static final int MAX_CLICK_TIME = 300;
    private static final boolean DEFAULT_FADE = true;
    private static final boolean DEFAULT_BOUNDARY = true;
    private static final boolean DEFAULT_HIDE_PANEL_TITLE = false;

    private static float MAX_CLICK_DISTANCE = 5;

    private float density;
    private boolean isAnimating = false;
    private boolean isPanelShowing = false;

    private float xVelocity;
    private float yVelocity;
    private float touchSlop;
    private int maxVelocity;
    private int minVelocity;
    private VelocityTracker velocityTracker;

    private int measureHeight;
    private float firstDownX;
    private float firstDownY;
    private float downY;
    private float deltaY;
    private long pressStartTime;
    private boolean isDragging = false;

    private int backgroundId;
    private float panelHeight;
    private float titleHeightNoDisplay;
    private float moveDistanceToTrigger;
    private int animationDuration;
    private boolean isFade = true;
    private boolean boundary = true;
    private boolean hidePanelTitle = false;
    private boolean isPanelOnTouch = false;

    private Interpolator openAnimationInterpolator = new AccelerateInterpolator();
    private Interpolator closeAnimationInterpolator = new AccelerateInterpolator();

    private FadingLayout darkFrameLayout;

    public BottomSheetPanel(Context context) {
        this(context, null);
    }

    public BottomSheetPanel(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomSheetPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        density = getResources().getDisplayMetrics().density;

        ViewConfiguration vc = ViewConfiguration.get(context);
        maxVelocity = vc.getScaledMaximumFlingVelocity();
        minVelocity = vc.getScaledMinimumFlingVelocity();
        touchSlop = vc.getScaledTouchSlop();

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BottomSheetPanel, defStyleAttr, 0);

        backgroundId = a.getResourceId(R.styleable.BottomSheetPanel_sbp_background_layout, DEFAULT_BACKGROUND_ID);
        panelHeight = a.getDimension(R.styleable.BottomSheetPanel_sbp_panel_height, dp2px(DEFAULT_PANEL_HEIGHT));
        boundary = a.getBoolean(R.styleable.BottomSheetPanel_sbp_boundary, DEFAULT_BOUNDARY);
        MAX_CLICK_DISTANCE = titleHeightNoDisplay = a.getDimension(R.styleable.BottomSheetPanel_sbp_title_height_no_display, dp2px(DEFAULT_TITLE_HEIGHT_NO_DISPLAY));
        moveDistanceToTrigger = a.getDimension(R.styleable.BottomSheetPanel_sbp_move_distance_trigger, dp2px(DEFAULT_MOVE_DISTANCE_TO_TRIGGER));
        animationDuration = a.getInt(R.styleable.BottomSheetPanel_sbp_animation_duration, DEFAULT_ANIMATION_DURATION);
        hidePanelTitle = a.getBoolean(R.styleable.BottomSheetPanel_sbp_hide_panel_title, DEFAULT_HIDE_PANEL_TITLE);
        isFade = a.getBoolean(R.styleable.BottomSheetPanel_sbp_fade, DEFAULT_FADE);

        a.recycle();

        initBackgroundView();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int mChildCount = getChildCount();
        int t = (int) (measureHeight - titleHeightNoDisplay);
        for (int i = 0; i < mChildCount; i++) {
            View childView = getChildAt(i);
            if (childView.getTag() == null || (int) childView.getTag() != TAG_BACKGROUND) {
                childView.layout(0, t, childView.getMeasuredWidth(), childView.getMeasuredHeight() + t);
                childView.setTag(TAG_PANEL);
                if (childView instanceof ViewGroup) {
                    ((ViewGroup)childView).setClipChildren(false);
                }
            } else if ((int) childView.getTag() == TAG_BACKGROUND) {
                childView.layout(0, 0, childView.getMeasuredWidth(), childView.getMeasuredHeight());
                childView.setPadding(0, 0, 0, (int) titleHeightNoDisplay);
            }
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return isDragging;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureHeight = getMeasuredHeight();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        initVelocityTracker(ev);
        boolean isConsume = false;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isConsume = handleActionDown(ev);
                break;
            case MotionEvent.ACTION_MOVE:
                handleActionMove(ev);
                break;
            case MotionEvent.ACTION_UP:
                handleActionUp(ev);
                releaseVelocityTracker();
                break;
        }
        return isConsume || super.dispatchTouchEvent(ev);
    }

    private void initBackgroundView() {
        if (backgroundId != -1) {
            darkFrameLayout = new FadingLayout(getContext());
            darkFrameLayout.addView(LayoutInflater.from(getContext()).inflate(backgroundId, null));
            darkFrameLayout.setTag(TAG_BACKGROUND);
            darkFrameLayout.setSlideBottomPanel(this);
            addView(darkFrameLayout);
        }
    }

    private void handleActionUp(MotionEvent event) {
        if (!isPanelOnTouch) {
            return;
        }
        long pressDuration = System.currentTimeMillis() - pressStartTime;
        computeVelocity();
        if (!isPanelShowing && ((event.getY() - firstDownY) < 0 && (Math.abs(event.getY() - firstDownY) > moveDistanceToTrigger))
                || (yVelocity < 0 && Math.abs(yVelocity) > Math.abs(xVelocity) && Math.abs(yVelocity) > minVelocity)) {
            displayPanel();
        } else if (!isPanelShowing && pressDuration < MAX_CLICK_TIME &&
                distance(firstDownX, firstDownY, event.getX(), event.getY()) < MAX_CLICK_DISTANCE) {
            displayPanel();
        } else if (!isPanelShowing && isDragging && ((event.getY() - firstDownY > 0) ||
                Math.abs(event.getY() - firstDownY) < moveDistanceToTrigger)) {
            hidePanel();
        }

        if (isPanelShowing) {
            View mPanel = findViewWithTag(TAG_PANEL);
            float currentY = ViewHelper.getY(mPanel);
            if (currentY < (measureHeight - panelHeight) ||
                    currentY < (measureHeight - panelHeight + moveDistanceToTrigger)) {
                ObjectAnimator.ofFloat(mPanel, "y", currentY, measureHeight - panelHeight)
                        .setDuration(animationDuration).start();
            } else if (currentY > measureHeight - panelHeight + moveDistanceToTrigger) {
                hidePanel();
            }
        }

        isPanelOnTouch = false;
        isDragging = false;
        deltaY = 0;
    }

    private void handleActionMove(MotionEvent event) {
        if (!isPanelOnTouch) {
            return;
        }
        if (isPanelShowing && supportScrollInView((int) (firstDownY - event.getY()))) {
            return;
        }
        computeVelocity();
        if (Math.abs(xVelocity) > Math.abs(yVelocity)) {
            return;
        }
        if (!isDragging && Math.abs(event.getY() - firstDownY) > touchSlop
                && Math.abs(event.getX() - firstDownX) < touchSlop) {
            isDragging = true;
            downY = event.getY();
        }
        if (isDragging) {
            deltaY = event.getY() - downY;
            downY = event.getY();

            View touchingView = findViewWithTag(TAG_PANEL);

            if (hidePanelTitle && isPanelShowing) {
                hidePanelTitle(touchingView);
            }

            if (darkFrameLayout != null && isFade) {
                float currentY = ViewHelper.getY(touchingView);
                if (currentY > measureHeight - panelHeight &&
                        currentY < measureHeight - titleHeightNoDisplay) {
                    darkFrameLayout.fade(
                            (int) ((1 - currentY / (measureHeight - titleHeightNoDisplay)) * FadingLayout.MAX_ALPHA));
                }
            }
            if (!boundary) {
                touchingView.offsetTopAndBottom((int) deltaY);
            } else {
                float touchingViewY = ViewHelper.getY(touchingView);
                if (touchingViewY + deltaY <= measureHeight - panelHeight) {
                    touchingView.offsetTopAndBottom((int) (measureHeight - panelHeight - touchingViewY));
                } else if (touchingViewY + deltaY >= measureHeight - titleHeightNoDisplay) {
                    touchingView.offsetTopAndBottom((int) (measureHeight - titleHeightNoDisplay - touchingViewY));
                } else {
                    touchingView.offsetTopAndBottom((int) deltaY);
                }
            }
        }
    }

    private boolean handleActionDown(MotionEvent event) {
        boolean isConsume = false;
        pressStartTime = System.currentTimeMillis();
        firstDownX = event.getX();
        firstDownY = downY = event.getY();
        if (!isPanelShowing && downY > measureHeight - titleHeightNoDisplay) {
            isPanelOnTouch = true;
            isConsume = true;
        } else if (!isPanelShowing && downY <= measureHeight - titleHeightNoDisplay) {
            isPanelOnTouch = false;
        } else if (isPanelShowing && downY > measureHeight - panelHeight) {
            isPanelOnTouch = true;
        } else if (isPanelShowing && downY < measureHeight - panelHeight) {
            hidePanel();
            isPanelOnTouch = false;
        }
        return isConsume;
    }

    private void hidePanel() {
        if (isAnimating) {
            return;
        }
        final View mPanel = findViewWithTag(TAG_PANEL);
        final int t = (int) (measureHeight - titleHeightNoDisplay);
        ValueAnimator animator = ValueAnimator.ofFloat(
                ViewHelper.getY(mPanel), measureHeight - titleHeightNoDisplay);
        animator.setInterpolator(closeAnimationInterpolator);
        animator.setTarget(mPanel);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                ViewHelper.setY(mPanel, value);
                if (darkFrameLayout != null && isFade && value < t) {
                    darkFrameLayout.fade((int) ((1 - value / t) * FadingLayout.MAX_ALPHA));
                }
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isAnimating = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimating = false;
                isPanelShowing = false;
                showPanelTitle(mPanel);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                isAnimating = false;
                isPanelShowing = false;
                showPanelTitle(mPanel);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        animator.start();
    }

    public void displayPanel() {
        if (isPanelShowing || isAnimating) {
            return;
        }
        if (isFade || darkFrameLayout != null) {
            darkFrameLayout.fade(true);
        }
        final View mPanel = findViewWithTag(TAG_PANEL);
        ValueAnimator animator = ValueAnimator.ofFloat(ViewHelper.getY(mPanel), measureHeight - panelHeight)
                .setDuration(animationDuration);
        animator.setTarget(mPanel);
        animator.setInterpolator(openAnimationInterpolator);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                ViewHelper.setY(mPanel, value);
                if (darkFrameLayout != null && isFade
                        && darkFrameLayout.getCurrentAlpha() != FadingLayout.MAX_ALPHA) {
                    darkFrameLayout.fade(
                            (int) ((1 - value / (measureHeight - titleHeightNoDisplay)) * FadingLayout.MAX_ALPHA));
                }
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isAnimating = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimating = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                isAnimating = false;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        animator.start();
        isPanelShowing = true;
        hidePanelTitle(mPanel);
    }

    private void showPanelTitle(View panel) {
        if (panel instanceof ViewGroup && hidePanelTitle) {
            try {
                View childView = ((ViewGroup) panel).getChildAt(1);
                if (childView.getVisibility() != View.VISIBLE) {
                    childView.layout(0, 0, childView.getMeasuredWidth(), childView.getMeasuredHeight());
                    childView.setVisibility(View.VISIBLE);
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    private void hidePanelTitle(View panel) {
        if (panel instanceof ViewGroup && hidePanelTitle) {
            try {
                ((ViewGroup) panel).getChildAt(1).setVisibility(View.INVISIBLE);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    public void hide() {
        if (!isPanelShowing) return;
        hidePanel();
    }

    private void computeVelocity() {
        //units是单位表示， 1代表px/毫秒, 1000代表px/秒
        velocityTracker.computeCurrentVelocity(1000, maxVelocity);
        xVelocity = velocityTracker.getXVelocity();
        yVelocity = velocityTracker.getYVelocity();
    }

    private boolean supportScrollInView(int direction) {

        View view = findViewWithTag(TAG_PANEL);
        if (view instanceof ViewGroup) {
            View childView = findTopChildUnder((ViewGroup) view, firstDownX, firstDownY);
            if (childView == null) {
                return false;
            }
            if (childView instanceof AbsListView) {
                AbsListView absListView = (AbsListView) childView;
                if (Build.VERSION.SDK_INT >= 19) {
                    return absListView.canScrollList(direction);
                } else {
                    return absListViewCanScrollList(absListView, direction);
                }
            } else if (childView instanceof ScrollView) {
                ScrollView scrollView = (ScrollView) childView;
                if (Build.VERSION.SDK_INT >= 14) {
                    return scrollView.canScrollVertically(direction);
                } else {
                    return scrollViewCanScrollVertically(scrollView, direction);
                }

            } else if (childView instanceof ViewGroup) {
                View grandchildView = findTopChildUnder((ViewGroup) childView, firstDownX, firstDownY);
                if (grandchildView == null) {
                    return false;
                }
                if (grandchildView instanceof ViewGroup) {
                    if (grandchildView instanceof AbsListView) {
                        AbsListView absListView = (AbsListView) grandchildView;
                        if (Build.VERSION.SDK_INT >= 19) {
                            return absListView.canScrollList(direction);
                        } else {
                            return absListViewCanScrollList(absListView, direction);
                        }
                    } else if (grandchildView instanceof ScrollView) {
                        ScrollView scrollView = (ScrollView) grandchildView;
                        if (Build.VERSION.SDK_INT >= 14) {
                            return scrollView.canScrollVertically(direction);
                        } else {
                            return scrollViewCanScrollVertically(scrollView, direction);
                        }
                    }
                }
            }


        }
        return false;
    }

    private View findTopChildUnder(ViewGroup parentView, float x, float y) {
        int childCount = parentView.getChildCount();
        for (int i = childCount - 1; i >= 0; i--) {
            final View child = parentView.getChildAt(i);
            if (x >= child.getLeft() && x < child.getRight() &&
                    y >= child.getTop() + measureHeight - panelHeight &&
                    y < child.getBottom() + measureHeight - panelHeight) {
                return child;
            }
        }
        return null;
    }

    /**
     * Copy From ScrollView (API Level >= 14)
     *
     * @param direction Negative to check scrolling up, positive to check
     *                  scrolling down.
     * @return true if the scrollView can be scrolled in the specified direction,
     * false otherwise
     */
    private boolean scrollViewCanScrollVertically(ScrollView scrollView, int direction) {
        final int offset = Math.max(0, scrollView.getScrollY());
        final int range = computeVerticalScrollRange(scrollView) - scrollView.getHeight();
        if (range == 0) return false;
        if (direction < 0) { //scroll up
            return offset > 0;
        } else {//scroll down
            return offset < range - 1;
        }
    }

    /**
     * Copy From ScrollView (API Level >= 14)
     * <p>The scroll range of a scroll view is the overall height of all of its
     * children.</p>
     */
    private int computeVerticalScrollRange(ScrollView scrollView) {
        final int count = scrollView.getChildCount();
        final int contentHeight = scrollView.getHeight() - scrollView.getPaddingBottom() - scrollView.getPaddingTop();
        if (count == 0) {
            return contentHeight;
        }

        int scrollRange = scrollView.getChildAt(0).getBottom();
        final int scrollY = scrollView.getScrollY();
        final int overScrollBottom = Math.max(0, scrollRange - contentHeight);
        if (scrollY < 0) {
            scrollRange -= scrollY;
        } else if (scrollY > overScrollBottom) {
            scrollRange += scrollY - overScrollBottom;
        }

        return scrollRange;
    }

    /**
     * Copy From AbsListView (API Level >= 19)
     *
     * @param absListView AbsListView
     * @param direction   Negative to check scrolling up, positive to check
     *                    scrolling down.
     * @return true if the list can be scrolled in the specified direction,
     * false otherwise
     */
    private boolean absListViewCanScrollList(AbsListView absListView, int direction) {
        final int childCount = absListView.getChildCount();
        if (childCount == 0) {
            return false;
        }
        final int firstPosition = absListView.getFirstVisiblePosition();
        if (direction > 0) {//can scroll down
            final int lastBottom = absListView.getChildAt(childCount - 1).getBottom();
            final int lastPosition = firstPosition + childCount;
            return lastPosition < absListView.getCount() || lastBottom > absListView.getHeight() - absListView.getPaddingTop();
        } else {//can scroll  up
            final int firstTop = absListView.getChildAt(0).getTop();
            return firstPosition > 0 || firstTop < absListView.getPaddingTop();
        }
    }

    private void initVelocityTracker(MotionEvent event) {
        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain();
        }
        velocityTracker.addMovement(event);
    }

    private void releaseVelocityTracker() {
        if (velocityTracker != null) {
            velocityTracker.clear();
            velocityTracker.recycle();
            velocityTracker = null;
        }
    }

    private double distance(float x1, float y1, float x2, float y2) {
        float deltaX = x1 - x2;
        float deltaY = y1 - y2;
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }

    private int px2dp(int pxValue) {
        return (int) (pxValue / density + 0.5f);
    }

    private int dp2px(int dpValue) {
        return (int) (dpValue * density + 0.5f);
    }

    public boolean isPanelShowing() {
        return isPanelShowing;
    }
}