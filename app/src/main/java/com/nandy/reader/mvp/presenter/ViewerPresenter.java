package com.nandy.reader.mvp.presenter;

import android.content.Context;

import com.nandy.reader.model.Book;
import com.nandy.reader.mvp.contract.ViewerContract;
import com.nandy.reader.mvp.model.ViewerModel;

import io.reactivex.disposables.Disposable;

/**
 * Created by yana on 26.08.17.
 */

public class ViewerPresenter implements ViewerContract.Presenter {

    private ViewerContract.View view;
    private ViewerModel model;

    private Disposable parserSubscription;

    public ViewerPresenter(ViewerContract.View view){
        this.view = view;
    }

    public void setViewerModel(ViewerModel model) {
        this.model = model;
    }

    @Override
    public void start() {

      parserSubscription =   model.parseBook()
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
