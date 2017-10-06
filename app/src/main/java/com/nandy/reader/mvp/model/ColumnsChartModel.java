package com.nandy.reader.mvp.model;

import com.nandy.reader.emums.Status;
import com.nandy.reader.model.Book;
import com.nandy.reader.model.word.Word;
import com.nandy.reader.mvp.contract.ColumnsChartContract;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by yana on 06.10.17.
 */

public class ColumnsChartModel {


    public Single<List<ColumnsChartContract.Value>> getChartValues() {

        return Single.create(e -> {
            Realm realm = Realm.getDefaultInstance();
            RealmResults<Book> books = realm
                    .where(Book.class).findAllSorted("name");
            books.load();

            List<ColumnsChartContract.Value> data = new ArrayList<>();

            for (Book book : books) {
                int wordsCount = (int) realm.where(Word.class)
                        .equalTo("info.bookId", book.getPath())
                        .count();


                if (wordsCount > 0) {
                    int unknownWordsCount = (int) realm.where(Word.class)
                            .equalTo("info.bookId", book.getPath())
                            .equalTo("info.status", Status.UNKNOWN.name())
                            .count();

                    data.add(new ColumnsChartContract.Value(book.getName(), wordsCount, unknownWordsCount));
                }
            }

            e.onSuccess(data);
        });
    }



}
