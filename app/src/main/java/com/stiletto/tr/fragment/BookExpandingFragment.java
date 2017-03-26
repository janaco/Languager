package com.stiletto.tr.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.stiletto.tr.R;
import com.stiletto.tr.core.BookItemListener;
import com.stiletto.tr.manager.NavigationManager;
import com.stiletto.tr.model.Book;
import com.stiletto.tr.view.ExpandingFragment;
import com.stiletto.tr.view.Fragment;

/**
 * Created by yana on 19.03.17.
 */

public class BookExpandingFragment extends ExpandingFragment {


    private int position;
    private Book book;
    private BookItemListener bookItemListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        book = getArguments().getParcelable("book");
        position = getArguments().getInt("position");

    }

    @Override
    public Fragment getFragmentTop() {
        return FragmentTop.newInstance(book, this);
    }

    @Override
    public Fragment getFragmentBottom() {
        return FragmentBottom.newInstance(book, bookItemListener, position);
    }


    public void setBookItemListener(BookItemListener bookItemListener) {
        this.bookItemListener = bookItemListener;
    }

    public static BookExpandingFragment newInstance(Book book, int position, BookItemListener listener) {

        Bundle args = new Bundle();
        args.putParcelable("book", book);
        args.putInt("position", position);

        BookExpandingFragment fragment = new BookExpandingFragment();
        fragment.setArguments(args);
        fragment.setBookItemListener(listener);

        return fragment;
    }

}