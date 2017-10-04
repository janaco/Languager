package com.nandy.reader.mvp.presenter;

import com.nandy.reader.mvp.contract.PageContract;
import com.nandy.reader.mvp.model.PageModel;

/**
 * Created by yana on 27.08.17.
 */

public class PagePresenter implements PageContract.Presenter {

    private PageModel pageModel;
    private PageContract.View view;

    public PagePresenter(PageContract.View view){
        this.view = view;
    }

    public void setPageModel(PageModel pageModel) {
        this.pageModel = pageModel;
    }

    @Override
    public void start() {
        ;
    }

    @Override
    public void destroy() {

    }

    @Override
    public void onWordClick(String word, int x, int y) {
        view.showTranslationDialog(word, pageModel.getLanguages(), pageModel.getBookId(), x, y);
    }

}
