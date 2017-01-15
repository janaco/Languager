package com.stiletto.tr.pagination;

import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yana on 15.01.17.
 */

public class PageSplitter {
    private final int pageWidth;
    private final int pageHeight;
    private final TextPaint textPaint;
    private final float lineSpacingMultiplier;
    private final float lineSpacingExtra;
    private final List<CharSequence> pages = new ArrayList<CharSequence>();
    private SpannableStringBuilder mSpannableStringBuilder = new SpannableStringBuilder();

    public PageSplitter(CharSequence text, TextView view) {
        this.pageWidth = view.getWidth();
        this.pageHeight = view.getHeight();
        this.textPaint = view.getPaint();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            this.lineSpacingMultiplier = view.getLineSpacingMultiplier()+ 0.1f;
            this.lineSpacingExtra = view.getLineSpacingExtra();
        } else {
            this.lineSpacingMultiplier = 1.2f;
            this.lineSpacingExtra = 0;
        }


        Log.d("PageSplitter", "\nlineSpacingMultiplier: " + lineSpacingMultiplier
                + "\nlineSpacingExtra: " + lineSpacingExtra);

        append(text);
        split(textPaint);
    }


    public void append(CharSequence charSequence) {
        mSpannableStringBuilder.append(charSequence);
    }

    public void split(TextPaint textPaint) {
        StaticLayout staticLayout = new StaticLayout(
                mSpannableStringBuilder,
                textPaint,
                pageWidth,
                Layout.Alignment.ALIGN_NORMAL,
                lineSpacingMultiplier,
                lineSpacingExtra,
                false
        );
        int startLine = 0;
        while (startLine < staticLayout.getLineCount()) {
            int startLineTop = staticLayout.getLineTop(startLine);
            int endLine = staticLayout.getLineForVertical(startLineTop + pageHeight);
            int endLineBottom = staticLayout.getLineBottom(endLine);
            int lastFullyVisibleLine;
            if (endLineBottom > startLineTop + pageHeight)
                lastFullyVisibleLine = endLine - 1;
            else
                lastFullyVisibleLine = endLine;
            int startOffset = staticLayout.getLineStart(startLine);
            int endOffset = staticLayout.getLineEnd(lastFullyVisibleLine);
            pages.add(mSpannableStringBuilder.subSequence(startOffset, endOffset));
            startLine = lastFullyVisibleLine + 1;
        }
    }

    public List<CharSequence> getPages() {
        return pages;
    }

    public CharSequence get(int position) {
        return pages.get(position);
    }

    public int getPagesCount() {
        return pages.size();
    }
}