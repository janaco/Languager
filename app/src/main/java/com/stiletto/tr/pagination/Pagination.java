package com.stiletto.tr.pagination;

import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;

import com.stiletto.tr.utils.ReaderPrefs;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yana on 29.12.16.
 */

public class Pagination {

    private final int width;
    private final int height;
    private final float lineSpacingMultiplier;
    private final float lineSpacingExtra;
    private final CharSequence text;
    private final TextPaint textPaint;
    private final List<CharSequence> pages;

    private int padding;
    private int lineHeight;
    private int maxLinesOnPage;

    public Pagination(CharSequence text, ReaderPrefs prefs) {
        this.text = text;
        this.width = prefs.getPageWidth() - prefs.getPaddingHorizontal() * 2;
        this.height = prefs.getPageHeight() - prefs.getPaddingVertical() * 3;
        this.textPaint = prefs.getTextPaint();
        this.lineSpacingMultiplier = prefs.getLineSpacingMultiplier();
        this.lineSpacingExtra = prefs.getLineSpacingExtra();

        padding = prefs.getPaddingVertical();
        lineHeight = prefs.getLineHeight();
        maxLinesOnPage = height / lineHeight;

        this.pages = new ArrayList<>();

        layout();

    }

    @Override
    public String toString() {
        return "Pagination{" + "\n" +
                ", width=" + width + "\n" +
                ", height=" + height + "\n" +
                ", lineSpacingMultiplier=" + lineSpacingMultiplier + "\n" +
                ", lineSpacingExtra=" + lineSpacingExtra + "\n" +
                '}';
    }

    private void layout() {
        final StaticLayout layout =
                new StaticLayout(text, textPaint, width, Layout.Alignment.ALIGN_NORMAL,
                        lineSpacingMultiplier, lineSpacingExtra, false);

        final int lineCount = layout.getLineCount();
        final CharSequence text = layout.getText();
        int startOffset = 0;
        int height = this.height;

        Log.d("PAGINATION_", "lineCount: " + lineCount + "\nstartOffset: " + startOffset + "\nheight: " + height);
        for (int i = 0; i < lineCount; ++i) {
            if (height < layout.getLineBottom(i)) {
                // When the layout height has been exceeded
                int to = layout.getLineEnd(i);
                Log.d("PAGINATION_", "line: " + i + "\nfrom: " + startOffset + "\nto(lineEnd): " + to);
                CharSequence t = text.subSequence(startOffset, to);
                addPage(t);
                startOffset = to;
                height = layout.getLineTop(i) + this.height;
            }

            Log.d("PAGINATION_", "height: " + height + "\nlineBottom: " + layout.getLineBottom(i)
                    + "\nlineTop: " + layout.getLineTop(i) + "\n ");

            if (i == lineCount - 1) {
                // Put the rest of the text into the last page
                addPage(text.subSequence(startOffset, layout.getLineEnd(i)));
                return;
            }
        }

//        final int lineCount = layout.getLineCount();
//        final CharSequence text = layout.getText();
//        int startOffset = 0;
//        int height = this.height;
//
//        for (int i = 0; i < lineCount; i++) {
//            if (height < lineCount * lineHeight - this.height ) {
//                // When the layout height has been exceeded
//                int to = startOffset + this.height;
//
//                Log.d("PAGINATION", "from: " + startOffset + "\nto: " + to + "\nlineCount: " + lineCount + "\nlinesOnPage: " + maxLinesOnPage
//                        + "\nlineHeight: " + lineHeight + "\npageHeight: " + this.height);
//                CharSequence t = text.subSequence(startOffset, to);
//                addPage(t);
//                startOffset = to;
//                height = to;
//            }
//
//            if (i == lineCount - 1) {
//                // Put the rest of the text into the last page
//                addPage(text.subSequence(startOffset, lineCount * this.height));
//                return;
//            }
//        }
    }

    private void addPage(CharSequence text) {
        pages.add(text);
    }

    public int getPagesCount() {
        return pages.size();
    }

    public CharSequence get(int index) {
        return (index >= 0 && index < pages.size()) ? pages.get(index) : null;
    }

    public List<CharSequence> getPages() {
        return pages;
    }
}
