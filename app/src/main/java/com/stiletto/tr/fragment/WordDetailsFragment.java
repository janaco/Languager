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
import com.stiletto.tr.model.word.Word;
import com.stiletto.tr.view.Fragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Translation, usage examples and any additional information about certain word
 * are displayed on this fragment.
 *
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

    private String text;
    private DictionaryItemListener listener;
    private int position;

    private Word word;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        text = getArguments().getString("word");
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

        Realm realm = Realm.getDefaultInstance();
        RealmQuery<Word> query = realm.where(Word.class).equalTo("original", text);
        RealmResults<Word> results = query.findAllAsync();
        results.load();

        word = results.first();
        String languages = word.getOriginLanguage() + "-" + word.getTranslationLanguage();
        itemLanguages.setText(languages);
        itemTitle.setText(word.getText());
        itemSubtitle.setVisibility(View.VISIBLE);

        DictionaryAdapter adapter = new DictionaryAdapter(word.getDictionaryItems());
        recyclerView.setAdapter(adapter);
    }

    @OnClick(R.id.item_remove)
    void onRemoveClick() {

        word.delete();
        listener.onDictionaryItemRemoved(position);
        getActivity().onBackPressed();
    }

    @OnClick(R.id.item_back)
    void onBackClick() {
        getActivity().onBackPressed();
    }

    public void setListener(DictionaryItemListener listener) {
        this.listener = listener;
    }

    public static WordDetailsFragment getInstance(Word word, int position, DictionaryItemListener listener) {

        Bundle bundle = new Bundle();
        bundle.putString("word", word.getText());
        bundle.putInt("position", position);

        WordDetailsFragment fragment = new WordDetailsFragment();
        fragment.setArguments(bundle);
        fragment.setListener(listener);

        return fragment;
    }
}
