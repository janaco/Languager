package com.stiletto.tr.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.stiletto.tr.fragment.DictionaryFragment;
import com.stiletto.tr.model.Word;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by yana on 21.05.17.
 */

public class DictionariesPagerAdapter extends FragmentPagerAdapter {

    private String[] titles;
    private List<DictionaryFragment> fragments;

    public DictionariesPagerAdapter(FragmentManager fragmentManager, Map<String, ArrayList<Word>> dictionaries) {
        super(fragmentManager);

        this.titles = getTitles(dictionaries);
        setFragments(dictionaries);
    }

    private String[] getTitles(Map<String, ArrayList<Word>> dictionaries) {

        List<String> list = new ArrayList<>(dictionaries.keySet());
        Collections.sort(list);
        return list.toArray(new String[list.size()]);
    }

    private void setFragments(Map<String, ArrayList<Word>> dictionaries) {
        fragments = new ArrayList<>();

        for (String title : titles) {
            fragments.add(DictionaryFragment.getInstance(dictionaries.get(title)));
        }
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
