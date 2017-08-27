package com.nandy.reader.ui.fragments.pager;

import android.content.Context;

import com.nandy.reader.model.Book;
import com.nandy.reader.model.word.Word;

import io.reactivex.disposables.Disposable;

/**
 * Created by yana on 26.08.17.
 */

public class ViewerPresenter implements ViewerContract.Presenter {

    private ViewerContract.View view;
    private ViewerContract.Model model;

    private Disposable parserSubscription;

    ViewerPresenter(ViewerContract.Model model, ViewerContract.View view){
        this.model = model;
        this.view = view;
    }
    @Override
    public void start(Context context) {

      parserSubscription =   model.parseBook(context)
                .doOnSubscribe(disposable -> view.startLoadingProgress(model.getTitle()))
                .doFinally(() -> view.cancelLoadingProgress())
                .subscribe(pagination -> {
                    model.setPagination(pagination);
                    view.afterParsingFinished(pagination.getPages(), pagination.getPagesCount(), model.getBookmark());

                });
    }

    @Override
    public void destroy() {

        if (parserSubscription != null && !parserSubscription.isDisposed()){
            parserSubscription.dispose();
        }

    }



    @Override
    public void onPageSelected(int position) {
        model.setBookmark(position);
        view.notifyNextPageOpened(position);
    }


    @Override
    public void saveBookmarkOnDestroy(int bookmark) {
        model.setBookmark(bookmark);
    }

    @Override
    public Book getBook() {
        return model.getBook();
    }
}
