package com.nandy.reader.mvp.presenter;

import android.util.Pair;

import com.nandy.reader.emums.TaskType;
import com.nandy.reader.model.test.ABCTest;
import com.nandy.reader.model.test.BooleanTest;
import com.nandy.reader.model.test.Test;
import com.nandy.reader.model.test.WritingTest;
import com.nandy.reader.mvp.contract.TestContract;
import com.nandy.reader.mvp.model.TestModel;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * Created by yana on 05.10.17.
 */

public class TestPresenter implements TestContract.Presenter {

    private TestContract.View view;

    private TestModel testModel;

    private Disposable testLoadingSubscription;
    private Disposable testSubscription;

    public TestPresenter(TestContract.View view) {
        this.view = view;
    }

    public void setTestModel(TestModel testModel) {
        this.testModel = testModel;
    }

    @Override
    public void start() {
        view.setLanguages(testModel.getLanguages());
        testLoadingSubscription = testModel.loadTests().observeOn(AndroidSchedulers.mainThread())
                .subscribe(loaded -> {
                    if (!loaded) {
                        //TODO: implement error
                    }

                    startTest();
                });
    }

    private void startTest() {
        testModel.start().observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Pair<TaskType, Test>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        testSubscription = d;
                    }

                    @Override
                    public void onNext(Pair<TaskType, Test> taskTypeTestPair) {
                        view.setCount(testModel.getProgress());

                        switch (taskTypeTestPair.first) {
                            case CHOOSE:
                                view.showNextABCTest((ABCTest) taskTypeTestPair.second);
                                break;

                            case BOOLEAN:
                                view.showNextBooleanTest((BooleanTest) taskTypeTestPair.second);
                                break;

                            case WRITING:
                                view.showNextWritingTest((WritingTest) taskTypeTestPair.second);
                                break;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showNoTestsAlert();
                    }

                    @Override
                    public void onComplete() {
                        view.onTestFinished();
                    }
                });
    }

    @Override
    public void destroy() {
        if (testLoadingSubscription != null && !testLoadingSubscription.isDisposed()) {
            testLoadingSubscription.dispose();
        }

        if (testSubscription != null && !testSubscription.isDisposed()){
            testSubscription.dispose();
        }
    }

    @Override
    public void tryAgain() {
        startTest();
    }

    @Override
    public void skipTest() {
        testModel.skipTest();
    }

    @Override
    public void approveBooleanTest() {
        view.onBooleanTestApproved(testModel.approveBooleanTest());
    }

    @Override
    public void rejectBooleanTest() {
        view.onBooleanTestRejected(testModel.rejectBooleanTest());
    }

    @Override
    public void checkWritingTest(String answer) {
        view.onWritingTestPassed(testModel.checkWritingTest(answer));
    }
}
