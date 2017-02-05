package com.stiletto.tr.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.stiletto.tr.fragment.PageFragment;
import com.stiletto.tr.pagination.Pagination;
import com.stiletto.tr.translator.yandex.Language;

/**
 * Created by yana on 04.01.17.
 */

public class PagerAdapter extends FragmentPagerAdapter {

    //    private Pagination pagination;
//    private Language primaryLanguage;
//    private Language translationLanguage;
    CharSequence content =
            "I know this question has been asked on stackoverflow, (Link To Original Question) I was following the solution provided there, and got a few errors. (I wanted to ask in comment there only, but think I don't have enough reputation to ask in comment of a marked answer) My original question is same, i.e. \"I want to paginate long text\"" +
                    "Errors encountered in the provided answer(link posted above) - 1. After telling about PageSplitter class, solution provider (mixel) stated this -" +
                    "...Then by using PageSplitter.getPages() method you can get original text splitted to pages and put each of them into TextView:" +
                    "pageView.setAdapter(new TextPagerAdapter(getSupportFragmentManager(), pageSplitter.getPages()));" +
                    "I think here he meant pagesView. (my real concern is written below, mentioning it just in case i missed something else.)" +
                    "This is the real problem I am facing - In the PageFragment class, I am getting this error";


    public PagerAdapter(FragmentManager fragmentManager, Pagination pagination,
                        Language primaryLanguage, Language translationLanguage) {
        super(fragmentManager);
//        this.pagination = pagination;
//        this.primaryLanguage = primaryLanguage;
//        this.translationLanguage = translationLanguage;
    }


    @Override
    public android.support.v4.app.Fragment getItem(final int position) {

        return PageFragment.create(1, content, Language.ENGLISH, Language.UKRAINIAN);

//        return PageFragment.create(position, pagination.get(position), primaryLanguage, translationLanguage);
    }


    @Override
    public int getCount() {
//        return pagination.getPagesCount();
        return 20;
    }

}
