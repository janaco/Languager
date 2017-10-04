package com.nandy.reader.mvp.model;

import com.nandy.reader.model.word.Word;

import io.reactivex.Observable;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by yana on 24.09.17.
 */

public class DictionaryModel {

    private String primaryLang;
    private String translationLang;

    public DictionaryModel(String primaryLang, String translationLang) {
        this.primaryLang = primaryLang;
        this.translationLang = translationLang;
    }

    public Observable<Word> loadItems() {

        return Observable.create(e -> {

            Realm realm = Realm.getDefaultInstance();
            RealmQuery<Word> query = realm.where(Word.class)
                    .equalTo(Word.FIELD_LANGUAGE_ORIGIN, primaryLang)
                    .equalTo(Word.FIELD_LANGUAGE_TRANSLATION, translationLang);
            RealmResults<Word> results = query.findAllSortedAsync(Word.FIELD_ORIGINAL);
            results.load();

            if (results.isLoaded()) {
                for (Word word : results) {
                    e.onNext(word);
                }
            }
            e.onComplete();

        });
    }
}
