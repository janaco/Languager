package com.stiletto.tr.view.text;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.stiletto.tr.R;

import java.util.List;

/**
 * Created by yana on 25.12.16.
 */

public class ClickableTextView extends TextView {

    private CharSequence charSequence;
    private BufferType bufferType;

    private OnWordClickListener onWordClickListener;
    private SpannableString spannableString;

    private BackgroundColorSpan backgroundColorSpan;
    private ForegroundColorSpan foregroundColorSpan;

    private int highlightColor;
    private String highlightText;
    private int selectedColor;

    public ClickableTextView(Context context) {
        this(context, null);
    }

    public ClickableTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClickableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ClickableTextView);
        highlightColor = typedArray.getColor(R.styleable.ClickableTextView_highlightColor, Color.RED);
        highlightText = typedArray.getString(R.styleable.ClickableTextView_highlightText);
        selectedColor = typedArray.getColor(R.styleable.ClickableTextView_selectedColor, Color.BLUE);
        typedArray.recycle();
    }


    @Override
    public void setText(CharSequence text, BufferType type) {
        this.charSequence = text;
        bufferType = type;
        setHighlightColor(Color.TRANSPARENT);
        setMovementMethod(LinkMovementMethod.getInstance());
        setText();
    }

    private void setText() {
        spannableString = new SpannableString(charSequence);
        setHighLightSpan(spannableString);
        splitText();
        super.setText(spannableString, bufferType);

    }

    private void splitText() {
        List<Word> wordInfoList = ClickableTextUtils.getEnglishWordIndices(charSequence.toString());
        for (Word wordInfo : wordInfoList) {
            spannableString.setSpan(getClickableSpan(), wordInfo.getStart(), wordInfo.getEnd(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        }
    }


    private void setHighLightSpan(SpannableString spannableString) {
        if (TextUtils.isEmpty(highlightText)) {
            return;
        }
        int hIndex = charSequence.toString().indexOf(highlightText);
        while (hIndex != -1) {
            spannableString.setSpan(new ForegroundColorSpan(highlightColor), hIndex, hIndex + highlightText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            hIndex += highlightText.length();
            hIndex = charSequence.toString().indexOf(highlightText, hIndex);
        }
    }

    private void setSelectedSpan(TextView tv) {
        if (backgroundColorSpan == null || foregroundColorSpan == null) {
            backgroundColorSpan = new BackgroundColorSpan(selectedColor);
            foregroundColorSpan = new ForegroundColorSpan(Color.WHITE);
        } else {
            spannableString.removeSpan(backgroundColorSpan);
            spannableString.removeSpan(foregroundColorSpan);
        }
        spannableString.setSpan(backgroundColorSpan, tv.getSelectionStart(), tv.getSelectionEnd(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(foregroundColorSpan, tv.getSelectionStart(), tv.getSelectionEnd(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ClickableTextView.super.setText(spannableString, bufferType);
    }

    public void dismissSelected() {
        spannableString.removeSpan(backgroundColorSpan);
        spannableString.removeSpan(foregroundColorSpan);
        ClickableTextView.super.setText(spannableString, bufferType);
    }

    private ClickableSpan getClickableSpan() {
        return new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                TextView tv = (TextView) widget;
                String word = tv
                        .getText()
                        .subSequence(tv.getSelectionStart(),
                                tv.getSelectionEnd()).toString();
                setSelectedSpan(tv);

                if (onWordClickListener != null) {
                    onWordClickListener.onClick(word);
                }
            }

            @Override
            public void updateDrawState(TextPaint ds) {
            }
        };
    }


    public void setOnWordClickListener(OnWordClickListener listener) {
        this.onWordClickListener = listener;
    }

    public void setHighLightText(String text) {
        highlightText = text;
    }

    public void setHighLightColor(int color) {
        highlightColor = color;
    }

    public interface OnWordClickListener {
        void onClick(String word);
    }
}