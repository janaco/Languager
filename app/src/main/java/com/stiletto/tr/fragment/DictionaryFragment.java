package com.stiletto.tr.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.softes.categorizedlistview.CategorizedListView;
import com.stiletto.tr.R;
import com.stiletto.tr.adapter.BaseDictionaryAdapter;
import com.stiletto.tr.core.DictionaryItemListener;
import com.stiletto.tr.manager.NavigationManager;
import com.stiletto.tr.model.word.Word;
import com.stiletto.tr.view.Fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by yana on 21.05.17.
 */

public class DictionaryFragment extends Fragment implements BaseDictionaryAdapter.OnItemClickListener, DictionaryItemListener {

    @Bind(R.id.recycler_view)
    CategorizedListView recyclerView;

    private BaseDictionaryAdapter adapter;
    private List<Word> list;

    private String primaryLang;
    private String translationLang;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        primaryLang = getArguments().getString("primary");
        translationLang = getArguments().getString("translation");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dictionary, container, false);
        ButterKnife.bind(this, view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        Realm realm = Realm.getDefaultInstance();
        RealmQuery<Word> query = realm.where(Word.class)
                .equalTo("info.originLanguage", primaryLang)
                .equalTo("info.translationLanguage", translationLang);
        RealmResults<Word> results = query.findAllSortedAsync("original");
        results.load();

        list = results;

        adapter = new BaseDictionaryAdapter(list);
        adapter.setOnListItemClickListener(this);
        recyclerView.setAdapter(adapter);

//        HashSet<String> set = new HashSet<>();
//        for (Word item : list) {
//            String word = item.getText().substring(0, 1).toUpperCase();
//            set.add(word);
//        }
//
//        ArrayList<String> items = new ArrayList<>(set);
//        Collections.sort(items);
//        recyclerView.setIndexBarItems(items);
//
//        if (adapter.getItemCount() <= 10) {
//            recyclerView.setIndexBarVisibility(View.GONE);
//        }
    }

    @Override
    public void onItemClick(String key, Word word, int position) {
        NavigationManager.addFragment(getActivity(), WordDetailsFragment.getInstance(word, position, this));
    }

    @Override
    public void onDictionaryItemRemoved(int position) {
        adapter.remove(position);
    }

    public static DictionaryFragment getInstance(ArrayList<Word> list) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("items", list);

        DictionaryFragment fragment = new DictionaryFragment();
        fragment.setArguments(bundle);

        return fragment;
    }
}
