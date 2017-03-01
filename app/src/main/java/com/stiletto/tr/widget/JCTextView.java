package com.stiletto.tr.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.CharacterStyle;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.stiletto.tr.R;
import com.stiletto.tr.text.ClickableTextUtils;
import com.stiletto.tr.text.Word;
import com.stiletto.tr.utils.TextAligmentUtils;

import java.util.List;

/**
 * Created by yana on 05.02.17.
 */

public class JCTextView extends TextView {

    public static final String TAG = "JCTextView";

    private CharSequence charSequence;
    private BufferType bufferType;

    private JCTextView.OnWordClickListener onWordClickListener;
    private SpannableString spannableString;

    private ForegroundColorSpan foregroundColorSpan;
    private CharacterStyle characterStyle;

    private int highlightColor;
    private String highlightText;

    private CharSequence content;


    //Justify
    private boolean measuring = false;

    public JCTextView(Context context) {
        this(context, null);
        highlightColor = ContextCompat.getColor(context, R.color.colorPrimary);

    }

    public JCTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ClickableTextView);
        highlightColor = typedArray.getColor(R.styleable.ClickableTextView_highlightColor,
                ContextCompat.getColor(context, R.color.colorPrimary));
    }

    public JCTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ClickableTextView);
        highlightColor = typedArray.getColor(R.styleable.ClickableTextView_highlightColor,
                ContextCompat.getColor(context, R.color.colorPrimary));
        highlightText = typedArray.getString(R.styleable.ClickableTextView_highlightText);
        typedArray.recycle();
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        Log.d(TAG, "setText method");
        this.charSequence = text;
        bufferType = type;
        setHighlightColor(highlightColor);
        setMovementMethod(LinkMovementMethod.getInstance());
        setText();

        content = getText();
    }

    private void setText() {
        spannableString = new SpannableString(charSequence);
        setHighLightSpan(spannableString);
        splitText();

        super.setText(spannableString, bufferType);
    }

    private void splitText() {
        List<Word> wordInfoList = ClickableTextUtils.getWordIndices(charSequence.toString());
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
            spannableString.setSpan(
                    new ForegroundColorSpan(highlightColor),
                    hIndex, hIndex + highlightText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            hIndex += highlightText.length();
            hIndex = charSequence.toString().indexOf(highlightText, hIndex);
        }
    }

    private void setSelectedSpan(int indexStart, int indexEnd) {
        if (foregroundColorSpan == null || characterStyle == null) {
            foregroundColorSpan = new ForegroundColorSpan(
                    ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
            characterStyle = new StyleSpan(Typeface.BOLD);

        } else {
            spannableString.removeSpan(foregroundColorSpan);
            spannableString.removeSpan(characterStyle);
        }
        spannableString.setSpan(foregroundColorSpan, indexStart, indexEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(characterStyle, indexStart, indexEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        JCTextView.super.setText(spannableString, bufferType);
    }

    public void dismissSelected() {
        spannableString.removeSpan(foregroundColorSpan);
        spannableString.removeSpan(characterStyle);
        JCTextView.super.setText(spannableString, bufferType);
    }

    private ClickableSpan getClickableSpan() {
        return new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                TextView tv = (TextView) widget;
                int indexStart = tv.getSelectionStart();
                int indexEnd = tv.getSelectionEnd();

                try {

                    String word = tv.getText()
                            .subSequence(indexStart, indexEnd).toString();
                    setSelectedSpan(indexStart, indexEnd);

                    if (onWordClickListener != null) {
                        onWordClickListener.onClick(word);
                    }
                } catch (StringIndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void updateDrawState(TextPaint ds) {
            }
        };
    }


    public void setOnWordClickListener(JCTextView.OnWordClickListener listener) {
        this.onWordClickListener = listener;
    }

    public void setHighLightText(String text) {
        highlightText = text;
    }

    public void setHighLightColor(int color) {
        highlightColor = color;
    }


    private Typeface mTypeface = null;
    private float mTextSize = 0f;
    private float mTextScaleX = 0f;
    private boolean mFakeBold = false;
    private int mWidth = 0;

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d(TAG, "onMeasure");

        if (!measuring) {
            this.measuring = true;
            TextAligmentUtils.setupScaleSpans((Spannable) content, this);
        }

    }
        @Override
        protected void onTextChanged ( final CharSequence text,
        final int start, final int lengthBefore, final int lengthAfter){
            super.onTextChanged(text, start, lengthBefore, lengthAfter);
            final Layout layout = getLayout();
            if (layout != null) {
                TextAligmentUtils.setupScaleSpans((Spannable) content, this);
            }
        }


        public interface OnWordClickListener {
            void onClick(String word);
        }
    }