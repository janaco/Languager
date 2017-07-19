package com.stiletto.tr.tests;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.stiletto.tr.R;
import com.stiletto.tr.adapter.ABCTestAdapter;
import com.stiletto.tr.core.OnListItemClickListener;
import com.stiletto.tr.emums.TaskType;
import com.stiletto.tr.model.test.ABCTest;
import com.stiletto.tr.model.test.Test;
import com.stiletto.tr.model.word.Word;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by yana on 18.07.17.
 */

public class ABCTestManager extends TestBuilder implements OnListItemClickListener<ABCTest.Variant>{

    @Bind(R.id.task_abc)
    TextView viewTask;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    private ABCTestAdapter adapter;
    private TestsListener testsListener;

    public static ABCTestManager init(View view, TestsListener testsListener){
        return new ABCTestManager(view.getContext(), view, testsListener);
    }

    private ABCTestManager(Context context, View view, TestsListener testsListener){
        ButterKnife.bind(this, view);

        this.testsListener = testsListener;

        adapter = new ABCTestAdapter(context);
        adapter.setOnListItemClickListener(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected ABCTest createTest(Word word, List<Word> words, int limit) {

        if (nativeLanguageTask()) {
            return createNativeLanguageTest(words, word, limit);
        }

        return createLearningLanguageTest(words, word, limit);
    }

    @Override
    public boolean showNext() {
        Log.d("TESTS_", "CHOOSE.hasNext: " + hasNext());
        if (hasNext()){
            ABCTest test = (ABCTest) getNext();
            Log.d("TESTS_", "CHOOSE.next: " + test);
            viewTask.setText(test.getTask());
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

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    testsListener.onTextIsDone(TaskType.CHOOSE, test.isPassed());
                }
            }, 500);
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
