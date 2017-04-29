package com.softes.categorizedlistview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;

/**
 * Created by yana on 09.03.17.
 */

public class IndexBarView extends View {

    private boolean isIndexing = false;
    private int currentSectionPosition = -1;
    private float categoryBarMargin;

    private List<String> listItems;
    private Paint categoryPaint;
    private CategorizedListView listView;


    public IndexBarView(Context context) {
        super(context);
    }

    public IndexBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IndexBarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    public void setData(CategorizedListView listView, List<String> listItems) {
        this.listItems = listItems;
        this.listView = listView;

        categoryBarMargin = getResources().getDimension(R.dimen.category_bar_margin);

        // category bar item color and text size
        categoryPaint = new Paint();
        categoryPaint.setColor(ContextCompat.getColor(getContext(), R.color.black));
        categoryPaint.setAntiAlias(true);
        categoryPaint.setTextSize(getResources().getDimension(R.dimen.category_bar_text_size));
    }


    @Override
    protected void onDraw(Canvas canvas) {

        if (listItems != null && listItems.size() > 1) {
            int itemCount = listItems.size();
            float sectionHeight = (getMeasuredHeight() - 2 * categoryBarMargin) / itemCount;
            float paddingTop = (sectionHeight - (categoryPaint.descent() - categoryPaint.ascent())) / 2;

            for (int i = 0; i < itemCount; i++) {
                float paddingLeft = (getMeasuredWidth() - categoryPaint.measureText(listItems.get(i))) / 2;

                canvas.drawText(listItems.get(i),
                        paddingLeft,
                        categoryBarMargin + (sectionHeight * i) + paddingTop + categoryPaint.descent(),
                        categoryPaint);
            }
        }
        super.onDraw(canvas);
    }


    boolean contains(float x, float y) {
        return (x >= getLeft() && y >= getTop() && y <= getTop() + getMeasuredHeight());
    }


    void filterListItem(float sideIndexY) {

        // filter list items and get touched section position with in index bar
        currentSectionPosition = (int) (((sideIndexY) - getTop() - categoryBarMargin) /
                ((getMeasuredHeight() - (2 * categoryBarMargin)) / listItems.size()));

        if (currentSectionPosition >= 0 && currentSectionPosition < listItems.size()) {
            int position = currentSectionPosition;
            String previewText = listItems.get(position);
            listView.filterList(sideIndexY, position, previewText);
        }
    }


    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {

            case MotionEvent.ACTION_DOWN:
                // If down event occurs inside index bar region, start indexing
                if (contains(ev.getX(), ev.getY())) {
                    // It demonstrates that the motion event started from index
                    // bar
                    isIndexing = true;
                    // Determine which section the point is in, and move the
                    // list to
                    // that section
                    filterListItem(ev.getY());
                    return true;
                } else {
                    currentSectionPosition = -1;
                    return false;
                }
            case MotionEvent.ACTION_MOVE:
                if (isIndexing) {
                    // If this event moves inside index bar
                    if (contains(ev.getX(), ev.getY())) {
                        // Determine which section the point is in, and move the
                        // list to that section
                        filterListItem(ev.getY());
                        return true;
                    } else {
                        currentSectionPosition = -1;
                        return false;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (isIndexing) {
                    isIndexing = false;
                    currentSectionPosition = -1;
                }
                break;
        }
        return false;
    }
}