package com.stiletto.tr.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.AsyncTask;
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

public class JCTextView extends TextView
//        implements TextAligmentUtils.Justified
{

    private CharSequence charSequence;
    private BufferType bufferType;

    private JCTextView.OnWordClickListener onWordClickListener;
    private SpannableString spannableString;

    private ForegroundColorSpan foregroundColorSpan;
    private CharacterStyle characterStyle;

    private int highlightColor;
    private String highlightText;


    //Justify
    private static final int MAX_SPANS = 512;

    private boolean measuring = false;

    private Typeface typeface = null;
    private float textSize = 0f;
    private float textScaleX = 0f;
    private boolean fakeBold = false;
    private int width = 0;

    private int[] spanStarts = new int[MAX_SPANS];
    private int[] spanEnds = new int[MAX_SPANS];
    private TextAligmentUtils.ScaleSpan[] spans = new TextAligmentUtils.ScaleSpan[MAX_SPANS];

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
        this.charSequence = text;
        bufferType = type;
        setHighlightColor(highlightColor);
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

//    @NotNull
//    @Override
//    public TextView getTextView() {
//        return this;
//    }
//
//    @Override
//    public float getMaxProportion() {
//        return TextAligmentUtils.DEFAULT_MAX_PROPORTION;
//    }


    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d("TEXT_VIEW", "onMeasure: measuring=" + measuring);

        if (!measuring) {
            this.measuring = true;

                    TextAligmentUtils.setupScaleSpans((Spannable) getText(), this);
        }
    }



    public interface OnWordClickListener {
        void onClick(String word);
    }
}