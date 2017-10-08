package com.softes.clickabletextview;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
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

public class ClickableTextView extends android.support.v7.widget.AppCompatTextView {

    private CharSequence charSequence;
    private BufferType bufferType;

    private OnWordClickListener onWordClickListener;
    private SpannableString spannableString;

    private CharSequence content;

    private boolean measuring = false;

    public ClickableTextView(Context context) {
        this(context, null);
    }

    public ClickableTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClickableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {

        this.charSequence = text;
        bufferType = type;
        setHighlightColor(ContextCompat.getColor(getContext(), R.color.zinnwaldite));
        setMovementMethod(LinkMovementMethod.getInstance());
        setText();

        content = getText();
    }

    private void setText() {
        spannableString = new SpannableString(charSequence);
        splitText();

        super.setText(spannableString, bufferType);
    }

    private void splitText() {
        List<ClickableWord> wordInfoList = TextUtils.getWordIndices(charSequence.toString());
        for (ClickableWord wordInfo : wordInfoList) {
            spannableString.setSpan(getClickableSpan(), wordInfo.getStart(), wordInfo.getEnd(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }


    private ClickableSpan getClickableSpan() {
        return new ClickableSpan() {
            @Override
            public void onClick(View widget) {

                TextView tv = (TextView) widget;
                int indexStart = tv.getSelectionStart();
                int indexEnd = tv.getSelectionEnd();

                try {

                    String word = new StringBuilder( tv.getText()).substring(indexStart, indexEnd);

                    if (onWordClickListener != null) {

                        TextView parentTextView = (TextView) widget;

                        Rect parentTextViewRect = new Rect();

                        SpannableString completeText = (SpannableString)(parentTextView).getText();
                        Layout textViewLayout = parentTextView.getLayout();

                        double startOffsetOfClickedText = completeText.getSpanStart(this);
                        double endOffsetOfClickedText = completeText.getSpanEnd(this);
                        double startXCoordinatesOfClickedText = textViewLayout.getPrimaryHorizontal((int)startOffsetOfClickedText);
                        double endXCoordinatesOfClickedText = textViewLayout.getPrimaryHorizontal((int)endOffsetOfClickedText);


                        int currentLineStartOffset = textViewLayout.getLineForOffset((int)startOffsetOfClickedText);
                        int currentLineEndOffset = textViewLayout.getLineForOffset((int)endOffsetOfClickedText);
                        boolean keywordIsInMultiLine = currentLineStartOffset != currentLineEndOffset;
                        textViewLayout.getLineBounds(currentLineStartOffset, parentTextViewRect);


                        int[] parentTextViewLocation = {0,0};
                        parentTextView.getLocationOnScreen(parentTextViewLocation);

                        double parentTextViewTopAndBottomOffset = (
                                parentTextViewLocation[1] -
                                        parentTextView.getScrollY() +
                                        parentTextView.getCompoundPaddingTop());
                        parentTextViewRect.top += parentTextViewTopAndBottomOffset;
                        parentTextViewRect.bottom += parentTextViewTopAndBottomOffset;

                        parentTextViewRect.left += (
                                parentTextViewLocation[0] +
                                        startXCoordinatesOfClickedText +
                                        parentTextView.getCompoundPaddingLeft() -
                                        parentTextView.getScrollX()
                        );
                        parentTextViewRect.right = (int) (
                                parentTextViewRect.left +
                                        endXCoordinatesOfClickedText -
                                        startXCoordinatesOfClickedText
                        );

                        int x = (parentTextViewRect.left + parentTextViewRect.right) / 2;
                        int y = parentTextViewRect.bottom;
                        if (keywordIsInMultiLine) {
                            x = parentTextViewRect.left;
                        }

                        onWordClickListener.onClick(word, x, y, indexStart, indexEnd);
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
}