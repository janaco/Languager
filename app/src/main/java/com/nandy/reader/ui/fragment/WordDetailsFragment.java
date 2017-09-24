package com.nandy.reader.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nandy.reader.R;
import com.nandy.reader.adapter.DictionaryAdapter;
import com.nandy.reader.core.DictionaryItemListener;
import com.nandy.reader.emums.Status;
import com.nandy.reader.model.word.Word;
import com.nandy.reader.mvp.contract.WordContract;
import com.nandy.reader.mvp.model.WordModel;
import com.nandy.reader.mvp.presenter.WordPresenter;
import com.nandy.reader.view.Fragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Translation, usage examples and any additional information about certain word
 * are displayed on this fragment.
 * <p>
 * Created by yana on 12.03.17.
 */

public class WordDetailsFragment extends Fragment implements WordContract.View {

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.title)
    TextView itemTitle;
    @Bind(R.id.subtitle)
    TextView itemSubtitle;
    @Bind(R.id.item_lang)
    TextView itemLanguages;

    private WordContract.Presenter presenter;


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

        presenter.start();


    }

    @Override
    public void onWordLoaded(Word word) {
        String languages = word.getOriginLanguage() + "-" + word.getTranslationLanguage();
        itemLanguages.setText(languages);
        itemTitle.setText(word.getText());
        Status status = word.getStatus();

        itemSubtitle.setText(status.name());
        itemSubtitle.setTextColor(ContextCompat.getColor(getContext(), status.getColor()));
        itemSubtitle.setVisibility(View.VISIBLE);

        DictionaryAdapter adapter = new DictionaryAdapter(word.getDictionaryItems());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void setPresenter(WordContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @OnClick(R.id.item_remove)
    void onRemoveClick() {

        presenter.removeWord();
    }

    @Override
    public void onWordRemoved() {
        getActivity().onBackPressed();
    }

    @OnClick(R.id.item_back)
    void onBackClick() {
        getActivity().onBackPressed();
    }


    public static WordDetailsFragment newInstance(String text) {


        WordDetailsFragment fragment = new WordDetailsFragment();

        WordPresenter presenter  = new WordPresenter(fragment);
        presenter.setWordModel(new WordModel(text));
        fragment.setPresenter(presenter);

        return fragment;
    }
}
