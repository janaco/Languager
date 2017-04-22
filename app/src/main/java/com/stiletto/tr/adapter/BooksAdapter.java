package com.stiletto.tr.adapter;

import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.stiletto.tr.core.BookItemListener;
import com.stiletto.tr.fragment.BookExpandingFragment;
import com.stiletto.tr.model.Book;
import com.stiletto.tr.view.ExpandingViewPagerAdapter;
import com.stiletto.tr.view.Fragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yana on 19.03.17.
 */

public class BooksAdapter extends ExpandingViewPagerAdapter {

    private List<BookExpandingFragment> fragments;
    private List<Book> books = new ArrayList<>();

    private BookItemListener listener;

    public BooksAdapter(FragmentManager fm, BookItemListener listener) {
        super(fm);
        this.fragments = new ArrayList<>();
        this.listener = listener;

    }

    public void add(Book book) {

        if (!books.contains(book)) {
            int position = fragments.size();
            fragments.add(BookExpandingFragment.newInstance(book, position, listener));
            books.add(book);
            notifyDataSetChanged();
        }
    }

    public void set(Book book, int position) {
        books.set(position, book);
        fragments.set(position, BookExpandingFragment.newInstance(book, position, listener));
        notifyDataSetChanged();
    }

    public int getPositionOf(String name) {
        name = name.toLowerCase();
        int index = 0;
        for (Book book : books) {
            if (book.getName().toLowerCase().contains(name)) {
                return index;
            }
            index++;
        }

        return -1;
    }

    public boolean removeItem(Book book, int position) {

        Log.d("TR_", "remove: " + position);
        books.remove(book);
        fragments.remove(position);
        notifyDataSetChanged();

        return fragments.size() > 0;
    }

    @Override
    public Fragment getItem(int position) {
        return getContent(position);
    }

    private BookExpandingFragment getContent(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

}