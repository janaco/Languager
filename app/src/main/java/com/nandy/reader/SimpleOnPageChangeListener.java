package com.nandy.reader;

import android.support.v4.view.ViewPager;

/**
 * Created by yana on 24.09.17.
 */

public  abstract class SimpleOnPageChangeListener implements ViewPager.OnPageChangeListener {


    public abstract void onPageScrolled(int position);

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        onPageScrolled(position);
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
