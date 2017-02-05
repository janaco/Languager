package com.stiletto.tr.utils;

import android.text.Layout;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;
import android.text.style.ScaleXSpan;
import android.util.Log;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yana on 05.02.17.
 */

public class TextAligmentUtils {

    private static final Pattern WHITESPACE_PATTERN = Pattern.compile("\\s");

    public static final float DEFAULT_MAX_PROPORTION = 10f;
    public static final int MAX_SPANS = 12;

    /**
     * Adds ScaleX spans to expand widespaces and justify the lines.
     */
    public static void setupScaleSpans(final @NotNull Spannable spannable, TextView textView) {


        int[] textViewSpanStarts = new int[MAX_SPANS];
        int[] textViewSpanEnds = new int[MAX_SPANS];

        final int length = spannable.length();
        if (length == 0) {
            return;
        }

        // We use the layout to get line widths before justification
        final Layout layout = textView.getLayout();

        if (layout == null || layout.getLineCount() == 0) {
            return;
        }

        final int lineCount = layout.getLineCount();

        // Layout line widths do not include the padding
        final int wantedWidth = textView.getMeasuredWidth() -
                textView.getCompoundPaddingLeft() - textView.getCompoundPaddingRight();

        // We won't justify lines if it requires expanding the spaces beyond the maximum proportion.
        final float maxProportion = DEFAULT_MAX_PROPORTION;

        for (int line = 0; line < lineCount; ++line) {

            final int lineStart = layout.getLineStart(line);
            final int lineEnd = line == lineCount - 1 ? length : layout.getLineEnd(line);

            // Don't justify empty lines
            if (lineEnd == lineStart) {
                continue;
            }

            // Don't justify the last line or lines ending with a newline.
            if (spannable.charAt(lineEnd - 1) == '\n') {
                continue;
            }

            // Don't include the trailing whitespace as an expandable whitespace.
            final int visibleLineEnd = layout.getLineVisibleEnd(line);

            // Don't justify lines that only contain whitespace
            if (visibleLineEnd == lineStart) {
                continue;
            }

            // Layout line width
            final float width = Layout.getDesiredWidth(spannable, lineStart, lineEnd, layout.getPaint());

            // Remaining space to fill
            int remaining = (int) Math.floor(wantedWidth - width);

            if (remaining > 0) {
                // Make sure trailing whitespace doesn't use any space by setting its scaleX to 0
                if (visibleLineEnd < lineEnd) {
                    spannable.setSpan(
                            new ScaleXSpan(0f),
                            visibleLineEnd,
                            lineEnd,
                            Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                }

                // Line text
                final CharSequence sub = spannable.subSequence(lineStart, visibleLineEnd);

                // Accumulated total whitespace width
                float spaceWidth = 0f;
                // Number of whitespace sections
                int sectionNumber = 0;

                // Find whitespace sections and store their start and end positions
                final Matcher matcher = WHITESPACE_PATTERN.matcher(sub);

                while (matcher.find()) {
                    final int matchStart = matcher.start();
                    final int matchEnd = matcher.end();
                    // If the line starts with whitespace, it's probably an indentation
                    // and we don't want to expand indentation space to preserve alignment
                    if (matchStart == 0) continue;
                    // skip single thin and hair spaces, as well as a single non breaking space
                    if ((matchEnd - matchStart) == 1) {
                        final int c = sub.charAt(matchStart);
                        if (c == '\u200a' || c == '\u2009' || c == '\u00a0') continue;
                    }
                    if (layout.getPaint() == null) {
                        return;
                    }
                    final float matchWidth =
                            layout.getPaint().measureText(spannable, lineStart + matchStart, lineStart + matchEnd);

                    spaceWidth += matchWidth;

                    textViewSpanStarts[sectionNumber] = matchStart;
                    textViewSpanEnds[sectionNumber] = matchEnd;
                    ++sectionNumber;
                }

                if (sectionNumber > MAX_SPANS) {
                    sectionNumber = MAX_SPANS;
                }

                // Excess space is distributed evenly
                // (with the same proportions for all whitespace sections)
                final float proportion = (spaceWidth + remaining) / spaceWidth;

                // Don't justify the line if we can't do it without expanding whitespaces too much.
                if (proportion > maxProportion) continue;

                // Add ScaleX spans on the whitespace sections we want to expand.
                for (int i = 0; i < sectionNumber; ++i) {
//                    textViewSpans[i] = new ScaleSpan(proportion);

                    int start = lineStart + textViewSpanStarts[i];
                    int end = lineStart + textViewSpanEnds[i];

                    Log.d("TEXT_VIEW", "setSpan:\nproportion: " + proportion
                            + "\nlineStart: " + lineStart
                            + "\nstart: " + start + "\nend: " + end + "\n \n ");

                    spannable.setSpan(
                            new ScaleSpan(proportion),
                            start,
                            end,
                            Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                }

                // Compute the excess space.
//                int excess = (int) Math.ceil(Layout.getDesiredWidth(spannable,
//                        lineStart, lineEnd,
//                        layout.getPaint())) - wantedWidth;
                // We might have added too much space because of rounding errors and because adding spans
                // can modify the kerning.
                // If that is the case, then we try to reduce the extra space slightly until there's no
                // excess space left.

//                while (excess > 0) {
//
//                     Clear the spans from the previous attempt.
//                    for (int span = 0; span < sectionNumber; ++span) {
//                        spannable.removeSpan(textViewSpans[span]);
//                    }
//                     Reduce the remaining space exponentially for each iteration.
//                    remaining -= excess;
//                     Set the spans with the new proportions.
//                    final float reducedProportions = (spaceWidth + remaining) / spaceWidth;
//                    for (int span = 0; span < sectionNumber; ++span) {
//                        textViewSpans[span] = new ScaleSpan(reducedProportions);
//                        spannable.setSpan(
//                                textViewSpans[span],
//                                lineStart + textViewSpanStarts[span],
//                                lineStart + textViewSpanEnds[span],
//                                Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//                    }
//                     recompute the excess space.
//                    excess = (int) Math.ceil(Layout.getDesiredWidth(spannable,
//                            lineStart, lineEnd,
//                            layout.getPaint())) - wantedWidth;
//                }

            }
        }

//        textView.setText(spannable);

    }

    private static void removeScaleSpans(Spannable spannable, ScaleSpan[] scaleSpans) {
        if (scaleSpans != null) {
            for (final ScaleSpan span : scaleSpans) {
                spannable.removeSpan(span);
            }
        }
    }

    public static interface Justified {

        /**
         * Gets the TextView (usually the class implementing this interface).
         *
         * @return the TextView.
         */
        @NotNull
        public TextView getTextView();

        /**
         * Gets the maximum stretching proportion allowed for whitespaces. Lines that require
         * expanding whitespace beyond this proportion to be justified will not be justified.
         *
         * @return the maximum stretching proportion allowed.
         */
        public float getMaxProportion();

    }

    public static class ScaleSpan extends MetricAffectingSpan {

        private final float mProportion;

        public ScaleSpan(final float proportion) {
            mProportion = proportion;
        }

        @Override
        public void updateDrawState(final @NotNull TextPaint ds) {
            ds.setTextScaleX(ds.getTextScaleX() * mProportion);
        }

        @Override
        public void updateMeasureState(final @NotNull TextPaint ds) {
            ds.setTextScaleX(ds.getTextScaleX() * mProportion);
        }

    }

}
