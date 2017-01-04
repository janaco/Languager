package com.stiletto.tr.fragment.expanding;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.stiletto.tr.view.fragment.expanding.ExpandingFragment;

/**
 * Created by yana on 25.12.16.
 */

public class PageExpandingFragment extends ExpandingFragment {

    static final String PAGE = "PAGE";
    String page;

    public static PageExpandingFragment newInstance(String page){
        PageExpandingFragment fragment = new PageExpandingFragment();
        Bundle args = new Bundle();
        args.putString(PAGE, page);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if(args != null) {
            page = args.getString(PAGE);
        }
    }

    /**
     * include TopFragment
     * @return
     */
    @Override
    public Fragment getFragmentTop() {
        return FragmentTop.newInstance(page);
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