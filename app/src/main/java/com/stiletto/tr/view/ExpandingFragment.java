package com.stiletto.tr.view;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.stiletto.tr.R;

/**
 * Created by yana on 19.03.17.
 */

public abstract class ExpandingFragment extends Fragment
        implements OnExpandableItemClickListener {

    private static final float SCALE_OPENED = 1.2f;
    private static final int SCALE_CLOSED = 1;
    private static final int ELEVATION_OPENED = 40;

    Fragment fragmentFront;
    Fragment fragmentBottom;

    private CardView cardBack;
    private CardView cardFront;
    private CardView cardBottom;

    float defaultCardElevation;
    private ObjectAnimator frontAnimator;
    private ObjectAnimator backAnimator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.expanding_fragment, container, false);
    }

    @Override
    public void onExpandableItemClick() {
        toggle();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.fragmentFront = onCreateFragmentTop();
        this.fragmentBottom = onCreateFragmentBottom();

        if (fragmentFront != null && fragmentBottom != null) {
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.front, fragmentFront)
                    .replace(R.id.bottomLayout, fragmentBottom)
                    .commit();
        }

        cardBack = (CardView) view.findViewById(R.id.back);
        cardFront = (CardView) view.findViewById(R.id.front);
        cardBottom = (CardView) view.findViewById(R.id.bottomLayout);

        defaultCardElevation = cardFront.getCardElevation();
    }

    public abstract Fragment onCreateFragmentTop();

    public abstract Fragment onCreateFragmentBottom();

    public boolean isClosed() {
        return ViewCompat.getScaleX(cardBack) == SCALE_CLOSED;
    }

    public boolean isOpened() {
        return ViewCompat.getScaleX(cardBack) == SCALE_OPENED;
    }

    public void toggle() {
        if (isOpened()) {
            close();
        } else {
            open();
        }
    }

    public void open() {
        ViewGroup.LayoutParams layoutParams = cardBottom.getLayoutParams();
        layoutParams.height = (int) (cardFront.getHeight() * SCALE_OPENED / 3 * SCALE_OPENED);
        cardBottom.setLayoutParams(layoutParams);


        ViewCompat.setPivotY(cardBack, 0);

        PropertyValuesHolder front1 = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, 0, -cardFront.getHeight() / 4);
        PropertyValuesHolder front2 = PropertyValuesHolder.ofFloat(View.SCALE_X, 1, 1);
        frontAnimator = ObjectAnimator.ofPropertyValuesHolder(cardFront, front1, front2);
        PropertyValuesHolder backX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1f, 1.2f);
        PropertyValuesHolder backY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, 1.2f);
        backAnimator = ObjectAnimator.ofPropertyValuesHolder(cardBack, backX, backY);
        cardBack.setPivotY(0);
        frontAnimator.start();
        backAnimator.start();

        cardFront.setCardElevation(ELEVATION_OPENED);
    }

    public void close() {
        if (frontAnimator != null) {
            frontAnimator.reverse();
            backAnimator.reverse();
            backAnimator = null;
            frontAnimator = null;
        }
        cardFront.setCardElevation(defaultCardElevation);
    }

    public Fragment getFragmentFront() {
        return fragmentFront;
    }

    public Fragment getFragmentBottom() {
        return fragmentBottom;
    }
}