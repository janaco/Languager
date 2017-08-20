package com.nandy.reader.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nandy.reader.R;
import com.nandy.reader.adapter.TestGroupsAdapter;
import com.nandy.reader.emums.Status;
import com.nandy.reader.emums.TestType;
import com.nandy.reader.manager.NavigationManager;
import com.nandy.reader.model.word.Word;
import com.nandy.reader.model.word.WordInfo;
import com.nandy.reader.view.Fragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by yana on 17.07.17.
 */

public class FragmentTests extends Fragment implements TestGroupsAdapter.OnItemClickListener {

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.view_empty)
    View viewEmpty;
    @Bind(R.id.alert)
    TextView viewAlert;

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

        if (items.size() == 0){
            viewEmpty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            viewAlert.setText(R.string.no_tests_available);
            return;
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


    @OnClick(R.id.close)
    void onBackPressed() {
        getActivity().onBackPressed();
    }
}
