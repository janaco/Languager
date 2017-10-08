package com.softes.flippy;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;

/**
 * Created by yana on 27.05.17.
 */

public class FlipView extends FrameLayout
        implements Animation.AnimationListener, View.OnClickListener, OnSwipeListener, ToggleListener {
    public static final int ANIM_DURATION_MILLIS = 500;
    private static final Interpolator fDefaultInterpolator = new DecelerateInterpolator();
    private OnFlipListener listener;
    private FlipAnimator animator;
    private boolean isFlipped;
    private OnFlipTouchListener touchListener;
    private View frontView, backView;

    public FlipView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public FlipView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FlipView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        animator = new FlipAnimator();
        animator.setAnimationListener(this);
        animator.setInterpolator(fDefaultInterpolator);
        animator.setDuration(ANIM_DURATION_MILLIS);
        animator.setDirection(Direction.DOWN);
        animator.setToggleListener(this);
        setSoundEffectsEnabled(true);
        touchListener = new OnFlipTouchListener(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        if (getChildCount() > 2) {
            throw new IllegalStateException("FlipLayout can host only two direct children");
        }

        frontView = getChildAt(0);
        frontView.setOnTouchListener(touchListener);
        frontView.setOnClickListener(this);
        backView = getChildAt(1);
        backView.setOnTouchListener(touchListener);
        backView.setOnClickListener(this);
        touchListener.addSwipeListener(this);
        reset();
    }

    public void toggleView() {
        if (frontView == null || backView == null) {
            return;
        }

        if (isFlipped) {
            frontView.setVisibility(View.VISIBLE);
            backView.setVisibility(View.GONE);
        } else {
            frontView.setVisibility(View.GONE);
            backView.setVisibility(View.VISIBLE);
        }

        isFlipped = !isFlipped;
    }

    public void setOnFlipListener(OnFlipListener listener) {
        this.listener = listener;
    }

    public void reset() {
        isFlipped = false;
        animator.setDirection(Direction.DOWN);
        frontView.setVisibility(View.VISIBLE);
        backView.setVisibility(View.GONE);
    }

    public void toggleUp() {
        animator.setDirection(Direction.UP);
        startAnimation();
    }

    public void toggleDown() {
        animator.setDirection(Direction.DOWN);
        startAnimation();
    }

    public void toggleLeft() {
        animator.setDirection(Direction.LEFT);
        startAnimation();
    }

    public void toggleRight() {
        animator.setDirection(Direction.RIGHT);
        startAnimation();
    }

    public void startAnimation() {
        animator.setVisibilitySwapped();
        startAnimation(animator);
    }

    @Override
    public void onAnimationStart(Animation animation) {
        if (listener != null) {
            listener.onFlipStart(this);
        }
    }

    public boolean isFlipped() {
        return isFlipped;
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if (listener != null) {
            listener.onFlipEnd(this);
        }

        if (animator.getDirection() == Direction.UP) animator.setDirection(Direction.DOWN);
        if (animator.getDirection() == Direction.DOWN) animator.setDirection(Direction.UP);
        if (animator.getDirection() == Direction.LEFT) animator.setDirection(Direction.RIGHT);
        if (animator.getDirection() == Direction.RIGHT) animator.setDirection(Direction.LEFT);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
    }

    public void setAnimationListener(Animation.AnimationListener listener) {
        animator.setAnimationListener(listener);
    }

    @Override
    public void onClick(View view) {
        toggleDown();
    }

    @Override
    public void onSwipeLeft() {
        toggleLeft();
    }

    @Override
    public void onSwipeRight() {
        toggleRight();
    }

    @Override
    public void onSwipeUp() {
        toggleUp();
    }

    @Override
    public void onSwipeDown() {
        toggleDown();
    }
}