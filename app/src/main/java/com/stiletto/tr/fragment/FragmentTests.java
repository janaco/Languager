package com.stiletto.tr.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stiletto.tr.R;
import com.stiletto.tr.adapter.DictionariesAdapter;
import com.stiletto.tr.adapter.TestGroupsAdapter;
import com.stiletto.tr.core.OnListItemClickListener;
import com.stiletto.tr.emums.Status;
import com.stiletto.tr.emums.TestType;
import com.stiletto.tr.manager.NavigationManager;
import com.stiletto.tr.model.word.Word;
import com.stiletto.tr.model.word.WordInfo;
import com.stiletto.tr.translator.yandex.Language;
import com.stiletto.tr.view.Fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by yana on 17.07.17.
 */

public class FragmentTests extends Fragment implements TestGroupsAdapter.OnItemClickListener {

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    private TestGroupsAdapter adapter;

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

        Realm.init(getContext());
        Realm realm = Realm.getDefaultInstance();
        RealmQuery<WordInfo> query = realm.where(WordInfo.class);
        RealmResults<WordInfo> results = query.distinct("originLanguage", "translationLanguage");


        List<TestGroupsAdapter.Item> items = new ArrayList<>();
        for (WordInfo info : results) {

            int wordsCount = (int) Realm.getDefaultInstance()
                    .where(Word.class)
                    .equalTo("info.originLanguage", info.getOriginLanguage())
                    .equalTo("info.translationLanguage", info.getTranslationLanguage())
                    .count();

            int unknownWordsCount = (int) Realm.getDefaultInstance()
                    .where(Word.class)
                    .equalTo("info.originLanguage", info.getOriginLanguage())
                    .equalTo("info.translationLanguage", info.getTranslationLanguage())
                    .equalTo("info.status", Status.UNKNOWN.name())
                    .count();

            items.add(new TestGroupsAdapter.Item(info.getOriginLanguage(), info.getTranslationLanguage(), wordsCount, unknownWordsCount));
        }


        adapter = new TestGroupsAdapter(items);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onItemClick(String originLanguage, String translationLanguage, TestType testType) {

        if (testType == TestType.LEARNING) {
            NavigationManager.addFragment(getActivity(), FragmentTestLearning.getInstance(originLanguage, translationLanguage));
            return;
        }

        NavigationManager.addFragment(getActivity(), FragmentTest.getInstance(originLanguage, translationLanguage, testType));

}


    @OnClick(R.id.item_back)
    void onBackPressed() {
        getActivity().onBackPressed();
    }
}
