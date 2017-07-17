package com.stiletto.tr.fragment;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stiletto.tr.R;
import com.stiletto.tr.adapter.TestLearningAdapter;
import com.stiletto.tr.core.OnListItemClickListener;
import com.stiletto.tr.emums.Status;
import com.stiletto.tr.model.test.TestLearning;
import com.stiletto.tr.model.test.TestVariant;
import com.stiletto.tr.model.word.DictionaryItem;
import com.stiletto.tr.model.word.Translation;
import com.stiletto.tr.model.word.Word;
import com.stiletto.tr.translator.yandex.Language;
import com.stiletto.tr.view.Fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by yana on 27.05.17.
 */

public class FragmentTestLearning extends Fragment {

    @Bind(R.id.item_text)
    TextView itemText;
    @Bind(R.id.item_transcription)
    TextView itemTranscription;
    @Bind(R.id.item_translation)
    TextView itemTranslation;

    private String langPrimary;
    private String langTranslation;

    private List<Word> words;

    private int index;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        langPrimary = getArguments().getString("primary");
        langTranslation = getArguments().getString("translation");
        index = 0;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test_learning, container, false);
        ButterKnife.bind(this, view);
        return view;
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

        if (results.load()) {
            words = results;
            loadWord(index);
        }
    }


    @OnClick(R.id.item_prev)
    void goToPreviousItem() {
        loadWord(--index);
    }

    @OnClick(R.id.item_next)
    void goToNextItem() {
        loadWord(++index);
    }

    private void loadWord(int index) {

        if (index < 0 || index >= words.size()) {
            return;
        }

        Word word = words.get(index);

        if (word.getDictionaryItems().size() > 0) {
            DictionaryItem item = word.getDictionaryItems().get(
                    new Random().nextInt(word.getDictionaryItems().size()));
            itemText.setText(item.getOriginText());
            itemTranscription.setText("[" + item.getTranscription() + "]");
            itemTranslation.setText(item.getTranslationsAsString());
        } else {
            itemText.setText(word.getText());
            itemTranscription.setText("[ - ]");
            itemTranslation.setText(word.getTranslationsAsString());
        }
    }

    public static FragmentTestLearning getInstance(Bundle args) {

        FragmentTestLearning fragment = new FragmentTestLearning();
        fragment.setArguments(args);

        return fragment;
    }


}
