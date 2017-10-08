package com.softes.clickabletextview;

import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;

import org.jetbrains.annotations.NotNull;

/**
 * Created by yana on 08.10.17.
 */

public class ScaleSpan  extends MetricAffectingSpan {

    private final float mProportion;

    ScaleSpan(final float proportion) {
        mProportion = proportion;
    }

    @Override
    public void updateDrawState(final @NotNull TextPaint ds) {
        ds.setTextScaleX(ds.getTextScaleX() * mProportion);
    }

    @Override
    public void updateMeasureState(final @NotNull TextPaint ds) {
        ds.setTextScaleX(ds.getTextScaleX() * mProportion);
    }

}