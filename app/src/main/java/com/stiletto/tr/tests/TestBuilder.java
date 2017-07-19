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

    private List<Test> tests;
    private int index;

    public void create(List<Word> words) {

        tests = new ArrayList<>();

        for (Word word : words) {
            tests.add(createTest(word, words, words.size()));
        }
    }

    protected abstract Test createTest(Word word, List<Word> words, int limit);

    public Test getNext() {
        return tests.get(index++);
    }

    public boolean hasNext() {
        return index < tests.size();
    }

    /**
     * Test task should be written on native language and translation
     * or answers variants should be on unknown(learning) language.
     * @return
     */
    protected boolean nativeLanguageTask() {

        return new Random().nextInt(2) == 1;
    }

}
