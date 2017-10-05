package com.nandy.reader.mvp.model;

import android.os.Handler;

import com.nandy.reader.emums.TaskType;
import com.nandy.reader.model.test.Test;
import com.nandy.reader.model.test.WritingTest;
import com.nandy.reader.model.word.Word;
import com.nandy.reader.tests.TestBuilder;
import com.nandy.reader.tests.TestsListener;

import java.util.List;

/**
 * Created by yana on 05.10.17.
 */

public class WritingTestModel extends TestBuilder {

    private TestsListener testsListener;

    public WritingTestModel( TestsListener testsListener) {
        this.testsListener = testsListener;
    }

    @Override
    protected Test createTest(Word word, List<Word> words) {

        if (nativeLanguageTask()) {
            return new WritingTest(
                    new Test.MetaData(word.getText(), word.getOriginLanguage(), word.getTranslationLanguage()),
                    word.getTranslationsAsString(), word.getText());
        }

        return new WritingTest(
                new Test.MetaData(word.getText(), word.getOriginLanguage(), word.getTranslationLanguage()),
                word.getText(), word.getTranslationsAsString());
    }


    @Override
    public boolean showNext() {
        if (hasNext()) {

            WritingTest test = (WritingTest) getNext();

            testsListener.onNextTest(test, TaskType.WRITING);

            return true;
        }

        return false;
    }

    boolean check(String answer) {
        final WritingTest test = (WritingTest) getCurrentTest();
        test.setPassed(test.isAnswerCorrect(answer));


        new Handler().postDelayed(() -> testsListener.onTextIsDone(test), 500);

        return test.isPassed();

    }


}
