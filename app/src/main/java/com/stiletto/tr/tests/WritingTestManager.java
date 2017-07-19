package com.stiletto.tr.tests;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.EditText;
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

    @Bind(R.id.item_task_writing)
    TextView viewTask;
    @Bind(R.id.item_answer_writing)
    EditText viewAnswer;
    @Bind(R.id.item_next)
    TextView btnNext;

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
    protected Test createTest(Word word, List<Word> words, int limit) {

        if (nativeLanguageTask()) {
            return new WritingTest(word.getTranslationsAsString(), word.getText());
        }

        return new WritingTest(word.getText(), word.getTranslationsAsString());
    }


    @Override
    public boolean showNext() {
        if (hasNext()) {

            WritingTest test = (WritingTest) getNext();
            viewTask.setText(test.getTask());
            viewAnswer.setText("");

            final Drawable drawable = ContextCompat.getDrawable(context, R.drawable.rectangle_rounded);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                drawable.setTint(ContextCompat.getColor(context, R.color.colorPrimaryDark));
            }
            btnNext.setBackground(drawable);
            testsListener.onNextTest(test, TaskType.WRITING);

            return true;
        }

        return false;
    }

    @OnClick(R.id.item_next)
    void onNextClick() {
        final WritingTest test = (WritingTest) getCurrentTest();
        String userAnswer = viewAnswer.getText().toString();
        test.setPassed(test.isAnswerCorrect(userAnswer));

        final Drawable drawable = ContextCompat.getDrawable(context, R.drawable.rectangle_rounded);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawable.setTint(test.isPassed() ?
                    ContextCompat.getColor(context, R.color.green_500) :
                    ContextCompat.getColor(context, R.color.red_500));
        }
        btnNext.setBackground(drawable);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                testsListener.onTextIsDone(TaskType.WRITING, test.isPassed());
            }
        }, 500);

    }


}
