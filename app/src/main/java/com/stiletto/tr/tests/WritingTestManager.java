package com.stiletto.tr.tests;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.stiletto.tr.R;
import com.stiletto.tr.emums.TaskType;
import com.stiletto.tr.model.test.Test;
import com.stiletto.tr.model.test.WritingTest;
import com.stiletto.tr.model.word.Word;
import com.stiletto.tr.readers.task.PDFParser;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yana on 18.07.17.
 */

public class WritingTestManager extends TestBuilder {

    @Bind(R.id.writing_text)
    TextView viewTask;
    @Bind(R.id.writing_answer)
    EditText viewAnswer;
    @Bind(R.id.check)
    ImageView btnNext;

    private Context context;
    private TestsListener testsListener;

    public static WritingTestManager init(View view, TestsListener testsListener) {
        return new WritingTestManager(view, testsListener);
    }

    private WritingTestManager(View view, TestsListener testsListener) {
        this.testsListener = testsListener;
        this.context = view.getContext();
        ButterKnife.bind(this, view);
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
            viewTask.setText(test.getTask());
            viewAnswer.setText("");

            btnNext.setBackgroundColor(Color.TRANSPARENT);
            testsListener.onNextTest(test, TaskType.WRITING);

            return true;
        }

        return false;
    }

    @OnClick(R.id.check)
    void onNextClick() {
        final WritingTest test = (WritingTest) getCurrentTest();
        String userAnswer = viewAnswer.getText().toString();
        test.setPassed(test.isAnswerCorrect(userAnswer));

        int color = test.isPassed() ?
                    ContextCompat.getColor(context, R.color.green_500) :
                    ContextCompat.getColor(context, R.color.red_500);

        btnNext.setBackgroundColor(color);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                testsListener.onTextIsDone(test);
            }
        }, 500);

    }


}
