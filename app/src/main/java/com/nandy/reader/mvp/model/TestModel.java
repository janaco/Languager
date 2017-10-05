package com.nandy.reader.mvp.model;

import android.content.Context;
import android.util.Pair;
import android.view.View;

import com.nandy.reader.emums.Status;
import com.nandy.reader.emums.TaskType;
import com.nandy.reader.emums.TestType;
import com.nandy.reader.model.test.Test;
import com.nandy.reader.model.word.Word;
import com.nandy.reader.tests.TestProgressMonitor;
import com.nandy.reader.tests.TestsListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Single;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by yana on 05.10.17.
 */

public class TestModel implements TestsListener {

    private String langPrimary;
    private String langTranslation;
    private TestType testType;

    private ABCTestModel abcTestModel;
    private BooleanTestModel booleanTestModel;
    private WritingTestModel writingTestModel;

    private TestProgressMonitor progressMonitor;


    private TaskType[] taskTypes;

    private final List<Word> words = new ArrayList<>();

    private int testsAmount = 10;
    private int testIndex;

    private ObservableEmitter<Pair<TaskType, Test>> emitter;

    public TestModel(TestType testType, String langPrimary, String langTranslation) {
        this.langPrimary = langPrimary;
        this.langTranslation = langTranslation;
        this.testType = testType;
    }

    public void setTasks(Context context, TaskType[] taskTypes) {
        this.taskTypes = taskTypes;

        for (TaskType taskType : taskTypes) {

            switch (taskType) {
                case CHOOSE:
                    abcTestModel = new ABCTestModel(context, this);
                    break;

                case BOOLEAN:
                    booleanTestModel = new BooleanTestModel(this);
                    break;

                case WRITING:
                    writingTestModel = new WritingTestModel(this);
                    break;
            }
        }
    }

    public Single<Boolean> loadTests() {

        return Single.create(e -> {

            RealmResults<Word> results = selectWordsQuery().findAll();

            if (!results.isLoaded()) {
                e.onSuccess(false);
            }

            words.addAll(results);
            Collections.shuffle(words);
            createTests();
            e.onSuccess(true);
        });
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

    private void createTests() {

        for (Word word : words) {

            for (TaskType taskType : taskTypes) {

                switch (taskType) {

                    case CHOOSE:
                        abcTestModel.create(words, word);
                        break;

                    case BOOLEAN:
                        booleanTestModel.create(words, word);
                        break;

                    case WRITING:
                        writingTestModel.create(words, word);
                        break;
                }
            }
        }
    }


    private void shuffleTests() {

        for (TaskType taskType : taskTypes) {

            switch (taskType) {

                case CHOOSE:
                    abcTestModel.shuffle();
                    break;

                case BOOLEAN:
                    booleanTestModel.shuffle();
                    break;

                case WRITING:
                    writingTestModel.shuffle();
                    break;
            }
        }
    }


    public Observable<Pair<TaskType, Test>> start() {

        return Observable.create(e -> {

            testIndex = 0;

            if (progressMonitor == null) {
                progressMonitor = new TestProgressMonitor();
            }

            progressMonitor.reset();
            progressMonitor.setTestData(getTestsCount());

            shuffleTests();
            emitter = e;
            if (taskTypes.length <= 0 || !showNextTest(getNextTaskType())) {
                e.onError(new Throwable());
            }
        });
    }

    private int getTestsCount() {

        switch (testType) {

            case QUICK:
                return testsAmount;

            default:
                int count = 0;

                for (TaskType taskType : taskTypes) {
                    switch (taskType) {

                        case BOOLEAN:
                            count += booleanTestModel.getTasksCount();
                            break;

                        case WRITING:
                            count += writingTestModel.getTasksCount();
                            break;

                        case CHOOSE:
                            count += abcTestModel.getTasksCount();
                            break;
                    }
                }
                return count;

        }
    }


    private void onTestFinished() {
        progressMonitor.saveResults();
        emitter.onComplete();
    }


    private boolean showNextTest(TaskType taskType) {
        switch (taskType) {

            case CHOOSE:
                return abcTestModel.showNext();

            case BOOLEAN:
                return booleanTestModel.showNext();

            case WRITING:
                return writingTestModel.showNext();

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

    @Override
    public void onNextTest(Test test, TaskType taskType) {
        emitter.onNext(new Pair<>(taskType, test));
    }

    @Override
    public void onTextIsDone(Test test) {
        progressMonitor.onTaskDoneSuccessfully(test);
        testIndex++;

        if (testIndex >= testsAmount && testType == TestType.QUICK) {
            onTestFinished();
            return;
        }

        if (taskTypes.length > 0) {
            TaskType nextTask = getNextTaskType();
            while (!showNextTest(nextTask)) {
                onTasksIsDone(nextTask);
                if (taskTypes.length > 0) {
                    nextTask = getNextTaskType();
                } else {
                    onTestFinished();
                }
            }
        }
    }

    @Override
    public void skipTest() {
        testIndex++;
        if (taskTypes.length > 0) {
            TaskType nextTask = getNextTaskType();
            while (!showNextTest(nextTask)) {
                onTasksIsDone(nextTask);
                if (taskTypes.length > 0) {
                    nextTask = getNextTaskType();
                } else {
                    onTestFinished();
                }
            }
        }
    }

    public String getProgress(){
        return testIndex + 1 + "/" + progressMonitor.getNumberOfTasks();
    }

    public String getLanguages(){
        return new Locale(langPrimary).getDisplayLanguage() + " - " + new Locale(langTranslation).getDisplayLanguage();
    }

    public boolean approveBooleanTest() {
        return booleanTestModel.approve();
    }

    public boolean rejectBooleanTest() {
        return booleanTestModel.reject();
    }

    public boolean checkWritingTest(String answer) {
        return writingTestModel.check(answer);
    }

    public void setAbcTestModel(ABCTestModel abcTestModel) {
        this.abcTestModel = abcTestModel;
    }

    public void setBooleanTestModel(BooleanTestModel booleanTestModel) {
        this.booleanTestModel = booleanTestModel;
    }

    public void setWritingTestModel(WritingTestModel writingTestModel) {
        this.writingTestModel = writingTestModel;
    }
}