package com.stiletto.tr.tests;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.stiletto.tr.R;
import com.stiletto.tr.emums.TaskType;
import com.stiletto.tr.model.test.BooleanTest;
import com.stiletto.tr.model.test.Test;
import com.stiletto.tr.model.word.Word;

import java.util.List;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yana on 18.07.17.
 */

public class BooleanTestManager extends TestBuilder {


    @Bind(R.id.boolean_text)
    TextView viewTask;
    @Bind(R.id.boolean_answer)
    TextView viewAnswer;
    @Bind(R.id.approve)
    TextView btnApprove;
    @Bind(R.id.reject)
    TextView btnReject;

    private Context context;
    private TestsListener testsListener;

    public static BooleanTestManager init(View view, TestsListener testsListener){
        return new BooleanTestManager(view, testsListener);
    }

    private BooleanTestManager(View view, TestsListener testsListener) {
        this.testsListener = testsListener;
        context = view.getContext();
        ButterKnife.bind(this, view);
    }

    @Override
    protected Test createTest(Word word, List<Word> words) {

        String task;
        String answer;
        String correctAnswer;
        boolean approvableTest = approvable();

        if (nativeLanguageTask()) {
            task = word.getTranslationsAsString();
            correctAnswer = word.getText();
            answer = createAnswerOnNativeLanguageTest(words, word, approvableTest, words.size());

        } else {
            task = word.getText();
            correctAnswer = word.getTranslationsAsString();
            answer = createAnswerOnLearningLanguageTest(words, word, approvableTest, words.size());
        }


        return new BooleanTest(
                new Test.MetaData(word.getText(), word.getOriginLanguage(), word.getTranslationLanguage()),
                task, answer, correctAnswer, approvableTest);

    }

    @Override
    public boolean showNext() {

        if (hasNext()){
            BooleanTest test = (BooleanTest) getNext();
            viewTask.setText(test.getTask());
            viewAnswer.setText(test.getAnswer());

            btnApprove.setBackgroundColor(ContextCompat.getColor(context, R.color.verdigris));
            btnReject.setBackgroundColor(ContextCompat.getColor(context, R.color.tea_rose));
            testsListener.onNextTest(test, TaskType.BOOLEAN);

            return true;
        }
        return false;
    }

    @OnClick(R.id.approve)
    void onApproveClick() {

        final BooleanTest test = (BooleanTest) getCurrentTest();
        test.setPassed(test.isApprovable());
        Log.d("TESTS_", "APPROVE.test: " + test);


        int color = test.isPassed() ?
                    ContextCompat.getColor(context, R.color.green_500) :
                    ContextCompat.getColor(context, R.color.red_500);

        btnApprove.setBackgroundColor(color);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                testsListener.onTextIsDone(test);
            }
        }, 500);
    }

    @OnClick(R.id.reject)
    void onRejectClick() {

        final BooleanTest test = (BooleanTest) getCurrentTest();
        test.setPassed(!test.isApprovable());

        Log.d("TESTS_", "REJECT.test: " + test);

        int color = test.isPassed() ?
                    ContextCompat.getColor(context, R.color.green_500) :
                    ContextCompat.getColor(context, R.color.red_500);

        btnReject.setBackgroundColor(color);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
              testsListener.onTextIsDone(test);
            }
        }, 500);
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
