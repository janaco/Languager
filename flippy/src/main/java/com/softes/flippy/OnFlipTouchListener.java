package com.softes.flippy;


import android.content.Context;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yana on 27.05.17.
 */

public class OnFlipTouchListener implements View.OnTouchListener {

    private final GestureDetector gestureDetector;
    private List<OnSwipeListener> swipeListeners = new ArrayList<>();

    OnFlipTouchListener(Context context) {
        gestureDetector = new GestureDetector(context.getApplicationContext(), new GestureListener());
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    void addSwipeListener(OnSwipeListener swipeListener) {
        if (swipeListener != null && !swipeListeners.contains(swipeListener)) {
            swipeListeners.add(swipeListener);
        }
    }

    public void removeSwipeListener(OnSwipeListener swipeListener) {
        if (swipeListener != null && swipeListeners.contains(swipeListener)) {
            swipeListeners.remove(swipeListener);
        }
    }

    public void removeSwipeListeners() {
        swipeListeners.clear();
    }

    private final class GestureListener extends SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            for (OnSwipeListener swipeListener : swipeListeners) {
                                swipeListener.onSwipeRight();
                            }
                        } else {
                            for (OnSwipeListener swipeListener : swipeListeners) {
                                swipeListener.onSwipeLeft();
                            }
                        }
                    }
                    result = true;
                } else if (Math.abs(diffY) > SWIPE_THRESHOLD
                        && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        for (OnSwipeListener swipeListener : swipeListeners) {
                            swipeListener.onSwipeDown();
                        }
                    } else {
                        for (OnSwipeListener swipeListener : swipeListeners) {
                            swipeListener.onSwipeUp();
                        }
                    }
                }
                result = true;
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            return result;
        }
    }
}
