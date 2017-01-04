package com.stiletto.tr.pagination;

import android.support.annotation.NonNull;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yana on 29.12.16.
 */

public class Pagination {

    private final boolean includePadding;
    private final int width;
    private final int height;
    private final float lineSpacingMultiplier;
    private final float lineSpacingExtra;
    private final CharSequence text;
    private final TextPaint textPaint;
    private final List<CharSequence> pages;

    public Pagination(CharSequence text, @NonNull TextView view){
        this.text = text;
        this.width = view.getWidth();
        this.height = view.getHeight();
        this.textPaint = view.getPaint();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            this.lineSpacingMultiplier = view.getLineSpacingMultiplier() + 0.1f;
            this.lineSpacingExtra = view.getLineSpacingExtra();
            this.includePadding = view.getIncludeFontPadding();
        }else {
            this.lineSpacingMultiplier = 0;
            this.lineSpacingExtra = 1.1f;
            this.includePadding = true;
        }

        this.pages = new ArrayList<>();

        layout();

    }

    @Override
    public String toString() {
        return "Pagination{" + "\n" +
                "includePadding=" + includePadding + "\n" +
                ", width=" + width + "\n" +
                ", height=" + height + "\n" +
                ", lineSpacingMultiplier=" + lineSpacingMultiplier + "\n" +
                ", lineSpacingExtra=" + lineSpacingExtra + "\n" +
                '}';
    }

    private void layout() {
        final StaticLayout layout =
                new StaticLayout(text, textPaint, width, Layout.Alignment.ALIGN_NORMAL,
                        lineSpacingMultiplier, lineSpacingExtra, includePadding);

        final int lineCount = layout.getLineCount();
        final CharSequence text = layout.getText();
        int startOffset = 0;
        int height = this.height;

        for (int i = 0; i < lineCount; i++) {
            if (height < layout.getLineBottom(i)) {
                // When the layout height has been exceeded
                addPage(text.subSequence(startOffset, layout.getLineStart(i)));
                startOffset = layout.getLineStart(i);
                height = layout.getLineTop(i) + this.height;
            }

            if (i == lineCount - 1) {
                // Put the rest of the text into the last page
                addPage(text.subSequence(startOffset, layout.getLineEnd(i)));
                return;
            }
        }
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
