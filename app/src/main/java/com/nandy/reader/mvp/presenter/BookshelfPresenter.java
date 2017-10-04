package com.nandy.reader.mvp.presenter;

import com.nandy.reader.adapter.BooksAdapter;
import com.nandy.reader.model.Book;
import com.nandy.reader.mvp.contract.BookshelfContract;
import com.nandy.reader.mvp.model.BookshelfModel;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * Created by yana on 24.09.17.
 */

public class BookshelfPresenter implements BookshelfContract.Presenter {

    private BookshelfContract.View view;
    private BookshelfModel bookshelfModel;

    private Disposable booksLadingSubscription;
    private Disposable searchBookSubscription;

    public BookshelfPresenter(BookshelfContract.View view) {
        this.view = view;
    }

    @Override
    public void start() {
        loadBooks();
    }

    @Override
    public void destroy() {

        if (booksLadingSubscription != null && !booksLadingSubscription.isDisposed()) {
            booksLadingSubscription.dispose();
        }

        if (searchBookSubscription != null && !searchBookSubscription.isDisposed()){
            searchBookSubscription.dispose();
        }
    }

    @Override
    public void loadBooks() {

        bookshelfModel.loadBooks()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Book>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        booksLadingSubscription = d;
                    }

                    @Override
                    public void onNext(Book book) {
                        view.addBook(book);
                    }

                    @Override
                    public void onError(Throwable e) {
                        //TODO
                    }

                    @Override
                    public void onComplete() {
                        //TODO
                    }
                });
    }


    @Override
    public void searchForBook(BooksAdapter adapter, String searchRequest) {
        bookshelfModel.searchForBook(adapter, searchRequest)
                .delaySubscription(1000, TimeUnit.MILLISECONDS)
                .subscribe(new SingleObserver<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        searchBookSubscription = d;
                    }

                    @Override
                    public void onSuccess(Integer index) {
                        view.moveToPosition(index);
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.onEmptySearchResults(e.getMessage());
                    }
                });
    }

    public void setBookshelfModel(BookshelfModel bookshelfModel) {
        this.bookshelfModel = bookshelfModel;
    }
}
