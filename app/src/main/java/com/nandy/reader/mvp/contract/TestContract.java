package com.nandy.reader.mvp.contract;

import android.support.v7.widget.RecyclerView;

import com.nandy.reader.model.test.ABCTest;
import com.nandy.reader.model.test.BooleanTest;
import com.nandy.reader.model.test.Test;
import com.nandy.reader.model.test.WritingTest;
import com.nandy.reader.mvp.BasePresenter;
import com.nandy.reader.mvp.BaseView;

/**
 * Created by yana on 05.10.17.
 */

public class TestContract {

    public interface View extends BaseView<Presenter>{

        void setLanguages(String languages);

        void setCount(String count);

        void showNextABCTest(ABCTest test);

        void showNextBooleanTest(BooleanTest test);

        void showNextWritingTest(WritingTest test);

        void onTestFinished();

        void showNoTestsAlert();

        void onBooleanTestApproved(boolean passed);

        void onBooleanTestRejected(boolean passed);

        void onWritingTestPassed(boolean passed);

        <T extends RecyclerView.Adapter>void setAbcAdapter(T adapter);
    }

    public interface Presenter extends BasePresenter{

        void tryAgain();

        void skipTest();

        void approveBooleanTest();

        void rejectBooleanTest();

        void checkWritingTest(String answer);
    }
}
