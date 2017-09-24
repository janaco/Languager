package com.nandy.reader.mvp.model;

import android.content.Context;

import com.nandy.reader.adapter.DictionariesListAdapter;
import com.nandy.reader.emums.Status;
import com.nandy.reader.model.word.Word;
import com.nandy.reader.model.word.WordInfo;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by yana on 24.09.17.
 */

public class DictionaryListModel {

    private Context context;

    public DictionaryListModel(Context context){
        this.context = context;
    }

    public Observable<DictionariesListAdapter.Item> loadDictionaries(){

        return Observable.create(e -> {

            Realm.init(context);
            Realm realm = Realm.getDefaultInstance();
            RealmQuery<WordInfo> query = realm.where(WordInfo.class);
            RealmResults<WordInfo> results =
                    query.distinct("originLanguage", "translationLanguage")
                            .sort("originLanguage", Sort.ASCENDING, "translationLanguage", Sort.ASCENDING);

            for (WordInfo info : results) {

                int wordsCount = (int) Realm.getDefaultInstance()
                        .where(Word.class)
                        .equalTo("info.originLanguage", info.getOriginLanguage())
                        .equalTo("info.translationLanguage", info.getTranslationLanguage())
                        .count();

                int unknownWordsCount = (int) Realm.getDefaultInstance()
                        .where(Word.class)
                        .equalTo("info.originLanguage", info.getOriginLanguage())
                        .equalTo("info.translationLanguage", info.getTranslationLanguage())
                        .equalTo("info.status", Status.UNKNOWN.name())
                        .count();

                e.onNext(new DictionariesListAdapter.Item(info.getOriginLanguage(), info.getTranslationLanguage(), wordsCount, unknownWordsCount));
            }

            e.onComplete();
        });

    }
}
