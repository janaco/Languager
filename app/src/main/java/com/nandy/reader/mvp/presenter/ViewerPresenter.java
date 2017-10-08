package com.nandy.reader.mvp.presenter;

import com.nandy.reader.model.Book;
import com.nandy.reader.mvp.contract.ViewerContract;
import com.nandy.reader.mvp.model.ParserModel;
import com.nandy.reader.mvp.model.ViewerModel;

import io.reactivex.disposables.Disposable;

/**
 * Created by yana on 26.08.17.
 */

public class ViewerPresenter implements ViewerContract.Presenter {

    private ViewerContract.View view;
    private ViewerModel viewerModel;
    private ParserModel parserModel;

    private Disposable parserSubscription;

    public ViewerPresenter(ViewerContract.View view){
        this.view = view;
    }

    public void setViewerModel(ViewerModel viewerModel) {
        this.viewerModel = viewerModel;
    }

    public void setParserModel(ParserModel parserModel) {
        this.parserModel = parserModel;
    }

    @Override
    public void start() {

      parserSubscription =   parserModel.parse()
                .doOnSubscribe(disposable -> view.startLoadingProgress(viewerModel.getTitle()))
                .doFinally(() -> view.cancelLoadingProgress())
                .subscribe(pagination -> {
                    viewerModel.setPagination(pagination);
                    view.afterParsingFinished(pagination.getPages(), pagination.getPagesCount(), viewerModel.getBookmark());

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
        viewerModel.setBookmark(position);
        view.notifyNextPageOpened(position);
    }


    @Override
    public void saveBookmarkOnDestroy(int bookmark) {
        viewerModel.setBookmark(bookmark);
    }

    @Override
    public Book getBook() {
        return viewerModel.getBook();
    }
}
