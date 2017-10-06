package com.nandy.reader.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nandy.reader.R;
import com.nandy.reader.emums.TaskType;
import com.nandy.reader.emums.TestType;
import com.nandy.reader.model.test.ABCTest;
import com.nandy.reader.model.test.BooleanTest;
import com.nandy.reader.model.test.WritingTest;
import com.nandy.reader.mvp.contract.TestContract;
import com.nandy.reader.mvp.model.TestModel;
import com.nandy.reader.mvp.presenter.TestPresenter;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yana on 17.07.17.
 */

public class TestFragment extends Fragment implements TestContract.View {

    @Bind(R.id.languages)
    TextView viewLanguages;
    @Bind(R.id.layout_test_abc)
    View viewTestABC;
    @Bind(R.id.layout_test_boolean)
    View viewTestBoolean;
    @Bind(R.id.layout_writing)
    View viewWritingTest;
    @Bind(R.id.view_curtain)
    View viewCurtain;
    @Bind(R.id.item_alert)
    TextView itemAlert;
    @Bind(R.id.btn_again)
    TextView btnAgain;
    @Bind(R.id.count)
    TextView viewCount;
    @Bind(R.id.abc_text)
    TextView viewABCTask;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.boolean_text)
    TextView viewBooleanTask;
    @Bind(R.id.boolean_answer)
    TextView viewBooleanAnswer;
    @Bind(R.id.approve)
    TextView btnApprove;
    @Bind(R.id.reject)
    TextView btnReject;
    @Bind(R.id.writing_text)
    TextView viewWritingTask;
    @Bind(R.id.writing_answer)
    EditText viewWritingAnswer;
    @Bind(R.id.layout_writing_answer)
    RelativeLayout layoutAnswer;

    private TestContract.Presenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_test, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        presenter.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.destroy();
    }

    @OnClick(R.id.close)
    void onCloseWindowClick() {
        getActivity().onBackPressed();
    }


    @OnClick(R.id.btn_again)
    void tryAgain() {
        presenter.tryAgain();
    }

    @OnClick(R.id.skip)
    void onSkipClick() {
        presenter.skipTest();
    }

    @OnClick(R.id.approve)
    void onApproveClick() {

        presenter.approveBooleanTest();
    }

    @OnClick(R.id.reject)
    void onRejectClick() {

        presenter.rejectBooleanTest();
    }

    @OnClick(R.id.check)
    void onNextClick() {
     presenter.checkWritingTest(viewWritingAnswer.getText().toString().trim());
    }


    @Override
    public void showNextABCTest(ABCTest test) {
        viewABCTask.setText(test.getTask());
        viewTestABC.setVisibility(View.VISIBLE);
        viewTestBoolean.setVisibility(View.GONE);
        viewWritingTest.setVisibility(View.GONE);
        viewCurtain.setVisibility(View.GONE);
    }

    @Override
    public void showNextBooleanTest(BooleanTest test) {
        viewBooleanTask.setText(test.getTask());
        viewBooleanAnswer.setText(test.getAnswer());

        btnApprove.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.verdigris));
        btnReject.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.tea_rose));

        viewTestABC.setVisibility(View.GONE);
        viewTestBoolean.setVisibility(View.VISIBLE);
        viewWritingTest.setVisibility(View.GONE);
        viewCurtain.setVisibility(View.GONE);
    }

    @Override
    public void showNextWritingTest(WritingTest test) {
        viewWritingTask.setText(test.getTask());
        viewWritingAnswer.setText("");
        layoutAnswer.setVisibility(View.VISIBLE);
        layoutAnswer.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.zinnwaldite));
        viewTestABC.setVisibility(View.GONE);
        viewTestBoolean.setVisibility(View.GONE);
        viewWritingTest.setVisibility(View.VISIBLE);
        viewCurtain.setVisibility(View.GONE);
    }

    @Override
    public void setCount(String count) {
        viewCount.setText(count);
    }

    @Override
    public <T extends RecyclerView.Adapter> void setAbcAdapter(T adapter) {
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void showNoTestsAlert() {
        viewTestABC.setVisibility(View.GONE);
        viewTestBoolean.setVisibility(View.GONE);
        viewWritingTest.setVisibility(View.GONE);
        viewCurtain.setVisibility(View.VISIBLE);
        itemAlert.setText("There are no test. You have already learned all hasWords.");
    }

    @Override
    public void onTestFinished() {
        btnAgain.setVisibility(View.VISIBLE);
        viewTestABC.setVisibility(View.GONE);
        viewWritingTest.setVisibility(View.GONE);
        viewTestBoolean.setVisibility(View.GONE);
        viewCurtain.setVisibility(View.VISIBLE);
        itemAlert.setText("Test is finished.");
    }

    @Override
    public void onBooleanTestApproved(boolean passed) {
        int color = passed ?
                ContextCompat.getColor(getContext(), R.color.verdigris) :
                ContextCompat.getColor(getContext(), R.color.tea_rose);

        btnApprove.setBackgroundColor(color);
    }

    @Override
    public void onBooleanTestRejected(boolean passed) {
        int color = passed ?
                ContextCompat.getColor(getContext(), R.color.verdigris) :
                ContextCompat.getColor(getContext(), R.color.tea_rose);

        btnReject.setBackgroundColor(color);
    }


    @Override
    public void onWritingTestPassed(boolean passed) {

        int color = passed ?
                ContextCompat.getColor(getContext(), R.color.verdigris) :
                ContextCompat.getColor(getContext(), R.color.tea_rose);

        layoutAnswer.setBackgroundColor(color);
    }

    @Override
    public void setPresenter(TestContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setLanguages(String languages) {
        viewLanguages.setText(languages);
    }

    public static TestFragment newInstance(Context context, TestType testType, String primaryLanguage, String translationLanguage){
        TestFragment fragment = new TestFragment();

        TestPresenter presenter = new TestPresenter(fragment);
        TestModel model = new TestModel(testType, primaryLanguage, translationLanguage);
        model.setTasks(context, TaskType.values());
        presenter.setTestModel(model);
        fragment.setPresenter(presenter);

        return fragment;
    }
}
