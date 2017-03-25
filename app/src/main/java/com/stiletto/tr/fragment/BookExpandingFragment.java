package com.stiletto.tr.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.stiletto.tr.model.Book;
import com.stiletto.tr.view.ExpandingFragment;
import com.stiletto.tr.view.Fragment;

/**
 * Created by yana on 19.03.17.
 */

public class BookExpandingFragment extends ExpandingFragment {

    public static BookExpandingFragment newInstance(Book book){
        BookExpandingFragment fragment = new BookExpandingFragment();

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();

    }

    /**
     * include TopFragment
     * @return
     */
    @Override
    public Fragment getFragmentTop() {
        return FragmentTop.newInstance();
    }

    /**
     * include BottomFragment
     * @return
     */
    @Override
    public Fragment getFragmentBottom() {
        return FragmentBottom.newInstance();
    }
}