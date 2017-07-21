package com.stiletto.tr.tests;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.stiletto.tr.R;
import com.stiletto.tr.emums.Status;
import com.stiletto.tr.emums.TaskType;
import com.stiletto.tr.emums.TestType;
import com.stiletto.tr.model.test.Test;
import com.stiletto.tr.model.word.Word;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

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
    @Bind(R.id.view_curtain)
    View viewCurtain;
    @Bind(R.id.item_alert)
    TextView itemAlert;
    @Bind(R.id.btn_again)
    TextView btnAgain;

    private ABCTestManager abcTestManager;
    private BooleanTestManager booleanTestManager;
    private WritingTestManager writingTestManager;

    private TaskType[] taskTypes;
    private TestType testType;

    private String langPrimary;
    private String langTranslation;

    private List<Word> words;

    private int testsAmount = 10;
    private int testIndex;

    public TestsManager(View view) {
        ButterKnife.bind(this, view);
    }

    @OnClick(R.id.btn_again)
    void tryAgain() {
        testIndex=0;
        start();
    }

    public void setTestType(TestType testType) {
        this.testType = testType;
    }

    public void setTasks(TaskType[] taskTypes) {
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

    public void loadTests() {
        RealmQuery<Word> query = selectWordsQuery();
        RealmResults<Word> results = query.findAllAsync();
        results.load();
        words = new ArrayList<>();

        for (Word word : results) {
            words.add(word);
        }
        Collections.shuffle(words);

        createTests();
    }

    private RealmQuery<Word> selectWordsQuery() {
        Realm realm = Realm.getDefaultInstance();
        RealmQuery<Word> query = realm.where(Word.class)
                .equalTo("info.originLanguage", langPrimary)
                .equalTo("info.translationLanguage", langTranslation);
        switch (testType) {

            case GENERAL:
            case QUICK:
                query.equalTo("info.status", Status.UNKNOWN.name());
                break;

            case EXAM:
                query.equalTo("info.status", Status.KNOWN.name());
                break;

        }

        return query;
    }

    public void createTests() {

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

    public void setLanguages(String langPrimary, String langTranslation) {
        this.langPrimary = langPrimary;
        this.langTranslation = langTranslation;
    }

    private void shuffleTests() {

        for (TaskType taskType : taskTypes) {

            switch (taskType) {

                case CHOOSE:
                    abcTestManager.shuffle();
                    break;

                case BOOLEAN:
                    booleanTestManager.shuffle();
                    break;

                case WRITING:
                    writingTestManager.shuffle();
                    break;
            }
        }
    }


    public void start() {
        shuffleTests();
        if (taskTypes.length <= 0 || !showNextTest(getNextTaskType())) {
            viewTestABC.setVisibility(View.GONE);
            viewTestBoolean.setVisibility(View.GONE);
            viewWritingTest.setVisibility(View.GONE);
            viewCurtain.setVisibility(View.VISIBLE);
            itemAlert.setText("There are no test. You have already learned all words.");
        }
    }

    @Override
    public void onNextTest(Test test, TaskType taskType) {

        switch (taskType) {
            case CHOOSE:
                viewTestABC.setVisibility(View.VISIBLE);
                viewTestBoolean.setVisibility(View.GONE);
                viewWritingTest.setVisibility(View.GONE);
                viewCurtain.setVisibility(View.GONE);
                break;

            case BOOLEAN:
                viewTestABC.setVisibility(View.GONE);
                viewTestBoolean.setVisibility(View.VISIBLE);
                viewWritingTest.setVisibility(View.GONE);
                viewCurtain.setVisibility(View.GONE);
                break;

            case WRITING:
                viewTestABC.setVisibility(View.GONE);
                viewTestBoolean.setVisibility(View.GONE);
                viewWritingTest.setVisibility(View.VISIBLE);
                viewCurtain.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onTextIsDone(Test test) {

        if (test.isPassed()) {
            increaseLearningProgress(test);
        }
        testIndex++;

        if (testIndex >= testsAmount && testType == TestType.QUICK) {
            viewTestABC.setVisibility(View.GONE);
            viewWritingTest.setVisibility(View.GONE);
            viewTestBoolean.setVisibility(View.GONE);
            viewCurtain.setVisibility(View.VISIBLE);
            itemAlert.setText("Test is finished.");
            btnAgain.setVisibility(View.VISIBLE);
            return;
        }

        if (taskTypes.length > 0) {
            TaskType nextTask = getNextTaskType();
            while (!showNextTest(nextTask)) {
                onTasksIsDone(nextTask);
                if (taskTypes.length > 0) {
                    nextTask = getNextTaskType();
                } else {
                    viewTestABC.setVisibility(View.GONE);
                    viewWritingTest.setVisibility(View.GONE);
                    viewTestBoolean.setVisibility(View.GONE);
                    viewCurtain.setVisibility(View.VISIBLE);
                    itemAlert.setText("Test is finished.");
                    btnAgain.setVisibility(View.GONE);
                }
            }
        }
    }

    private void increaseLearningProgress(Test test) {

        Word word = Realm.getDefaultInstance().where(Word.class)
                .equalTo("original", test.getMetaData().getWordKey())
                .equalTo("info.originLanguage", test.getMetaData().getLangPrimary())
                .equalTo("info.translationLanguage", test.getMetaData().getLangTranslation())
                .findFirst();
        int passedTests = word.getPassedTestsCount() + 1;

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        word.setPassedTestsCount(passedTests);
        word.setStatus(passedTests <= 3 ? Status.UNKNOWN.name() : Status.KNOWN.name());
        realm.copyToRealmOrUpdate(word);
        realm.commitTransaction();

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
        if (taskTypes.length > 0) {
            TaskType[] tasks = new TaskType[taskTypes.length - 1];
            int index = 0;
            for (TaskType type : taskTypes) {
                if (type != taskType) {
                    tasks[index++] = type;
                }
            }

            taskTypes = tasks;
        }
    }

    private TaskType getNextTaskType() {

        return taskTypes[new Random().nextInt(taskTypes.length)];
    }

    public static TestsManager init(View view) {
        return new TestsManager(view);
    }
}
