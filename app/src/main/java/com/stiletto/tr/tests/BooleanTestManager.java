package com.stiletto.tr.tests;

import com.stiletto.tr.model.test.BooleanTest;
import com.stiletto.tr.model.test.Test;
import com.stiletto.tr.model.word.DictionaryItem;
import com.stiletto.tr.model.word.Word;

import java.util.List;
import java.util.Random;

/**
 * Created by yana on 18.07.17.
 */

public class BooleanTestManager extends TestBuilder {

    @Override
    protected Test createTest(Word word, List<Word> words, int limit) {

        String task;
        String answer;
        String correctAnswer;
        boolean approvableTest = approvable();

        if (nativeLanguageTask()) {
            task = word.getTranslationsAsString();
            correctAnswer = word.getText();
            answer = createAnswerOnNativeLanguageTest(words, word, approvableTest, limit);

        } else {
            task = word.getText();
            correctAnswer = word.getTranslationsAsString();
            answer = createAnswerOnLearningLanguageTest(words, word, approvableTest, limit);
        }


        return new BooleanTest(task, answer, correctAnswer, approvableTest);

    }

    private boolean approvable() {
        return new Random().nextInt(140) % 2 == 0;
    }

    private String createAnswerOnNativeLanguageTest(List<Word> words, Word word, boolean approvableTest, int limit) {
        if (approvableTest) {
            return word.getText();
        }

        Word w = words.get(new Random().nextInt(limit));

        if (w.getDictionaryItems().size() > 0) {
            int itemsSize = w.getDictionaryItems().size();
            return w.getDictionaryItems().get(new Random().nextInt(itemsSize)).getOriginText();
        }

        return w.getText();
    }

    private String createAnswerOnLearningLanguageTest(List<Word> words, Word word, boolean approvableTest, int limit) {
        if (approvableTest) {
            return word.getTranslationsAsString();
        }

        Word w = words.get(new Random().nextInt(limit));
        if (w.getDictionaryItems().size() > 0) {
            int itemsSize = w.getDictionaryItems().size();
            return w.getDictionaryItems().get(new Random().nextInt(itemsSize)).getTranslationsAsString();
        }
        return w.getTranslationsAsString();
    }
}
