package com.stiletto.tr.view.text;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by yana on 25.12.16.
 */

public class ClickableTextUtils {


    private static List<Character> punctuations;

    static {
        Character[] punctuationsCharsArray =
                new Character[]{',', '.', ';', '!', '"', '，', '。', '！', '；',
                        '、', '：', '“', '”', '?', '？', '-', '+', '\n', '\t', '(', ')',
                        '[', ']', '{', '}', '/', '\\', '|'};
        punctuations = Arrays.asList(punctuationsCharsArray);
    }

    @NonNull
    static List<Word> getWordIndices(String content) {
        List<Integer> separatorIndices = getSeparatorIndices(content, ' ');
        for (Character punctuation : punctuations) {
            separatorIndices.addAll(getSeparatorIndices(content, punctuation));
        }
        Collections.sort(separatorIndices);
        List<Word> wordList = new ArrayList<>();
        int start = 0;
        int end;
        for (int i = 0; i < separatorIndices.size(); i++) {
            end = separatorIndices.get(i);
            if (start == end) {
                start++;
            } else {
                wordList.add(new Word(start, end));
                start = end + 1;
            }
        }
        return wordList;
    }

    /**
     * Get every word's index array of text
     *
     * @param word      the content
     * @param character separate char
     * @return index array
     */
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
