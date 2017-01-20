package com.stiletto.tr.widget;

import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.TypeEvaluator;
import android.graphics.Point;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;


/**
 * Created by yana on 20.01.17.
 */

public class AnimationManager {

    public static ObjectAnimator animate(Object target,
                                         String property,
                                         long delay,
                                         long duration,
                                         TimeInterpolator interpolator,
                                         AnimatorListenerAdapter listenerAdapter,
                                         float... values) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(target, property, values);
        animator.setStartDelay(delay);
        animator.setDuration(duration);
        if (interpolator != null) animator.setInterpolator(interpolator);
        if (listenerAdapter != null) animator.addListener(listenerAdapter);
        animator.start();
        return animator;
    }

    public static ObjectAnimator animate(Object target,
                                         String property,
                                         long delay,
                                         long duration,
                                         TimeInterpolator interpolator,
                                         float... values) {
        return animate(
                target,
                property,
                delay,
                duration,
                interpolator,
                null,
                values);
    }

    public static void animate(String property,
                               long delay,
                               long duration,
                               float[] values,
                               TimeInterpolator interpolator,
                               ArrayList<View> targets) {
        for (Object target : targets) {
            animate(
                    target,
                    property,
                    delay,
                    duration,
                    interpolator,
                    null,
                    values);
        }
    }

    public static void rotate(BoomButton boomButton,
                              long delay,
                              long duration,
                              TimeInterpolator interpolator,
                              float... degrees) {
        boomButton.setRotateAnchorPoints();
        for (int i = 0; i < boomButton.rotateViews().size(); i++) {
            View view = boomButton.rotateViews().get(i);
            animate(
                    view,
                    "rotation",
                    delay,
                    duration,
                    interpolator,
                    null,
                    degrees);
        }
    }

    public static ObjectAnimator animate(
            Object target,
            String property,
            long delay,
            long duration,
            TypeEvaluator evaluator,
            int... values) {
        return animate(target, property, delay, duration, evaluator, null, values);
    }

    public static ObjectAnimator animate(
            Object target,
            String property,
            long delay,
            long duration,
            TypeEvaluator evaluator,
            AnimatorListenerAdapter listenerAdapter,
            int... values) {
        ObjectAnimator animator = ObjectAnimator.ofInt(target, property, values);
        animator.setStartDelay(delay);
        animator.setDuration(duration);
        animator.setEvaluator(evaluator);
        if (listenerAdapter != null) animator.addListener(listenerAdapter);
        animator.start();
        return animator;
    }

    public static ArrayList<Integer> getOrderIndex(OrderEnum orderEnum, int size) {
        ArrayList<Integer> indexes = new ArrayList<>();
        switch (orderEnum) {
            case DEFAULT:
                for (int i = 0; i < size; i++) indexes.add(i);
                break;
        }
        return indexes;
    }

    public static void calculateShowXY(BoomEnum boomEnum,
                                       Point parentSize,
                                       int frames,
                                       Point startPosition,
                                       Point endPosition,
                                       float[] xs,
                                       float[] ys) {

        float x1 = startPosition.x;
        float y1 = startPosition.y;
        float x2 = endPosition.x;
        float y2 = endPosition.y;
        float p = 1.0f / frames;
        float xOffset = x2 - x1;
        float x3, y3, a, b, c;

        switch (boomEnum) {
            case HORIZONTAL_THROW_1:
                x3 = x2 * 2 - x1;
                y3 = y1;
                a = (y1 * (x3 - x2) + y3 * (x2 - x1) + y2 * (x1 - x3)) / (x1 * x1 * (x3 - x2) + x3 * x3 * (x2 - x1) + x2 * x2 * (x1 - x3));
                b = (y1 - y3) / (x1 - x3) - a * (x1 + x3);
                c = y1 - (x1 * x1) * a - x1 * b;
                for (int i = 0; i < xs.length; i++)
                {
                    float offset = i * p;
                    xs[i] = x1 + offset * xOffset;
                    ys[i] = a * xs[i] * xs[i] + b * xs[i] + c;
                }
                break;
            case HORIZONTAL_THROW_2:
                x2 = startPosition.x;
                y2 = startPosition.y;
                x1 = endPosition.x;
                y1 = endPosition.y;
                x3 = x2 * 2 - x1;
                y3 = y1;
                a = (y1 * (x3 - x2) + y3 * (x2 - x1) + y2 * (x1 - x3)) / (x1 * x1 * (x3 - x2) + x3 * x3 * (x2 - x1) + x2 * x2 * (x1 - x3));
                b = (y1 - y3) / (x1 - x3) - a * (x1 + x3);
                c = y1 - (x1 * x1) * a - x1 * b;
                for (int i = 0; i < xs.length; i++)
                {
                    float offset = i * p;
                    xs[i] = x2 + offset * xOffset;
                    ys[i] = a * xs[i] * xs[i] + b * xs[i] + c;
                }
                break;
            case RANDOM:
                calculateShowXY(
                        BoomEnum.values()[new Random().nextInt(BoomEnum.RANDOM.getValue())],
                        parentSize,
                        frames,
                        startPosition,
                        endPosition,
                        xs,
                        ys);
                break;
            case Unknown:
                throw new RuntimeException("UNKNOWN boom-enum!");
        }
    }

    public static void calculateHideXY(BoomEnum boomEnum,
                                       Point parentSize,
                                       int frames,
                                       Point startPosition,
                                       Point endPosition,
                                       float[] xs,
                                       float[] ys) {

        float x1 = startPosition.x;
        float y1 = startPosition.y;
        float x2 = endPosition.x;
        float y2 = endPosition.y;
        float p = 1.0f / frames;
        float xOffset = x2 - x1;
        float x3, y3, a, b, c;

        switch (boomEnum) {
            case RANDOM:
            case Unknown:
                calculateShowXY(
                        boomEnum,
                        parentSize,
                        frames,
                        startPosition,
                        endPosition,
                        xs,
                        ys);
                break;
            case HORIZONTAL_THROW_1:
                x2 = startPosition.x;
                y2 = startPosition.y;
                x1 = endPosition.x;
                y1 = endPosition.y;
                x3 = x2 * 2 - x1;
                y3 = y1;
                a = (y1 * (x3 - x2) + y3 * (x2 - x1) + y2 * (x1 - x3)) / (x1 * x1 * (x3 - x2) + x3 * x3 * (x2 - x1) + x2 * x2 * (x1 - x3));
                b = (y1 - y3) / (x1 - x3) - a * (x1 + x3);
                c = y1 - (x1 * x1) * a - x1 * b;
                for (int i = 0; i < xs.length; i++) {
                    float offset = i * p;
                    xs[i] = x2 + offset * xOffset;
                    ys[i] = a * xs[i] * xs[i] + b * xs[i] + c;
                }
                break;
            case HORIZONTAL_THROW_2:
                x3 = x2 * 2 - x1;
                y3 = y1;
                a = (y1 * (x3 - x2) + y3 * (x2 - x1) + y2 * (x1 - x3)) / (x1 * x1 * (x3 - x2) + x3 * x3 * (x2 - x1) + x2 * x2 * (x1 - x3));
                b = (y1 - y3) / (x1 - x3) - a * (x1 + x3);
                c = y1 - (x1 * x1) * a - x1 * b;
                for (int i = 0; i < xs.length; i++) {
                    float offset = i * p;
                    xs[i] = x1 + offset * xOffset;
                    ys[i] = a * xs[i] * xs[i] + b * xs[i] + c;
                }
                break;
        }
    }

    private static AnimationManager ourInstance = new AnimationManager();

    public static AnimationManager getInstance() {
        return ourInstance;
    }

    private AnimationManager() {
    }
}
