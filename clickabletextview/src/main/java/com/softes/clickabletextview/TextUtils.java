package com.softes.clickabletextview;

import android.support.annotation.NonNull;
import android.text.Layout;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.ScaleXSpan;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yana on 25.12.16.
 */
class TextUtils {

    private static Character[] punctuations =
            new Character[]{',', '.', ';', '!', '"', '，', '。', '！', '；',
                    '、', '：', '“', '”', '?', '？', '-', '+', '\n', '\t', '(', ')',
                    '[', ']', '{', '}', '/', '\\', '|'};

    private static final Pattern WHITESPACE_PATTERN = Pattern.compile("\\s");

    private static final float DEFAULT_MAX_PROPORTION = 10f;
    private static final int MAX_SPANS = 10;

    static void justifyText(final @NotNull Spannable spannable, TextView textView) {

        int[] textViewSpanStarts = new int[MAX_SPANS];
        int[] textViewSpanEnds = new int[MAX_SPANS];

        final int length = spannable.length();
        if (length == 0) {
            return;
        }

        final ScaleSpan[] scaleSpans = spannable.getSpans(0, spannable.length(), ScaleSpan.class);
        if (scaleSpans != null) {
            for (final ScaleSpan span : scaleSpans) {
                spannable.removeSpan(span);
            }
        }

        final Layout layout = textView.getLayout();

        if (layout == null || layout.getLineCount() == 0) {
            return;
        }

        final int lineCount = layout.getLineCount();

        final int wantedWidth = textView.getMeasuredWidth() -
                textView.getCompoundPaddingLeft() - textView.getCompoundPaddingRight();

        for (int line = 0; line < lineCount; ++line) {

            final int lineStart = layout.getLineStart(line);
            final int lineEnd = line == lineCount - 1 ? length : layout.getLineEnd(line);

            if (lineEnd == lineStart) {
                continue;
            }

            // Don't justify the last line or lines ending with a newline.
            if ((line >= lineCount - 1
                    && (spannable.charAt(lineEnd - 1) == '.'
                    || spannable.charAt(lineEnd - 1) == '?'
                    || spannable.charAt(lineEnd - 1) == '!'))
                    || spannable.charAt(lineEnd - 1) == '\n') {
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
                            layout.getPaint()
                                    .measureText(spannable, lineStart + matchStart, lineStart + matchEnd);

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
                if (proportion > DEFAULT_MAX_PROPORTION) continue;

                // Add ScaleX spans on the whitespace sections we want to expand.
                for (int i = 0; i < sectionNumber; ++i) {

                    int start = lineStart + textViewSpanStarts[i];
                    int end = lineStart + textViewSpanEnds[i];

                    spannable.setSpan(
                            new ScaleSpan(proportion),
                            start,
                            end,
                            Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                }

            }
        }
    }

    @NonNull
    static List<ClickableWord> getWordIndices(String content) {
        List<Integer> separatorIndices = getSeparatorIndices(content, ' ');
        for (Character punctuation : punctuations) {
            separatorIndices.addAll(getSeparatorIndices(content, punctuation));
        }
        Collections.sort(separatorIndices);
        List<ClickableWord> wordList = new ArrayList<>();
        int start = 0;
        int end;
        for (int i = 0; i < separatorIndices.size(); i++) {
            end = separatorIndices.get(i);
            if (start == end) {
                start++;
            } else {
                wordList.add(new ClickableWord(start, end));
                start = end + 1;
            }
        }
        return wordList;
    }

    private static List<Integer> getSeparatorIndices(String word, char character) {
        int index = word.indexOf(character);
        List<Integer> indices = new ArrayList<>();
        while (index != -1) {
            indices.add(index);
            index = word.indexOf(character, ++index);
        }
        return indices;
    }

}
