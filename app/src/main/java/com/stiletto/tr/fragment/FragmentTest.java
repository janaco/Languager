package com.stiletto.tr.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stiletto.tr.R;
import com.stiletto.tr.emums.TaskType;
import com.stiletto.tr.emums.TestType;
import com.stiletto.tr.tests.TestsManager;
import com.stiletto.tr.view.Fragment;

/**
 * Created by yana on 17.07.17.
 */

public class FragmentTest extends Fragment{

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
        return inflater.inflate(R.layout.fragment_test, container, false);
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
