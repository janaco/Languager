package com.nandy.reader.mvp.contract;

import android.content.Context;

import com.nandy.reader.mvp.BasePresenter;
import com.nandy.reader.mvp.BaseView;
import com.nandy.reader.model.Book;
import com.nandy.reader.pagination.Pagination;

import java.util.List;

import io.reactivex.Single;

/**
 * Created by yana on 26.08.17.
 */

public class ViewerContract {


   public interface View extends BaseView<Presenter>{

        void startLoadingProgress(String text);

        void cancelLoadingProgress();

        void notifyNextPageOpened(int position);

        void afterParsingFinished(List<CharSequence> pages, int pagesCount, int bookmark);
    }

    public interface Presenter extends BasePresenter{

        void onPageSelected(int position);

        void saveBookmarkOnDestroy(int bookmark);

        Book getBook();

    }
}
