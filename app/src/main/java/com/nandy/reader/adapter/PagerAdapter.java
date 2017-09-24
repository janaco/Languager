package com.nandy.reader.adapter;

import android.content.Context;
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
    private Context context;

    public PagerAdapter(Context context,FragmentManager fragmentManager, Book book) {
        super(fragmentManager);
        this.context = context;
        this.book = book;
    }

    public void setPages(List<CharSequence> pages) {

        this.pages.clear();
        this.pages.addAll(pages);
        notifyDataSetChanged();
    }


    @Override
    public android.support.v4.app.Fragment getItem(final int position) {

        return PageFragment.getInstance(context, book, pages.get(position));
    }

    @Override
    public int getCount() {
        return pages.size();
    }

}
