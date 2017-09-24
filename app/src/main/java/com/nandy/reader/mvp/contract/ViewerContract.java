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

    interface Model{

        void setPagination(Pagination pagination);

        void setBookmark(int bookmark);

        String getTitle();

        Single<Pagination> parseBook(Context context);

        int getBookmark();

        String getPath();

        Book getBook();

    }

    interface View extends BaseView<Presenter>{

        void startLoadingProgress(String text);

        void cancelLoadingProgress();

        void notifyNextPageOpened(int position);

        void afterParsingFinished(List<CharSequence> pages, int pagesCount, int bookmark);
    }

    interface Presenter extends BasePresenter{

        void onPageSelected(int position);

        void saveBookmarkOnDestroy(int bookmark);

        Book getBook();

    }
}
