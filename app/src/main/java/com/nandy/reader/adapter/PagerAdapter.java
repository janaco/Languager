package com.nandy.reader.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.nandy.reader.ui.fragment.PageFragment;
import com.nandy.reader.model.Book;

import java.util.ArrayList;
import java.util.List;

/**
 * And its can be used to display book pages with texts and all things.
 * <p>
 * Created by yana on 04.01.17.
 */

public class PagerAdapter extends FragmentPagerAdapter {

    private List<CharSequence> pages = new ArrayList<>();
    private Book book;

    public PagerAdapter(FragmentManager fragmentManager, Book book) {
        super(fragmentManager);
        this.book = book;
    }

    public void setPages(List<CharSequence> pages) {

        this.pages.clear();
        this.pages.addAll(pages);
        notifyDataSetChanged();
    }


    @Override
    public android.support.v4.app.Fragment getItem(final int position) {

        return PageFragment.getInstance(book, pages.get(position));
    }

    @Override
    public int getCount() {
        return pages.size();
    }

}
