package com.stiletto.tr.pagination;

import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;

import com.stiletto.tr.utils.ReaderPrefs;

import java.util.ArrayList;
import java.util.List;

/**
 * Use it to split book content on pages.
 * Page size depends on phone screen sizes and size of view(container)
 * where this content should be shown.
 * <p>
 * Created by yana on 29.12.16.
 */

public class Pagination {

    private final int width;
    private final int height;
    private final float lineSpacingMultiplier;
    private final float lineSpacingExtra;
    private final TextPaint textPaint;
    private final List<CharSequence> pages;

    public Pagination(CharSequence text, ReaderPrefs prefs) {
        this(prefs);
        splitOnPages(text);
    }

    public Pagination( ReaderPrefs prefs) {
        this.width = prefs.getPageWidth() - prefs.getPaddingHorizontal() * 2;
        this.height = prefs.getPageHeight() - prefs.getPaddingVertical() * 3;
        this.textPaint = prefs.getTextPaint();
        this.lineSpacingMultiplier = prefs.getLineSpacingMultiplier();
        this.lineSpacingExtra = prefs.getLineSpacingExtra();
        this.pages = new ArrayList<>();
    }

    private void splitOnPages(CharSequence content) {
        final StaticLayout layout =
                new StaticLayout(content, textPaint, width, Layout.Alignment.ALIGN_NORMAL,
                        lineSpacingMultiplier, lineSpacingExtra, false);

        final int lineCount = layout.getLineCount();
        final CharSequence text = layout.getText();
        int startOffset = 0;
        int height = this.height;

        for (int i = 0; i < lineCount; ++i) {
            if (height < layout.getLineBottom(i)) {
                int to = layout.getLineEnd(i);
                CharSequence t = text.subSequence(startOffset, to);
                addPage(t);
                startOffset = to;
                height = layout.getLineTop(i) + this.height;
            }

            if (i == lineCount - 1) {
                // Put the rest of the text into the last page
                addPage(text.subSequence(startOffset, layout.getLineEnd(i)));
                return;
            }
        }
    }

    public List<CharSequence> appendContent(CharSequence content) {
        int lastPageIndex = getPagesCount() - 1;
        if (lastPageIndex >= 0) {
            content = new StringBuilder().append(get(lastPageIndex)).append(content).toString();
            pages.remove(lastPageIndex);
        }

        final StaticLayout layout =
                new StaticLayout(content, textPaint, width, Layout.Alignment.ALIGN_NORMAL,
                        lineSpacingMultiplier, lineSpacingExtra, false);

        final int lineCount = layout.getLineCount();
        final CharSequence text = layout.getText();
        int startOffset = 0;
        int height = this.height;


        List<CharSequence> newPages = new ArrayList<>();
        for (int i = 0; i < lineCount; ++i) {
            if (height < layout.getLineBottom(i)) {
                int to = layout.getLineEnd(i);
                CharSequence t = text.subSequence(startOffset, to);
                addPage(t);
                startOffset = to;
                height = layout.getLineTop(i) + this.height;
            }

            if (i == lineCount - 1) {
                // Put the rest of the text into the last page
                newPages.add(text.subSequence(startOffset, layout.getLineEnd(i)));
                break;
            }
        }

        pages.addAll(newPages);
        return newPages;
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
