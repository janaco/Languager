package com.softes.categorizedlistview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by yana on 09.03.17.
 */

public class IndexBarView extends View {

    float indexBarMargin;
    float sideIndexY;

    boolean isIndexing = false;

    int currentSectionPosition = -1;

    ArrayList<String> listItems;
    Paint indexPaint;

    CategorizedListView listView;


    public IndexBarView(Context context) {
        super(context);
    }


    public IndexBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public IndexBarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    public void setData(CategorizedListView listView, ArrayList<String> listItems) {
        this.listItems = listItems;
        this.listView = listView;

        indexBarMargin = getResources().getDimension(R.dimen.category_bar_margin);

        // index bar item color and text size
        indexPaint = new Paint();
        indexPaint.setColor(ContextCompat.getColor(getContext(), R.color.black));
        indexPaint.setAntiAlias(true);
        indexPaint.setTextSize(getResources().getDimension(R.dimen.category_bar_text_size));
    }


    @Override
    protected void onDraw(Canvas canvas) {

        if (listItems != null && listItems.size() > 1) {
            int itemCount = listItems.size();
            float sectionHeight = (getMeasuredHeight() - 2 * indexBarMargin)/ itemCount;
            float paddingTop = (sectionHeight - (indexPaint.descent() - indexPaint.ascent())) / 2;

            for (int i = 0; i < itemCount; i++) {
                float paddingLeft = (getMeasuredWidth() - indexPaint.measureText(listItems.get(i))) / 2;

                canvas.drawText(listItems.get(i),
                        paddingLeft,
                        indexBarMargin + (sectionHeight * i) + paddingTop + indexPaint.descent(),
                        indexPaint);
            }
        }
        super.onDraw(canvas);
    }




    boolean contains(float x, float y) {
        return (x >= getLeft() && y >= getTop() && y <= getTop() + getMeasuredHeight());
    }


    void filterListItem(float sideIndexY) {
        this.sideIndexY = sideIndexY;

        // filter list items and get touched section position with in index bar
        currentSectionPosition = (int) (((this.sideIndexY) - getTop() - indexBarMargin) /
                ((getMeasuredHeight() - (2 * indexBarMargin)) / listItems.size()));

        if (currentSectionPosition >= 0 && currentSectionPosition < listItems.size()) {
            int position = currentSectionPosition;
            String previewText = listItems.get(position);
            listView.filterList(this.sideIndexY, position, previewText);
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
                }
                else {
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
                    }
                    else {
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