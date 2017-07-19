package com.stiletto.tr.tests;

import com.stiletto.tr.model.test.ABCTest;
import com.stiletto.tr.model.test.Test;
import com.stiletto.tr.model.word.Word;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by yana on 18.07.17.
 */

public abstract class TestBuilder {

    private List<Test> tests = new ArrayList<>();
    private int index = -1;

    public void create(List<Word> words) {

        for (Word word : words) {
            tests.add(createTest(word, words, words.size()));
        }
    }

    public void create(List<Word> words, Word word) {
        tests.add(createTest(word, words, words.size()));
    }

    protected abstract Test createTest(Word word, List<Word> words, int limit);

    public abstract boolean showNext();

    public Test getNext() {
        return tests.get(++index);
    }

    public Test getCurrentTest() {
        return tests.get(index);
    }

    public boolean hasNext() {
        return index < tests.size()-1;
    }

    /**
     * Test task should be written on native language and translation
     * or answers variants should be on unknown(learning) language.
     *
     * @return
     */
    protected boolean nativeLanguageTask() {

        return new Random().nextInt(2) == 1;
    }

}
