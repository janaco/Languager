package com.nandy.reader.mvp.model;

import com.nandy.reader.adapter.TestGroupsAdapter;
import com.nandy.reader.emums.Status;
import com.nandy.reader.model.word.Word;
import com.nandy.reader.model.word.WordInfo;
import com.nandy.reader.translator.yandex.Language;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by yana on 05.10.17.
 */

public class TestTypesModel {

    public Observable<TestGroupsAdapter.Item> loadTests() {

        return Observable.create(e -> {
            RealmResults<WordInfo> results = Realm.getDefaultInstance().where(WordInfo.class)
                    .distinct("originLanguage", "translationLanguage");


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

                e.onNext(new TestGroupsAdapter.Item(info.getOriginLanguage(), info.getTranslationLanguage(), wordsCount, unknownWordsCount));
            }

            e.onComplete();
        });

    }
}
