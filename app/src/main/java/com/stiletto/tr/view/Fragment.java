package com.stiletto.tr.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.stiletto.tr.activity.MainActivity;

/**
 * Created by yana on 30.12.16.
 */

public abstract class Fragment extends android.support.v4.app.Fragment {

    public String getName() {
        return getClass().getName();
    }

//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();
//        if (actionBar != null) {
//            onCreateActionBar(actionBar);
//        }
//    }
//
//    public void showActionBar(ActionBar actionBar) {
//        actionBar.show();
//    }
//
//    public void hideActionBar(ActionBar actionBar) {
//            actionBar.hide();
//    }
//
//    protected void onCreateActionBar(ActionBar actionBar) {
//        hideActionBar(actionBar);
//        actionBar.setDisplayShowCustomEnabled(false);
//    }
//
//    public void showUpNavigation(ActionBar actionBar) {
//            actionBar.setDisplayHomeAsUpEnabled(true);
//            actionBar.setDefaultDisplayHomeAsUpEnabled(true);
//            actionBar.setHomeButtonEnabled(false);
//            actionBar.setDisplayShowHomeEnabled(false);
//    }
//
//
//    public void showHomeNavigation(ActionBar actionBar) {
//        actionBar.setDisplayShowCustomEnabled(false);
//        actionBar.setDisplayHomeAsUpEnabled(false);
//        actionBar.setDefaultDisplayHomeAsUpEnabled(false);
//        actionBar.setHomeButtonEnabled(true);
//        actionBar.setDisplayShowHomeEnabled(true);
//
//    }
//
//
//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        menu.clear();
//    }
}
