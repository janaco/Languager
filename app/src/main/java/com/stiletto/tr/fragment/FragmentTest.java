package com.stiletto.tr.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stiletto.tr.R;
import com.stiletto.tr.emums.TaskType;
import com.stiletto.tr.emums.TestType;
import com.stiletto.tr.tests.TestsManager;
import com.stiletto.tr.view.Fragment;

import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yana on 17.07.17.
 */

public class FragmentTest extends Fragment{

    @Bind(R.id.languages)
    TextView viewLanguages;

    private String langPrimary;
    private String langTranslation;

    private TestsManager testsManager;
    private TestType testType;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        langPrimary = getArguments().getString("primary");
        langTranslation = getArguments().getString("translation");
        testType = TestType.valueOf(getArguments().getString("test"));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_test, container, false);
        ButterKnife.bind(this, view);

        String languages = new Locale(langPrimary).getDisplayLanguage() + " - "
                + new Locale(langTranslation).getDisplayLanguage();
        viewLanguages.setText(languages);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        testsManager = new TestsManager(view);
        testsManager.setTestType(testType);
        testsManager.setTasks(TaskType.values());
        testsManager.setLanguages(langPrimary, langTranslation);
        testsManager.loadTests();
        testsManager.start();
    }

    @OnClick(R.id.close)
    void onCloseWindowClick(){
        getActivity().onBackPressed();
    }

    public static FragmentTest getInstance(String originLanguage, String translationLanguage, TestType testType) {

        Bundle args = new Bundle();
        args.putString("primary", originLanguage);
        args.putString("translation", translationLanguage);
        args.putString("test", testType.name());

        FragmentTest fragment = new FragmentTest();
        fragment.setArguments(args);

        return fragment;
    }

}
