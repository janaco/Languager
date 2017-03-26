package com.stiletto.tr.adapter;

import android.support.v4.app.FragmentManager;

import com.stiletto.tr.fragment.BookExpandingFragment;
import com.stiletto.tr.model.Book;
import com.stiletto.tr.view.ExpandingViewPagerAdapter;
import com.stiletto.tr.view.Fragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yana on 19.03.17.
 */

public class BookShelfAdapter extends ExpandingViewPagerAdapter {

    private List<BookExpandingFragment> bookList;

    public BookShelfAdapter(FragmentManager fm) {
        super(fm);
        this.bookList = new ArrayList<>();
    }

    public void addBook(Book book) {

        if (!bookList.contains(book)) {
            bookList.add(BookExpandingFragment.newInstance(book));
            notifyDataSetChanged();
        }
    }

    @Override
    public Fragment getItem(int position) {
        return
                getContent(position);
    }

    private BookExpandingFragment getContent(int position){
        return bookList.get(position);
    }

    @Override
    public int getCount() {
        return bookList.size();
    }

}