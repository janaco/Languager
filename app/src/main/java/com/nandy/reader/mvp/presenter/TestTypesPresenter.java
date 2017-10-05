package com.nandy.reader.mvp.presenter;

import com.nandy.reader.adapter.TestGroupsAdapter;
import com.nandy.reader.emums.TestType;
import com.nandy.reader.ui.fragment.TestFragment;
import com.nandy.reader.fragment.FragmentTestLearning;
import com.nandy.reader.mvp.BasePresenter;
import com.nandy.reader.mvp.contract.TestTypesContract;
import com.nandy.reader.mvp.model.TestTypesModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * Created by yana on 05.10.17.
 */

public class TestTypesPresenter implements BasePresenter, TestGroupsAdapter.OnItemClickListener {

    private TestTypesContract.View view;
    private TestTypesModel testsModel;

    private final TestGroupsAdapter adapter = new TestGroupsAdapter();
    private Disposable testSubscription;

    public TestTypesPresenter(TestTypesContract.View view) {
        this.view = view;
    }

    public void setTestsModel(TestTypesModel testsModel) {
        this.testsModel = testsModel;
    }

    @Override
    public void start() {

        adapter.setOnItemClickListener(this);
        view.setAdapter(adapter);

        testSubscription = testsModel.loadTests().observeOn(AndroidSchedulers.mainThread())
                .subscribe(adapter::addItem, throwable -> view.showNoTestsAlert());
    }

    @Override
    public void destroy() {
        if (testSubscription != null && !testSubscription.isDisposed()) {
            testSubscription.dispose();
        }
    }

    @Override
    public void onItemClick(String originLanguage, String translationLanguage, TestType testType) {

        if (testType == TestType.LEARNING) {
            view.replaceWithLearningFragment(originLanguage, translationLanguage);
            return;
        }

        view.replaceWithTestFragment(testType, originLanguage, translationLanguage);
    }
}
