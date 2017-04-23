package com.softes.cardviewer;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

/**
 * Created by yana on 19.03.17.
 */

public class ExpandablePagerFactory {

    public static ExpandableCard getCurrentFragment(ViewPager viewPager){
        if (viewPager.getAdapter() instanceof ExpandablePagerAdapter) {
            ExpandablePagerAdapter adapter = (ExpandablePagerAdapter) viewPager.getAdapter();
            Fragment fragment = adapter.getCurrentFragment();
            if (fragment instanceof ExpandableCard) {
                return (ExpandableCard)fragment;
            }
        }
        return null;
    }

    public static void setupViewPager(final ViewPager viewPager) {
        ViewGroup.LayoutParams layoutParams = viewPager.getLayoutParams();
        layoutParams.width =
                ((Activity) viewPager.getContext()).getWindowManager()
                        .getDefaultDisplay().getWidth() / 7 * 5;
        layoutParams.height = (int) ((layoutParams.width / 0.75));

        viewPager.setOffscreenPageLimit(2);

        if (viewPager.getParent() instanceof ViewGroup) {
            ViewGroup viewParent = ((ViewGroup) viewPager.getParent());
            viewParent.setClipChildren(false);
            viewPager.setClipChildren(false);
        }

        viewPager.setPageTransformer(true, new ExpandableCardTransformer());
    }
}
