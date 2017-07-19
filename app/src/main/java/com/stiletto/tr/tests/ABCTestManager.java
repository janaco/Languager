package com.stiletto.tr.tests;

import com.stiletto.tr.model.test.ABCTest;
import com.stiletto.tr.model.word.Word;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by yana on 18.07.17.
 */

public class ABCTestManager extends TestBuilder {

    @Override
    protected ABCTest createTest(Word word, List<Word> words, int limit) {

        if (nativeLanguageTask()) {
            return createNativeLanguageTest(words, word, limit);
        }

        return createLearningLanguageTest(words, word, limit);
    }

    private ABCTest createNativeLanguageTest(List<Word> words, Word word, int limit) {
        List<ABCTest.Variant> variants = new ArrayList<>();

        int count = 0;

        while (count < 3) {

            int i = new Random().nextInt(limit);
            Word w = words.get(i);
            ABCTest.Variant variant = new ABCTest.Variant(w.getText(), w.getTranslationsAsString(), false);
            if (!variants.contains(variant)) {
                variants.add(variant);
                count++;
            }
        }

        variants.add(new Random().nextInt(4), new ABCTest.Variant(word.getText(), word.getTranslationsAsString(), true));

        return new ABCTest(word.getTranslationsAsString(), variants);
    }

    private ABCTest createLearningLanguageTest(List<Word> words, Word word, int limit) {
        List<ABCTest.Variant> variants = new ArrayList<>();

        int count = 0;

        while (count < 3) {

            int i = new Random().nextInt(limit);
            Word w = words.get(i);
            ABCTest.Variant variant = new ABCTest.Variant(w.getTranslationsAsString(), w.getText(), false);
            if (!variants.contains(variant)) {
                variants.add(variant);
                count++;
            }
        }

        variants.add(new Random().nextInt(4), new ABCTest.Variant(word.getTranslationsAsString(), word.getText(), true));

        return new ABCTest(word.getText(), variants);
    }


}
