package com.nandy.reader.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nandy.reader.R;
import com.nandy.reader.emums.Status;
import com.nandy.reader.manager.NavigationManager;
import com.nandy.reader.model.word.DictionaryItem;
import com.nandy.reader.model.word.Word;

import java.util.List;
import java.util.Locale;
import java.util.Random;

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

    @Bind(R.id.languages)
    TextView viewLanguages;
    @Bind(R.id.text)
    TextView viewText;
    @Bind(R.id.transcription)
    TextView viewTranscription;
    @Bind(R.id.translation)
    TextView viewTranslation;
    @Bind(R.id.count)
    TextView viewCountSmall;
    @Bind(R.id.count_large)
    TextView viewCountLarge;
    @Bind(R.id.prev)
    ImageView viewPrev;
    @Bind(R.id.next)
    ImageView viewNext;
    @Bind(R.id.pauseplay)
    ImageView viewPausePlay;

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

        String languages = new Locale(langPrimary).getDisplayLanguage() + " - "
                + new Locale(langTranslation).getDisplayLanguage();
        viewLanguages.setText(languages);

        Realm realm = Realm.getDefaultInstance();
        RealmQuery<Word> query = realm.where(Word.class)
                .equalTo("info.originLanguage", langPrimary)
                .equalTo("info.translationLanguage", langTranslation)
                .equalTo("info.status", Status.UNKNOWN.name());

        Log.d("LEARNING_", "query: " + query.toString() + "\noriginLanguage: " + langPrimary
                + "\n: translationLanguage: " + langTranslation);

        RealmResults<Word> results = query.findAllAsync();

        while (!results.load()) {
            //wait
        }

        Log.d("LEARNING_", "loaded: " + results.load() + ", count: " + results.size());

        words = results;
        loadWord(index);
    }


    @OnClick(R.id.prev)
    void goToPreviousItem() {
        loadWord(--index);
    }

    @OnClick(R.id.next)
    void goToNextItem() {
        loadWord(++index);
    }

    @OnClick(R.id.close)
    void onCloseWindowClick() {
        NavigationManager.removeFragment(getActivity(), this);
    }

    @OnClick(R.id.google_translate)
    void onTranslateWithGoogleClick() {

    }

    @OnClick(R.id.speetch)
    void onTextToSpeetchClick() {

    }

    private void loadWord(int index) {

        if (index < 0 || index >= words.size()) {
            return;
        }

        Word word = words.get(index);

        if (word.getDictionaryItems().size() > 0) {
            DictionaryItem item = word.getDictionaryItems().get(
                    new Random().nextInt(word.getDictionaryItems().size()));
            viewText.setText(item.getOriginText());
            viewTranscription.setText("[" + item.getTranscription() + "]");
            viewTranslation.setText(item.getTranslationsAsString());
        } else {
            viewText.setText(word.getText());
            viewTranscription.setText("No transcription");
            viewTranslation.setText(word.getTranslationsAsString());
        }

        viewCountSmall.setText(index + 1 + "/" + words.size());
        viewCountLarge.setText(index + 1 + "/" + words.size());

        if (index == 0) {
            viewPrev.setImageAlpha(50);
            viewPrev.setClickable(false);
        }

        if (index == words.size() - 1) {
            viewNext.setImageAlpha(50);
            viewNext.setClickable(false);
        }

        if (index > 0 && index < words.size() - 1) {
            viewPrev.setImageAlpha(255);
            viewNext.setImageAlpha(255);
            viewPrev.setClickable(true);
            viewNext.setClickable(true);
        }
    }

    public static FragmentTestLearning getInstance(String originLanguage, String translationLanguage) {

        FragmentTestLearning fragment = new FragmentTestLearning();

        Bundle args = new Bundle();
        args.putString("primary", originLanguage);
        args.putString("translation", translationLanguage);

        fragment.setArguments(args);

        return fragment;
    }


}
