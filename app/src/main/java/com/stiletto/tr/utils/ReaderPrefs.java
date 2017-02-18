package com.stiletto.tr.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by yana on 18.02.17.
 */

public class ReaderPrefs {

    private static ReaderPrefs prefs;

    private int pageWidth;
    private int pageHeight;
    private int paddingHorizontal;
    private int paddingVertical;
    private int textColor = Color.BLACK;

    private float lineSpacingMultiplier = 1.25f;
    private float lineSpacingExtra = 0;
    private float textSize = 18;

    private TextPaint textPaint;

    private final float scaledDensity;

    private int lineHeight;

    private ReaderPrefs(Context context) {

        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displaymetrics);
        int displayWidth = displaymetrics.widthPixels;
        int displayHeight = displaymetrics.heightPixels;
        scaledDensity = displaymetrics.scaledDensity;

        pageWidth = displayWidth;
        pageHeight = displayHeight;

        paddingHorizontal = displayWidth / 25;
        paddingVertical = displayHeight / 25;

        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize * scaledDensity);
        textPaint.setTypeface(Typeface.SERIF);

        lineHeight = (int) (lineSpacingMultiplier * 2 * scaledDensity + textSize * scaledDensity + textSize);
    }

    public static ReaderPrefs getPreferences(Context context) {
        if (prefs == null) {
            prefs = new ReaderPrefs(context);
        }

        return prefs;
    }

    public int getPageWidth() {
        return pageWidth;
    }

    public void setPageWidth(int pageWidth) {
        this.pageWidth = pageWidth;
    }

    public int getPageHeight() {
        return pageHeight;
    }

    public void setPageHeight(int pageHeight) {
        this.pageHeight = pageHeight;
    }

    public int getPaddingHorizontal() {
        return paddingHorizontal;
    }

    public void setPaddingHorizontal(int paddingHorizontal) {
        this.paddingHorizontal = paddingHorizontal;
    }

    public int getPaddingVertical() {
        return paddingVertical;
    }

    public void setPaddingVertical(int paddingVertical) {
        this.paddingVertical = paddingVertical;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        textPaint.setColor(textColor);
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
        textPaint.setTextSize(textSize * scaledDensity);
    }

    public TextPaint getTextPaint() {
        return textPaint;
    }

    public void setTextPaint(TextPaint textPaint) {
        this.textPaint = textPaint;
        textColor = textPaint.getColor();
        textSize = textPaint.getTextSize() / scaledDensity;
    }

    public float getLineSpacingMultiplier() {
        return lineSpacingMultiplier;
    }

    public void setLineSpacingMultiplier(float lineSpacingMultiplier) {
        this.lineSpacingMultiplier = lineSpacingMultiplier;
    }

    public float getLineSpacingExtra() {
        return lineSpacingExtra;
    }

    public void setLineSpacingExtra(float lineSpacingExtra) {
        this.lineSpacingExtra = lineSpacingExtra;
    }

    public int getLineHeight() {
        return lineHeight;
    }
}
