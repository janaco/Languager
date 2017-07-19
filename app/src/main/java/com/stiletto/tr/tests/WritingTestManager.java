package com.stiletto.tr.tests;

import com.stiletto.tr.model.test.Test;
import com.stiletto.tr.model.test.WritingTest;
import com.stiletto.tr.model.word.Word;

import java.util.List;

/**
 * Created by yana on 18.07.17.
 */

public class WritingTestManager extends TestBuilder {


    @Override
    protected Test createTest(Word word, List<Word> words, int limit) {

        if (nativeLanguageTask()) {
            return new WritingTest(word.getTranslationsAsString(), word.getText());
        }

        return new WritingTest(word.getText(), word.getTranslationsAsString());
    }


}
