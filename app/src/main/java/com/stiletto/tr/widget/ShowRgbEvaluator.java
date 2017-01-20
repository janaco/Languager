package com.stiletto.tr.widget;

import android.animation.TypeEvaluator;

/**
 * Created by yana on 20.01.17.
 */

public class ShowRgbEvaluator implements TypeEvaluator {

    private static final ShowRgbEvaluator sInstance = new ShowRgbEvaluator();

    public static ShowRgbEvaluator getInstance() {
        return sInstance;
    }

    @Override
    public Object evaluate(float fraction, Object startValue, Object endValue) {
        int startInt = (Integer) startValue;
        int startA = (startInt >> 24) & 0xff;
        int startR = (startInt >> 16) & 0xff;
        int startG = (startInt >> 8) & 0xff;
        int startB = startInt & 0xff;

        int endInt = (Integer) endValue;
        int endA = (endInt >> 24) & 0xff;
        int endR = (endInt >> 16) & 0xff;
        int endG = (endInt >> 8) & 0xff;
        int endB = endInt & 0xff;

        float trueFraction = speedMap(fraction);

        return (startA + (int)(trueFraction * (endA - startA))) << 24 |
                (startR + (int)(trueFraction * (endR - startR))) << 16 |
                (startG + (int)(trueFraction * (endG - startG))) << 8 |
                (startB + (int)(trueFraction * (endB - startB)));
    }

    private float speedMap(float fraction) {
        float trueSpeed = fraction * 2;
        if (trueSpeed > 1) trueSpeed = 1;
        if (trueSpeed < 0) trueSpeed = 0;
        return trueSpeed;
    }
}
