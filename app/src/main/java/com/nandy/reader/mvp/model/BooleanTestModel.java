package com.nandy.reader.mvp.model;

import android.os.Handler;

import com.nandy.reader.emums.TaskType;
import com.nandy.reader.model.test.BooleanTest;
import com.nandy.reader.model.test.Test;
import com.nandy.reader.model.word.RealmString;
import com.nandy.reader.model.word.Word;
import com.nandy.reader.TestBuilder;
import com.nandy.reader.TestsListener;

import java.util.List;
import java.util.Random;

/**
 * Created by yana on 05.10.17.
 */

public class BooleanTestModel extends TestBuilder {


    private TestsListener testsListener;

    public BooleanTestModel(TestsListener testsListener) {
        this.testsListener = testsListener;
    }

    @Override
    protected Test createTest(Word word, List<Word> words) {

        String task;
        String answer;
        String correctAnswer;
        boolean approvableTest = approvable();
        List<RealmString> translations = word.getTranslations();

        if (nativeLanguageTask()) {
            task = translations.get(new Random().nextInt(translations.size())).value;
            correctAnswer = word.getText();
            answer = createAnswerOnNativeLanguageTest(words, word, approvableTest, words.size());

        } else {
            task = word.getText();
            correctAnswer = translations.get(new Random().nextInt(translations.size())).value;
            answer = createAnswerOnLearningLanguageTest(words, word, approvableTest, words.size());
        }


        return new BooleanTest(
                new Test.MetaData(word.getText(), word.getOriginLanguage(), word.getTranslationLanguage()),
                task, answer, correctAnswer, approvableTest);

    }

    @Override
    public boolean showNext() {

        if (hasNext()) {
            BooleanTest test = (BooleanTest) getNext();
            testsListener.onNextTest(test, TaskType.BOOLEAN);
            return true;
        }

        return false;
    }

    boolean approve() {

        final BooleanTest test = (BooleanTest) getCurrentTest();
        test.setPassed(test.isApprovable());

        new Handler().postDelayed(() -> testsListener.onTextIsDone(test), 500);

        return test.isPassed();
    }

    boolean reject() {

        final BooleanTest test = (BooleanTest) getCurrentTest();
        test.setPassed(!test.isApprovable());

        new Handler().postDelayed(() -> testsListener.onTextIsDone(test), 500);

        return test.isPassed();
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