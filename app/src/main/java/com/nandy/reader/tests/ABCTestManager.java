package com.nandy.reader.tests;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.nandy.reader.R;
import com.nandy.reader.adapter.ABCTestAdapter;
import com.nandy.reader.core.OnListItemClickListener;
import com.nandy.reader.emums.TaskType;
import com.nandy.reader.model.test.ABCTest;
import com.nandy.reader.model.test.Test;
import com.nandy.reader.model.word.Word;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by yana on 18.07.17.
 */

public class ABCTestManager extends TestBuilder implements OnListItemClickListener<ABCTest.Variant> {

    @Bind(R.id.abc_text)
    TextView viewTask;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    private ABCTestAdapter adapter;
    private TestsListener testsListener;

    public static ABCTestManager init(View view, TestsListener testsListener) {
        return new ABCTestManager(view.getContext(), view, testsListener);
    }

    private ABCTestManager(Context context, View view, TestsListener testsListener) {
        ButterKnife.bind(this, view);

        this.testsListener = testsListener;

        adapter = new ABCTestAdapter(context);
        adapter.setOnListItemClickListener(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
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
            viewTask.setText(test.getTask());
            adapter.setTests(test.getAnswer());
            recyclerView.setAdapter(adapter);
            testsListener.onNextTest(test, TaskType.CHOOSE);
            return true;
        }
        return false;
    }

    @Override
    public void onListItemClick(final ABCTest.Variant item, int position) {

        final ABCTest test = (ABCTest) getCurrentTest();
        test.setPassed(item.isCorrect());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                testsListener.onTextIsDone(test);
            }
        }, 500);
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