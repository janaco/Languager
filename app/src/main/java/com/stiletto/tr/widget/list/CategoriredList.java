package com.stiletto.tr.widget.list;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.TextView;

import com.stiletto.tr.R;
import com.stiletto.tr.widget.categorized_recycler_view.CategoryFilter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yana on 09.03.17.
 */

public class CategoriredList extends RecyclerView
        implements CategoryFilter {

    IndexBarView indexBarView;
    TextView previewTextView;

    boolean previewVisibility = false;
    boolean indexBarVisibility = true;

    int indexBarViewWidth,
            indexBarViewHeight,
            indexBarViewMargin,
            previewTextViewWidth,
            previewTextViewHeight;

    float indexBarY;

    public CategoriredList(Context context) {
        super(context);
        setLayoutManager(new LinearLayoutManager(getContext()));
        seIndexBarView();
        setPreviewView();
    }

    public CategoriredList(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setLayoutManager(new LinearLayoutManager(getContext()));
        seIndexBarView();
        setPreviewView();
    }

    public CategoriredList(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setLayoutManager(new LinearLayoutManager(getContext()));
        seIndexBarView();
        setPreviewView();
    }

    @Override
    public void filterList(float indexBarY, int position, String previewText) {
        this.indexBarY = indexBarY;

        previewTextView.setText(previewText);
        scrollToPosition(position);
    }

    private void seIndexBarView() {
        indexBarViewMargin = (int) getResources().getDimension(R.dimen.index_bar_view_margin);
        indexBarView = (IndexBarView) LayoutInflater.from(getContext()).inflate(R.layout.index_bar_view, this, false);
    }

    public void setIndexBarItems(ArrayList<String> items){
        indexBarView.setData(this, items);
    }

    private void setPreviewView() {
        this.previewTextView = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.preview_view, this, false);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (indexBarView != null && indexBarVisibility) {
            measureChild(indexBarView, widthMeasureSpec, heightMeasureSpec);
            indexBarViewWidth = indexBarView.getMeasuredWidth();
            indexBarViewHeight = indexBarView.getMeasuredHeight();
        }

        if (previewTextView != null && previewVisibility) {
            measureChild(previewTextView, widthMeasureSpec, heightMeasureSpec);
            previewTextViewWidth = previewTextView.getMeasuredWidth();
            previewTextViewHeight = previewTextView.getMeasuredHeight();
        }
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if (indexBarView != null && indexBarVisibility) {
            indexBarView.layout(getMeasuredWidth() - indexBarViewMargin - indexBarViewWidth, indexBarViewMargin
                    , getMeasuredWidth() - indexBarViewMargin, getMeasuredHeight() - indexBarViewMargin);
        }

        if (previewTextView != null && previewVisibility) {
            previewTextView.layout(indexBarView.getLeft() - previewTextViewWidth, (int) indexBarY - (previewTextViewHeight / 2)
                    , indexBarView.getLeft(), (int) (indexBarY - (previewTextViewHeight / 2)) + previewTextViewHeight);
        }
    }


    public void setIndexBarVisibility(Boolean isVisible) {
        if (isVisible) {
            indexBarVisibility = true;
        } else {
            indexBarVisibility = false;
        }
    }


    private void setPreviewTextVisibility(Boolean isVisible) {
        if (isVisible) {
            previewVisibility = true;
        } else {
            previewVisibility = false;
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);// draw list view elements (zIndex == 1)

        if (indexBarView != null && indexBarVisibility) {
            drawChild(canvas, indexBarView, getDrawingTime()); // draw index bar view (zIndex == 3)
        }
        if (previewTextView != null && previewVisibility) {
            drawChild(canvas, previewTextView, getDrawingTime()); // draw preview text view (zIndex == 4)
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (indexBarView != null && (indexBarView).onTouchEvent(ev)) {
            setPreviewTextVisibility(true);
            return true;
        } else {
            setPreviewTextVisibility(false);
            return super.onTouchEvent(ev);
        }
    }

}