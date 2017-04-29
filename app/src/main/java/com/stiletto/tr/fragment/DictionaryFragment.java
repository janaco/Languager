package com.stiletto.tr.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.softes.categorizedlistview.CategorizedListView;
import com.stiletto.tr.R;
import com.stiletto.tr.adapter.GeneralDictionaryAdapter;
import com.stiletto.tr.core.DictionaryItemListener;
import com.stiletto.tr.manager.NavigationManager;
import com.stiletto.tr.model.Word;
import com.stiletto.tr.view.Fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Used to display list of all items available in the dictionary.
 *
 * Created by yana on 08.03.17.
 */

public class DictionaryFragment extends Fragment
        implements GeneralDictionaryAdapter.OnItemClickListener, DictionaryItemListener {

    @Bind(R.id.recycler_view)
    CategorizedListView categoriredListView;

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
        categoriredListView = (CategorizedListView) view.findViewById(R.id.recycler_view);
        categoriredListView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Collections.sort(dictionary);
        adapter = new GeneralDictionaryAdapter(dictionary);
        adapter.setOnListItemClickListener(this);
        categoriredListView.setAdapter(adapter);

//        Create and display list of available sections
//         (all words in dictionary are grouped in sections by principe:
//         words that starts with the same letter are grouped to the same section)
        ArrayList<String> items = getSectionKeys();
        Collections.sort(items);
        categoriredListView.setIndexBarItems(items);

        if (adapter.getItemCount() <= 10) {
            categoriredListView.setIndexBarVisibility(false);
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

    private ArrayList<String> getSectionKeys(){
        HashSet<String> set = new HashSet<>();
        for (Word item : dictionary) {
            String word = item.getText().substring(0, 1).toUpperCase();
            set.add(word);
        }
        return new ArrayList<>(set);
    }

    public static DictionaryFragment getInstance(ArrayList<Word> list){
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("items", list);

        DictionaryFragment fragment = new DictionaryFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

}
