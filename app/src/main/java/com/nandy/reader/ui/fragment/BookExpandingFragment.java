package com.nandy.reader.ui.fragment;

import com.softes.cardviewer.ExpandableCard;
import com.nandy.reader.model.Book;

/**
 * Item of books list
 *
 * Created by yana on 19.03.17.
 */

public class BookExpandingFragment extends ExpandableCard {

    private Book book;

    @Override
    public FragmentTop createFragmentTop() {
        return FragmentTop.newInstance(book, this);
    }

    @Override
    public FragmentBottom createFragmentBottom() {
        return FragmentBottom.newInstance(book);
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public static BookExpandingFragment newInstance(Book book) {

        BookExpandingFragment fragment = new BookExpandingFragment();
        fragment.setBook(book);

        return fragment;
    }

}