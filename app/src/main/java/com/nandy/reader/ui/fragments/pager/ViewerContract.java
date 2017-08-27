package com.nandy.reader.ui.fragments.pager;

import android.content.Context;

import com.nandy.reader.BasePresenter;
import com.nandy.reader.BaseView;
import com.nandy.reader.model.Book;
import com.nandy.reader.model.word.Word;
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

        void onNewTranslation(Context context, Word word);

        void saveBookmarkOnDestroy(int bookmark);

        Book getBook();

    }
}
