package com.stiletto.tr.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stiletto.tr.R;
import com.stiletto.tr.emums.TaskType;
import com.stiletto.tr.model.word.Word;
import com.stiletto.tr.tests.TestsManager;
import com.stiletto.tr.view.Fragment;

import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by yana on 17.07.17.
 */

public class FragmentGeneralTest extends Fragment{

    private String langPrimary;
    private String langTranslation;

    private TestsManager testsManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        langPrimary = getArguments().getString("primary");
        langTranslation = getArguments().getString("translation");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_test, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Realm realm = Realm.getDefaultInstance();
        RealmQuery<Word> query = realm.where(Word.class)
                .equalTo("info.originLanguage", langPrimary)
                .equalTo("info.translationLanguage", langTranslation)
                .equalTo("info.status", "Unknown");

        RealmResults<Word> results = query.findAllAsync();
        results.load();

        testsManager = new TestsManager(view);
        testsManager.initTests(TaskType.values());
        testsManager.createTests(results);
        testsManager.start();
    }



    public static FragmentGeneralTest getInstance(Bundle args) {

        FragmentGeneralTest fragment = new FragmentGeneralTest();
        fragment.setArguments(args);

        return fragment;
    }

}
