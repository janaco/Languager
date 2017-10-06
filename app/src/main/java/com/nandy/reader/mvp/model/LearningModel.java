package com.nandy.reader.mvp.model;

import com.nandy.reader.emums.Status;
import com.nandy.reader.model.word.Word;

import java.util.List;
import java.util.Locale;

import io.reactivex.Single;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by yana on 06.10.17.
 */

public class LearningModel {

    private String langPrimary;
    private String langTranslation;

    private List<Word> words;

    private int index;

    public LearningModel(String langPrimary, String langTranslation){
        this.langPrimary = langPrimary;
        this.langTranslation = langTranslation;
    }

    public Single<Boolean> loadWords() {

        return Single.create(e -> {

            RealmResults<Word> results = Realm.getDefaultInstance()
                    .where(Word.class)
                    .equalTo("info.originLanguage", langPrimary)
                    .equalTo("info.translationLanguage", langTranslation)
                    .equalTo("info.status", Status.UNKNOWN.name())
                    .findAllAsync();

            if (results.isLoaded()){
                words = results;
                e.onSuccess(true);
            }else {
                e.onSuccess(false);
            }
        });
    }

    public String getLanguages() {
        return new Locale(langPrimary).getDisplayLanguage() + " - "
                + new Locale(langTranslation).getDisplayLanguage();
    }

    public boolean hasWords(){
        return index >= 0 && index < words.size();
    }

    public Word getNext(){
        return words.get(++index);
    }

    public Word getPrevious(){
        return words.get(--index);
    }

    public boolean hasPreviousWord(){
        return index > 0;
    }

    public boolean hasNextWord(){
        return index < words.size() - 1;
    }
    public String getCounterText(){
        return index + 1 + "/" + words.size();
    }

}
