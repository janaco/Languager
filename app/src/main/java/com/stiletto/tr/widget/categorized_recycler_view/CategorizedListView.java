package com.stiletto.tr.widget.categorized_recycler_view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.stiletto.tr.R;

/**
 * Created by yana on 08.03.17.
 */
public class CategorizedListView extends ListView implements CategoryFilter {

    // interface object that configure pinned header view position in list view
    ListHeader listHeader;

    // view objects
    View headerView, indexBarView, previewTextView;

    // flags that decide view visibility
    boolean headerVisibility =false;
    boolean previewVisibility =false;
    // initially show index bar view with it's content
    boolean indexBarVisibility =true;

    // context object
    Context context;

    // view height and width
    int headerViewWidth,
            headerViewHeight,
            indexBarViewWidth,
            indexBarViewHeight,
            indexBarViewMargin,
            previewTextViewWidth,
            previewTextViewHeight;

    // touched index bar Y axis position used to decide preview text view position
    float indexBarY;


    public CategorizedListView(Context context) {
        super(context);
        this.context = context;
    }


    public CategorizedListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }


    public CategorizedListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
    }


    @Override
    public void setAdapter(ListAdapter adapter) {
        this.listHeader = (CategoryAdapter)adapter;
        super.setAdapter(adapter);
    }


    public void setPinnedHeaderView(View headerView) {
        this.headerView = headerView;
        // Disable vertical fading when the pinned header is present
        // TODO change ListView to allow separate measures for top and bottom fading edge;
        // in this particular case we would like to disable the top, but not the bottom edge.
        if (this.headerView != null) {
            setFadingEdgeLength(0);
        }
    }


    public void setIndexBarView(View indexBarView) {
        indexBarViewMargin = (int) context.getResources().getDimension(R.dimen.index_bar_view_margin);
        this.indexBarView = indexBarView;
    }


    public void setPreviewView(View previewTextView) {
        this.previewTextView =previewTextView;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (headerView != null) {
            measureChild(headerView, widthMeasureSpec, heightMeasureSpec);
            headerViewWidth = headerView.getMeasuredWidth();
            headerViewHeight = headerView.getMeasuredHeight();
        }

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

        if (headerView != null) {
            headerView.layout(0, 0, headerViewWidth, headerViewHeight);
            configureHeaderView(getFirstVisiblePosition());
        }

        if (indexBarView != null && indexBarVisibility) {
            indexBarView.layout(getMeasuredWidth()- indexBarViewMargin - indexBarViewWidth, indexBarViewMargin
                    , getMeasuredWidth()- indexBarViewMargin, getMeasuredHeight()- indexBarViewMargin);
        }

        if (previewTextView != null && previewVisibility) {
            previewTextView.layout(indexBarView.getLeft()- previewTextViewWidth, (int) indexBarY -(previewTextViewHeight /2)
                    , indexBarView.getLeft(), (int)(indexBarY -(previewTextViewHeight /2))+ previewTextViewHeight);
        }
    }


    public void setIndexBarVisibility(Boolean isVisible) {
        if(isVisible) {
            indexBarVisibility =true;
        }
        else {
            indexBarVisibility =false;
        }
    }


    private void setPreviewTextVisibility(Boolean isVisible) {
        if(isVisible) {
            previewVisibility =true;
        }
        else {
            previewVisibility =false;
        }
    }


    public void configureHeaderView(int position) {
        if (headerView == null) {
            return;
        }

        int state = listHeader.getPinnedHeaderState(position);

        switch (state) {

            case ListHeader.PINNED_HEADER_GONE:
                headerVisibility = false;
                break;
            case ListHeader.PINNED_HEADER_VISIBLE:
                if (headerView.getTop() != 0) {
                    headerView.layout(0, 0, headerViewWidth, headerViewHeight);
                }
                listHeader.configurePinnedHeader(headerView, position);
                headerVisibility = true;
                break;
            case ListHeader.PINNED_HEADER_PUSHED_UP:
                View firstView = getChildAt(0);
                int bottom = firstView.getBottom();
                // int itemHeight = firstView.getHeight();
                int headerHeight = headerView.getHeight();
                int y;
                if (bottom < headerHeight) {
                    y = (bottom - headerHeight);
                }
                else {
                    y = 0;
                }

                if (headerView.getTop() != y) {
                    headerView.layout(0, y, headerViewWidth, headerViewHeight + y);
                }
                listHeader.configurePinnedHeader(headerView, position);
                headerVisibility = true;
                break;
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);// draw list view elements (zIndex == 1)

        if (headerView != null && headerVisibility) {
            drawChild(canvas, headerView, getDrawingTime()); // draw pinned header view (zIndex == 2)
        }
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
        }
        else {
            setPreviewTextVisibility(false);
            return super.onTouchEvent(ev);
        }
    }


    @Override
    public void filterList(float indexBarY, int position,String previewText) {
        this.indexBarY =indexBarY;

        if(previewTextView instanceof TextView)
            ((TextView) previewTextView).setText(previewText);

        setSelection(position);
    }
}