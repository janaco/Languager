package com.stiletto.tr.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stiletto.tr.R;
import com.stiletto.tr.adapter.DictionaryAdapter;
import com.stiletto.tr.core.DictionaryItemListener;
import com.stiletto.tr.db.tables.DictionaryTable;
import com.stiletto.tr.model.DictionaryItem;
import com.stiletto.tr.model.Word;
import com.stiletto.tr.view.Fragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yana on 12.03.17.
 */

public class WordDetailsFragment extends Fragment {

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.title)
    TextView itemTitle;
    @Bind(R.id.subtitle)
    TextView itemSubtitle;
    @Bind(R.id.item_lang)
    TextView itemLanguages;

    private DictionaryAdapter adapter;
    private Word word;
    private DictionaryItemListener listener;
    private int position;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        word = getArguments().getParcelable("word");
        position = getArguments().getInt("position");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_word_details, container, false);
        ButterKnife.bind(this, view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String languages = word.getOriginLanguage().toString() + "-" + word.getTranslationLanguage().toString();
        itemLanguages.setText(languages);
        itemTitle.setText(word.getText());

        adapter = new DictionaryAdapter(getContext(), word.getDictionaryItems());
        recyclerView.setAdapter(adapter);
    }

    public void setListener(DictionaryItemListener listener) {
        this.listener = listener;
    }

    @OnClick(R.id.item_remove)
    void onRemoveClick() {
        DictionaryTable.remove(getContext(), new DictionaryItem(word.getText()));
        listener.onDictionaryItemRemoved(position);
        getActivity().onBackPressed();
    }

    @OnClick(R.id.item_back)
    void onBackClick() {
        getActivity().onBackPressed();
    }

    public static WordDetailsFragment getInstance(Word word, int position, DictionaryItemListener listener) {

        Bundle bundle = new Bundle();
        bundle.putParcelable("word", word);
        bundle.putInt("position", position);

        WordDetailsFragment fragment = new WordDetailsFragment();
        fragment.setArguments(bundle);
        fragment.setListener(listener);

        return fragment;
    }
}
