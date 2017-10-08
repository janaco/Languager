package com.nandy.reader.mvp.model;

import android.content.Context;
import android.os.Handler;

import com.nandy.reader.adapter.ABCTestAdapter;
import com.nandy.reader.OnListItemClickListener;
import com.nandy.reader.emums.TaskType;
import com.nandy.reader.model.test.ABCTest;
import com.nandy.reader.model.test.Test;
import com.nandy.reader.model.word.Word;
import com.nandy.reader.TestBuilder;
import com.nandy.reader.TestsListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by yana on 05.10.17.
 */

public class ABCTestModel extends TestBuilder implements OnListItemClickListener<ABCTest.Variant> {


    private ABCTestAdapter adapter;
    private TestsListener testsListener;

    public ABCTestModel(Context context, TestsListener testsListener) {
        this.testsListener = testsListener;

        adapter = new ABCTestAdapter(context);
        adapter.setOnListItemClickListener(this);
    }

    public ABCTestAdapter getAdapter() {
        return adapter;
    }

    @Override
    protected ABCTest createTest(Word word, List<Word> words) {

        if (nativeLanguageTask()) {
            return createNativeLanguageTest(words, word);
        }

        return createLearningLanguageTest(words, word);
    }

    @Override
    public boolean showNext() {
        if (hasNext()) {
            ABCTest test = (ABCTest) getNext();
            adapter.setTests(test.getAnswer());
            testsListener.onNextTest(test, TaskType.CHOOSE);
            return true;
        }
        return false;
    }

    @Override
    public void onListItemClick(final ABCTest.Variant item, int position) {

        final ABCTest test = (ABCTest) getCurrentTest();
        test.setPassed(item.isCorrect());

        new Handler().postDelayed(() -> testsListener.onTextIsDone(test), 500);
    }


    private ABCTest createNativeLanguageTest(List<Word> words, Word word) {
        List<ABCTest.Variant> variants = new ArrayList<>();

        int count = 0;

        while (count < 3) {

            int i = new Random().nextInt(words.size());
            Word w = words.get(i);
            ABCTest.Variant variant = new ABCTest.Variant(w.getText(), w.getTranslationsAsString(), false);
            if (!variants.contains(variant)) {
                variants.add(variant);
                count++;
            }
        }

        variants.add(new Random().nextInt(4), new ABCTest.Variant(word.getText(), word.getTranslationsAsString(), true));

        return new ABCTest(new Test.MetaData(word.getText(), word.getOriginLanguage(), word.getTranslationLanguage()),
                word.getTranslationsAsString(), variants);
    }

    private ABCTest createLearningLanguageTest(List<Word> words, Word word) {
        List<ABCTest.Variant> variants = new ArrayList<>();

        int count = 0;

        while (count < 3) {

            int i = new Random().nextInt(words.size());
            Word w = words.get(i);
            ABCTest.Variant variant = new ABCTest.Variant(w.getTranslationsAsString(), w.getText(), false);
            if (!variants.contains(variant)) {
                variants.add(variant);
                count++;
            }
        }

        variants.add(new Random().nextInt(4), new ABCTest.Variant(word.getTranslationsAsString(), word.getText(), true));

        return new ABCTest(
                new Test.MetaData(word.getText(), word.getOriginLanguage(), word.getTranslationLanguage()),
                word.getText(), variants);
    }
}