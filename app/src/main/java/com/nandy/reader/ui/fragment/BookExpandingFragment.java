package com.nandy.reader.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.softes.cardviewer.ExpandableCard;
import com.nandy.reader.R;
import com.nandy.reader.core.BookItemListener;
import com.nandy.reader.core.RenameModeCallback;
import com.nandy.reader.model.Book;

import java.io.File;

/**
 * Item of books list
 *
 * Created by yana on 19.03.17.
 */

public class BookExpandingFragment extends ExpandableCard {

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
    public FragmentTop createFragmentTop() {
        return FragmentTop.newInstance(book, this);
    }

    @Override
    public FragmentBottom createFragmentBottom() {
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