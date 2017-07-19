package com.stiletto.tr.tests;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.stiletto.tr.R;
import com.stiletto.tr.emums.TaskType;
import com.stiletto.tr.model.test.Test;
import com.stiletto.tr.model.word.Word;

import java.util.List;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by yana on 19.07.17.
 */

public class TestsManager implements TestsListener {

    @Bind(R.id.view_abc_test)
    View viewTestABC;
    @Bind(R.id.view_boolean_test)
    View viewTestBoolean;
    @Bind(R.id.view_writing_test)
    View viewWritingTest;

    private ABCTestManager abcTestManager;
    private BooleanTestManager booleanTestManager;
    private WritingTestManager writingTestManager;

    private TaskType[] taskTypes;

    public TestsManager(View view) {
        ButterKnife.bind(this, view);
    }

    public void initTests(TaskType[] taskTypes) {
        this.taskTypes = taskTypes;

        for (TaskType taskType : taskTypes) {

            switch (taskType) {
                case CHOOSE:
                    abcTestManager = ABCTestManager.init(viewTestABC, this);
                    break;

                case BOOLEAN:
                    booleanTestManager = BooleanTestManager.init(viewTestBoolean, this);
                    break;

                case WRITING:
                    writingTestManager = WritingTestManager.init(viewWritingTest, this);
                    break;
            }
        }
    }

    public void createTests(List<Word> words) {

        for (Word word : words) {

            for (TaskType taskType : taskTypes) {

                switch (taskType) {

                    case CHOOSE:
                        abcTestManager.create(words, word);
                        break;

                    case BOOLEAN:
                        booleanTestManager.create(words, word);
                        break;

                    case WRITING:
                        writingTestManager.create(words, word);
                        break;
                }
            }
        }
    }

    public void start() {
        showNextTest(getNextTaskType());
    }

    @Override
    public void onNextTest(Test test, TaskType taskType) {

        switch (taskType) {
            case CHOOSE:
                viewTestABC.setVisibility(View.VISIBLE);
                viewTestBoolean.setVisibility(View.GONE);
                viewWritingTest.setVisibility(View.GONE);
                break;

            case BOOLEAN:
                viewTestABC.setVisibility(View.GONE);
                viewTestBoolean.setVisibility(View.VISIBLE);
                viewWritingTest.setVisibility(View.GONE);
                break;

            case WRITING:
                viewTestABC.setVisibility(View.GONE);
                viewTestBoolean.setVisibility(View.GONE);
                viewWritingTest.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onTextIsDone(TaskType taskType, boolean passed) {

        TaskType nextTask = getNextTaskType();
        while (!showNextTest(nextTask)){
            onTasksIsDone(nextTask);
            nextTask = getNextTaskType();
        }
    }

    private boolean showNextTest(TaskType taskType) {
        switch (taskType) {

            case CHOOSE:
                return abcTestManager.showNext();

            case BOOLEAN:
                return booleanTestManager.showNext();

            case WRITING:
                return writingTestManager.showNext();

            default:
                return false;
        }
    }

    private void onTasksIsDone(TaskType taskType) {
        TaskType[] tasks = new TaskType[taskTypes.length - 1];
        int index = 0;
        for (TaskType type : taskTypes) {
            if (type != taskType) {
                tasks[index++] = type;
            }
        }

        taskTypes = tasks;
    }

    private TaskType getNextTaskType() {

        return taskTypes[new Random().nextInt(taskTypes.length)];
    }

    public static TestsManager init(View view) {
        return new TestsManager(view);
    }
}
