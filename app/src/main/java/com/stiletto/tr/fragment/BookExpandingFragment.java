package com.stiletto.tr.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.stiletto.tr.model.Book;
import com.stiletto.tr.view.ExpandingFragment;
import com.stiletto.tr.view.Fragment;

/**
 * Created by yana on 19.03.17.
 */

public class BookExpandingFragment extends ExpandingFragment {


    private Book book;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        book = getArguments().getParcelable("book");

    }

    /**
     * include TopFragment
     *
     * @return
     */
    @Override
    public Fragment getFragmentTop() {
        return FragmentTop.newInstance(book);
    }

    /**
     * include BottomFragment
     *
     * @return
     */
    @Override
    public Fragment getFragmentBottom() {
        return FragmentBottom.newInstance(book);
    }

    public static BookExpandingFragment newInstance(Book book) {

        Bundle args = new Bundle();
        args.putParcelable("book", book);

        BookExpandingFragment fragment = new BookExpandingFragment();
        fragment.setArguments(args);

        return fragment;
    }
}