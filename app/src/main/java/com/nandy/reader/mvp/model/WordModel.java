package com.nandy.reader.mvp.model;

import android.util.Log;

import com.nandy.reader.model.word.Word;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by yana on 24.09.17.
 */

public class WordModel {

    private String text;
    private Word word;

    public WordModel(String text) {
        this.text = text;
    }

    public Single<Word> loadWord() {

        return Single.create(e -> {

            Realm realm = Realm.getDefaultInstance();
            RealmQuery<Word> query = realm.where(Word.class).equalTo("original", text);
            RealmResults<Word> results = query.findAll();
            results.load();

            Log.d("WORD_", "isLoaded: " + results.isLoaded());
            if (!results.isLoaded()){
                e.onError(new Throwable());
            }

                word = results.first();
                e.onSuccess(word);
        });
    }

    public Single<Boolean> remove() {

        return Single.create(e -> {
            word.delete();
            e.onSuccess(true);
        });
    }
}
