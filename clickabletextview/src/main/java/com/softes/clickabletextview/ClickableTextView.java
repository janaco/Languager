package com.softes.clickabletextview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.CharacterStyle;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.example.clickabletextview.R;

import java.util.List;


/**
 * Clickable text view.
 * Supports justify alignment.
 * <p>
 * Created by yana on 25.12.16.
 */

public class ClickableTextView extends TextView {

    public static final String TAG = "JCTextView";

    private CharSequence charSequence;
    private BufferType bufferType;

    private OnWordClickListener onWordClickListener;
    private SpannableString spannableString;

    private ForegroundColorSpan foregroundColorSpan;
    private CharacterStyle characterStyle;

    private int highlightColor;
    private String highlightText;

    private CharSequence content;

    private boolean measuring = false;

    public ClickableTextView(Context context) {
        this(context, null);
        highlightColor = ContextCompat.getColor(context,
                R.color.colorPrimary);
    }

    public ClickableTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ClickableTextView);
        highlightColor = typedArray.getColor(R.styleable.ClickableTextView_highlightColor,
                ContextCompat.getColor(context, R.color.colorPrimary));
    }

    public ClickableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
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

        content = getText();
    }

    private void setText() {
        spannableString = new SpannableString(charSequence);
        setHighLightSpan(spannableString);
        splitText();

        super.setText(spannableString, bufferType);
    }

    private void splitText() {
        List<ClickableWord> wordInfoList = TextUtils.getWordIndices(charSequence.toString());
        for (ClickableWord wordInfo : wordInfoList) {
            spannableString.setSpan(getClickableSpan(), wordInfo.getStart(), wordInfo.getEnd(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }


    private void setHighLightSpan(SpannableString spannableString) {
        if (android.text.TextUtils.isEmpty(highlightText)) {
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
        super.setText(spannableString, bufferType);
    }

    public void dismissSelected() {
        spannableString.removeSpan(foregroundColorSpan);
        spannableString.removeSpan(characterStyle);
        super.setText(spannableString, bufferType);
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


    public void setOnWordClickListener(OnWordClickListener listener) {
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

        if (!measuring) {
            try {
                TextUtils.justifyText((Spannable) content, this);
                this.measuring = true;
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    protected void onTextChanged(final CharSequence text,
                                 final int start, final int lengthBefore, final int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        final Layout layout = getLayout();
        if (layout != null) {
            TextUtils.justifyText((Spannable) content, this);
        }
    }


    public interface OnWordClickListener {
        void onClick(String word);
    }
}