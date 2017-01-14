package com.stiletto.tr.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.stiletto.tr.R;

/**
 * Created by yana on 08.01.17.
 */

public class PopupFragment {

    private View mainView;
    private View popupView;
    private PopupWindow popupWindow;
    private long duration = 400;
    private int screenHeight;
    private int popHeight;

    public PopupFragment(Activity activity, View mainView, int displayLayoutId) {
        this.mainView = mainView;
        this.popupView = LayoutInflater.from(activity).inflate(displayLayoutId, null);
        Point screenSize = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(screenSize);
        screenHeight = screenSize.y;
        popHeight = screenHeight / 2;
    }

    public boolean isShowing() {
        return popupView != null && popupWindow != null && popupWindow.isShowing();
    }

    public View showPopup() {
            startAnim();
        return popupView;
    }

    public void hidePopup() {
        if (popupWindow.isShowing()) {
            popupWindow.dismiss();
            resumeAnim();
        }
    }

    private void startAnim() {
        int mainViewHeight = mainView.getRootView().getHeight();
        ObjectAnimator mainViewScaleXAnim = ObjectAnimator.ofFloat(mainView, "scaleX", 1.0f, 0.8f);
        ObjectAnimator mainViewScaleYAnim = ObjectAnimator.ofFloat(mainView, "scaleY", 1.0f, 0.9f);
        ObjectAnimator mainViewAlphaAnim = ObjectAnimator.ofFloat(mainView, "alpha", 1.0f, 0.5f);
        ObjectAnimator mainViewRotationXAnim = ObjectAnimator.ofFloat(mainView, "rotationX", 0f, 8f);
        ObjectAnimator mainViewRotationXAnimResume = ObjectAnimator.ofFloat(mainView, "rotationX", 8f, 0f);
        mainViewRotationXAnimResume.setStartDelay(200);
        ObjectAnimator mainViewTranslationYAnim = ObjectAnimator.ofFloat(mainView, "translationY", 0, -(mainViewHeight / 20));
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(mainViewScaleXAnim, mainViewScaleYAnim, mainViewAlphaAnim, mainViewRotationXAnim, mainViewRotationXAnimResume, mainViewTranslationYAnim);
        animatorSet.setDuration(duration);
        animatorSet.start();
        showPopupWindow();
    }

    private void showPopupWindow() {
        popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, popHeight, true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setAnimationStyle(R.style.showScalingAnimation);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                resumeAnim();
            }
        });

        popupWindow.showAtLocation(popupView, Gravity.BOTTOM, 0, 0);
    }

    private void resumeAnim() {
        int mainViewHeight = mainView.getRootView().getHeight();
        ObjectAnimator mainViewScaleXAnim = ObjectAnimator.ofFloat(mainView, "scaleX", 0.8f, 1.0f);
        ObjectAnimator mainViewScaleYAnim = ObjectAnimator.ofFloat(mainView, "scaleY", 0.9f, 1.0f);
        ObjectAnimator mainViewAlphaAnim = ObjectAnimator.ofFloat(mainView, "alpha", 0.5f, 1.0f);
        ObjectAnimator mainViewRotationXAnim = ObjectAnimator.ofFloat(mainView, "rotationX", 0f, 8f);
        ObjectAnimator mainViewRotationXAnimResume = ObjectAnimator.ofFloat(mainView, "rotationX", 8f, 0f);
        mainViewRotationXAnimResume.setStartDelay(200);
        ObjectAnimator mainViewTranslationYAnim = ObjectAnimator.ofFloat(mainView, "translationY", -(mainViewHeight / 20), 0);//将缩小后的主View向上平移height/20的高度，即在标题栏下方
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(mainViewScaleXAnim, mainViewScaleYAnim, mainViewAlphaAnim, mainViewRotationXAnim, mainViewRotationXAnimResume, mainViewTranslationYAnim);
        animatorSet.setDuration(duration);
        animatorSet.start();
    }

    /**
     * @return get Animated duration
     */
    public long getDuration() {
        return duration;
    }

    /**
     * @param duration animation duration in milliseconds
     */
    public void setDuration(long duration) {
        this.duration = duration;
    }

    /**
     * Setting pop-up view is one-third the height of the screen
     */
    public void setPopViewHeightIsOneThirdOfScreen() {
        popHeight = screenHeight / 3;
    }

    /**
     * Setting pop-up view is two-thirds the height of the screen
     */
    public void setPopViewHeightIsTwoThirdOfScreen() {
        popHeight = 2 * screenHeight / 3;
    }
}
