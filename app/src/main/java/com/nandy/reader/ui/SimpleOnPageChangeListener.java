package com.nandy.reader.ui;

import android.support.v4.view.ViewPager;

/**
 * Created by yana on 27.08.17.
 */

public abstract class SimpleOnPageChangeListener implements ViewPager.OnPageChangeListener {

    protected abstract void onNextPageSelected(int position);

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        onNextPageSelected(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
