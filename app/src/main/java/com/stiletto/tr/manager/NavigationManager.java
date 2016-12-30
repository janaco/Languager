package com.stiletto.tr.manager;

import android.support.v4.app.FragmentActivity;

import com.stiletto.tr.R;
import com.stiletto.tr.view.Fragment;

/**
 * Created by yana on 30.12.16.
 */

public class NavigationManager {

    public static void replaceFragment(FragmentActivity activity, Fragment nextFragment) {

        if (nextFragment == null) {
            return;
        }
        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, nextFragment, nextFragment.getName())
                .commit();
    }


    public static void addFragment(FragmentActivity activity, Fragment nextFragment) {

        if (nextFragment == null) {
            return;
        }
        activity.getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, nextFragment, nextFragment.getName())
                .commit();
    }

    public static void removeFragment(FragmentActivity activity, Fragment fragment) {

        if (fragment == null) {
            return;
        }
        activity.getSupportFragmentManager()
                .beginTransaction().remove(fragment).commit();
    }

}
