package com.nandy.reader.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.nandy.reader.core.TranslationCallback;
import com.nandy.reader.fragment.PageFragment;
import com.nandy.reader.model.Book;
import com.nandy.reader.translator.yandex.Language;

import java.util.ArrayList;
import java.util.List;

/**
 * And its can be used to display book pages with texts and all things.
 * <p>
 * Created by yana on 04.01.17.
 */

public class PagerAdapter extends FragmentPagerAdapter {

    private Language primaryLanguage;
    private Language translationLanguage;

    private List<CharSequence> pages = new ArrayList<>();

    private TranslationCallback translationCallback;

    public PagerAdapter(FragmentManager fragmentManager, Book book) {
        super(fragmentManager);
        this.primaryLanguage = book.getOriginLanguage();
        this.translationLanguage = book.getTranslationLanguage();
    }

    public void setPages(List<CharSequence> pages) {

        this.pages.clear();
        this.pages.addAll(pages);
        notifyDataSetChanged();
    }


    public void setTranslationCallback(TranslationCallback translationCallback) {
        this.translationCallback = translationCallback;
    }

    @Override
    public android.support.v4.app.Fragment getItem(final int position) {

        return PageFragment.create(position, pages.get(position),
                primaryLanguage, translationLanguage, translationCallback);
    }

    @Override
    public int getCount() {
        return pages.size();
    }

}
