package com.stiletto.tr.view;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;

/**
 * Created by yana on 19.03.17.
 */

public abstract class ExpandingViewPagerAdapter extends FragmentStatePagerAdapter {

    WeakReference<Fragment> currentFragmentReference;

    public ExpandingViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public Fragment getCurrentFragment() {
        if(currentFragmentReference != null){
            return currentFragmentReference.get();
        }
        return null;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        if (getCurrentFragment() != object && object instanceof Fragment) {
            currentFragmentReference = new WeakReference<>((Fragment) object);
        }
        super.setPrimaryItem(container, position, object);
    }


}