package com.stiletto.tr.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.stiletto.tr.core.TranslationCallback;
import com.stiletto.tr.fragment.PageFragment;
import com.stiletto.tr.pagination.Pagination;
import com.stiletto.tr.translator.yandex.Language;

/**
 * Created by yana on 04.01.17.
 */

public class PagerAdapter extends FragmentPagerAdapter {

    private Pagination pagination;
    private Language primaryLanguage;
    private Language translationLanguage;

    private TranslationCallback translationCallback;

    public PagerAdapter(FragmentManager fragmentManager, Pagination pagination,
                        Language primaryLanguage, Language translationLanguage) {
        super(fragmentManager);
        this.pagination = pagination;
        this.primaryLanguage = primaryLanguage;
        this.translationLanguage = translationLanguage;
    }

    public void setTranslationCallback(TranslationCallback translationCallback) {
        this.translationCallback = translationCallback;
    }

    @Override
    public android.support.v4.app.Fragment getItem(final int position) {

       return PageFragment.create(position, pagination.get(position),
                primaryLanguage, translationLanguage, translationCallback);
    }


    @Override
    public int getCount() {
        return pagination.getPagesCount();
    }

}
