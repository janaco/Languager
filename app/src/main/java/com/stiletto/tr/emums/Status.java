package com.stiletto.tr.emums;

import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;

import com.stiletto.tr.R;

/**
 * Created by yana on 11.03.17.
 */

public enum Status {

    UNKNOWN(R.color.red_500), KNOWN(R.color.yellow_500), APPROVED(R.color.green_500);

    @ColorRes
    private int color;
    Status(@ColorRes int color){
        this.color = color;
    }

    public int getColor() {
        return color;
    }
}
