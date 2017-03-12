package com.stiletto.tr.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stiletto.tr.R;
import com.stiletto.tr.adapter.GeneralDictionaryAdapter;
import com.stiletto.tr.core.DictionaryItemListener;
import com.stiletto.tr.db.tables.DictionaryTable;
import com.stiletto.tr.manager.NavigationManager;
import com.stiletto.tr.model.Word;
import com.stiletto.tr.view.Fragment;
import com.stiletto.tr.widget.list.CategoriredList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yana on 08.03.17.
 */

public class DictionaryFragment extends Fragment
        implements GeneralDictionaryAdapter.OnItemClickListener, DictionaryItemListener {

    @Bind(R.id.recycler_view)
    CategoriredList recyclerView;

    private GeneralDictionaryAdapter adapter;
    private List<Word> dictionary;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dictionary = getArguments().getParcelableArrayList("items");
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
        super.onViewCreated(view, savedInstanceState);

        Collections.sort(dictionary);
        adapter = new GeneralDictionaryAdapter(dictionary);
        adapter.setOnListItemClickListener(this);
        recyclerView.setAdapter(adapter);

        HashSet<String> set = new HashSet<>();
        for (Word item : dictionary) {
            String word = item.getText().substring(0, 1).toUpperCase();
            set.add(word);
        }

        ArrayList<String> items = new ArrayList<>(set);
        Collections.sort(items);
        recyclerView.setIndexBarItems(items);

        if (adapter.getItemCount() <= 10) {
            recyclerView.setIndexBarVisibility(View.GONE);
        }

    }

    @Override
    public void onItemClick(String key, Word word, int position) {
        NavigationManager.addFragment(getActivity(), WordDetailsFragment.getInstance(word, position, this));
    }

    @Override
    public void onDictionaryItemRemoved(int position) {
        adapter.remove(position);
    }

    public static DictionaryFragment getInstance(ArrayList<Word> list){
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("items", list);

        DictionaryFragment fragment = new DictionaryFragment();
        fragment.setArguments(bundle);

        return fragment;
    }
}
