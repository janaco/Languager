package com.nandy.reader.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.softes.cardviewer.ExpandablePagerAdapter;
import com.nandy.reader.ui.fragment.BookExpandingFragment;
import com.nandy.reader.model.Book;

import java.util.ArrayList;
import java.util.List;

/**
 * Used to display books with its information on main screen.
 *
 * Created by yana on 19.03.17.
 */

public class BooksAdapter extends ExpandablePagerAdapter {

    private List<Book> books = new ArrayList<>();

    public BooksAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {return BookExpandingFragment.newInstance(books.get(position));}

    @Override
    public int getCount() {
        return books.size();
    }

    public void add(Book book) {

        if (!books.contains(book)) {
            books.add(book);
            notifyDataSetChanged();
        }
    }

    public void set(Book book, int position) {
        books.set(position, book);
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

}