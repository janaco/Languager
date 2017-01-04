package com.stiletto.tr.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.stiletto.tr.fragment.PageFragment;
import com.stiletto.tr.pagination.Pagination;

/**
 * Created by yana on 04.01.17.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {

    private Pagination pagination;

    public PagerAdapter(FragmentManager fragmentManager, Pagination pagination) {
        super(fragmentManager);
        this.pagination = pagination;
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        return PageFragment.create(position, pagination.get(position));
    }

    @Override
    public int getCount() {
        return pagination.getPagesCount();
    }
}
