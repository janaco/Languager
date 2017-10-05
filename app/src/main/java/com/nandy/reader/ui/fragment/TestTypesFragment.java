package com.nandy.reader.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nandy.reader.R;
import com.nandy.reader.activity.MainActivity;
import com.nandy.reader.emums.TestType;
import com.nandy.reader.fragment.FragmentTestLearning;
import com.nandy.reader.mvp.BasePresenter;
import com.nandy.reader.mvp.contract.TestTypesContract;
import com.nandy.reader.mvp.model.TestTypesModel;
import com.nandy.reader.mvp.presenter.TestTypesPresenter;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yana on 17.07.17.
 */

public class TestTypesFragment extends Fragment implements TestTypesContract.View {

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.view_empty)
    View viewEmpty;
    @Bind(R.id.alert)
    TextView viewAlert;

    private BasePresenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test_language_groups, container, false);
        ButterKnife.bind(this, view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.start();
    }


    @OnClick(R.id.close)
    void onBackPressed() {
        getActivity().onBackPressed();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.destroy();
    }

    @Override
    public void setPresenter(BasePresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showNoTestsAlert() {
        viewEmpty.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        viewAlert.setText(R.string.no_tests_available);
    }

    @Override
    public <T extends RecyclerView.Adapter> void setAdapter(T adapter) {
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void replaceWithLearningFragment(String originLanguage, String translationLanguage) {
        ((MainActivity) getActivity()).replace(FragmentTestLearning.getInstance(originLanguage, translationLanguage));
    }

    @Override
    public void replaceWithTestFragment(TestType testType, String originLanguage, String translationLanguage) {
        ((MainActivity) getActivity()).replace(TestFragment.newInstance(getContext(), testType, originLanguage, translationLanguage));
    }

    public static TestTypesFragment newInstance() {

        TestTypesFragment fragment = new TestTypesFragment();

        TestTypesPresenter presenter = new TestTypesPresenter(fragment);
        presenter.setTestsModel(new TestTypesModel());

        fragment.setPresenter(presenter);

        return fragment;
    }

}
