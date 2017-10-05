package com.nandy.reader.tests;

import android.content.Context;

import com.nandy.reader.emums.Status;
import com.nandy.reader.model.test.Result;
import com.nandy.reader.model.test.Test;
import com.nandy.reader.model.word.Word;

import io.realm.Realm;

/**
 * Created by yana on 21.07.17.
 */

public class TestProgressMonitor {

    private static final int MIN_SUCCESSFULL_RESULT = 70;

    private int numberOfTasks;
    private int numberSuccessfullyDoneTasks;

    public void onTaskDoneSuccessfully(Test test){
        if (test.isPassed()) {
            increaseLearningProgress(test);
            numberSuccessfullyDoneTasks++;
        }
    }

    public void setTestData(int numberOfTasks){
        this.numberOfTasks = numberOfTasks;
    }

    public void saveResults(){
        int percentage = getResult();
        Result result = new Result(System.currentTimeMillis(), percentage, isTestPassed(percentage));
        result.insert();
    }

    private int getResult(){
        return numberSuccessfullyDoneTasks/numberOfTasks * 100;
    }

    private boolean isTestPassed(int result){
        return result >= MIN_SUCCESSFULL_RESULT;
    }

    public void reset(){
        this.numberOfTasks = 0;
        this.numberSuccessfullyDoneTasks = 0;
    }


    public int getNumberOfTasks() {
        return numberOfTasks;
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
}
