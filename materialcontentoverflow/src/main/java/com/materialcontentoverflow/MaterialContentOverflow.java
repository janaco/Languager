package com.materialcontentoverflow;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.nineoldandroids.view.ViewHelper;

/**
 * Created by artur on 20/08/15.
 */

public class MaterialContentOverflow extends FrameLayout {

    private static final String INSTANCE_KEY = "com.materialcontentoverflow.INSTANCE_KEY";

    public static final int LEFT = 0;

    public static final int CENTER = 1;

    public static final int RIGHT = 2;

    private FrameLayout contentFrame;

    private OverflowGestureListener overflowGestureListener;
    private int fabTotalHeight;
    private float initialYPosition;
    private int fabMargin;

    public MaterialContentOverflow(Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public MaterialContentOverflow(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    public MaterialContentOverflow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(21)
    public MaterialContentOverflow(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    public FrameLayout getContentFrame() {
        return contentFrame;
    }

    public OverflowGestureListener getOverflowGestureListener() {
        return overflowGestureListener;
    }

    public void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {

        TypedArray a = context.obtainStyledAttributes(
                attrs,
                R.styleable.MaterialContentOverflow,
                defStyleAttr, 0);

        int buttonDrawable = 0;
        int buttonColor = 0;
        int contentColor = 0;
        int buttonPosition = 0;

        try {
            buttonDrawable = a.getResourceId(R.styleable.MaterialContentOverflow_buttonDrawable, 0);
            buttonColor = a.getResourceId(R.styleable.MaterialContentOverflow_fabButtonColor, 0);
            contentColor = a.getResourceId(R.styleable.MaterialContentOverflow_contentColor, 0);
            buttonPosition = a.getInt(R.styleable.MaterialContentOverflow_buttonPosition, 0);
        } finally {
            a.recycle();
            makeView(context, buttonDrawable, buttonColor, contentColor, buttonPosition);
        }
    }

    public void makeView(Context context, int buttonDrawable, int buttonColor, int contentColor, int buttonPosition) {

        FrameLayout contentFrame = createContentFrame(context, contentColor);

        FloatingActionButton fab = createFab(context, buttonDrawable, buttonColor, buttonPosition);

        overflowGestureListener = new OverflowGestureListener(this);

        fab.setOnTouchListener(overflowGestureListener.getMotionEvent());

        contentFrame.setOnTouchListener(overflowGestureListener.getMotionEvent());
    }

    private FrameLayout createContentFrame(Context context, int contentColor) {

        if (contentFrame == null) {
                contentFrame = new FrameLayout(this.getContext());
                contentFrame.setTag("FRAME");
        }

        int contentElevationInPixels = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());

        LayoutParams contentLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        contentLayoutParams.gravity = Gravity.BOTTOM;

        if (contentColor > 0) {
            contentFrame.setBackgroundColor(ContextCompat.getColor(context, contentColor));
        }

        contentFrame.setLayoutParams(contentLayoutParams);

        ViewCompat.setElevation(contentFrame, contentElevationInPixels);

        this.addView(contentFrame);

        return contentFrame;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int utilizedWidth = getPaddingLeft() + getPaddingRight();
        int utilizedHeight = getPaddingTop() + getPaddingBottom();

        MarginLayoutParams fabLayoutParams = (MarginLayoutParams) fab.getLayoutParams();

        measureChildWithMargins(
                fab,
                widthMeasureSpec,
                utilizedWidth,
                heightMeasureSpec,
                utilizedHeight);

        utilizedHeight += (fab.getMeasuredHeight() / 2) + fabLayoutParams.topMargin;

        measureChildWithMargins(
                contentFrame,
                widthMeasureSpec,
                utilizedWidth,
                heightMeasureSpec,
                utilizedHeight);

        utilizedHeight += contentFrame.getMeasuredHeight();

        setMeasuredDimension(
                resolveSize(contentFrame.getMeasuredWidth(), widthMeasureSpec),
                resolveSize(utilizedHeight, heightMeasureSpec));

    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (child.getTag() == null) {
            child.setTag("FOO");
        }
        if (!child.getTag().equals("FAB")) {
            if (!child.getTag().equals("FRAME")) {
                if (contentFrame == null) {
                    contentFrame = new FrameLayout(this.getContext());
                    contentFrame.setTag("FRAME");
                }
                contentFrame.addView(child, index, params);
            } else {
                super.addView(child, index, params);
            }
        } else {
            super.addView(child, index, params);
        }
        if (child.getTag().equals("FOO")) {
            child.setTag(null);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

        fabTotalHeight = fab.getHeight() +
                fabMargin + /*bottom margin*/
                fabMargin;  /*top margin*/

        initialYPosition = getInitialYPosition();

        if (overflowGestureListener.isOpened() && !changed) {
            ViewHelper.setY(this, 0f);
        } else {
            ViewHelper.setY(this, initialYPosition);
        }

        overflowGestureListener.setInitialYPosition(initialYPosition);

        if (!isInEditMode()) {
            //measured on device
            contentFrame.setPadding(0, fabTotalHeight / 2, 0, 0);
        } else {
            //measured on visual editor
            contentFrame.setPadding(0, fabTotalHeight, 0, 0);
        }

        super.onLayout(changed, left, top, right, bottom);
    }

    private float getInitialYPosition() {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX,
                ((ViewGroup) this.getParent()).getHeight() - fabTotalHeight,
                getResources().getDisplayMetrics());
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        overflowGestureListener.setIsOpened(savedInstanceState.getBoolean(INSTANCE_KEY));
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(INSTANCE_KEY, overflowGestureListener.isOpened());
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        overflowGestureListener.clearReferences();
        ViewCompat.setBackgroundTintList(fab, null);
        fab.setImageResource(0);
        fab.setImageDrawable(null);
        fab.setImageBitmap(null);
        fab = null;
        contentFrame = null;
    }
}
