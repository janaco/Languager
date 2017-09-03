package com.nandy.reader.ui;

import android.animation.Animator;

/**
 * Created by yana on 03.09.17.
 */

public abstract class SimleAnimationListener implements Animator.AnimatorListener {

    public abstract void onAnimationFinished(Animator animator);

    @Override
    public void onAnimationEnd(Animator animator) {
        onAnimationFinished(animator);
    }


    @Override
    public void onAnimationCancel(Animator animator) {
        onAnimationFinished(animator);
    }

    @Override
    public void onAnimationRepeat(Animator animator) {

    }
}
