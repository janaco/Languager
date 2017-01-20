package com.stiletto.tr.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;

import com.stiletto.tr.R;

/**
 * Created by yana on 20.01.17.
 */

final class Dot extends BoomPiece {

    public Dot(Context context) {
        super(context);
    }

    @Override
    public void init(int color) {
        Drawable backgroundDrawable = Util.getDrawable(this, R.drawable.piece_dot, null);
        ((GradientDrawable)backgroundDrawable).setColor(color);
        Util.setDrawable(this, backgroundDrawable);
    }

    @Override
    public void setColor(int color) {
        ((GradientDrawable)getBackground()).setColor(color);
    }
}
