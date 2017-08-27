package com.nandy.reader.ui.dialogs.floating_translation_dialog;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import com.michael.easydialog.EasyDialog;
import com.nandy.reader.R;

/**
 * Created by yana on 27.08.17.
 */

public class TranslationDialog {

    public void show(Context context,int x, int y) {
        new EasyDialog(context)
                .setLayoutResourceId(R.layout.dialog_translation)
                .setBackgroundColor(context.getResources().getColor(R.color.white))
                .setLocation(new int[]{x, y})
                .setGravity(EasyDialog.GRAVITY_BOTTOM)
                .setAnimationTranslationShow(EasyDialog.DIRECTION_X, 500, -800, 0)
                .setAnimationAlphaShow(500, 0f, 1.0f)
                .setAnimationTranslationDismiss(EasyDialog.DIRECTION_X, 500, 0, 800)
                .setAnimationAlphaDismiss(500, 1.0f, 0.0f)
                .setTouchOutsideDismiss(true)
                .setMatchParent(true)
                .setMarginLeftAndRight(24, 24)
                .setOutsideColor(Color.parseColor("#20000000"))
                .show();
    }
}
