package com.nandy.reader.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.nandy.reader.view.Fragment;

/**
 * Created by yana on 06.08.17.
 */

public class StatisticsPagerAdapter extends FragmentPagerAdapter {

    private Fragment[] fragments;

    public StatisticsPagerAdapter(FragmentManager fragmentManager, Fragment[] fragments) {
        super(fragmentManager);
        this.fragments = fragments;
    }

    @Override
    public android.support.v4.app.Fragment getItem(final int position) {

        return fragments[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }

}
