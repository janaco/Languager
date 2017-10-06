package com.nandy.reader;

import android.view.animation.Animation;

/**
 * Created by yana on 06.10.17.
 */

public abstract class SimpleAnimationEndListener implements Animation.AnimationListener {

    public abstract void onAnimationEnd();

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        onAnimationEnd();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
