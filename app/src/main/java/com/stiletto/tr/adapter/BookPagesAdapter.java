package com.stiletto.tr.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.stiletto.tr.fragment.expanding.PageExpandingFragment;
import com.stiletto.tr.view.fragment.expanding.ExpandingViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yana on 25.12.16.
 */

public class BookPagesAdapter extends ExpandingViewPagerAdapter {

    List<String> pages;

    public BookPagesAdapter(FragmentManager fm) {
        super(fm);
        pages = new ArrayList<>();
    }

    public void addAll(List<String> travels){
        this.pages.addAll(travels);
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        String page = pages.get(position);
        return PageExpandingFragment.newInstance(page);
    }

    @Override
    public int getCount() {
        return pages.size();
    }

}