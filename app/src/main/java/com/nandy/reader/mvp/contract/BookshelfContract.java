package com.nandy.reader.mvp.contract;

import com.nandy.reader.adapter.BooksAdapter;
import com.nandy.reader.model.Book;
import com.nandy.reader.mvp.BasePresenter;
import com.nandy.reader.mvp.BaseView;

/**
 * Created by yana on 24.09.17.
 */

public class BookshelfContract {

    public interface View extends BaseView<Presenter>{

        void onBookLoaded(Book book);

        void onBookNotFound(String message);

        void moveToPosition(int index);
    }

    public interface Presenter extends BasePresenter{

        void loadBooks();

        void searchForBook(BooksAdapter adapter, String searchRequest);
    }
}
